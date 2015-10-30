package com.windy.bluetooth.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.windy.bluetooth.R;
import com.windy.bluetooth.bean.DeviceInfo;

import java.util.ArrayList;

/**
 * author: wang
 * time: 2015/10/13
 * description:
 */
public class AddBluetoothAdapter extends AppBaseAdapter<DeviceInfo>{
    public AddBluetoothAdapter(ArrayList<DeviceInfo> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh;
        if (view==null){
            view = inflater.inflate(R.layout.item_bluetooth_address,parent,false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        vh.address.setText(data.get(position).getAddress());
        vh.name.setText(data.get(position).getName());
        return view;
    }
    class ViewHolder{
        @ViewInject(R.id.item_bluetooth_address)
        TextView address;
        @ViewInject(R.id.item_bluetooth_name)
        TextView name;
        ViewHolder(View convertView){
            ViewUtils.inject(this,convertView);
        }
    }
}
