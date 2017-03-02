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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import java.lang.reflect.Field;

/**
 * @author woodys
 *
 * @since 2016/1/4.
 */
public class DialogSharePlatformSelector extends BaseSharePlatformSelector {

    private final String mShareDialogTag;
    private ShareDialogFragment mShareDialog;

    public DialogSharePlatformSelector(FragmentActivity context, OnShareSelectorDismissListener dismissListener, AdapterView.OnItemClickListener itemClickListener) {
        super(context, dismissListener, itemClickListener);
        mShareDialogTag = "share.dialog" + context.getComponentName().getShortClassName();
    }

    @Override
    public void show() {
        FragmentActivity context = getContext();
        if (mShareDialog == null && (mShareDialog = (ShareDialogFragment) context.getSupportFragmentManager().findFragmentByTag(mShareDialogTag)) == null) {
            mShareDialog = new ShareDialogFragment();
        }
        mShareDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (getDismissListener() != null)
                    getDismissListener().onDismiss();
            }
        });
        mShareDialog.setOnItemClickListener(getItemClickListener());
        mShareDialog.show(context.getSupportFragmentManager(), mShareDialogTag);
    }

    @Override
    public boolean isShow() {
        return null!=mShareDialog && mShareDialog.isShow();
    }

    @Override
    public void dismiss() {
        if (mShareDialog != null)
            mShareDialog.dismissAllowingStateLoss();
    }

    @Override
    public void release() {
        if (mShareDialog != null && mShareDialog.getActivity() == null)
            return;
        dismiss();
        mShareDialog = null;
        super.release();
    }

    public static class ShareDialogFragment extends DialogFragment {
        private AdapterView.OnItemClickListener mShareItemClick;
        private DialogInterface.OnDismissListener mDismiss;

        @Override
        public void show(FragmentManager manager, String tag) {
            if(isShow()){return;}
            super.show(manager, tag);
        }

        public boolean isShow(){
            Boolean show = null;
            try {
                Field field = DialogFragment.class.getDeclaredField("mShownByMe");
                if (field != null) {
                    field.setAccessible(true);
                    show = field.getBoolean(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (show != null && show);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // use alert Dialog Theme here
            final TypedValue outValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.alertDialogTheme, outValue, true);
            int theme = outValue.resourceId;
            setStyle(STYLE_NORMAL, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            GridView grid = createShareGridView(inflater.getContext(), mShareItemClick);
            grid.setBackgroundColor(0xffffffff);
            return grid;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setOnDismissListener(mDismiss);
            return dialog;
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener click) {
            mShareItemClick = click;
        }

        public void setOnDismissListener(DialogInterface.OnDismissListener dismiss) {
            mDismiss = dismiss;
        }
    }

}
