package com.wanandroid.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 *
 */
public class ScreenUtil {
    /**
     * 获取屏幕宽(不含虚拟键盘)
     */
    public static int getScrWidth(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    /**
     * 获取屏幕高(不含虚拟键盘)
     */
    public static int getScrHeight(Context context) {
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return heightPixels;
    }

    /**
     * 获取屏幕真实高度(包含虚拟键盘)
     */
    public static int getScrRealWidth(Context context) {
        int px = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            px = displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return px;
    }

    /**
     * 获取屏幕真实高度(包含虚拟键盘)
     */
    public static int getScrRealHeight(Context context) {
        int px = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            px = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return px;
    }

    /**
     * 获取虚拟键盘高度
     */
    public static int getNavigationButtonHeight(Context context) {
        return getScrRealHeight(context) - getScrHeight(context);
    }

    /**
     * 获取虚拟键盘的宽度
     */
    public static int getNavigationButtonWidth(Context context) {
        return getScrRealWidth(context) - getScrWidth(context);
    }

    /**
     * 获取状态栏高度
     */
    public static int getTitleBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
