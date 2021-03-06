package com.woodys.socialdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.connect.common.Constants;
import com.woodys.socialsdk.SocialSDK;
import com.woodys.socialsdk.model.SocialInfo;
import com.woodys.socialsdk.share.view.ShareView;
import com.woodys.socialsdk.sso.SocialSSOProxy;

/**
 * 社交授权activity
 * Created by woodys on 2016/11/20.
 */
public class SocialOauthActivity extends Activity {

    private static final String TAG = "SocialOauthActivity";
    private static boolean DEBUG = SocialSDK.isDebugModel();

    private SocialInfo info;
    private ShareView llWeibo;
    private ShareView llWeChat;
    private ShareView llQQ;

    /**
     * type=0, 用户选择QQ或者微博登录
     * type=1，用户选择微信登录
     */
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social_oauth);

        info = (SocialInfo) getIntent().getExtras().getSerializable("info");

        llWeibo = (ShareView) findViewById(R.id.social_oauth_sb_weibo);
        llWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginWeibo(SocialOauthActivity.this, info);
            }
        });
        llWeChat = (ShareView) findViewById(R.id.social_oauth_sb_wechat);
        llWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginWeChat(SocialOauthActivity.this, info);
                type = 1;
            }
        });
        llQQ = (ShareView) findViewById(R.id.social_oauth_sb_qq);
        llQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSSOProxy.loginQQ(SocialOauthActivity.this, info);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.oauthWeiboCallback(SocialOauthActivity.this, requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            SocialSDK.oauthQQCallback(requestCode, resultCode, data);
        }

        if (type == 0) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (type == 1) {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.snack_out);
    }
}
