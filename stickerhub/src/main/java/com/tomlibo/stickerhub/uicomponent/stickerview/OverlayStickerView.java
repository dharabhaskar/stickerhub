package com.tomlibo.stickerhub.uicomponent.stickerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.tomlibo.stickerhub.model.OverlayAction;


import java.util.Stack;

public class OverlayStickerView extends StickerFrameView {
    private Stack<OverlayAction> actions;
    private Stack<OverlayAction> undoActions;

    public OverlayStickerView(Context context) {
        super(context);
    }

    public OverlayStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayStickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        actions=new Stack<>();
        undoActions=new Stack<>();
        //actions.add(new OverlayAction(OverlayAction.CHANGE_COLOR,null));
    }

    public void setAlpha(Float alpha) {
        iv_main.setAlpha(alpha);
        //actions.push(new OverlayAction(OverlayAction.CHANGE_ALPHA, alpha));
    }

    public void setColor(Integer color) {
        setColor(color,true);
    }

    public void setColor(int color,boolean saveAction) {
        iv_main.setColorFilter(color);
        if(saveAction) {
            undoActions.clear();
            actions.push(new OverlayAction(OverlayAction.CHANGE_COLOR, color));
        }
    }

    public void saveAlpha(float alpha){
        undoActions.clear();
        actions.push(new OverlayAction(OverlayAction.CHANGE_ALPHA, alpha));
    }

    private void applyAction(@NonNull OverlayAction action){
        switch (action.getActionType()){
            case OverlayAction.CHANGE_ALPHA:
                setAlpha((Float) action.getActionValue());
                break;
            case OverlayAction.CHANGE_COLOR:
                setColor((Integer)action.getActionValue(),false);
        }
    }

    public void undo(){
        if(!actions.empty()) {
            undoActions.push(actions.pop());
            applyAction(undoActions.peek());
        }
    }

    public void redo(){
        if(!undoActions.isEmpty()){
            actions.push(undoActions.pop());
            applyAction(actions.peek());
        }
    }




}
