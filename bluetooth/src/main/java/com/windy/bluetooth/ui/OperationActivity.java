package com.windy.bluetooth.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.windy.bluetooth.R;
import com.windy.bluetooth.le.BleManager;
import com.windy.bluetooth.le.GattAttributes;

public class OperationActivity extends Activity {
    private static final String TAG = "OperationActivity";

    @ViewInject(R.id.gate_address)
    TextView addView;
    @ViewInject(R.id.gate_state)
    TextView state;
    @ViewInject(R.id.gate_info)
    TextView info;

    private String address;

    private BleManager manager;

    private int count = 1;
    private BroadcastReceiver broadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            info.setText("第" + (count++) + "次蓝牙设备返回数据：" + intent.getStringExtra(GattAttributes.BROAD_INFO));
            Log.i(TAG, "接收到蓝牙数据:" + intent.getStringExtra(GattAttributes.BROAD_INFO));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        ViewUtils.inject(this);
        address = getIntent().getStringExtra(MainActivity.ADD);
        init();
    }

    private void init() {
        manager = BleManager.getInstance();

        addView.setText("蓝牙设备地址：" + address);

        IntentFilter filter = new IntentFilter();
        filter.addAction(address);
        registerReceiver(broadReceiver, filter);
    }

    @Override
    protected void onResume() {
        refreshState();
        super.onResume();
    }

    private void refreshState() {
        String stateInfo = "";
        switch (manager.connectState(address)) {
            case -1:
                stateInfo = "找不到连接设备";
                break;
            case BluetoothDevice.BOND_NONE:
                stateInfo = "没有连接";
                break;
            case BluetoothDevice.BOND_BONDING:
                stateInfo = "正在连接";
                break;
            case BluetoothDevice.BOND_BONDED:
                stateInfo = "已经成功连接";
                break;

        }
        state.setText("连接状态：" + stateInfo);
    }

    /**
     * button 点击事件
     */
    @OnClick({R.id.gate_open, R.id.gate_close, R.id.gate_read, R.id.gate_refresh})
    public void openGate(View view) {
        switch (view.getId()) {
            case R.id.gate_open://开闸
                manager.write(address, GattAttributes.open);
                break;
            case R.id.gate_close://关闸
                manager.write(address, GattAttributes.close);
                break;
            case R.id.gate_read://读取状态
                manager.write(address, GattAttributes.read);
                break;
            case R.id.gate_refresh://更新连接状态
                refreshState();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadReceiver);
        super.onDestroy();
    }
}
