/*
 * Copyright (c) 2015. SocialSocial Inc.
 */

package com.woodys.socialsdk.util;

import android.os.Build;

/**
 * @author woodys
 *
 * @since 2016/4/11
 */
public class BuildHelper {
    public static int HONEYCOMB = 11;

    public static boolean isApi11_HoneyCombOrLater() {
        return getSDKVersion() >= HONEYCOMB;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

}
