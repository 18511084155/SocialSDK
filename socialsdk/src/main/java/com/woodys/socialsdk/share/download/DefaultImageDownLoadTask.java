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

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.woodys.socialsdk.util.FileUtil;
import com.woodys.socialsdk.util.IOUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author woodys
 *
 * @since 2015/10/26 11:04
 */
public class DefaultImageDownLoadTask extends Thread {
    private static final String TEMP_EXTENSION = ".temp";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private String mDownloadUrl;
    private String mFilePath;

    private IImageDownloader.OnImageDownloadListener mDownLoadListener;

    public DefaultImageDownLoadTask(String downloadUrl, String filePath, IImageDownloader.OnImageDownloadListener downLoadListener) {
        mDownloadUrl = downloadUrl;
        mFilePath = filePath;
        mDownLoadListener = downLoadListener;
    }

    @Override
    public void run() {
        super.run();
        publishProgress(0);
        File tmpFile = new File(mFilePath + TEMP_EXTENSION);
        File parentFile = tmpFile.getParentFile();
        if (!parentFile.isDirectory() || (!parentFile.exists() && !parentFile.mkdirs())) {
            onPostExecute(null);
            return;
        }
        HttpURLConnection conn;
        try {
            URL url = new URL(mDownloadUrl);
            conn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            onPostExecute(null);
            return;
        }
        try {
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Referer", mDownloadUrl);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            int code = conn.getResponseCode();
            OutputStream out = null;
            InputStream in = null;

            try {
                if (code == HttpURLConnection.HTTP_OK) {
                    out = new BufferedOutputStream(new FileOutputStream(tmpFile));
                    in = conn.getInputStream();
                    IOUtil.copyLarge(in, out);
                } else
                    mFilePath = null;
            } catch (IOException e) {
                mFilePath = null;
            } finally {
                IOUtil.closeQuietly(out);
                IOUtil.closeQuietly(in);
            }
            if (mFilePath != null) {
                File file = new File(mFilePath);
                if (!tmpFile.renameTo(file)) {
                    FileUtil.copyFile(tmpFile, file);
                    tmpFile.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mFilePath = null;
        } finally {
            conn.disconnect();
        }

        onPostExecute(mFilePath);
    }

    protected void publishProgress(final Integer... values) {
        if (values != null && values.length > 0 && values[0] == 0 && mDownLoadListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownLoadListener.onStart();
                }
            });
        }
    }

    protected void onPostExecute(final String filePath) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mFilePath)) {
                    onDownloadFailed();
                } else {
                    onDownloadSuccess();
                }
            }
        });
    }

    private void onDownloadFailed() {
        if (mDownLoadListener != null) {
            mDownLoadListener.onFailed(mDownloadUrl);
        }
    }

    private void onDownloadSuccess() {
        if (mDownLoadListener != null) {
            mDownLoadListener.onSuccess(mFilePath);
        }
    }

}
