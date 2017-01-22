package com.woodys.socialsdk.sso.wechat;

import com.woodys.socialsdk.model.SocialToken;
import com.woodys.socialsdk.model.SocialUser;

/**
 * 微信授权回调接口
 * <p>
 * Created by woodys on 2016/7/11.
 */
public interface IWXCallback {
    void onGetCodeSuccess(String code);

    void onGetTokenSuccess(SocialToken token);

    void onGetUserInfoSuccess(SocialUser user);

    void onFailure(Exception e);

    void onCancel();
}
