package com.windy.bluetooth.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.windy.bluetooth.R;

/**
 * author: wang
 * time: 2015/10/29
 * description:
 *  正在加载的dialog
 */
public class LoadingDialog{


    public Dialog createDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView

        Dialog loadingDialog = new Dialog(context);// 创建自定义样式dialog
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setTitle("请稍后...");
        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }
}
