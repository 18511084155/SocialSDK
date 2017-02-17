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

package com.woodys.socialsdk.share.core.handler;

import android.app.Activity;

import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.share.core.shareparam.ShareParamAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareParamImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamText;
import com.woodys.socialsdk.share.core.shareparam.ShareParamVideo;
import com.woodys.socialsdk.share.core.shareparam.ShareParamWebPage;

/**
 * 定义模板步骤
 *
 * @author woodys
 *
 * @since 2015/10/8
 */
public abstract class BaseShareHandler extends AbsShareHandler {

    public BaseShareHandler(Activity context, SocialShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    public void share(BaseShareParam params, SocializeListeners.ShareListener listener) throws Exception {
        super.share(params, listener);
        checkConfig();
        init();

        mImageHelper.saveBitmapToExternalIfNeed(params);
        mImageHelper.copyImageToCacheFileDirIfNeed(params);

        if (params instanceof ShareParamText) {
            shareText((ShareParamText) params);
        } else if (params instanceof ShareParamImage) {
            shareImage((ShareParamImage) params);
        } else if (params instanceof ShareParamWebPage) {
            shareWebPage((ShareParamWebPage) params);
        } else if (params instanceof ShareParamAudio) {
            shareAudio((ShareParamAudio) params);
        } else if (params instanceof ShareParamVideo) {
            shareVideo((ShareParamVideo) params);
        }
    }

    /**
     * 检查配置，比如appKey，appSecret
     */
    protected abstract void checkConfig() throws Exception;

    protected abstract void init() throws Exception;

    protected abstract void shareText(ShareParamText params) throws ShareException;

    protected abstract void shareImage(ShareParamImage params) throws ShareException;

    protected abstract void shareWebPage(ShareParamWebPage params) throws ShareException;

    protected abstract void shareAudio(ShareParamAudio params) throws ShareException;

    protected abstract void shareVideo(ShareParamVideo params) throws ShareException;

}
