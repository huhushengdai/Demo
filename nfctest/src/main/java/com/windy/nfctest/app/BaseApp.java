package com.windy.nfctest.app;

import android.app.Application;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

/**
 * author: wang
 * time: 2015/10/20
 * description:
 */
public class BaseApp extends Application {
    private static BaseApp baseApp;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        Stetho.initialize(Stetho.newInitializerBuilder(this).
                enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).
                enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
    }

    public static BaseApp getInstance() {
        return baseApp;
    }

    public static void toast(String text) {
        if (baseApp != null) {
            Toast.makeText(baseApp, text, Toast.LENGTH_SHORT).show();
        }
    }


}
