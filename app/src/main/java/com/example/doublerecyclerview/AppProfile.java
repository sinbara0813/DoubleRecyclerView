package com.example.doublerecyclerview;

import android.app.Application;
import android.content.Context;

import com.example.doublerecyclerview.util.ScreenUtil;
import com.example.doublerecyclerview.util.SrictModeUtil;

/**
 * Created by zhangzz on 2017/5/8.
 *
 */

public class AppProfile {
    private static Context context;

    public void onCreate(Application context) {
        this.context = context;
        ScreenUtil.GetInfo(context);
        if (BuildConfig.DEBUG) {
            SrictModeUtil.startStrictMode();
        }
    }

    public static final Context getContext() {
        return context;
    }
}
