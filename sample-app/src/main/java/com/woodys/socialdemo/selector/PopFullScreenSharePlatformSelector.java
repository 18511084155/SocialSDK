/*
 * Copyright (C) 2015 Bilibili <jungly.ik@gmail.com>
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

package com.woodys.socialdemo.selector;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.woodys.socialdemo.R;


/**
 * @author woodys
 *
 * @since 2016/1/4.
 */
public class PopFullScreenSharePlatformSelector extends BaseSharePlatformSelector {

    protected PopupWindow mShareWindow;
    protected View mAnchorView;
    protected View view;

    private GridView gridView;
    private Animation enterAnimation;

    public PopFullScreenSharePlatformSelector(FragmentActivity context, View anchorView, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(context, dismissListener, itemClickListener);
        mAnchorView = anchorView;
    }

    @Override
    public void show() {
        createShareWindowIfNeed();
        if (!mShareWindow.isShowing()) {
            mShareWindow.showAtLocation(mAnchorView, Gravity.BOTTOM, 0, 0);
        }
        showEnterAnimation();
    }

    @Override
    public boolean isShow() {
        return (null!=mShareWindow && mShareWindow.isShowing());
    }

    private void showEnterAnimation() {
        if (enterAnimation == null)
            enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.socialize_shareboard_animation_in);
        view.setAnimation(enterAnimation);
        enterAnimation.start();
    }

    @Override
    public void dismiss() {
        if (mShareWindow != null) {
            mShareWindow.dismiss();
        }
    }

    @Override
    public void release() {
        dismiss();
        super.release();
        mAnchorView = null;
        mShareWindow = null;
        gridView = null;
        enterAnimation = null;
    }

    private void createShareWindowIfNeed() {
        if (mShareWindow != null)
            return;

        Context context = getContext();

        gridView = createShareGridView(context, getItemClickListener());
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(gridParams);

        view = View.inflate(context,R.layout.view_share_item,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dismiss();}
        });
        ((LinearLayout) view.findViewById(R.id.ll_container_view)).addView(gridView);
        view.findViewById(R.id.iv_share_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dismiss();}
        });

        mShareWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mShareWindow.setOutsideTouchable(true);
        mShareWindow.setAnimationStyle(-1);
        mShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getDismissListener() != null)
                    getDismissListener().onDismiss();
            }
        });
    }
}
