package com.woodys.socialsdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elbbbird.android.socialsdk.R;

/**
 * Created by woodys on 2016/12/11.
 */
public class ShareView extends LinearLayout {

    private ImageView a;
    private TextView b;
    private Drawable c;
    private String d;

    public ShareView(Context context) {
        this(context, null);
    }

    public ShareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs);
        TypedArray v1 = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShareView, defStyleAttr, 0);
        this.d = v1.getString(0);
        this.c = v1.getDrawable(1);
        LayoutInflater v2 = LayoutInflater.from(context);
        View view = v2.inflate(R.layout.view_btn_share, null);
        this.a = (ImageView) view.findViewById(R.id.view_btn_share_iv);
        this.b = (TextView) view.findViewById(R.id.view_btn_share_tv);
        this.b.setText(this.d);
        this.a.setImageDrawable(this.c);
        this.addView(view);
        v1.recycle();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.a.setOnClickListener(l);
    }
}
