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
import android.os.Bundle;

/**
 * 分享回调
 *
 * @author woodys
 *
 * @since 2015/10/12
 */
public interface IActivityLifecycleMirror {

    void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener);

    void onActivityNewIntent(Activity activity, Intent intent);

    void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener);

    void onActivityDestroy();

}