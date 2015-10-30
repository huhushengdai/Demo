package com.windy.im.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.windy.im.R;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends Fragment {
    @ViewInject(R.id.list_view)
    private ListView listView;

    public LeftFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        ViewUtils.inject(this, view);
        init();
        return view;
    }
    private String[] data = {"1","2","3","4","5"};
    private void init() {



        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1,data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(data[position]);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        // Unregister
//        EventBus.getDefault().unregister(this);
    }
}
