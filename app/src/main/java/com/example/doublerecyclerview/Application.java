package com.example.doublerecyclerview;

import android.app.ActivityManager;
import android.content.Context;

public class Application extends android.app.Application {

    AppProfile appProfile;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(this));
        }
        appProfile = new AppProfile();
        appProfile.onCreate(this);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(base);
    }

}