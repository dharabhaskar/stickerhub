package com.tomlibo.stickerhub.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.tomlibo.stickerhub.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class StickerInfo {
    boolean selected;
    private int start;
    private int end;
    @SerializedName("name")
    private String categoryTitle;

    public String getThumbUrl() {
        return String.format("%s/%s/%s.png", Constants.STICKER_BASE_URL, getFormattedTitle(), getFormattedTitle());
    }

    @SuppressLint("DefaultLocale")
    public List<String> getStickerUrlList() {
        List<String> urlList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            String url = String.format("%s/%s/%d.png", Constants.STICKER_BASE_URL, getFormattedTitle(), i);
            urlList.add(url);
        }
        return urlList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCategoryTitle() {
        if (categoryTitle.equals("In Love"))
            return "Love";
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getFormattedTitle() {
        return categoryTitle.replaceAll(" ", "-").toLowerCase();
    }
}
