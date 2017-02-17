

package com.woodys.socialdemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.woodys.socialdemo.helper.ShareHelper;
import com.woodys.socialsdk.share.core.SocialShare;
import com.woodys.socialsdk.share.core.error.SocialShareStatusCode;


/**
 * Share Helper Activity
 *
 * @author yrom
 */
public abstract class BaseShareableActivity extends FragmentActivity implements ShareHelper.Callback {
    protected ShareHelper mShare;

    public void startShare(@Nullable View anchor) {
        startShare(anchor, false);
    }

    public void startShare(@Nullable View anchor, boolean isWindowFullScreen) {
        if (mShare == null) {
            mShare = ShareHelper.instance(this, this);
        }
        if (anchor == null) {
            mShare.showShareDialog();
        } else {
            if (isWindowFullScreen)
                mShare.showShareFullScreenWindow(anchor);
            else
                mShare.showShareWarpWindow(anchor);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialShare.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mShare != null) {
            mShare.reset(); // reset held instance
            mShare = null;
        }
        super.onDestroy();
    }

    @Override
    public void onShareStart(ShareHelper helper) {

    }

    @Override
    public void onShareComplete(ShareHelper helper, int code) {
        if (code == SocialShareStatusCode.ST_CODE_SUCCESSED)
            Toast.makeText(this, R.string.social_share_sdk_share_success, Toast.LENGTH_SHORT).show();
        else if (code == SocialShareStatusCode.ST_CODE_ERROR)
            Toast.makeText(this, R.string.social_share_sdk_share_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(ShareHelper helper) {
    }

}
