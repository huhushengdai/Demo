package com.windy.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * author: wang
 * time: 2015/10/15
 * description:
 */
public class SetPinBroadcast extends BroadcastReceiver{
    private static final String TAG = "SetPinBroadcast";
    private static final String pin = "000000";
    private static final Handler HANDLER = new Handler();
    @Override
    public void onReceive(Context context, final Intent intent) {
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"成功接收到广播");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device!=null){
                    try {
                boolean flag = BleManager.autoBond(device, pin);
                Log.i(TAG,"输入匹配密码："+flag);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        },1000);
    }

//    private void setPin(Intent intent){
//        Log.i(TAG,"成功接收到广播");
//        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//        if (device!=null){
//            try {
////                boolean flag = BleManager.autoBond(device, pin);
////                Log.i(TAG,"输入匹配密码："+flag);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
