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
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.woodys.socialsdk.R;;
import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SharePlatformConfig;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.error.ShareConfigException;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.handler.BaseShareHandler;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.Map;

/**
 * @author woodys
 *
 * @since 2015/10/8
 */
public abstract class BaseQQShareHandler extends BaseShareHandler {

    private static String mAppId;
    protected static Tencent mTencent;

    public BaseQQShareHandler(Activity context, SocialShareConfiguration configuration) {
        super(context, configuration);
    }

    private static Map<String, Object> getAppConfig() {
        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.QQ);
        if (appConfig == null || appConfig.isEmpty()) {
            appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.QZONE);
        }

        return appConfig;
    }

    @Override
    protected void checkConfig() throws Exception {
        if (!TextUtils.isEmpty(mAppId)) {
            return;
        }

        Map<String, Object> appConfig = getAppConfig();
        if (appConfig == null || appConfig.isEmpty()
                || TextUtils.isEmpty(mAppId = (String) appConfig.get(SharePlatformConfig.APP_ID))) {
            throw new ShareConfigException("Please set QQ platform dev info.");
        }
    }

    @Override
    protected void init() throws Exception {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppId, getContext().getApplicationContext());
        }
    }

    /**
     * 必须在主线程分享
     *
     * @param activity
     * @param params
     */
    protected void doShareToQQ(final Activity activity, final Bundle params) {
        doOnMainThread(new Runnable() {
            @Override
            public void run() {
                postProgressStart();
                onShare(activity, mTencent, params, mUiListener);
                if (activity != null && !Util.isMobileQQSupportShare(activity.getApplicationContext())) {
                    String msg = getContext().getString(R.string.social_share_sdk_not_install_qq);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    if (getShareListener() != null) {
                        getShareListener().onError(getShareMedia(), SocialShareStatusCode.ST_CODE_SHARE_ERROR_NOT_INSTALL, new ShareException(msg));
                    }
                }
            }
        });
    }

    protected abstract void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener);

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    protected final IUiListener mUiListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (getShareListener() != null) {
                getShareListener().onCancel(getShareMedia());
            }
        }

        @Override
        public void onComplete(Object response) {
            if (getShareListener() != null) {
                getShareListener().onSuccess(getShareMedia(), SocialShareStatusCode.ST_CODE_SUCCESSED);
            }
        }

        @Override
        public void onError(UiError e) {
            if (getShareListener() != null) {
                getShareListener().onError(getShareMedia(), SocialShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, new ShareException(e.errorMessage));
            }
        }
    };
}
