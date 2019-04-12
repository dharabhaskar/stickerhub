package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tomlibo.stickerhub.uicomponent.stickerview.util.AutoResizeTextView;

public class StickerTextView extends StickerView {

    private AutoResizeTextView tv_main;
    private TextStickerClickListener mTextStickerClickListener;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        //tv_main.setTextSize(22);
        tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(400);
        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);
        tv_main.setMaxLines(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);

        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(View.GONE);

        if (getColorPalette() != null) {
            getColorPalette().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextStickerClickListener.onColorPaletteClicked(StickerTextView.this);
                }
            });
        }

        if (getImageViewTextEditor() != null) {
            getImageViewTextEditor().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextStickerClickListener.onTextEditorClicked(StickerTextView.this, tv_main.getText().toString());
                }
            });
        }

        return tv_main;
    }

    public void setText(String text) {
        if (tv_main != null)
            tv_main.setText(text);
    }

    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public void setTextColor(String colorCode) {
        if (tv_main != null)
            tv_main.setTextColor(Color.parseColor(colorCode));
    }

    public int getTextColor() {
        if (tv_main != null)
            return tv_main.getCurrentTextColor();

        return 0;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }

    public void setTextStickerClickListener(TextStickerClickListener textStickerClickListener) {
        this.mTextStickerClickListener = textStickerClickListener;
    }

    public interface TextStickerClickListener {
        void onColorPaletteClicked(View view);

        void onTextEditorClicked(View view, String text);
    }
}
