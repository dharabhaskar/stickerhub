package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.tomlibo.stickerhub.R;
import com.tomlibo.stickerhub.uicomponent.colorpicker.ColorPickerDialog;
import com.tomlibo.stickerhub.util.Utils;

public class StickerHolder extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private View rootView;
    private RelativeLayout layoutMainView;
    private FrameLayout innerStickerHolder;
    private AppCompatImageView backgroundImageView;
    private StickerTextView currentStickerTextView;
    private StickerDrawView currentStickerDrawView;
    private StickerFrameView currentStickerOverlayView;
    private boolean isOverlay;
    private boolean isDrawing;

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

    private void init(final Context context) {
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

        innerStickerHolder.setTag("StickerHolder");
        innerStickerHolder.setOnTouchListener(mOnTouchListener);
    }

    public void hideControlsOfAllChildStickerView() {
        for (int i = 0; i < innerStickerHolder.getChildCount(); i++) {
            View view = innerStickerHolder.getChildAt(i);
            if (view instanceof StickerView && !(view instanceof StickerFrameView)) {
                ((StickerView) view).hideControls();

                if (view instanceof StickerDrawView) {
                    ((StickerDrawView) view).stopDrawing();
                }
            }
        }

        textEditorControlVisibility(false);
        drawingToolsControlVisibility(false);
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }

    public Bitmap getFinalBitmap() {
        return Utils.getBitmapFromView(layoutMainView);
    }

    public void addImageSticker(Bitmap bitmap) {
        hideControlsOfAllChildStickerView();

        StickerImageView stickerImageView = new StickerImageView(mContext);
        stickerImageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        innerStickerHolder.addView(stickerImageView);
    }

    public void addTextSticker() {
        hideControlsOfAllChildStickerView();

        StickerTextView stickerTextView = new StickerTextView(mContext);
        stickerTextView.setText("Your Text");
        innerStickerHolder.addView(stickerTextView);

        stickerTextView.setTextStickerClickListener(new StickerTextView.TextStickerClickListener() {
            @Override
            public void onColorPaletteClicked(View view) {
                currentStickerTextView = (StickerTextView) view;
                textColorPicker();
            }

            @Override
            public void onTextEditorClicked(View view, String text) {
                currentStickerTextView = (StickerTextView) view;
                textEditorControlVisibility(true);
            }
        });

        stickerTextView.setStickerDeleteListener(new StickerView.StickerDeleteListener() {
            @Override
            public void onStickerRemoved() {
                textEditorControlVisibility(false);
            }
        });
    }

    public void addFrameSticker(Bitmap bitmap) {
        hideControlsOfAllChildStickerView();

        StickerFrameView stickerFrameView = new StickerFrameView(mContext);
        stickerFrameView.setTag("frame");
        stickerFrameView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

        int position;

        if (innerStickerHolder.getChildAt(0) != null
                && innerStickerHolder.getChildAt(0).getTag().equals("overlay")) {
            position = 1;
        } else {
            position = 0;
        }

        if (innerStickerHolder.getChildAt(position) instanceof StickerFrameView
                && innerStickerHolder.getChildAt(0).getTag().equals("frame")) {
            innerStickerHolder.removeViewAt(position);
        }

        innerStickerHolder.addView(stickerFrameView, position);
    }

    public void addOverlay(Bitmap bitmap) {
        if (isOverlay)
            return;

        hideControlsOfAllChildStickerView();

        currentStickerOverlayView = new StickerFrameView(mContext);
        currentStickerOverlayView.setTag("overlay");
        currentStickerOverlayView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

        if (innerStickerHolder.getChildAt(0) instanceof StickerFrameView
                && innerStickerHolder.getChildAt(0).getTag().equals("overlay")) {
            innerStickerHolder.removeViewAt(0);
        }

        innerStickerHolder.addView(currentStickerOverlayView, 0);

        currentStickerOverlayView.setStickerDeleteListener(new StickerView.StickerDeleteListener() {
            @Override
            public void onStickerRemoved() {
                overlayToolsControlVisibility(false);
            }
        });

        overlayToolsControlVisibility(true);
    }

    public void addDrawView() {
        if (isDrawing)
            return;

        hideControlsOfAllChildStickerView();

        currentStickerDrawView = new StickerDrawView(mContext);
        innerStickerHolder.addView(currentStickerDrawView);

        currentStickerDrawView.setDrawingDoneClickListener(new StickerDrawView.DrawingDoneClickListener() {
            @Override
            public void onDoneClicked() {
                hideControlsOfAllChildStickerView();
            }
        });

        currentStickerDrawView.setStickerDeleteListener(new StickerView.StickerDeleteListener() {
            @Override
            public void onStickerRemoved() {
                drawingToolsControlVisibility(false);
            }
        });

        drawingToolsControlVisibility(true);
    }

    public void textEditorControlVisibility(boolean visible) {
        if (visible) {
            TextEditorFragment textEditorFragment = new TextEditorFragment();
            textEditorFragment.setStickerTextView(mContext, currentStickerTextView);

            rootView.findViewById(R.id.flContainerTool).setVisibility(VISIBLE);

            ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainerTool, textEditorFragment)
                    .commit();

            textEditorFragment.setTextEditorDismissListener(new TextEditorFragment.TextEditorDismissListener() {
                @Override
                public void textEditorDismissed() {
                    textEditorControlVisibility(false);
                }
            });
        } else {
            rootView.findViewById(R.id.flContainerTool).setVisibility(GONE);
        }
    }

    public void overlayToolsControlVisibility(boolean visible) {
        if (visible) {
            EditingToolsFragment editingToolsFragment = EditingToolsFragment.newInstance("Alpha", 100);
            editingToolsFragment.setEditingToolsListener(overlayEditingToolsListener);

            rootView.findViewById(R.id.flContainerTool).setVisibility(VISIBLE);

            ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainerTool, editingToolsFragment)
                    .commit();

            isOverlay = true;
        } else {
            rootView.findViewById(R.id.flContainerTool).setVisibility(GONE);
            isOverlay = false;
        }
    }

    public void drawingToolsControlVisibility(boolean visible) {
        if (visible) {
            EditingToolsFragment editingToolsFragment = EditingToolsFragment.newInstance("Brush Size", 10);
            editingToolsFragment.setEditingToolsListener(drawEditingToolsListener);

            rootView.findViewById(R.id.flContainerTool).setVisibility(VISIBLE);

            ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainerTool, editingToolsFragment)
                    .commit();

            isDrawing = true;
        } else {
            rootView.findViewById(R.id.flContainerTool).setVisibility(GONE);
            isDrawing = false;
        }
    }

    private void textColorPicker() {
        new ColorPickerDialog.Builder(mContext)
                .initialColor(currentStickerTextView.getTextColor()) // Set initial color
                .enableBrightness(true) // Enable brightness slider or not
                .enableAlpha(false) // Enable alpha slider or not
                .okTitle("SELECT")
                .cancelTitle("CANCEL")
                .showIndicator(true)
                .showValue(false)
                .build()
                .show(new ColorPickerDialog.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(String color) {
                        currentStickerTextView.setTextColor(color);
                    }
                });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private EditingToolsFragment.EditingToolsListener drawEditingToolsListener = new EditingToolsFragment.EditingToolsListener() {
        @Override
        public void onColorSelected(String color) {
            currentStickerDrawView.setPaintColor(Color.parseColor(color));
        }

        @Override
        public void onSeekBarProgressChanged(int progress) {
            currentStickerDrawView.setPaintStrokeWidth(progress);
        }

        @Override
        public void onUndoClicked() {
            currentStickerDrawView.undo();
        }

        @Override
        public void onRedoClicked() {
            currentStickerDrawView.redo();
        }
    };

    private EditingToolsFragment.EditingToolsListener overlayEditingToolsListener = new EditingToolsFragment.EditingToolsListener() {
        @Override
        public void onColorSelected(String color) {

        }

        @Override
        public void onSeekBarProgressChanged(int progress) {

        }

        @Override
        public void onUndoClicked() {

        }

        @Override
        public void onRedoClicked() {

        }
    };
}
