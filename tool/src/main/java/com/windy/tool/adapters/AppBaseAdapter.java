package com.windy.tool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * author: wang
 * time: 2015/7/24
 * description:
 */
public abstract class AppBaseAdapter<T> extends BaseAdapter{
    protected ArrayList<T> data;
    protected Context context;
    protected LayoutInflater inflater;

    public AppBaseAdapter(ArrayList<T> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data!=null&&!data.isEmpty()?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data!=null&&!data.isEmpty()?data.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
