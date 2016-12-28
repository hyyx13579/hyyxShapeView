package com.example.mylibrary.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by hyyx on 16/12/1.
 */

public class CommonUtils {


    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int CompareDate(String dateOne, String dateTwo) {

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(dateOne));
            c2.setTime(df.parse(dateTwo));
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        return result;

    }


    public static int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());

    }


}
