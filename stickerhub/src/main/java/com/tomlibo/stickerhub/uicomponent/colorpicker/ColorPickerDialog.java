package com.tomlibo.stickerhub.uicomponent.colorpicker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tomlibo.stickerhub.R;

import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ColorPickerDialog {

    private Context context;
    private AlertDialog.Builder colorPickerBuilder;
    private AlertDialog colorPickerDialog;
    private int initialColor;
    private boolean enableBrightness;
    private boolean enableAlpha;
    private String okTitle;
    private String cancelTitle;
    private boolean showIndicator;
    private boolean showValue;
    private boolean onlyUpdateOnTouchEventUp;

    private ColorPickerDialog(Builder builder) {
        this.context = builder.context;
        this.initialColor = builder.initialColor;
        this.enableBrightness = builder.enableBrightness;
        this.enableAlpha = builder.enableAlpha;
        this.okTitle = builder.okTitle;
        this.cancelTitle = builder.cancelTitle;
        this.showIndicator = builder.showIndicator;
        this.showValue = builder.showValue;
        this.onlyUpdateOnTouchEventUp = builder.onlyUpdateOnTouchEventUp;
    }

    public void show(final ColorPickerObserver observer) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.top_defaults_view_color_picker_dialog, null);
        colorPickerBuilder = new AlertDialog.Builder(context);
        colorPickerBuilder.setView(layout);

        final ColorPickerView colorPickerView = layout.findViewById(R.id.colorPickerView);
        colorPickerView.setInitialColor(initialColor);
        colorPickerView.setEnabledBrightness(enableBrightness);
        colorPickerView.setEnabledAlpha(enableAlpha);
        colorPickerView.setOnlyUpdateOnTouchEventUp(onlyUpdateOnTouchEventUp);
        colorPickerView.subscribe(observer);

        colorPickerBuilder.setPositiveButton(
                cancelTitle,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        colorPickerBuilder.setNegativeButton(
                okTitle,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                        if (observer != null) {
                            observer.onColorPicked("#" + Integer.toHexString(colorPickerView.getColor()));
                        }
                    }
                });

        final View colorIndicator = layout.findViewById(R.id.colorIndicator);
        final TextView colorHex = layout.findViewById(R.id.colorHex);

        colorIndicator.setVisibility(showIndicator ? View.VISIBLE : View.GONE);
        colorHex.setVisibility(showValue ? View.VISIBLE : View.GONE);

        if (showIndicator) {
            colorIndicator.setBackgroundColor(initialColor);
        }

        if (showValue) {
            colorHex.setText(colorHex(initialColor));
        }

        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                if (showIndicator) {
                    colorIndicator.setBackgroundColor(color);
                }
                if (showValue) {
                    colorHex.setText(colorHex(color));
                }
            }
        });

        colorPickerBuilder.setCancelable(false);
        colorPickerDialog = colorPickerBuilder.create();
        colorPickerDialog.setCanceledOnTouchOutside(true);
        colorPickerDialog.show();
    }

    private void dismiss() {
        if (colorPickerDialog != null) {
            colorPickerDialog.dismiss();
        }
    }

    private String colorHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "0x%02X%02X%02X%02X", a, r, g, b);
    }

    public static class Builder {

        private Context context;
        private int initialColor = Color.MAGENTA;
        private boolean enableBrightness = true;
        private boolean enableAlpha = false;
        private String okTitle = "OK";
        private String cancelTitle = "Cancel";
        private boolean showIndicator = true;
        private boolean showValue = true;
        private boolean onlyUpdateOnTouchEventUp = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder initialColor(int color) {
            initialColor = color;
            return this;
        }

        public Builder enableBrightness(boolean enable) {
            enableBrightness = enable;
            return this;
        }


        public Builder enableAlpha(boolean enable) {
            enableAlpha = enable;
            return this;
        }

        public Builder okTitle(String title) {
            okTitle = title;
            return this;
        }

        public Builder cancelTitle(String title) {
            cancelTitle = title;
            return this;
        }

        public Builder showIndicator(boolean show) {
            showIndicator = show;
            return this;
        }

        public Builder showValue(boolean show) {
            showValue = show;
            return this;
        }

        public Builder onlyUpdateOnTouchEventUp(boolean only) {
            onlyUpdateOnTouchEventUp = only;
            return this;
        }

        public ColorPickerDialog build() {
            return new ColorPickerDialog(this);
        }
    }

    public abstract static class ColorPickerObserver implements ColorObserver {
        public abstract void onColorPicked(String color);

        @Override
        public final void onColor(int color, boolean fromUser, boolean shouldPropagate) {

        }
    }
}
