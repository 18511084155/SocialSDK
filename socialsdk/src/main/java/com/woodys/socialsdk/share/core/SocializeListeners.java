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

import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;

/**
 * @author woodys
 *
 * @since 2015/9/31
 */
public abstract class SocializeListeners {

    private SocializeListeners() {
    }

    public interface ShareListener {

        void onStart(SocializeMedia type);

        void onProgress(SocializeMedia type, String progressDesc);

        void onSuccess(SocializeMedia type, int code);

        void onError(SocializeMedia type, int code, Throwable error);

        void onCancel(SocializeMedia type);

    }

    public static abstract class ShareListenerAdapter implements ShareListener {

        public void onStart(SocializeMedia type) {

        }

        protected abstract void onComplete(SocializeMedia type, int code, Throwable error);

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {

        }

        public final void onSuccess(SocializeMedia type, int code) {
            onComplete(type, SocialShareStatusCode.ST_CODE_SUCCESSED, null);
        }

        public final void onError(SocializeMedia type, int code, Throwable error) {
            onComplete(type, SocialShareStatusCode.ST_CODE_ERROR, error);
        }

        public final void onCancel(SocializeMedia type) {
            onComplete(type, SocialShareStatusCode.ST_CODE_ERROR_CANCEL, null);
        }

    }

}