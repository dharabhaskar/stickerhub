package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tomlibo.stickerhub.uicomponent.stickerview.util.AutoResizeTextView;

public class StickerTextView extends StickerView {

    private AutoResizeTextView tv_main;
    private TextStickerClickListener mTextStickerClickListener;
    private Typeface currentTypeFace = null;
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(400);
        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);

        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(View.GONE);

        if (getImageViewDone() != null)
            getImageViewDone().setVisibility(View.GONE);

        if (getImageViewColorPalette() != null) {
            getImageViewColorPalette().setOnClickListener(new OnClickListener() {
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

    @Override
    protected void changeControlVisibility(boolean visible) {
        super.changeControlVisibility(visible);
        iv_color_palette.setVisibility(visible ? VISIBLE : GONE);
        iv_text_editor.setVisibility(visible ? VISIBLE : GONE);
    }

    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public void setText(String text) {
        if (tv_main != null)
            tv_main.setText(text);
    }

    public int getTextColor() {
        if (tv_main != null)
            return tv_main.getCurrentTextColor();

        return 0;
    }

    public void setTextColor(String colorCode) {
        if (tv_main != null)
            tv_main.setTextColor(Color.parseColor(colorCode));
    }

    public void setFontTypeFace(Typeface fontTypeFace) {
        currentTypeFace = fontTypeFace;
        changeTextStyle();
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
        changeTextStyle();
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
        changeTextStyle();
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean underline) {
        isUnderline = underline;
        changeTextStyle();
    }

    private void changeTextStyle() {
        if (isUnderline) {
            tv_main.setPaintFlags(tv_main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            tv_main.setPaintFlags(tv_main.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        }

        if (isBold && isItalic) {
            tv_main.setTypeface(currentTypeFace, Typeface.BOLD_ITALIC);
        } else if (isBold) {
            tv_main.setTypeface(currentTypeFace, Typeface.BOLD);
        } else if (isItalic) {
            tv_main.setTypeface(currentTypeFace, Typeface.ITALIC);
        } else {
            tv_main.setTypeface(currentTypeFace, Typeface.NORMAL);
        }
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
