package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class StickerHolder extends FrameLayout {
    private static final float PARAM_MARGIN = 16;
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ImageView backgroundImageView;

    public StickerHolder(Context context) {
        super(context);
        init(context);
    }

    public StickerHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        this.setTag("StickerHolder");
        backgroundImageView = new ImageView(context);
        backgroundImageView.setTag("iv_backImage");

        int margin = convertDpToPixel(PARAM_MARGIN, getContext()) / 2;
        LayoutParams iv_main_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_main_params.setMargins(margin, margin, margin, margin);
        addView(backgroundImageView, iv_main_params);

        setOnTouchListener(mOnTouchListener);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (view.getTag().equals("StickerHolder")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        hideControlsOfAllChildStickerView();
                }
            }
            return true;
        }
    };

    private void hideControlsOfAllChildStickerView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof StickerView) {
                ((StickerView) view).hideControls();
            }
        }
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }

    public void addImageSticker(Bitmap bitmap) {
        StickerImageView stickerImageView = new StickerImageView(mContext);
        stickerImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        addView(stickerImageView);
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
