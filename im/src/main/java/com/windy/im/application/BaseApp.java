package com.windy.im.application;

import android.app.Application;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

/**
 * author: wang
 * time: 2015/11/13
 * description:
 */
public class BaseApp extends Application {
    private static final String TAG = "BaseApp";

    private static BaseApp baseApp;


    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        //chrome 调试   依赖  'com.facebook.stetho:stetho:1.0.1'
        Stetho.initialize(Stetho.newInitializerBuilder(this).
                enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).
                enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
    }



    /**
     * 获取BaseApp  实例
     *
     * @return
     */
    public static BaseApp getInstance() {
        return baseApp;
    }

    public static void toast(String text) {
        if (baseApp != null) {
            Toast.makeText(baseApp, text, Toast.LENGTH_SHORT).show();
        }
    }
}
