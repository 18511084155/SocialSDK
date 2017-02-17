

package com.woodys.socialdemo.helper;


import com.woodys.socialsdk.SocialPlatformConfigHelper;
import com.woodys.socialsdk.share.core.SharePlatformConfig;

/**
 * @author woodys
 *
 * @since 2015/10/8
 */
public class ConfigHelper {

    // FIXME: 16/4/12
    public static final String QQ_APPID = "";
    public static final String QQ_APPKEY = "";
    public static final String WECHAT_APPID = "";
    public static final String WECHAT_APPSECRET = "";
    public static final String SINA_APPKEY = "";

    private ConfigHelper() {
    }

    public static void configPlatformsIfNeed() {
        if (SharePlatformConfig.hasAlreadyConfig())
            return;

        SocialPlatformConfigHelper.configQQPlatform(QQ_APPID, QQ_APPKEY);
        SocialPlatformConfigHelper.configWeixinPlatform(WECHAT_APPID, WECHAT_APPSECRET);
        SocialPlatformConfigHelper.configSina(SINA_APPKEY);

    }
}
