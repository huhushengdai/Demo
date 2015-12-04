package com.windy.tab;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


public class YellowActivity extends Activity implements SensorEventListener {
    private static final String TAG = "YellowActivity";

    private SensorManager mSensorManager;
    private Sensor GVSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow);

        //
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensorManager.registerListener(mySensorEventListener,
//                sensor_accelerometer,   SensorManager.SENSOR_DELAY_UI);
        GVSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        GVSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        mSensorManager.
        //
        /*
         * 最常用的一个方法 注册事件
         * 参数1 ：SensorEventListener监听器
         * 参数2 ：Sensor 一个服务可能有多个Sensor实现，此处调用getDefaultSensor获取默认的Sensor
         * 参数3 ：模式 可选数据变化的刷新频率
         * */
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        printInfo("create");
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }



    private void printInfo(String info){
        Log.i(TAG,info);
    }


    /**
     * 感应器监听
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.i(TAG,"onSensorChanged");

            //图解中已经解释三个值的含义
            float X_lateral = sensorEvent.values[0];
            float Y_longitudinal = sensorEvent.values[1];
            float Z_vertical = sensorEvent.values[2];
            Log.i(TAG,"\n heading "+X_lateral);
            Log.i(TAG,"\n pitch "+Y_longitudinal);
            Log.i(TAG,"\n roll "+Z_vertical);

            if (X_lateral>5){
                setLandscape();
            }else if (X_lateral<-5){
                setReLandscape();
            }else{
                setPortrait();
            }
//            heading -0.038307227
//            pitch -0.047884032
//            roll 9.749189

//            heading -1.685518
//            pitch 8.676587
//            roll 4.3861775
        }
    }

    /**
     * 设置横屏
     */
    private void setLandscape(){
        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
    /**
     * 设置反向横屏
     */
    private void setReLandscape(){
        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    /**
     * 设置竖屏
     */
    private void setPortrait(){
        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //复写onAccuracyChanged方法
    public void onAccuracyChanged(Sensor sensor , int accuracy){
        Log.i(TAG, "onAccuracyChanged");
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }
}
