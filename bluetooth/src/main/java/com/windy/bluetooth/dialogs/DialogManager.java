package com.windy.bluetooth.dialogs;

import android.app.Dialog;
import android.content.Context;

/**
 * author: wang
 * time: 2015/10/29
 * description:
 *      管理dialog
 */
public class DialogManager {

    public static final String LOADING_DIALOG = "LoadingDialog";

    public static Dialog createDialog(String dialogType,Context context){
        if (dialogType.equals(LOADING_DIALOG)){
            return new LoadingDialog().createDialog(context);
        }
        return null;
    }



    public static void dismiss(Dialog dialog){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
