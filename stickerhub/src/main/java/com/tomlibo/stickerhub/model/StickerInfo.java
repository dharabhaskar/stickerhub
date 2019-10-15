package com.tomlibo.stickerhub.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tomlibo.stickerhub.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class StickerInfo implements Parcelable {
    boolean selected;
    private int start;
    private int end;
    @SerializedName("name")
    private String categoryTitle;

    protected StickerInfo(Parcel in) {
        selected = in.readByte() != 0;
        start = in.readInt();
        end = in.readInt();
        categoryTitle = in.readString();
    }

    public static final Creator<StickerInfo> CREATOR = new Creator<StickerInfo>() {
        @Override
        public StickerInfo createFromParcel(Parcel in) {
            return new StickerInfo(in);
        }

        @Override
        public StickerInfo[] newArray(int size) {
            return new StickerInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeInt(start);
        dest.writeInt(end);
        dest.writeString(categoryTitle);
    }
}
