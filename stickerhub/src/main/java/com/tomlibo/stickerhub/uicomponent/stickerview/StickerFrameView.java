package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class StickerFrameView extends StickerView {

    private String owner_id;
    private AppCompatImageView iv_main;

    public StickerFrameView(Context context) {
        super(context);
    }

    public StickerFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerFrameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutParams this_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        this_params.gravity = Gravity.CENTER;
        setLayoutParams(this_params);

        //Setting layout params for main layout...
        LayoutParams iv_main_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_main_params.setMargins(0, 0, 0, 0);
        getMainView().setLayoutParams(iv_main_params);

        //Removing border
        removeView(findViewWithTag("iv_border"));

        //Removing on touch listener...
        setOnTouchListener(null);
    }

    public String getOwnerId() {
        return this.owner_id;
    }

    public void setOwnerId(String owner_id) {
        this.owner_id = owner_id;
    }

    @Override
    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new AppCompatImageView(getContext());
            this.iv_main.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(View.GONE);

        if (getImageViewColorPalette() != null)
            getImageViewColorPalette().setVisibility(View.GONE);

        if (getImageViewTextEditor() != null)
            getImageViewTextEditor().setVisibility(View.GONE);

        if (getImageViewExpane() != null)
            getImageViewExpane().setVisibility(View.GONE);

        return iv_main;
    }

    public void setImageResource(int res_id) {
        this.iv_main.setImageResource(res_id);
    }

    public void setImageDrawable(Drawable drawable) {
        this.iv_main.setImageDrawable(drawable);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.iv_main.getDrawable()).getBitmap();
    }

    public void setImageBitmap(Bitmap bmp) {
        this.iv_main.setImageBitmap(bmp);
    }
}
