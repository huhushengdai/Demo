package com.windy.bluetooth.le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import com.windy.bluetooth.app.BaseApp;

/**
 *
 */
public class MyGattCallBack extends BluetoothGattCallback {
    private static final String TAG = "MyGattCallBack";
    public BluetoothGattCharacteristic char1;
    public BluetoothGattCharacteristic char6;

    private String mAddress;

    public MyGattCallBack(String address) {
        this.mAddress = address;
    }

    /**
     * get BluetoothGattCharacteristic
     */
    public void getCharacteristic(BluetoothGatt gatt, int status) {

    }
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }
    }
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            char6 = gatt.getService(GattAttributes.notifyUUID).getCharacteristic(GattAttributes.char6UUID);
            char1 = gatt.getService(GattAttributes.notifyUUID).getCharacteristic(GattAttributes.char1UUID);
            gatt.setCharacteristicNotification(char6, true);
            BluetoothGattDescriptor descriptor = char6
                    .getDescriptor(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
            Log.i(TAG, "蓝牙描述写入成功");
        }
        afterServicesDiscovered(gatt,status);
    }
    public void afterServicesDiscovered(BluetoothGatt gatt, int status){}

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        //获取到蓝牙设备的数据（只要发送指令，这里就可以获取到数据）
        String info = new String(characteristic.getValue());
        Intent intent = new Intent();
        intent.putExtra(GattAttributes.BROAD_INFO, info);
        intent.setAction(mAddress);
        BaseApp.getInstance().sendBroadcast(intent);
        Log.i(TAG,"蓝牙设备"+mAddress+",信息："+info);
    }

    /**
     * 设置写入蓝牙设备的指令
     */
    public BluetoothGattCharacteristic setChar(byte[] instruction) {
        if (char1!=null&&char1.setValue(instruction)) {
            return char1;
        }
        return null;
    }

    /**
     * 设置写入蓝牙设备的信息
     */
    public BluetoothGattCharacteristic setChar(String message) {
        if (char6!=null&&char6.setValue(message)) {
            return char6;
        }
        return null;
    }
}
