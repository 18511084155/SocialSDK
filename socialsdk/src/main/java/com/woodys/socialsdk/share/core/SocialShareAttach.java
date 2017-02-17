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

package com.woodys.socialsdk.share.core;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.woodys.socialsdk.share.core.error.ShareException;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.handler.IShareHandler;
import com.woodys.socialsdk.share.core.handler.ShareHandlerPool;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;

/**
 * @author woodys
 *
 * @since 2015/9/31 16:57
 */
class SocialShareAttach {

    private IShareHandler mCurrentHandler;

    private SocialShareConfiguration mShareConfiguration;

    private SocializeListeners.ShareListener mOuterShareListener;

    public void init(SocialShareConfiguration configuration) {
        mShareConfiguration = configuration;
    }

    public void share(Activity activity, SocializeMedia type, BaseShareParam params, SocializeListeners.ShareListener listener) {

        if (mShareConfiguration == null) {
            throw new IllegalArgumentException("SocialShareConfiguration must be initialized before share");
        }

        if (mCurrentHandler != null) {
            release(mCurrentHandler.getShareMedia());
        }

        mCurrentHandler = ShareHandlerPool.newHandler(activity, type, mShareConfiguration);

        if (mCurrentHandler != null) {
            try {
                mOuterShareListener = listener;

                if (params == null) {
                    throw new IllegalArgumentException(("Share param cannot be null"));
                }

                mInnerProxyListener.onStart(type);
                mCurrentHandler.share(params, mInnerProxyListener);

                if (mCurrentHandler.isDisposable()) {
                    release(mCurrentHandler.getShareMedia());
                }

            } catch (ShareException e) {
                e.printStackTrace();
                mInnerProxyListener.onError(type, e.getCode(), e);
            } catch (Exception e) {
                mInnerProxyListener.onError(type, SocialShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, e);
                e.printStackTrace();
            }
        } else {
            mInnerProxyListener.onError(type, SocialShareStatusCode.ST_CODE_SHARE_ERROR_UNEXPLAINED, new Exception("Unknown share type"));
        }
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (mCurrentHandler != null) {
            mCurrentHandler.onActivityResult(activity, requestCode, resultCode, data, mInnerProxyListener);
        }
    }

    private void release(SocializeMedia type) {
        mOuterShareListener = null;
        if (mCurrentHandler != null) {
            mCurrentHandler.release();
        }
        mCurrentHandler = null;
        ShareHandlerPool.remove(type);
    }

    public SocialShareConfiguration getShareConfiguration() {
        return mShareConfiguration;
    }

    private IShareHandler.InnerShareListener mInnerProxyListener = new IShareHandler.InnerShareListener() {

        @Override
        public void onStart(SocializeMedia type) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onStart(type);
            }
        }

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {
            if (mCurrentHandler != null && mCurrentHandler.getContext() != null) {
                Toast.makeText(mCurrentHandler.getContext(), progressDesc, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSuccess(SocializeMedia type, int code) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onSuccess(type, code);
            }
            release(type);
        }

        @Override
        public void onError(SocializeMedia type, int code, Throwable error) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onError(type, code, error);
            }
            release(type);
        }

        @Override
        public void onCancel(SocializeMedia type) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onCancel(type);
            }
            release(type);
        }

    };
}