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

package com.woodys.socialsdk.share.download;

import android.content.Context;

import com.woodys.socialsdk.share.core.error.ShareException;

/**
 * @author woodys
 *
 * @since 2016/2/4
 */
public interface IImageDownloader {

    void download(Context context, String imageUrl, String targetFileDirPath, OnImageDownloadListener listener) throws ShareException;

    interface OnImageDownloadListener {

        void onStart();

        void onSuccess(String filePath);

        void onFailed(String url);

    }
}
