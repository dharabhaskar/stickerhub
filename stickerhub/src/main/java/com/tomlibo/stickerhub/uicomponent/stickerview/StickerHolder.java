package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tomlibo.stickerhub.R;
import com.tomlibo.stickerhub.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

public class StickerHolder extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    private static final long DELAY = 1000;
    private Context mContext;
    private View rootView;
    private RelativeLayout layoutMainView;
    private FrameLayout innerStickerHolder;
    private AppCompatImageView backgroundImageView;
    private LinearLayout layoutTextEditor;
    private AppCompatEditText etTextEditor;
    private AppCompatImageButton btDone;
    private StickerTextView currentStickerTextView;
    private Timer timer;

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

        //Inflate component layout
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_sticker_holder, this, false);
        rootView.setTag("rootView");
        LayoutParams root_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        addView(rootView, root_params);

        // ui component initialize
        layoutMainView = findViewById(R.id.layout_mainView);
        innerStickerHolder = findViewById(R.id.layout_innerView);
        backgroundImageView = findViewById(R.id.iv_background);
        layoutTextEditor = findViewById(R.id.layoutTextEditor);
        etTextEditor = findViewById(R.id.etTextEditor);
        btDone = findViewById(R.id.btDone);

        innerStickerHolder.setTag("StickerHolder");
        innerStickerHolder.setOnTouchListener(mOnTouchListener);

        btDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textEditorControlVisibility(false);
                currentStickerTextView.setText(etTextEditor.getText().toString().trim());
            }
        });

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
        for (int i = 0; i < innerStickerHolder.getChildCount(); i++) {
            View view = innerStickerHolder.getChildAt(i);
            if (view instanceof StickerView) {
                ((StickerView) view).hideControls();
            }
        }
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }

    public Bitmap getFinalBitmap() {
        return Utils.getBitmapFromView(layoutMainView);
    }

    public void addImageSticker(Bitmap bitmap) {
        StickerImageView stickerImageView = new StickerImageView(mContext);
        stickerImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        innerStickerHolder.addView(stickerImageView);
    }

    public void addTextSticker() {
        StickerTextView stickerTextView = new StickerTextView(mContext);
        stickerTextView.setText("Your Text");
        innerStickerHolder.addView(stickerTextView);

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void textEditorControlVisibility(boolean visible) {
        layoutTextEditor.setVisibility(visible ? VISIBLE : GONE);
    }
}
