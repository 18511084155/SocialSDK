/*
 * Copyright (C) 2015 Socialbili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woodys.socialsdk.share.core.handler.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.InvalidParamException;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.share.core.shareparam.ShareImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareParamImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamText;
import com.woodys.socialsdk.share.core.shareparam.ShareParamVideo;
import com.woodys.socialsdk.share.core.shareparam.ShareParamWebPage;

/**
 * @author woodys
 *
 * @since 2015/10/8
 */
public class QQChatShareHandler extends BaseQQShareHandler {

    public QQChatShareHandler(Activity context, SocialShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        super.onActivityResult(activity, requestCode, resultCode, data, listener);
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mUiListener);
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, mUiListener);
            }
        }
    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        shareImageText(params, null);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        final ShareImage image = params.getImage();
        if (image == null || (!image.isLocalImage() && !image.isNetImage())) {
            shareImageText(params, params.getImage());
        } else {
            shareImage(params, params.getImage());
        }
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        if (TextUtils.isEmpty(params.getTitle()) || TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Title or target url is empty or illegal");
        }
        if (TextUtils.isEmpty(params.getAudioUrl())) {
            throw new InvalidParamException("Audio url is empty or illegal");
        }

        final Bundle bundle = new Bundle();
        ShareImage thumb = params.getThumb();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.getContent());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.getTargetUrl());

        if (thumb != null) {
            if (thumb.isNetImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumb.getNetImageUrl());
            } else if (thumb.isLocalImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumb.getLocalPath());
            }
        }

        bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, params.getAudioUrl());
        doShareToQQ((Activity) getContext(), bundle);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        shareImageText(params, params.getThumb());
    }

    /**
     * 图文模式，title、targetURL不能为空
     *
     * @param params
     * @param image
     * @throws ShareException
     */
    private void shareImageText(BaseShareParam params, ShareImage image) throws ShareException {
        if (TextUtils.isEmpty(params.getTitle()) || TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Title or target url is empty or illegal");
        }

        final Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, params.getTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, params.getContent());
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.getTargetUrl());

        if (image != null) {
            if (image.isNetImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image.getNetImageUrl());
            } else if (image.isLocalImage()) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, image.getLocalPath());
            }
        }

        doShareToQQ((Activity) getContext(), bundle);
    }

    /**
     * 纯图模式，localPath不能为空
     *
     * @param params
     * @param image
     */
    private void shareImage(BaseShareParam params, final ShareImage image) throws ShareException {
        mImageHelper.downloadImageIfNeed(image, new Runnable() {
            @Override
            public void run() {
                final Bundle bundle = new Bundle();
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);

                if (image.isLocalImage()) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, image.getLocalPath());
                }

                doShareToQQ((Activity) getContext(), bundle);
            }
        });
    }

    @Override
    protected void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener) {
        tencent.shareToQQ(activity, params, iUiListener);
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.QQ;
    }
}
