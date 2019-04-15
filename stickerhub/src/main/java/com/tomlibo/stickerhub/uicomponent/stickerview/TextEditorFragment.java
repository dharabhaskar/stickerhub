package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomlibo.stickerhub.R;
import com.tomlibo.stickerhub.util.FontUtils;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class TextEditorFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private AppCompatImageButton btFont;
    private AppCompatImageButton btBold;
    private AppCompatImageButton btItalic;
    private AppCompatImageButton btUnderline;
    private AppCompatEditText etTextEditor;
    private AppCompatImageButton btDone;

    private Context mContext;
    private StickerTextView mStickerTextView;
    private static final long DELAY = 1000;
    private Timer timer;
    private TextEditorDismissListener mTextEditorDismissListener;

    public static TextEditorFragment newInstance() {
        return new TextEditorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_text_editor, container, false);

        btFont = view.findViewById(R.id.btFont);
        btBold = view.findViewById(R.id.btBold);
        btItalic = view.findViewById(R.id.btItalic);
        btUnderline = view.findViewById(R.id.btUnderline);
        etTextEditor = view.findViewById(R.id.etTextEditor);
        btDone = view.findViewById(R.id.btDone);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTextEditor.setText(mStickerTextView.getText());
        etTextEditor.setSelection(etTextEditor.getText().length());
        etTextEditor.requestFocus();

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
                                mStickerTextView.setText(String.valueOf(s));
                            }
                        });
                    }
                }, DELAY);
            }
        });

        btFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatTextView) mStickerTextView.getMainView()).setTypeface(FontUtils.getNextFont(mContext));
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerTextView.setText(etTextEditor.getText().toString().trim());
                mTextEditorDismissListener.textEditorDismissed();
            }
        });
    }

    public void setStickerTextView(Context context, StickerTextView stickerTextView) {
        mContext = context;
        mStickerTextView = stickerTextView;
    }

    public void setTextEditorDismissListener(TextEditorDismissListener textEditorDismissListener) {
        this.mTextEditorDismissListener = textEditorDismissListener;
    }

    interface TextEditorDismissListener {
        void textEditorDismissed();
    }
}
