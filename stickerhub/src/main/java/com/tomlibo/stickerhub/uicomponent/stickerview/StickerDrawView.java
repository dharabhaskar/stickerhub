package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tomlibo.stickerhub.uicomponent.DrawingView;

public class StickerDrawView extends StickerView {

    private DrawingView drawingView;
    private DrawingDoneClickListener mDrawingDoneClickListener;

    public StickerDrawView(Context context) {
        super(context);
    }

    public StickerDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerDrawView(Context context, AttributeSet attrs, int defStyle) {
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
        LayoutParams drawing_view_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        drawing_view_params.setMargins(0, 0, 0, 0);
        getMainView().setLayoutParams(drawing_view_params);

        //Removing border
        removeView(findViewWithTag("iv_border"));

        //Removing on touch listener...
        setOnTouchListener(null);
    }

    @Override
    protected View getMainView() {
        if (drawingView != null)
            return drawingView;

        drawingView = new DrawingView(getContext());
        drawingView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        if (getImageViewExpand() != null)
            getImageViewExpand().setVisibility(View.GONE);

        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(View.GONE);

        if (getImageViewColorPalette() != null)
            getImageViewColorPalette().setVisibility(View.GONE);

        if (getImageViewTextEditor() != null)
            getImageViewTextEditor().setVisibility(View.GONE);

        if (getImageViewDone() != null) {
            getImageViewDone().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawingDoneClickListener.onDoneClicked();
                    drawingView.setTouchable(false);
                }
            });
        }

        return drawingView;
    }

    @Override
    protected void changeControlVisibility(boolean visible) {
        super.changeControlVisibility(visible);
        iv_delete.setVisibility(visible ? VISIBLE : GONE);
        iv_done.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setDrawingDoneClickListener(DrawingDoneClickListener drawingDoneClickListener) {
        this.mDrawingDoneClickListener = drawingDoneClickListener;
    }

    public interface DrawingDoneClickListener {
        void onDoneClicked();
    }
}
