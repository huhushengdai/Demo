package com.windy.bluetooth.ui;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.windy.bluetooth.R;
import com.windy.bluetooth.adapters.AddBluetoothAdapter;
import com.windy.bluetooth.bean.DeviceInfo;
import com.windy.bluetooth.dialogs.DialogManager;
import com.windy.bluetooth.le.BleManager;
import com.windy.bluetooth.le.BluetoothStateBroadcast;
import com.windy.bluetooth.le.MyGattCallBack;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";
    public static String ADD = "address";

    @ViewInject(R.id.gate_list)
    private ListView gateList;
    private ArrayList<DeviceInfo> data = new ArrayList<>();
    private AddBluetoothAdapter adapter;

    private BleManager manager;

    private Dialog loadingDialog;

    private BleManager.LeScanResult leScanResult = new BleManager.LeScanResult() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //在UI线程中执行
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!deviceExist(device.getAddress())) {
                        DeviceInfo deviceInfo = new DeviceInfo(device.getAddress(),device.getName());
                        data.add(deviceInfo);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        adapter = new AddBluetoothAdapter(data, this);
        gateList.setAdapter(adapter);
        gateList.setOnItemClickListener(this);

        manager = BleManager.getInstance();
        if (!manager.bluetoothEnable()) {
            toToast("设备部支持蓝牙4.0，即将关闭程序");
            finish();
        }
        manager.setLeScanResult(leScanResult);

        EventBus.getDefault().register(this);//注册EventBus
        //注册广播
//        register();
        loadingDialog = DialogManager.createDialog(DialogManager.LOADING_DIALOG,this);
    }

    /**
     * 注册广播
     */
    private void register() {
        IntentFilter intent = new IntentFilter();
//        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//连接状态发生改变
//        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//密码请求
//        intent.addAction(BluetoothDevice.EXTRA_PAIRING_VARIANT);//
//        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, intent);
    }


    BluetoothDevice device = null;
    private BroadcastReceiver receiver = new BluetoothStateBroadcast();
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            getState(intent);
////            try {
////                toToast("广播输入密码：" + manager.autoBond(address, "000000"));
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//        }
//    };

    //-----
    public void getState(Intent intent) {
        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDING:
                try {
//                        manager.autoBond(address,"000000");
//                        manager.createBond(address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "正在配对......");
                break;
            case BluetoothDevice.BOND_BONDED:
                Log.d(TAG, "完成配对");
//                    connect(device);//连接设备
                break;
            case BluetoothDevice.BOND_NONE:
                Log.d(TAG, "取消配对");
            default:
                break;
        }
    }
    //-----
    public void onEventMainThread(BluetoothDevice device){
        Log.d(TAG, "onEventMainThread......");
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDING:
                Log.d(TAG, "正在配对......");
                break;
            case BluetoothDevice.BOND_BONDED:
                Log.d(TAG, "完成配对");
                if (manager.addGatt(device,new MyGattCallBack(address))){
                    loadingDialog.dismiss();
                    DialogManager.dismiss(loadingDialog);
                    Intent intent = new Intent(MainActivity.this,OperationActivity.class);
                    intent.putExtra(ADD, address);
                    startActivity(intent);
                }

                break;
            case BluetoothDevice.BOND_NONE:
                Log.d(TAG, "取消配对");
                loadingDialog.dismiss();
                toToast("已经取消匹配，请重新匹配");
                break;
        }
    }

//    public void onEventPostThread(BluetoothDevice device){
//        Log.d(TAG, "onEventPostThread......");
//    }
//    public void onEventBackgroundThread(BluetoothDevice device){
//        Log.d(TAG, "onEventBackgroundThread......");
//    }public void onEventAsync(BluetoothDevice device){
//        Log.d(TAG, "onEventAsync......");
//    }

    @Override
    protected void onPause() {
        DialogManager.dismiss(loadingDialog);
        super.onPause();
    }

    /**
     * 开启搜索蓝牙设备
     */
    public void scanBlue(View view) {
        manager.scanLeDevice(true);
    }

    public void toToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        manager.disconnectAll();
//        manager.closeBluetooth();
        manager = null;

        EventBus.getDefault().unregister(this);
        //取消广播
//        unregisterReceiver(receiver);
        super.onDestroy();
    }

    String address = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "点击item:" + position);
        address = data.get(position).getAddress();
//        if (manager.connect(address,new MyGattCallBack(address,MainActivity.this))){
//            Intent intent = new Intent(MainActivity.this,OperationActivity.class);
//            intent.putExtra(ADD,address);
//            startActivity(intent);
//        }
        manager.connect(address);
        loadingDialog.show();
        //---
//        try {
////            manager.autoBond(address, "000000");
////            manager.createBond(address);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 取消匹配
     */
    public void cancelPin(View view) {
        try {
            manager.removeBond(address);
        } catch (Exception e) {
            Log.w(TAG, "取消匹配失败");
        }
    }


    /**
     * 设备是否已经添加
     */
    private boolean deviceExist(String address){
        for (DeviceInfo deviceInfo:data) {
            if (deviceInfo.getAddress().equals(address)){
                return true;
            }
        }
        return false;
    }



}
