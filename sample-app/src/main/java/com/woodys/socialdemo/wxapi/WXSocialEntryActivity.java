

package com.woodys.socialdemo.wxapi;

import com.woodys.socialdemo.helper.ConfigHelper;
import com.woodys.socialsdk.share.core.ui.BaseWXEntryActivity;

public class WXSocialEntryActivity extends BaseWXEntryActivity {

    @Override
    protected String getAppId() {
        return ConfigHelper.WECHAT_APPID;
    }

}