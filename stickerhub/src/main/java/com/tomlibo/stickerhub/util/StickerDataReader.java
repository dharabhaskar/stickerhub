package com.tomlibo.stickerhub.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomlibo.stickerhub.R;
import com.tomlibo.stickerhub.model.StickerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StickerDataReader {

    private static String jsonData;
    private static List<StickerInfo> stickerInfos = new ArrayList<>();

    private static void init(Context context) throws IOException {
        if (jsonData != null) return;
        InputStream inputStream = context.getResources().openRawResource(R.raw.sticker_info);
        byte[] buff = new byte[inputStream.available()];
        inputStream.read(buff);
        jsonData = new String(buff);
    }

    public static void init(List<StickerInfo> stickerInfoList) {
        stickerInfos = stickerInfoList;
    }

    private static List<StickerInfo> getStickerData(Context context) throws IOException {
        init(context);
        return new Gson().fromJson(jsonData, new TypeToken<List<StickerInfo>>() {
        }.getType());
    }

    public static List<StickerInfo> getOnlyStickers() {
        List<StickerInfo> stickerInfoList = new ArrayList<>();
        for (StickerInfo stickerInfo : stickerInfos) {
            if (stickerInfo.getFormattedTitle().contains("frame")) continue;
            if (stickerInfo.getFormattedTitle().contains("overlay")) continue;
            stickerInfoList.add(stickerInfo);
        }
        return stickerInfoList;
    }

    public static List<StickerInfo> getOnlyStickersOnline(Context context) throws IOException {
        List<StickerInfo> stickerInfoList = new ArrayList<>();
        for (StickerInfo stickerInfo : getStickerData(context)) {
            if (stickerInfo.getFormattedTitle().contains("frame")) continue;
            if (stickerInfo.getFormattedTitle().contains("overlay")) continue;
            stickerInfoList.add(stickerInfo);
        }
        return stickerInfoList;
    }

    public static List<String> getStickersByCategory(Context context, String categoryTitle) throws IOException {
        for (StickerInfo stickerInfo : getStickerData(context)) {
            if (stickerInfo.getFormattedTitle().contains(categoryTitle)) {
                return stickerInfo.getStickerUrlList();
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getAllCategoryThumbs() {
        List<String> categoryImageList = new ArrayList<>();
        for (StickerInfo info : getOnlyStickers()) {
            categoryImageList.add(info.getThumbUrl());
        }
        return categoryImageList;
    }

    public static StickerInfo getStickerInfoByIndex(int index) {
        List<StickerInfo> allStickers = getOnlyStickers();
        if (index < 0 || index >= allStickers.size()) {
            throw new IndexOutOfBoundsException("index outside range");
        }
        return allStickers.get(index);
    }

    public static List<String> getStickersByIndex(int index) {
        List<StickerInfo> allStickers = getOnlyStickers();
        if (index < 0 || index >= allStickers.size()) {
            throw new IndexOutOfBoundsException("index outside range");
        }
        return allStickers.get(index).getStickerUrlList();
    }

    public static List<String> getStickersByIndexOnline(Context context, int index) throws IOException {
        List<StickerInfo> allStickers = getOnlyStickersOnline(context);
        if (index < 0 || index >= allStickers.size()) {
            throw new IndexOutOfBoundsException("index outside range");
        }
        return allStickers.get(index).getStickerUrlList();
    }

    public static List<String> getAllStickers() {
        List<String> stickerUrlList = new ArrayList<>();
        for (StickerInfo stickerInfo : getOnlyStickers()) {
            stickerUrlList.addAll(stickerInfo.getStickerUrlList());
        }
        return stickerUrlList;
    }

    public static List<String> getFrames(Context context) throws IOException {
        return getStickersByCategory(context, "frames");
    }

    public static List<String> getOverlays(Context context) throws IOException {
        return getStickersByCategory(context, "overlays");
    }
}
