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
import android.text.TextUtils;

import com.woodys.socialsdk.share.core.error.ShareException;

import java.io.File;

/**
 * @author woodys
 *
 * @since 2016/2/7
 */
public abstract class AbsImageDownloader implements IImageDownloader {

    @Override
    public final void download(Context context, String imageUrl, String targetFileDirPath, OnImageDownloadListener listener) throws ShareException {
        if (TextUtils.isEmpty(imageUrl)) {
            if (listener != null) {
                listener.onFailed(imageUrl);
            }
        } else {
            String filePath = createFileIfNeed(context, imageUrl, targetFileDirPath);
            if (TextUtils.isEmpty(filePath)) {
                if (listener != null) {
                    listener.onFailed(imageUrl);
                }
                return;
            }

            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                if (listener != null) {
                    listener.onSuccess(targetFile.getAbsolutePath());
                }
                return;
            }

            downloadDirectly(imageUrl, filePath, listener);
        }
    }

    protected abstract void downloadDirectly(String imageUrl, String filePath, OnImageDownloadListener listener);

    protected String createFileIfNeed(Context context, String imageUrl, String targetFileDirPath) throws ShareException {
        if (context == null || TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(targetFileDirPath)) {
            return null;
        }

        File targetFileDir = new File(targetFileDirPath);
        String fileName = String.valueOf(imageUrl.hashCode());
        File targetFile = new File(targetFileDir, fileName);
        if (targetFile.exists()) {
            return targetFile.getAbsolutePath();
        }

        if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
            return null;
        }

        return targetFile.getAbsolutePath();
    }

}
