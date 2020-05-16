package com.dj.ip.proxy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

/**
 * description:
 * author:kyXiao
 * date:2020/5/16
 */
public class PopUtils {
    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
//        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
//        lp.alpha = bgAlpha;
//        ((Activity) mContext).getWindow().setAttributes(lp);

        if (bgAlpha == 1f) {
            clearDim((Activity) mContext);
        } else {
            applyDim((Activity) mContext, bgAlpha);
        }
    }

    private static void applyDim(Activity activity, float bgAlpha) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        //activity跟布局
//        ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * bgAlpha));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    private static void clearDim(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        //activity跟布局
//        ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
}
