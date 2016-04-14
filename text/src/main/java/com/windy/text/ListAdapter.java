package com.windy.text;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windy.tool.adapters.AppBaseAdapter;

import java.util.ArrayList;

/**
 * Author: wanglizhi
 * Description:
 * Date: 2016/4/14
 */
public class ListAdapter extends AppBaseAdapter<String>{
    public ListAdapter(ArrayList<String> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh ;
        if (view == null){
            view = View.inflate(context,R.layout.item_refresh_text,null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        vh.textView.setText(data.get(position));
        return view;
    }

    static class ViewHolder{
        TextView textView;
        public ViewHolder(View convertView){
            textView = (TextView) convertView.findViewById(R.id.item_refresh_text);
        }
    }
}
