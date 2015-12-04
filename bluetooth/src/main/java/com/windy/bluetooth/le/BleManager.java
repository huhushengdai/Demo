package com.windy.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * author: wang
 * time: 2015/10/12
 * description:
 * 蓝牙4.0管理器
 */
public class BleManager {
    private static final String TAG = "BleManager";

    private static BleManager manager;//

    private static Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean enable;//蓝牙设备是否可用
    private Handler mHandler;
    private boolean mScanning;//蓝牙设备开启标识
    private LeScanResult mScanResult;//蓝牙搜索结果的接口回调
    // 存储gatt 跟 向蓝牙设备发送信息的接口回调
    HashMap<String, BluetoothContainer> gatts = new HashMap<String, BluetoothContainer>();
    private static final long SCAN_PERIOD = 10000;//蓝牙搜索的时间
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (mScanResult != null) {
                mScanResult.onLeScan(device, rssi, scanRecord);
                Log.i(TAG, "address:" + device.getAddress() + ",rssi:" + rssi);
            } else {
                Log.w(TAG, "mScanResult is null");
            }
        }
    };

//------------------------------------方法----------------------------------------------------------

    private BleManager() {
        mHandler = new Handler();
        enable = initAdapter();
    }

    /**
     * 获取BleManager实例
     */
    public static BleManager getInstance() {
        if (manager == null) {
            synchronized (BleManager.class) {
                if (manager == null) {
                    manager = new BleManager();
                }
            }
        }
        return manager;
    }

    /**
     * 设置上下文——主要在application中调用
     */
    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * init BluetoothAdapter
     */
    public boolean initAdapter() {
        // 判断手机是否支持蓝牙4.0
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }
        //
        mBluetoothAdapter = ((BluetoothManager) mContext
                .getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        // Checks if Bluetooth is supported on the device.
        // 判断手机是否支持蓝牙
        if (mBluetoothAdapter == null) {
            return false;
        }
        // 开启蓝牙
        mBluetoothAdapter.enable();

        return true;
    }

    /**
     * 手机是否支持蓝牙4.0功能
     *
     * @return
     */
    public boolean bluetoothEnable() {
        return enable;
    }

    /**
     * 打开蓝牙
     */
    public boolean openBluetooth() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.enable();
        }
        return false;
    }

    /**
     * 关闭蓝牙
     */
    public boolean closeBluetooth() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.disable();
        }
        return false;
    }

    /**
     * 查看蓝牙设备是否打开
     *
     * @return 如果已经开启蓝牙设备，则返回true
     */
    public boolean isOpenBluetooth() {
        if (mBluetoothAdapter == null || mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) {
            Log.w(TAG, "BluetoothAdapter is null,state:" + mBluetoothAdapter.getState());
            return false;
        }
        return true;
    }

    /**
     * 设置蓝牙搜索到结果的接口回调
     *
     * @param scanResult 蓝牙搜索结果后的接口回调
     */
    public void setLeScanResult(LeScanResult scanResult) {
        mScanResult = scanResult;
    }

    /**
     * 启动或关闭蓝牙搜索
     *
     * @param enable true为开启搜索
     */
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            //扫描10秒（SCAN_PERIOD）后，关闭扫描
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Log.i(TAG, "关闭扫描");
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.i(TAG, "开启扫描");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.i(TAG, "关闭扫描");
        }
    }

    /**
     * 连接设备
     *
     * @param address 设备地址
     * @return 连接成功返回true
     */
    public synchronized boolean connect(final String address, final MyGattCallBack gattCallBack) {
        if (!isOpenBluetooth() || TextUtils.isEmpty(address)) {
            Log.w(TAG, "没有开启蓝牙设备，或者address=" + address);
            return false;
        }
        if (gatts.containsKey(address)) {
            BluetoothGatt gatt = gatts.get(address).getGatt();
            BluetoothDevice deviceExist = gatt.getDevice();
            if (deviceExist.getBondState()==BluetoothDevice.BOND_BONDED){
               return gatt.connect();
            }
//            else{
//                createBond(deviceExist);
//                return false;
//            }
//            if (gatt != null && gatts.get(address).getGatt().connect()) {
//                Log.i(TAG, "连接蓝牙设备成功，连接对象：" + address);
//                return true;
//            } else {
//                Log.i(TAG, "连接蓝牙设备失败，连接对象：" + address);
//                return false;
//            }
        }

        // 获得给定的蓝牙硬件地址的蓝牙设备对象
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        //判断设备是否匹配
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            createBond(device);
            return false;
        }

        BluetoothGatt gatt = device.connectGatt(mContext, false, gattCallBack);
