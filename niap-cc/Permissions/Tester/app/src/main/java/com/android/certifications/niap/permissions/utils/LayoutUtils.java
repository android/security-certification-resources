package com.android.certifications.niap.permissions.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LayoutUtils {
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
            System.out.println(params.toString());
            //params.setMargins(l, t, r, b); //substitute parameters for left, top, right, bottom
            //v.setLayoutParams(params);
        }
    }
}
