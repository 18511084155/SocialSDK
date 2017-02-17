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


package com.woodys.socialsdk.share.core.shareparam;

import android.os.Parcel;

/**
 * @author woodys
 *
 * @since 2015/10/7 22:13
 */
public class ShareParamVideo extends BaseShareParam {

    protected ShareVideo mVideo;

    public ShareParamVideo() {
    }

    public ShareParamVideo(String title, String content) {
        super(title, content);
    }

    public ShareParamVideo(String title, String content, String targetUrl) {
        super(title, content, targetUrl);
    }

    public ShareVideo getVideo() {
        return mVideo;
    }

    public void setVideo(ShareVideo video) {
        mVideo = video;
    }

    public ShareImage getThumb() {
        return mVideo == null ? null : mVideo.getThumb();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mVideo, flags);
    }

    protected ShareParamVideo(Parcel in) {
        super(in);
        mVideo = in.readParcelable(ShareVideo.class.getClassLoader());
    }

    public static final Creator<ShareParamVideo> CREATOR = new Creator<ShareParamVideo>() {
        public ShareParamVideo createFromParcel(Parcel source) {
            return new ShareParamVideo(source);
        }

        public ShareParamVideo[] newArray(int size) {
            return new ShareParamVideo[size];
        }
    };
}
