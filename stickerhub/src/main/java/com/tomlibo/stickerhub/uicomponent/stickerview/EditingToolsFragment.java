package com.tomlibo.stickerhub.uicomponent.stickerview;

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

public class EditingToolsFragment extends Fragment {

    private static String PARAM_SEEK_BAR_TITLE = "title";
    private static String PARAM_SEEK_BAR_INITIAL_PROGRESS = "initial_progress";
    private CircleView viewColor;
    private AppCompatTextView tvUndo;
    private AppCompatTextView tvRedo;
    private AppCompatTextView tvSeekBarTitle;
    private AppCompatSeekBar seekBar;

    private String mSbTitle;
    private int mSbInitialProgress;

    private EditingToolsListener mEditingToolsListener;

    public static EditingToolsFragment newInstance(String sbTitle, int sbInitialProgress) {
        EditingToolsFragment fragment = new EditingToolsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SEEK_BAR_TITLE, sbTitle);
        bundle.putInt(PARAM_SEEK_BAR_INITIAL_PROGRESS, sbInitialProgress);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSbTitle = getArguments().getString(PARAM_SEEK_BAR_TITLE);
            mSbInitialProgress = getArguments().getInt(PARAM_SEEK_BAR_INITIAL_PROGRESS, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_editing_tools, container, false);

        viewColor = view.findViewById(R.id.colorIndicator);
        tvUndo = view.findViewById(R.id.tvUndo);
        tvRedo = view.findViewById(R.id.tvRedo);
        tvSeekBarTitle = view.findViewById(R.id.tvSeekBarTitle);
        seekBar = view.findViewById(R.id.seekBar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewColor.setBackgroundColor(getResources().getColor(R.color.red));

        viewColor.setOnClickListener(v -> {
            new ColorPickerDialog.Builder(getContext())
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
                            mEditingToolsListener.onColorSelected(color);
                        }
                    });
        });

        tvUndo.setOnClickListener(v -> {
            mEditingToolsListener.onUndoClicked();
        });

        tvRedo.setOnClickListener(v -> {
            mEditingToolsListener.onRedoClicked();
        });

        seekBar.setProgress(mSbInitialProgress);
        setSbProgress(mSbInitialProgress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSbProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mEditingToolsListener.onSeekBarStopTracking(seekBar.getProgress());
            }
        });
    }

    private void setSbProgress(int progress) {
        tvSeekBarTitle.setText(String.format("%s: %s", mSbTitle, progress));
        mEditingToolsListener.onSeekBarProgressChanged(progress);
    }

    public void setEditingToolsListener(EditingToolsListener editingToolsListener) {
        mEditingToolsListener = editingToolsListener;
    }

    public interface EditingToolsListener {
        void onColorSelected(String color);

        void onSeekBarProgressChanged(int progress);

        void onSeekBarStopTracking(int progress);

        void onUndoClicked();

        void onRedoClicked();
    }
}
