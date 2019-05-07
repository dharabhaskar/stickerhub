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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.tomlibo.stickerhub.model.OverlayAction;

import java.util.Stack;

public class StickerOverlayView extends StickerView {

    private Stack<OverlayAction> actions;
    private Stack<OverlayAction> undoActions;
    private DoneClickListener mDoneClickListener;
    protected AppCompatImageView iv_main;

    public StickerOverlayView(Context context) {
        super(context);
    }

    public StickerOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerOverlayView(Context context, AttributeSet attrs, int defStyle) {
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

        actions = new Stack<>();
        undoActions = new Stack<>();
    }

    @Override
    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new AppCompatImageView(getContext());
            this.iv_main.setScaleType(ImageView.ScaleType.FIT_XY);
        }

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
                    mDoneClickListener.onDoneClicked();
                }
            });
        }

        return iv_main;
    }

    @Override
    protected void changeControlVisibility(boolean visible) {
        super.changeControlVisibility(visible);
        iv_delete.setVisibility(visible ? VISIBLE : GONE);
        iv_done.setVisibility(visible ? VISIBLE : GONE);
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

    public void setAlpha(Float alpha) {
        iv_main.setAlpha(alpha);
    }

    public void setColor(Integer color) {
        setColor(color, true);
    }

    public void setColor(int color, boolean saveAction) {
        iv_main.setColorFilter(color);
        if (saveAction) {
            undoActions.clear();
            actions.push(new OverlayAction(OverlayAction.CHANGE_COLOR, color));
        }
    }

    public void saveAlpha(float alpha) {
        undoActions.clear();
        actions.push(new OverlayAction(OverlayAction.CHANGE_ALPHA, alpha));
    }

    private void applyAction(@NonNull OverlayAction action) {
        switch (action.getActionType()) {
            case OverlayAction.CHANGE_ALPHA:
                setAlpha((Float) action.getActionValue());
                break;
            case OverlayAction.CHANGE_COLOR:
                setColor((Integer) action.getActionValue(), false);
        }
    }

    public void undo() {
        if (!actions.empty()) {
            undoActions.push(actions.pop());
            applyAction(undoActions.peek());
        }
    }

    public void redo() {
        if (!undoActions.isEmpty()) {
            actions.push(undoActions.pop());
            applyAction(actions.peek());
        }
    }

    public void setDoneClickListener(DoneClickListener doneClickListener) {
        this.mDoneClickListener = doneClickListener;
    }

    public interface DoneClickListener {
        void onDoneClicked();
    }
}
