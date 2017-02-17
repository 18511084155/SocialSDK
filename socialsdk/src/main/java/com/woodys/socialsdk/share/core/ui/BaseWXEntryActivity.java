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

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.woodys.socialsdk.share.core.SocialShareConfiguration;
import com.woodys.socialsdk.share.core.SocializeListeners;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;
import com.woodys.socialsdk.share.core.handler.AbsShareHandler;
import com.woodys.socialsdk.share.core.handler.IShareHandler;
import com.woodys.socialsdk.share.core.handler.ShareHandlerPool;
import com.woodys.socialsdk.share.core.handler.wx.BaseWxShareHandler;
import com.woodys.socialsdk.share.core.handler.wx.WxChatShareHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.woodys.socialsdk.sso.wechat.WeChatSSOProxy;

/**
 * @author woodys
 *
 * @since 2015/10/8
 */
public abstract class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;
    private BaseWxShareHandler mShareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IShareHandler wxHandler = ShareHandlerPool.getCurrentHandler(SocializeMedia.WEIXIN);
        if (wxHandler == null) {
            wxHandler = ShareHandlerPool.getCurrentHandler(SocializeMedia.WEIXIN_MONMENT);
        }
        if (wxHandler == null) {
            wxHandler = new WxChatShareHandler(this, new SocialShareConfiguration.Builder(this).build());
            ((AbsShareHandler) wxHandler).setShareListener(mShareListener);
            initWXApi();
        }

        mShareHandler = (BaseWxShareHandler) wxHandler;

        if (isAutoCreateWXAPI() && mIWXAPI == null) {
            initWXApi();
        }
    }

    private void initWXApi() {
        mIWXAPI = WXAPIFactory.createWXAPI(this, getAppId(), true);
        if (mIWXAPI.isWXAppInstalled()) {
            mIWXAPI.registerApp(getAppId());
        }
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (mShareHandler != null) {
            mShareHandler.onReq(baseReq);
        }
        if (isAutoFinishAfterOnReq()) {
            finish();
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            WeChatSSOProxy.authComplete((SendAuth.Resp) resp);
        }else if (resp instanceof SendMessageToWX.Resp) {
            if (mShareHandler != null) {
                mShareHandler.onResp(resp);
            }
        }
        if (isAutoFinishAfterOnResp()) {
            finish();
        }
    }

    private SocializeListeners.ShareListener mShareListener = new SocializeListeners.ShareListener() {
        @Override
        public void onStart(SocializeMedia type) {

        }

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {

        }

        @Override
        public void onSuccess(SocializeMedia type, int code) {
            sendResult(SocialShareStatusCode.ST_CODE_SUCCESSED);
        }

        @Override
        public void onError(SocializeMedia type, int code, Throwable error) {
            sendResult(SocialShareStatusCode.ST_CODE_ERROR);
        }

        @Override
        public void onCancel(SocializeMedia type) {
            sendResult(SocialShareStatusCode.ST_CODE_ERROR_CANCEL);
        }

        private void sendResult(int statusCode) {
            if (mShareHandler != null) {
                mShareHandler.release();
            }
            Intent intent = new Intent(BaseWxShareHandler.ACTION_RESULT);
            intent.putExtra(BaseWxShareHandler.BUNDLE_STATUS_CODE, statusCode);
            sendBroadcast(intent);
        }
    };

    protected boolean isAutoFinishAfterOnReq() {
        return true;
    }

    protected boolean isAutoFinishAfterOnResp() {
        return true;
    }

    protected boolean isAutoCreateWXAPI() {
        return true;
    }

    protected abstract String getAppId();

}
