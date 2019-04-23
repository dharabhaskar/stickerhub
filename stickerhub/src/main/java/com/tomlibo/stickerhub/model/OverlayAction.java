package com.tomlibo.stickerhub.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OverlayAction {
    public static final int CHANGE_ALPHA = 1;
    public static final int CHANGE_COLOR = 2;
    private int actionType;
    private Float alphaValue;
    private Integer colorValue;

    public OverlayAction(@ActionType int actionType, Object actionValue) {
        this.actionType = actionType;
        if (actionValue instanceof Integer) colorValue = (Integer) actionValue;
        if (actionValue instanceof Float) alphaValue = (Float) actionValue;
    }

    public int getActionType() {
        return actionType;
    }

    public Object getActionValue() {
        switch (actionType) {
            case CHANGE_ALPHA:
                return alphaValue;
            case CHANGE_COLOR:
                return colorValue;

        }
        return null;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CHANGE_ALPHA, CHANGE_COLOR})
    public @interface ActionType {
    }
}
