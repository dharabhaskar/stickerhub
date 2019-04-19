package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.tomlibo.stickerhub.R;
import com.tomlibo.stickerhub.uicomponent.CircleView;
import com.tomlibo.stickerhub.uicomponent.colorpicker.ColorPickerDialog;

public class DrawingToolsFragment extends Fragment {

    private CircleView viewColor;
    private AppCompatTextView tvUndo;
    private AppCompatTextView tvRedo;
    private AppCompatTextView tvBrushSize;
    private AppCompatSeekBar sbStrokeSize;

    private Context mContext;
    private StickerDrawView mStickerDrawView;
    private int initialStrokeSize = 10;

    public static DrawingToolsFragment newInstance() {
        return new DrawingToolsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_drawing_tools, container, false);

        viewColor = view.findViewById(R.id.colorIndicator);
        tvUndo = view.findViewById(R.id.tvUndo);
        tvRedo = view.findViewById(R.id.tvRedo);
        tvBrushSize = view.findViewById(R.id.tvBrushSize);
        sbStrokeSize = view.findViewById(R.id.sbStrokeSize);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewColor.setBackgroundColor(getResources().getColor(R.color.red));

        viewColor.setOnClickListener(v -> {
            new ColorPickerDialog.Builder(mContext)
                    .initialColor(viewColor.getBackgroundColor()) // Set initial color
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
                            viewColor.setBackgroundColor(Color.parseColor(color));
                            mStickerDrawView.setPaintColor(Color.parseColor(color));
                        }
                    });
        });

        tvUndo.setOnClickListener(v -> {
            mStickerDrawView.undo();
        });

        tvRedo.setOnClickListener(v -> {
            mStickerDrawView.redo();
        });

        sbStrokeSize.setProgress(initialStrokeSize);
        setBrushSize(initialStrokeSize);

        sbStrokeSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setStickerDrawView(Context context, StickerDrawView stickerDrawView) {
        mContext = context;
        mStickerDrawView = stickerDrawView;
    }

    private void setBrushSize(int size) {
        tvBrushSize.setText(String.format("Brush Size: %s", size));
        mStickerDrawView.setPaintStrokeWidth(size);
    }
}
