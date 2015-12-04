package com.windy.im.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.lidroid.xutils.ViewUtils;

/**
 * author: wang
 * time: 2015/8/26
 * description:
 *      BaseActivity，所有自定义activity继承的BaseActivity，可以做处理
 */
public class BaseActivity extends ActionBarActivity {





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        init();
        fillData();
    }

    protected void fillData() {

    }

    protected void init() {

    }
}
