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

package com.woodys.socialsdk.share.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.woodys.socialsdk.share.core.SocialShare;
import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SharePlatformConfig;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.handler.sina.SinaShareHandler;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.SocialPlatformConfigHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import java.util.Map;

/**
 * 处理微博分享，相当于QQ的{@link com.tencent.connect.common.AssistActivity}
 *
 * @author woodys
 *
 * @since 2015/10/15 14:00
 */
public class SinaAssistActivity extends Activity implements IWeiboHandler.Response {
    private static final String TAG = SinaAssistActivity.class.getSimpleName();

    public static final String KEY_CONFIG = "sina_share_config";
    public static final String KEY_APPKEY = "sina_share_appkey";
    public static final String KEY_CODE = "sina_share_result_code";
    public static final String KEY_PARAM = "sina_share_param";

    private SinaShareHandler mShareHandler;

    private boolean mIsActivityResultCanceled;
    private boolean mHasOnNewIntentCalled;
    private boolean mHasResponseCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //解决多进程问题
        SocialShareConfiguration shareConfig = SocialShare.getShareConfiguration();
        if (shareConfig == null) {
            shareConfig = getIntent().getParcelableExtra(KEY_CONFIG);
        }
        if (shareConfig == null) {
            finishWithFailResult();
            return;
        }

        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.SINA);
        if (appConfig == null || appConfig.isEmpty() || TextUtils.isEmpty((String) appConfig.get(SharePlatformConfig.APP_KEY))) {
            String appKey = getIntent().getStringExtra(KEY_APPKEY);
            if (TextUtils.isEmpty(appKey)) {
                finishWithFailResult();
                return;
            } else {
                SocialPlatformConfigHelper.configSina(appKey);
            }
        }

        mShareHandler = new SinaShareHandler(this, shareConfig);
        try {
            mShareHandler.checkConfig();
            mShareHandler.init();
        } catch (Exception e) {
            e.printStackTrace();
            finishWithFailResult();
            return;
        }

        mShareHandler.onActivityCreated(this, savedInstanceState, mInnerListener);
        try {
            if (savedInstanceState == null) {
                BaseShareParam param = getShareParam();
                if (param == null) {
                    mInnerListener.onError(SocializeMedia.SINA, SocialShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION,
                            new ShareException("sina share param error"));
                    finishWithCancelResult();
                } else {
                    mShareHandler.share(getShareParam(), mInnerListener);
                }
            }
        } catch (Exception e) {
            mInnerListener.onError(SocializeMedia.SINA, SocialShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasOnNewIntentCalled || mHasResponseCalled) {
            return;
        }

        if (SinaShareHandler.mWeiboShareAPI != null &&
                SinaShareHandler.mWeiboShareAPI.isWeiboAppInstalled() &&
                mIsActivityResultCanceled && !isFinishing()) {
            finishWithCancelResult();
        }
    }

    private BaseShareParam getShareParam() {
        return getIntent().getParcelableExtra(KEY_PARAM);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mHasOnNewIntentCalled = true;
        mShareHandler.onActivityNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsActivityResultCanceled = resultCode == Activity.RESULT_CANCELED;
        mShareHandler.onActivityResult(this, requestCode, resultCode, data, mInnerListener);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        mHasResponseCalled = true;
        if (mShareHandler != null) {
            mShareHandler.onResponse(baseResponse);
        }
    }

    private void finishWithCancelResult() {
        finishWithResult(SocialShareStatusCode.ST_CODE_ERROR_CANCEL);
    }

    private void finishWithFailResult() {
        finishWithResult(SocialShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED);
    }

    protected SocializeListeners.ShareListenerAdapter mInnerListener = new SocializeListeners.ShareListenerAdapter() {

        @Override
        public void onStart(SocializeMedia type) {
            super.onStart(type);
        }

        @Override
        public void onComplete(SocializeMedia type, int code, Throwable error) {
            finishWithResult(code);
        }

    };

    private void finishWithResult(int code) {

        if (mShareHandler != null) {
            mShareHandler.onActivityDestroy();
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_CODE, code);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
