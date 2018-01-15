package com.woodys.socialsdk;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 之所以抽取WeChat类，是因为IWXAPI需要在SSO授权和分享同时用到
 * Created by woodys on 2016/11/24.
 */
public class WeChat {
    private static IWXAPI weChatApi;

    public static IWXAPI getIWXAPIInstance(Context context, String appId) {
        if (null == weChatApi) {
            weChatApi = WXAPIFactory.createWXAPI(context, appId, true);
            weChatApi.registerApp(appId);
        }
        return weChatApi;
    }

    public static IWXAPI getIWXAPIInstance() {
        return weChatApi;
    }
}
