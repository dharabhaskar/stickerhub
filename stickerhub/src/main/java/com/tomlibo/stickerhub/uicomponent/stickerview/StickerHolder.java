package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tomlibo.stickerhub.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

public class StickerHolder extends FrameLayout {
    private static final float PARAM_MARGIN = 16;
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private AppCompatImageView backgroundImageView;
    private View viewTextEditor;
    private LinearLayout layoutTextEditor;
    private AppCompatEditText etTextEditor;
    private AppCompatImageButton btDone;
    private StickerTextView currentStickerTextView;
    private Timer timer;
    private final long DELAY = 1000;

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

        // add background image view
        backgroundImageView = new AppCompatImageView(context);
        backgroundImageView.setTag("iv_backImage");
        backgroundImageView.setAdjustViewBounds(true);

        LayoutParams iv_main_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        iv_main_params.gravity = Gravity.CENTER;
        addView(backgroundImageView, iv_main_params);

        // add text editor view layout
        viewTextEditor = LayoutInflater.from(mContext).inflate(R.layout.layout_text_editor, this, false);
        viewTextEditor.setTag("viewTextEditor");

        layoutTextEditor = viewTextEditor.findViewById(R.id.layoutTextEditor);
        etTextEditor = viewTextEditor.findViewById(R.id.etTextEditor);
        btDone = viewTextEditor.findViewById(R.id.btDone);

        LayoutParams view_textEditor_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        view_textEditor_params.gravity = Gravity.BOTTOM;
        addView(viewTextEditor, view_textEditor_params);

        timer = new Timer();

        setOnTouchListener(mOnTouchListener);

        etTextEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentStickerTextView.setText(String.valueOf(s));
                            }
                        });
                    }
                }, DELAY);
            }
        });

        btDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textEditorControlVisibility(false);
            }
        });
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

    public void hideControlsOfAllChildStickerView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof StickerView) {
                ((StickerView) view).hideControls();
            }
        }

        textEditorControlVisibility(false);
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }

    public void addImageSticker(Bitmap bitmap) {
        hideControlsOfAllChildStickerView();

        StickerImageView stickerImageView = new StickerImageView(mContext);
        stickerImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        addView(stickerImageView);
    }

    public void addTextSticker() {
        hideControlsOfAllChildStickerView();

        StickerTextView stickerTextView = new StickerTextView(mContext);
        stickerTextView.setText("Your Text");
        addView(stickerTextView);

        stickerTextView.setTextStickerClickListener(new StickerTextView.TextStickerClickListener() {
            @Override
            public void onTextEditorClicked(View view, String text) {
                currentStickerTextView = (StickerTextView) view;
                textEditorControlVisibility(true);
                etTextEditor.setText(text);
                etTextEditor.setSelection(etTextEditor.getText().length());
                etTextEditor.requestFocus();
            }
        });

        stickerTextView.setStickerDeleteListener(new StickerView.StickerDeleteListener() {
            @Override
            public void onStickerRemoved() {
                textEditorControlVisibility(false);
            }
        });
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

    public void textEditorControlVisibility(boolean visible) {
        layoutTextEditor.setVisibility(visible ? VISIBLE : GONE);
    }
}
