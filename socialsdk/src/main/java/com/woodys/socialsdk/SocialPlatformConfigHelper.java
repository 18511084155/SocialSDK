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

package com.woodys.socialsdk;

import com.woodys.socialsdk.share.core.SharePlatformConfig;
import com.woodys.socialsdk.share.core.SocializeMedia;

/**
 * @author woodys
 *
 * @since 2016/4/11
 */
public class SocialPlatformConfigHelper {

    private SocialPlatformConfigHelper() {

    }

    public static void configQQPlatform(String appId, String appKey) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.QQ, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_KEY, appKey);
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.QZONE, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_KEY, appKey);
    }

    public static void configWeixinPlatform(String appId, String appSecret) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.WEIXIN, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_SECRET, appSecret);
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.WEIXIN_MONMENT, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_SECRET, appSecret);
    }

    public static void configSina(String appKey) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.SINA, SharePlatformConfig.APP_KEY, appKey);
    }

}
