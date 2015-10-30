package com.windy.tab;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class YellowActivity extends Activity {
    private static final String TAG = "YellowActivity";

    private TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow);
        printInfo("create");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onStart() {
        printInfo("start");
        super.onStart();
    }

    @Override
    protected void onResume() {
        printInfo("resume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        printInfo("pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        printInfo("stop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        printInfo("restart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        printInfo("destroy");
        super.onDestroy();
    }

    private void printInfo(String info){
        Log.i(TAG,info);
    }
}
