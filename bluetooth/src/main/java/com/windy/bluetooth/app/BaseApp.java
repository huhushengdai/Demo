package com.windy.bluetooth.app;

import android.app.Application;

import com.windy.bluetooth.le.BleManager;

/**
 * author: wang
 * time: 2015/10/13
 * description:
 */
public class BaseApp extends Application{
    private static BaseApp baseApp;
    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        BleManager.setContext(this);
    }

    public static BaseApp getInstance(){
        return baseApp;
    }
}
