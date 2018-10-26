package com.example.doublerecyclerview.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.example.doublerecyclerview.AppProfile;

import java.lang.reflect.Field;

/**
 * Created by zhangzz on 2017/5/8.
 * 获取屏幕宽度高度 状态栏高度工具类
 */

public class ScreenUtil {
    private static final String TAG = "ScreenUtil";
    private static double RATIO = 0.85;
    public static int screenWidth;
    public static int screenHeight;
    public static int screenMin;// 宽高中，较小的值
    public static int screenMax;// 宽高中，较大的值

    public static float density;
    public static float scaleDensity;
    public static float xdpi;
    public static float ydpi;
    public static int densityDpi;

    public static int dialogWidth;
    public static int statusbarheight;
    public static int navbarheight;

    private static Point deviceSize;

    public static void GetInfo(Context context) {
        if (null == context) {
            return;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        screenMax = (screenWidth < screenHeight) ? screenHeight : screenWidth;
        density = dm.density;
        scaleDensity = dm.scaledDensity;
        xdpi = dm.xdpi;
        ydpi = dm.ydpi;
        densityDpi = dm.densityDpi;
        statusbarheight = getStatusBarHeight(context);
        navbarheight = getNavBarHeight(context);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception E) {
            E.printStackTrace();
        }
        return sbar;
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int dip2px(float dipValue) {
        final float scale = getDisplayDensity();
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = getDisplayDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float fontScale=getScaleDensity();
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale=getScaleDensity();
        return (int) (spValue * fontScale + 0.5f);
    }

    private static float getDisplayDensity() {
        if (density == 0) {
            GetInfo(AppProfile.getContext());
        }
        return density;
    }

    public static float getScaleDensity(){
        if (scaleDensity==0){
            GetInfo(AppProfile.getContext());
        }
        return scaleDensity;
    }

    public static int getDisplayWidth() {
        if (screenWidth == 0) {
            GetInfo(AppProfile.getContext());
        }
        return screenWidth;
    }

    public static int getDisplayHeight() {
        if (screenHeight == 0) {
            GetInfo(AppProfile.getContext());
        }
        return screenHeight;
    }

    public static int getScreenMin() {
        if (screenMin == 0) {
            GetInfo(AppProfile.getContext());
        }
        return screenMin;
    }

    public static int getScreenMax() {
        if (screenMin == 0) {
            GetInfo(AppProfile.getContext());
        }
        return screenMax;
    }

    public static int getDialogWidth() {
        dialogWidth = (int) (getScreenMin() * RATIO);
        return dialogWidth;
    }

    public static Point getDeviceSize(Context context) {
        if (deviceSize == null || deviceSize.x == 0 || deviceSize.y == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            deviceSize = new Point();
            display.getSize(deviceSize);
        }
        return deviceSize;
    }
}
