package com.windy.bluetooth.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.windy.bluetooth.R;
import com.windy.bluetooth.adapters.AddBluetoothAdapter;
import com.windy.bluetooth.le.BleManager;
import com.windy.bluetooth.le.GattAttributes;
import com.windy.bluetooth.le.MyGattCallBack;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";
    public static String ADD = "address";

    @ViewInject(R.id.gate_list)
    private ListView gateList;
    private ArrayList<String> data = new ArrayList<>();
    private AddBluetoothAdapter adapter;

    private BleManager manager;

    private BleManager.LeScanResult leScanResult = new BleManager.LeScanResult() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //在UI线程中执行
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String address = device.getAddress();
                    if (!data.contains(address)) {
                        data.add(address);
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
        //注册广播
//        register();
    }

    /**
     * 注册广播
     */
    private void register() {
        IntentFilter intent = new IntentFilter();
//        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
//        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//连接状态发生改变
        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//密码请求
//        intent.addAction(BluetoothDevice.EXTRA_PAIRING_VARIANT);//
//        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, intent);
    }

    BluetoothDevice device = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            getState(intent);
//            try {
//                toToast("广播输入密码：" + manager.autoBond(address, "000000"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };

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
        //取消广播
//        unregisterReceiver(receiver);
        super.onDestroy();
    }

    String address = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "点击item:" + position);
        address = data.get(position);
        if (manager.connect(address,new MyGattCallBack(address,MainActivity.this))){
            Intent intent = new Intent(MainActivity.this,OperationActivity.class);
            intent.putExtra(ADD,address);
            startActivity(intent);
        }

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
     * 创建匹配
     */
    public void createPin(View view) {
//        try {
//            manager.createBond(address);
//        } catch (Exception e) {
//            Log.w(TAG,"创建匹配失败");
////        }
//        BluetoothDevice device = manager.getDevice(address);
//        String stateInfo = "";
//        switch (device.getBondState()) {
//            case -1:
//                stateInfo = "找不到连接设备";
//                break;
//            case BluetoothDevice.BOND_NONE:
//                stateInfo = "没有连接";
//                try {
//                    manager.createBond(address);
////                    toToast("自动连接：" + manager.autoBond(address, "000000"));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case BluetoothDevice.BOND_BONDING:
////                try {
////                    toToast("自动连接：" + manager.autoBond(address,"000000"));
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                stateInfo = "正在连接";
//                break;
//            case BluetoothDevice.BOND_BONDED:
//                stateInfo = "已经成功连接";
//                break;
//
//        }
//        ((Button) view).setText("连接状态：" + stateInfo);
    }

    /**
     * 进入新页面
     */
    public void into(View view) {
        if (manager.connect(address, new MyGattCallBack(address, this))) {
            Intent intent = new Intent(this, OperationActivity.class);
            intent.putExtra(ADD, address);
            startActivity(intent);
        }
    }

    public void setPin(View view) {
//        try {
//            toToast("输入密码：" + manager.autoBond(address, "000000"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
