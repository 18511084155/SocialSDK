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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.woodys.socialdemo.R;

/**
 * @author woodys
 *
 * @since 2016/1/4.
 */
public class PopWrapSharePlatformSelector extends BaseSharePlatformSelector {
    protected PopupWindow mShareWindow;
    protected View mAnchorView;

    public PopWrapSharePlatformSelector(FragmentActivity context, View anchorView, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(context, dismissListener, itemClickListener);
        mAnchorView = anchorView;
    }

    @Override
    public void show() {
        createShareWindowIfNeed();
        if (!mShareWindow.isShowing()) {
            mShareWindow.showAtLocation(mAnchorView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public boolean isShow() {
        return (null!=mShareWindow && mShareWindow.isShowing());
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
        mShareWindow = null;
        super.release();
        mAnchorView = null;
    }

    protected void createShareWindowIfNeed() {
        if (mShareWindow != null)
            return;

        Context context = getContext();
        GridView grid = createShareGridView(context, getItemClickListener());
        mShareWindow = new PopupWindow(grid, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mShareWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mShareWindow.setOutsideTouchable(true);
        mShareWindow.setAnimationStyle(R.style.socialize_shareboard_animation);
        mShareWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getDismissListener() != null)
                    getDismissListener().onDismiss();
            }
        });
    }

}