//        while(device.getBondState() != BluetoothDevice.BOND_BONDED){
//            device = mBluetoothAdapter.getRemoteDevice(address);
//        }

        gatts.put(address, new BluetoothContainer(gatt, gattCallBack));
        Log.i(TAG, "连接蓝牙设备成功，连接对象：" + address);
        return true;
    }

    public synchronized boolean connect(String address){
        if (!isOpenBluetooth() || TextUtils.isEmpty(address)) {
            Log.w(TAG, "没有开启蓝牙设备，或者address=" + address);
            return false;
        }
//        if (gatts.containsKey(address)) {
//            BluetoothGatt gatt = gatts.get(address).getGatt();
//            BluetoothDevice deviceExist = gatt.getDevice();
//            if (deviceExist.getBondState()==BluetoothDevice.BOND_BONDED){
//                return gatt.connect();
//            }
//        }
        // 获得给定的蓝牙硬件地址的蓝牙设备对象
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        //判断设备是否匹配
        if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
            createBond(device);
            Log.i(TAG,"设备没有连接");
            return false;
        }else {
            EventBus.getDefault().post(device);
            Log.i(TAG, "设备已经连接");
            return true;
        }
    }
    public synchronized boolean addGatt(BluetoothDevice device,MyGattCallBack callback){
        if (device!=null){
            BluetoothGatt gatt = device.connectGatt(mContext, false, callback);
            gatts.put(device.getAddress(), new BluetoothContainer(gatt, callback));
            Log.i(TAG, "连接蓝牙设备成功，连接对象：" + device.getAddress());
            return true;
        }
        return false;
    }
    /**
     * 自动配对设置Pin值
     */
    public static boolean autoBond(BluetoothDevice btDevice, String strPin) throws Exception {
        return btDevice.setPin(strPin.getBytes());
    }

    /**
     * 创建连接，进行配对
     */
    private boolean createBond(BluetoothDevice btDevice) {
        return btDevice.createBond();
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public boolean removeBond(String address)
            throws Exception {
        BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(address);
        Class btClass = btDevice.getClass();
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 断开蓝牙设备连接
     *
     * @param address
     */
    public synchronized void disconnect(String address) {
        if (address.isEmpty()) {
            return;
        }
        BluetoothContainer container = gatts.get(address);
        if (container == null) {
            return;
        }
        BluetoothGatt gatt = container.getGatt();
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
            gatts.remove(address);
        }
    }

    /**
     * 断开所有已经连接的蓝牙设备
     */
    public void disconnectAll() {
        for (Map.Entry<String, BluetoothContainer> entry : gatts.entrySet()) {
            disconnect(entry.getKey());
        }
    }

    /**
     * 判断与设备连接的状态
     * -1：没有连接设备
     */
    public int connectState(String address) {
        BluetoothContainer container = gatts.get(address);
        if (container == null) {
            return -1;
        }
        return container.getGatt().getDevice().getBondState();
    }

    /**
     * write instruction
     * 修改密码用的char6
     */
    public boolean write(String address, String text) {
        BluetoothContainer container = gatts.get(address);
        if (container != null) {
            BluetoothGatt gatt = container.getGatt();
            MyGattCallBack callBack = container.getGattCallBack();
            if (gatt.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                //重新连接，匹配设备
                if (!connect(address,callBack)){
                    return false;
                }
            }
            if (callBack.setChar(text) != null) {
                Log.i(TAG, "char6写入指令：" + text);
                return gatt.writeCharacteristic(callBack.char6);
            }
        }
        Log.i(TAG, "char6没有写入指令：");
        return false;
    }

    /**
     * 写入指令
     * 开闸关闸用char1
     *
     * @param address
     * @param instruction
     * @return
     */
    public synchronized boolean write(String address, byte[] instruction) {
        BluetoothContainer container = gatts.get(address);
        if (container != null) {
            BluetoothGatt gatt = container.getGatt();
            MyGattCallBack callBack = container.getGattCallBack();
            if (gatt.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                //重新连接，匹配设备
                if (!connect(address,callBack)){
                    return false;
                }
            }
            if (callBack.setChar(instruction) != null) {
                Log.i(TAG, "char1写入指令：" + instruction[0]);
                return gatt.writeCharacteristic(callBack.char1);
            }
        }
        Log.i(TAG, "char1没有写入指令：");
        return false;
    }
//------------------------------------接口----------------------------------------------------------

    //蓝牙搜索获取结果后的接口回调
    public interface LeScanResult {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }
//------------------------------------类----------------------------------------------------------

    //装载蓝牙gatt 跟 gattCallBack的容器类
    class BluetoothContainer {
        private BluetoothGatt gatt;
        private MyGattCallBack gattCallBack;

        /**
         * @param gatt
         * @param gattCallBack
         */
        public BluetoothContainer(BluetoothGatt gatt, MyGattCallBack gattCallBack) {
            this.gatt = gatt;
            this.gattCallBack = gattCallBack;
        }

        public BluetoothContainer() {
        }

        @Override
        public String toString() {
            return "BluetoothContainer [gatt=" + gatt + ", gattCallBack=" + gattCallBack + "]";
        }

        /**
         * @return the gatt
         */
        public BluetoothGatt getGatt() {
            return gatt;
        }

        /**
         * @param gatt the gatt to set
         */
        public void setGatt(BluetoothGatt gatt) {
            this.gatt = gatt;
        }

        /**
         * @return the gattCallBack
         */
        public MyGattCallBack getGattCallBack() {
            return gattCallBack;
        }

        /**
         * @param gattCallBack the gattCallBack to set
         */
        public void setGattCallBack(MyGattCallBack gattCallBack) {
            this.gattCallBack = gattCallBack;
        }

    }
}
