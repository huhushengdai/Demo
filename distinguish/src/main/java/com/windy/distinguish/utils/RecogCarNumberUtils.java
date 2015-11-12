package com.windy.distinguish.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.wintone.lisence.DeviceFP;
import com.wintone.lisence.WintoneLSCOperateTools;
import com.wintone.lisence.WintoneLSCXMLInformation;
import com.wintone.plateid.PlateIDAPI;
import com.wintone.plateid.RecogService;
import com.wintone.plateid.TH_PlateIDCfg;
import com.wintone.plateid.TH_PlateIDResult;

/**
 * author: wang
 * time: 2015/11/2
 * description:
 *     车牌识别的工具方法
 */
public class RecogCarNumberUtils implements ServiceConnection {

    public RecogService.MyBinder recogBinder;
    private Context context;
    private Handler handler;

    // 调用方法的配置
    private int iInitPlateIDSDK = -1;
    private int imageformat = 1;
    private int bVertFlip = 0;
    private int bDwordAligned = 1;
    private String[] fieldvalue = new String[14];
    private int nRet = -1;
    private String recogPicPath;
    private boolean bGetVersion = false;

    // 初始化构造方法
    public RecogCarNumberUtils(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    // 接收到指令
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        recogBinder = (RecogService.MyBinder) service;
        recogPicPath = "/storage/sdcard0/carnumber.jpg";

        // 调用函数成功为0；
        iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();
        Log.v("iInitPlateIDSDK----", "" + iInitPlateIDSDK);
//        if (iInitPlateIDSDK != 0) {//失败
//            handler.sendEmptyMessage(2);
//            return;
//        } else {
//            // 开启识别服务
//            startRecogCarNumber();
//        }
        startRecogCarNumber();
        if (recogBinder != null) {
            context.unbindService(this);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        recogBinder = null;
    }

    /**
     * 开始识别车牌
     * */
    private void startRecogCarNumber(){
        recogBinder.setRecogArgu(recogPicPath, imageformat,
                bGetVersion, bVertFlip, bDwordAligned);

//        nRet = recogBinder.getnRet();
        // 返回的识别结果值
//        fieldvalue = recogBinder.doRecog(recogPicPath, 480, 800);
        fieldvalue = recogPlate((byte[])null,recogPicPath,480,800,(String)null);
//        nRet = recogBinder.getnRet();
        Log.e("nRet", nRet + "");

//        if (nRet != 0) {//识别失败
//            handler.sendEmptyMessage(2);
//            return;
//        } else {
        hanlderAnalysisResult();
//        }
    }
    public String[] recogPlate(byte[] picByte, String pic, int width, int height, String userData) {
        String[] fieldvalue = new String[15];
        int[] nResultNums = new int[]{10};
        int[] nRets = new int[]{-1};
        TH_PlateIDResult plateidresult = new TH_PlateIDResult();
        TH_PlateIDResult[] plateidresults;


        TelephonyManager telephonyManager1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        PlateIDAPI plateIDAPI = new PlateIDAPI();
        TH_PlateIDCfg c_Config2 = new TH_PlateIDCfg();




//        WintoneLSCXMLInformation wli = WintoneLSCOperateTools.ReadAuthFile();
//        cdkeyString = wli.anoString;
//        serialString = wli.snoString;
        DeviceFP deviceFP = new DeviceFP();
        deviceFP.deviceid = "358740055258908";

        plateIDAPI.TH_InitPlateIDSDK(c_Config2, telephonyManager1, deviceFP);

        if(picByte != null && picByte.length > 0) {
            plateidresults = plateIDAPI.TH_RecogImageByte(picByte, width, height, plateidresult, nResultNums, 0, 0, 0, 0, nRets);
        } else {
            plateidresults = plateIDAPI.TH_RecogImage(pic, width, height, plateidresult, nResultNums, 0, 0, 0, 0, nRets);
        }

        this.nRet = nRets[0];
        if(nRets[0] != 0) {
            fieldvalue[14] = userData;
        } else {
            fieldvalue[14] = userData;

            for(int i = 0; i < nResultNums[0]; ++i) {
                if(plateidresults != null && plateidresults[i] != null) {
                    if(i == 0) {
                        fieldvalue[0] = plateidresults[i].license;
                        fieldvalue[1] = plateidresults[i].color;
                        fieldvalue[2] = this.int2string(plateidresults[i].nColor);
                        fieldvalue[3] = this.int2string(plateidresults[i].nType);
                        fieldvalue[4] = this.int2string(plateidresults[i].nConfidence);
                        fieldvalue[5] = this.int2string(plateidresults[i].nBright);
                        fieldvalue[6] = this.int2string(plateidresults[i].nDirection);
                        fieldvalue[7] = this.int2string(plateidresults[i].left);
                        fieldvalue[8] = this.int2string(plateidresults[i].top);
                        fieldvalue[9] = this.int2string(plateidresults[i].right);
                        fieldvalue[10] = this.int2string(plateidresults[i].bottom);
                        fieldvalue[11] = this.int2string(plateidresults[i].nTime);
                        fieldvalue[12] = this.int2string(plateidresults[i].nCarBright);
                        fieldvalue[13] = this.int2string(plateidresults[i].nCarColor);
                        fieldvalue[14] = userData;
                    } else {
                        fieldvalue[0] = fieldvalue[0] + ";" + plateidresults[i].license;
                        fieldvalue[1] = fieldvalue[1] + ";" + plateidresults[i].color;
                        fieldvalue[2] = fieldvalue[2] + ";" + this.int2string(plateidresults[i].nColor);
                        fieldvalue[3] = fieldvalue[3] + ";" + this.int2string(plateidresults[i].nType);
                        fieldvalue[4] = fieldvalue[4] + ";" + this.int2string(plateidresults[i].nConfidence);
                        fieldvalue[5] = fieldvalue[5] + ";" + this.int2string(plateidresults[i].nBright);
                        fieldvalue[6] = fieldvalue[6] + ";" + this.int2string(plateidresults[i].nDirection);
                        fieldvalue[7] = fieldvalue[7] + ";" + this.int2string(plateidresults[i].left);
                        fieldvalue[8] = fieldvalue[8] + ";" + this.int2string(plateidresults[i].top);
                        fieldvalue[9] = fieldvalue[9] + ";" + this.int2string(plateidresults[i].right);
                        fieldvalue[10] = fieldvalue[10] + ";" + this.int2string(plateidresults[i].bottom);
                        fieldvalue[11] = fieldvalue[11] + ";" + this.int2string(plateidresults[i].nTime);
                        fieldvalue[12] = fieldvalue[12] + ";" + this.int2string(plateidresults[i].nCarBright);
                        fieldvalue[13] = fieldvalue[13] + ";" + this.int2string(plateidresults[i].nCarColor);
                    }
                }
            }
        }

        return fieldvalue;
    }
    private String int2string(int i) {
        String str = "";

        try {
            str = String.valueOf(i);
        } catch (Exception var4) {
            ;
        }

        return str;
    }
    /**
     * 分析返回的结果
     * */
    private void hanlderAnalysisResult(){
        String car_Id = fieldvalue[0];
        if (car_Id != null) {
            // 界面返回车牌号显示
            car_Id = car_Id.toUpperCase();
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("CarNumber", car_Id);
            msg.what = 1;
            msg.setData(bundle);
            handler.sendMessage(msg);
        } else{//失败
            handler.sendEmptyMessage(2);
        }
    }

}
