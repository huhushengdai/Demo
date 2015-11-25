package com.windy.nfctest.utils;

import android.util.Log;

import com.windy.nfctest.bean.CardByteArray;
import com.windy.nfctest.bean.CardInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * author: wang
 * time: 2015/9/28
 * description:
 *    数据转换
 */
public class ConvertManager {
    public static final String TAG = "ConvertManager";
    //---
    public static final String[] cardTypes = {"贵宾卡","月卡","储值卡","临时卡","免费卡"};
    public static final String[] carTypes = {"摩托车","小型车","中型车","大型车","特型车"};
    public static final int sector4 = 4;
    public static final int sector5 = 5;
    public static final int sector6 = 6;
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static CardInfo convertData(CardByteArray cardByte){
        CardInfo cardInfo = null;
        HashMap<Integer,byte[]> data = null;
        if (cardByte!=null){
            cardInfo = new CardInfo();
            cardInfo.setCardNum(cardByte.getCardNum());//卡号
            data = cardByte.getReadData();//读取信息
            //---4块信息
            byte[] arr = data.get(sector4);
            cardInfo.setCardType(cardTypes[arr[2]]);
            cardInfo.setCarType(carTypes[arr[3]]);
            //---5或6块信息，需要先判断有效块
            arr = checkSector(data.get(sector5),data.get(sector6));
            byte[] time = new byte[4];
            System.arraycopy(arr, 0, time, 0, 4);
            cardInfo.setEnterTime(long2datetime((long) getTime(time)));//入场时间
            cardInfo.setEnterState(arr[4]);//出入场状态
            cardInfo.setChargeFlag((byte)((arr[12] & 4) == 4?1:0));// 缴费状态
            cardInfo.setDiscountFlag((byte)((arr[12] & 8) == 8?1:0));// 折扣状态
        }

        return cardInfo;
    }

    /**
     * 校验有效扇区
     */
    public static byte[] checkSector(byte[] first,byte[] second){
        return first[13] >=second[13]? first: second;
    }

    /**
     * 打印byte数组
     */
    public static void printByte(byte[] b) {
        StringBuilder sb = new StringBuilder();
        sb.append("字节数组：");
        int length = b.length;
        for (int i = 0; i < length; i++) {
            sb.append(b[i]).append(",");
        }
        Log.e(TAG, sb.toString());
    }
    //----卡号
    public static long getCardNum(byte[] data) {
        byte d[] = new byte[8];
        System.arraycopy(data, 0, d, 0, 4);
        return byteArray2Long(d);
    }
    public static long byteArray2Long(byte[] a) {
        long res = 0L;
        int[] t = new int[8];
        for (int i = 0; i < 8; i++) {
            t[i] = a[7 - i];
        }
        res = t[0] & 0x0ff;
        for (int i = 1; i < 8; i++) {
            res <<= 8;
            res += (t[i] & 0x0ff);
        }
        return res;
    }
    //把卡号转换成key
    public static byte[] cypherKey(byte data[]) {
        byte[] result = new byte[6];
        System.arraycopy(bytesReverseOrder(data), 0, result, 1, 4);
        result[0] = 0x00;
        result[5] = (byte) 0xff;
        return result;
    }
    public static byte[] bytesReverseOrder(byte[] b) {
        int length = b.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[length - i - 1] = b[i];
        }
        return result;
    }

    /**
     * 将long转换为日期，
     * @param longdate 以秒为单位
     * @return 未作格式异常处理
     */
    public static String long2datetime(Long longdate) {
        if (longdate == null || "".equals(longdate)) {
            return null;
        }
        Date date = new Date(longdate * 1000);
        return df.format(date);
    }
    /**
     * 将低字节数组转换为int
     * 获得时间
     * @param b
     *            byte[]
     * @return int
     */
    public static int getTime(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }
    /**
     * 低字节数组转换为float
     *  转换成金额
     * @param b
     *            byte[]
     * @return float
     */
    public static float getMoney(byte[] b) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8
                | (b[0] & 0xff);
        return F.intBitsToFloat(i);
    }
}
