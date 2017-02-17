

package com.woodys.socialdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.woodys.socialdemo.helper.ShareHelper;
import com.woodys.socialsdk.share.core.SocializeMedia;
import com.woodys.socialsdk.share.core.shareparam.BaseShareParam;
import com.woodys.socialsdk.share.core.shareparam.ShareAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamAudio;
import com.woodys.socialsdk.share.core.shareparam.ShareParamImage;
import com.woodys.socialsdk.share.core.shareparam.ShareParamText;
import com.woodys.socialsdk.share.core.shareparam.ShareParamVideo;
import com.woodys.socialsdk.share.core.shareparam.ShareParamWebPage;
import com.woodys.socialsdk.share.core.shareparam.ShareVideo;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 这个类只是为了演示如何分享，如果想体验分享效果，请下载哔哩哔哩动画app。
 *
 * @author woodys
 *
 * @since 2016/4/13
 */
public class ShareMainActivity extends BaseShareableActivity {
    private static final String TITLE = "哔哩哔哩2016拜年祭";
    private static final String CONTENT = "【哔哩哔哩2016拜年祭】 UP主: 哔哩哔哩弹幕网 #哔哩哔哩动画# ";
    private static final String TARGET_URL = "http://www.bilibili.com/video/av3521416";
    private static final String IMAGE_URL = "http://i2.hdslb.com/320_200/video/85/85ae2b17b223a0cd649a49c38c32dd10.jpg";

    private RadioButton mTextRB, mImageRB, mWebPageRB, mAudioRB, mVideoRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_all);
        ButterKnife.bind(this);
        setUpViews();
    }

    private void setUpViews() {
        mTextRB = (RadioButton) findViewById(R.id.text);
        mImageRB = (RadioButton) findViewById(R.id.image);
        mWebPageRB = (RadioButton) findViewById(R.id.webpage);
        mAudioRB = (RadioButton) findViewById(R.id.audio);
        mVideoRB = (RadioButton) findViewById(R.id.video);
    }

    @Override
    public BaseShareParam getShareContent(ShareHelper helper, SocializeMedia target) {
        BaseShareParam param;
        if (mImageRB.isChecked()) {
            param = new ShareParamImage(TITLE, CONTENT, TARGET_URL);
            ShareParamImage paramImage = (ShareParamImage) param;
            paramImage.setImage(generateImage());
        } else if (mWebPageRB.isChecked()) {
            param = new ShareParamWebPage(TITLE, CONTENT, TARGET_URL);
            ShareParamWebPage paramWebPage = (ShareParamWebPage) param;
            paramWebPage.setThumb(generateImage());
        } else if (mAudioRB.isChecked()) {
            param = new ShareParamAudio(TITLE, CONTENT, TARGET_URL);
            ShareParamAudio paramAudio = (ShareParamAudio) param;
            ShareAudio audio = new ShareAudio(generateImage(), TARGET_URL, TITLE);
            paramAudio.setAudio(audio);
        } else if (mVideoRB.isChecked()) {
            param = new ShareParamVideo(TITLE, CONTENT, TARGET_URL);
            ShareParamVideo paramVideo = (ShareParamVideo) param;
            ShareVideo video = new ShareVideo(generateImage(), TARGET_URL, TITLE);
            paramVideo.setVideo(video);
        } else {
            param = new ShareParamText(TITLE, CONTENT, TARGET_URL);
        }

        if (target == SocializeMedia.SINA)
            param.setContent(String.format(Locale.CHINA, "%s #哔哩哔哩动画# ", CONTENT));
        else if (target == SocializeMedia.GENERIC || target == SocializeMedia.COPY) {
            param.setContent(CONTENT + " " + TARGET_URL);
        }

        return param;
    }

    private ShareImage generateImage() {
        //ShareImage image = new ShareImage(file);
        //ShareImage image = new ShareImage(bitmap);
        //ShareImage image = new ShareImage(resId);
        ShareImage image = new ShareImage(IMAGE_URL);
        return image;
    }

    @OnClick(R.id.btn1)
    void shareWithDialogSelector() {
        startShare(null);
    }

    @OnClick(R.id.btn2)
    void shareWithFullPopSelector(View clickView) {
        startShare(clickView, true);
    }

    @OnClick(R.id.btn3)
    void shareWithWrapPopSelector(View clickView) {
        startShare(clickView, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
