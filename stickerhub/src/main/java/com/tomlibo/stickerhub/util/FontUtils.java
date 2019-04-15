package com.tomlibo.stickerhub.util;

import android.content.Context;
import android.graphics.Typeface;

import com.tomlibo.stickerhub.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.res.ResourcesCompat;

public class FontUtils {

    private static int index;

    private static List<Typeface> getFontList(Context context) {
        List<Typeface> typefaceList = new ArrayList();
        typefaceList.add(ResourcesCompat.getFont(context, R.font.breeserif_regular));
        typefaceList.add(ResourcesCompat.getFont(context, R.font.opensans));
        typefaceList.add(ResourcesCompat.getFont(context, R.font.rubik_regular));
        typefaceList.add(ResourcesCompat.getFont(context, R.font.courgette_regular));
        typefaceList.add(ResourcesCompat.getFont(context, R.font.carroisgothic_regular));
        typefaceList.add(ResourcesCompat.getFont(context, R.font.aller_rg));

        return typefaceList;
    }

    public static Typeface getNextFont(Context context) {
        if (index > 5)
            index = 0;
        return getFontList(context).get(index++);
    }
}
