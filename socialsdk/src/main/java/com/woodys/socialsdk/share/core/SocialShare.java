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

import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;


/**
 * @author woodys
 *
 * @since 2015/9/31
 */
public class SocialShare {

    private static final SocialShareAttach sShareAttach = new SocialShareAttach();

    private SocialShare() {
    }

    /**
     * 分享之前必须先初始化.
     *
     * @param configuration
     */
    public static void init(SocialShareConfiguration configuration) {
        sShareAttach.init(configuration);
    }

    /**
     * 分享的入口，开启搞比利的大门。
     */
    public static void share(Activity activity, SocializeMedia type, BaseShareParam params, SocializeListeners.ShareListener listener) {
        sShareAttach.share(activity, type, params, listener);
    }

    /**
     * 顾名思义
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        sShareAttach.onActivityResult(activity, requestCode, resultCode, data);
    }

    public static SocialShareConfiguration getShareConfiguration() {
        return sShareAttach.getShareConfiguration();
    }

}
