package com.woodys.socialsdk.share.wechat;

/**
 * Created by woodys on 2016/7/21.
 */
public interface IWXShareCallback {

    void onSuccess();

    void onCancel();

    void onFailure(Exception e);
}
