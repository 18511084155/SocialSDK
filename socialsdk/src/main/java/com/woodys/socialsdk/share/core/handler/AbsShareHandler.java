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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.helper.ShareImageHelper;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.tencent.open.utils.ThreadManager;

/**
 * @author woodys
 *
 * @since 2015/10/8
 */
public abstract class AbsShareHandler implements IShareHandler {

    protected Context mContext;

    protected SocialShareConfiguration mShareConfiguration;

    private SocializeListeners.ShareListener mShareListener;

    protected ShareImageHelper mImageHelper;

    public AbsShareHandler(Activity context, SocialShareConfiguration configuration) {
        initContext(context);
        mShareConfiguration = configuration;
        mImageHelper = new ShareImageHelper(mContext, configuration, mImageCallback);
    }

    private void initContext(Activity context) {
        if (isNeedActivityContext()) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }
    }

    /**
     * 该平台是否必须用Activity类型的Context来调起分享
     */
    protected boolean isNeedActivityContext() {
        return false;
    }

    @Override
    public boolean isDisposable() {
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener) {
        initContext(activity);
        mShareListener = listener;
    }

    @Override
    public void onActivityNewIntent(Activity activity, Intent intent) {
        initContext(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        initContext(activity);
        mShareListener = listener;
    }

    @Override
    public void onActivityDestroy() {
        release();
    }

    @Override
    public void release() {
        mShareListener = null;
        mContext = null;
    }

    @Override
    public void share(BaseShareParam params, SocializeListeners.ShareListener listener) throws Exception {
        mShareListener = listener;
    }

    protected void doOnWorkThread(final Runnable runnable) {
        mShareConfiguration.getTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();

                    if (getShareListener() != null) {
                        doOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getShareListener().onError(getShareMedia(), SocialShareStatusCode.ST_CODE_SHARE_ERROR_IMAGE, new ShareException("Share failed"));
                            }
                        });
                    }
                }
            }
        });
    }

    protected void doOnMainThread(Runnable runnable) {
        ThreadManager.getMainHandler().post(runnable);
    }

    protected void postProgressStart() {
        //postProgress(R.string.social_share_sdk_share_start);
    }

    protected void postProgress(final int msgRes) {
        if (getContext() != null) {
            postProgress(getContext().getString(msgRes));
        }
    }

    protected void postProgress(final String msg) {
        doOnMainThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (getShareListener() != null) {
                            getShareListener().onProgress(getShareMedia(), msg);
                        }
                    }
                }
        );
    }

    protected SocializeListeners.ShareListener getShareListener() {
        return mShareListener;
    }

    public void setShareListener(SocializeListeners.ShareListener shareListener) {
        mShareListener = shareListener;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private ShareImageHelper.Callback mImageCallback = new ShareImageHelper.Callback() {
        @Override
        public void onProgress(int msgId) {
            postProgress(msgId);
        }

        @Override
        public void onImageDownloadFailed() {
            if (getShareListener() != null) {
                getShareListener().onError(getShareMedia(), SocialShareStatusCode.ST_CODE_SHARE_ERROR_IMAGE, new ShareException("Image compress failed"));
            }
        }
    };
}
