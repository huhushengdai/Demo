package com.windy.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * author: wang
 * time: 2015/10/15
 * description:
 */
public class SetPinBroadcast extends BroadcastReceiver{
    private static final String TAG = "SetPinBroadcast";
    private static final String pin = "000000";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"成功接收到广播");
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (device!=null){
            try {
                BleManager.autoBond(device, pin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
