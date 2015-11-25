package com.windy.nfctest.utils;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.util.Log;

import com.windy.nfctest.app.BaseApp;
import com.windy.nfctest.bean.CardByteArray;

import java.io.IOException;
import java.util.HashMap;

/**
 * author: wang
 * time: 2015/9/28
 * description:
 * NFC管理
 */
public class CardManager {
    public static final String TAG = "CardManager";

    private static final int sector = 1;//扇区
    //过滤器
    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;
    static {
        try {
            TECHLISTS = new String[][] { { IsoDep.class.getName() },
                    { NfcV.class.getName() }, { NfcF.class.getName() },
                    { NfcA.class.getName() }, { MifareClassic.class.getName() } };

            FILTERS = new IntentFilter[] { new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
        } catch (Exception e) {
        }
    }
    /**
     * 读取NFC数据
     */
    public static CardByteArray readCard(Intent intent){
//        String info = "正在读取数据";
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        byte[] key = ConvertManager.cypherKey(tag.getId());//读取NFC密钥

        final MifareClassic mifareClassic = MifareClassic.get(tag);
        if (mifareClassic!=null){
            try {
                return readData(mifareClassic,tag.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//            info = "无法读取MifareClassic";
            return null;

    }

    public static CardByteArray readData(MifareClassic mfc,byte[] cardNo) throws IOException {
        CardByteArray cardInfo = null;//存储卡片信息
        mfc.connect();
        HashMap<Integer,byte[]> data = new HashMap<>();
        ConvertManager.printByte(cardNo);
        byte[] teaaa = ConvertManager.cypherKey(cardNo);

        //----test
        boolean sector0 = mfc.authenticateSectorWithKeyA(0,MifareClassic.KEY_DEFAULT );
        sector0 = mfc.authenticateSectorWithKeyB(0, MifareClassic.KEY_DEFAULT);
        //----test
        //是否可读取信息
        boolean auth = mfc.authenticateSectorWithKeyA(sector, ConvertManager.cypherKey(cardNo));
        if (auth){
            BaseApp.toast(sector+"扇区获取读取权限成功");
            //sector 扇区中 块的数目，-1表示不要最后一个块（该块存密码）
            int count = mfc.getBlockCountInSector(sector) - 1;
            int bindex = mfc.sectorToBlock(sector);//sector扇区的第一个块
            for (int i = 0; i < count; i++) {
                data.put(bindex, mfc.readBlock(bindex));
                bindex++;
            }
//            bindex = mfc.sectorToBlock(sector+1);//读取2扇区，折扣信息
//            data.put(bindex, mfc.readBlock(bindex));
            cardInfo = new CardByteArray();//
            cardInfo.setCardNum(ConvertManager.getCardNum(cardNo));//卡号
            cardInfo.setReadData(data);

        }else {
            BaseApp.toast(sector+"扇区获取读取权限失败");
            Log.e(TAG,"密码不对");
        }
        return cardInfo;
    }
}
