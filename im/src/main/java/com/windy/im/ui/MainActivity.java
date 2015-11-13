package com.windy.im.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.lidroid.xutils.ViewUtils;
import com.windy.im.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(R.id.container_left,new LeftFragment(),"tag1");
//        ft.add(R.id.container_right,new RightFragment(),"tag2");
//        ft.commit();
    }
}
