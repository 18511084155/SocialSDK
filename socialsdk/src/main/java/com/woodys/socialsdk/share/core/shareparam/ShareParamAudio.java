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
import android.os.Parcelable;

/**
 * @author woodys
 *
 * @since 2015/10/8
 * <p>
 * TODO 区分低带宽音乐/普通音乐
 */
public class ShareParamAudio extends BaseShareParam implements Parcelable {

    protected ShareAudio mAudio;

    public ShareParamAudio(String title, String content) {
        super(title, content);
    }

    public ShareParamAudio(String title, String content, String targetUrl) {
        super(title, content, targetUrl);
    }

    public ShareAudio getAudio() {
        return mAudio;
    }

    public void setAudio(ShareAudio audio) {
        mAudio = audio;
    }

    public ShareImage getThumb() {
        return mAudio == null ? null : mAudio.getThumb();
    }

    public String getAudioUrl() {
        return mAudio == null ? null : mAudio.getAudioSrcUrl();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mAudio, 0);
    }

    protected ShareParamAudio(Parcel in) {
        super(in);
        mAudio = in.readParcelable(ShareAudio.class.getClassLoader());
    }

    public static final Creator<ShareParamAudio> CREATOR = new Creator<ShareParamAudio>() {
        public ShareParamAudio createFromParcel(Parcel source) {
            return new ShareParamAudio(source);
        }

        public ShareParamAudio[] newArray(int size) {
            return new ShareParamAudio[size];
        }
    };
}
