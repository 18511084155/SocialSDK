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

import java.util.HashMap;
import java.util.Map;

/**
 * @author woodys
 *
 * @since 2015/9/31 17:51
 */
public class SharePlatformConfig {

    public static final String APP_ID = "appId";
    public static final String APP_KEY = "appKey";
    public static final String APP_SECRET = "AppSecret";

    private static HashMap<SocializeMedia, Map<String, Object>> CONFIG = new HashMap<>();

    public static boolean hasAlreadyConfig() {
        return !CONFIG.isEmpty();
    }

    public static void addPlatformDevInfo(SocializeMedia media, HashMap<String, Object> value) {
        CONFIG.put(media, value);
    }

    public static void addPlatformDevInfo(SocializeMedia media, String... appInfo) {
        if (appInfo == null || appInfo.length % 2 != 0) {
            throw new RuntimeException("Please check your share app config info");
        }

        HashMap<String, Object> infoMap = new HashMap<>();
        int length = appInfo.length / 2;
        for (int i = 0; i < length; i++) {
            infoMap.put(appInfo[i * 2], appInfo[i * 2 + 1]);
        }
        addPlatformDevInfo(media, infoMap);
    }

    public static Map<String, Object> getPlatformDevInfo(SocializeMedia media) {
        return CONFIG.get(media);
    }

}
