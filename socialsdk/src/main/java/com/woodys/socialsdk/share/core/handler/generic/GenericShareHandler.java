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

package com.woodys.socialsdk.share.core.handler.generic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.handler.BaseShareHandler;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.share.core.shareparam.ShareParamAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareParamImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamText;
import com.woodys.socialsdk.share.core.shareparam.ShareParamVideo;
import com.woodys.socialsdk.share.core.shareparam.ShareParamWebPage;

/**
 * 只读title和content
 *
 * @author woodys
 *
 * @since 2015/10/12
 */
public class GenericShareHandler extends BaseShareHandler {

    public GenericShareHandler(Activity context, SocialShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    protected void checkConfig() throws Exception {

    }

    @Override
    protected void init() throws Exception {

    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareImage(ShareParamImage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        share(params);
    }

    private void share(BaseShareParam param) {
        SocializeListeners.ShareListener shareListener = getShareListener();
        Intent shareIntent = createIntent(param.getTitle(), param.getContent());
        Intent chooser = Intent.createChooser(shareIntent, "分享到：");
        try {
            getContext().startActivity(chooser);
        } catch (ActivityNotFoundException ignored) {
            if (shareListener != null) {
                shareListener.onError(getShareMedia(), SocialShareStatusCode.ST_CODE_ERROR, new ShareException("activity not found"));
            }
        }
    }

    private Intent createIntent(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    @Override
    public boolean isDisposable() {
        return true;
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.GENERIC;
    }
}
