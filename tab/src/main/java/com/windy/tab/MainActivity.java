package com.windy.tab;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class MainActivity extends TabActivity {
    private TabHost tabhost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        //从TabActivity上面获取放置Tab的TabHost
        tabhost = getTabHost();

//        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                Toast.makeText(MainActivity.this,tabId,Toast.LENGTH_SHORT).show();
////                if (tabId.equals("one")){
////                    if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
////                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////                    }
////                }else {
////                    if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
////                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////                    }
////                }
//            }
//        });

        TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
        tab1.setIndicator("红色");
//        tab1.setContent(R.id.widget_layout_red);
        tab1.setContent(new Intent(this, RedActivity.class));


        TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
        tab2.setIndicator("黄色");
        tab2.setContent(new Intent(this,YellowActivity.class));

        TabHost.TabSpec tab3 = tabhost.newTabSpec("three");
        tab3.setIndicator("黑色");
//        tab1.setContent(R.id.widget_layout_red);
        tab3.setContent(new Intent(this,Red2Activity.class));

        TabHost.TabSpec tab4 = tabhost.newTabSpec("four");
        tab4.setIndicator("蓝色");
//        tab1.setContent(R.id.widget_layout_red);
        tab4.setContent(new Intent(this,Red3Activity.class));
        tabhost.addTab(tab1);
        tabhost.addTab(tab2);
        tabhost.addTab(tab3);
        tabhost.addTab(tab4);

    }
}
