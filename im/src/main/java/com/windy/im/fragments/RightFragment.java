package com.windy.im.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.windy.im.R;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class RightFragment extends Fragment {
    @ViewInject(R.id.text)
    TextView text;

    public RightFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        ViewUtils.inject(this,view);
        EventBus.getDefault().register(this);
        return view;
    }

    /** List点击时会发送些事件，接收到事件后更新详情 */
    public void onEventMainThread(String item)
    {
        if (!TextUtils.isEmpty(item)) {
            text.setText(item);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }
}
