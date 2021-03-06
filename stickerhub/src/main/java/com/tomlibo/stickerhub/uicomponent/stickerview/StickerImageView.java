package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class StickerImageView extends StickerView {

    private String owner_id;
    private AppCompatImageView iv_main;

    public StickerImageView(Context context) {
        super(context);
    }

    public StickerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

        if (getImageViewColorPalette() != null)
            getImageViewColorPalette().setVisibility(View.GONE);

        if (getImageViewDone() != null)
            getImageViewDone().setVisibility(View.GONE);

        if (getImageViewTextEditor() != null)
            getImageViewTextEditor().setVisibility(View.GONE);

        return iv_main;
    }

    protected void changeControlVisibility(boolean visible) {
        super.changeControlVisibility(visible);
        iv_scale.setVisibility(visible ? VISIBLE : GONE);
        iv_delete.setVisibility(visible ? VISIBLE : GONE);
        iv_flip.setVisibility(visible ? VISIBLE : GONE);
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
