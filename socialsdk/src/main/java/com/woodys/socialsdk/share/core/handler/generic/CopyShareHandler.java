/*
 * Copyright (c) 2015. SocialSocial Inc.
 */

package com.woodys.socialsdk.share.core.handler.generic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.woodys.socialsdk.R;
import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.handler.BaseShareHandler;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.share.core.shareparam.ShareParamAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareParamImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamText;
import com.woodys.socialsdk.share.core.shareparam.ShareParamVideo;
import com.woodys.socialsdk.share.core.shareparam.ShareParamWebPage;
import com.woodys.socialsdk.util.BuildHelper;

/**
 * ShareParam，只读取content的内容。
 *
 * @author woodys
 *
 * @since 2016/4/11
 */
public class CopyShareHandler extends BaseShareHandler {

    public CopyShareHandler(Activity context, SocialShareConfiguration configuration) {
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
        Context context = getContext();
        if (context == null) {
            return;
        }

        String content = param.getContent();
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (BuildHelper.isApi11_HoneyCombOrLater()) {
            clip.setPrimaryClip(ClipData.newPlainText(null, content));
        } else {
            ((android.text.ClipboardManager) clip).setText(content);
        }
        Toast.makeText(context, R.string.social_share_sdk_share_copy, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isDisposable() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.COPY;
    }

}
