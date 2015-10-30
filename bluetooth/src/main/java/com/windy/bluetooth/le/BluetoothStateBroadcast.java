package com.windy.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * author: wang
 * time: 2015/10/29
 * description:
 */
public class BluetoothStateBroadcast extends BroadcastReceiver{
    private static final String TAG = "BluetoothStateBroadcast";
    private static final Handler HANDLER = new Handler();
    @Override
    public void onReceive(Context context, Intent intent) {

        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.i(TAG, "蓝牙设备匹配状态");

        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(device);
            }
        });
//        switch (device.getBondState()) {
//            case BluetoothDevice.BOND_BONDING:
//                Log.d(TAG, "正在配对......");
//                break;
//            case BluetoothDevice.BOND_BONDED:
//                Log.d(TAG, "完成配对");
//                break;
//            case BluetoothDevice.BOND_NONE:
//                Log.d(TAG, "取消配对");
//            default:
//                break;
//        }
    }
}
