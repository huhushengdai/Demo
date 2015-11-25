package com.windy.nfctest.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * author: wang
 * time: 2015/9/28
 * description:
 *     卡片字节数组信息
 */
public class CardByteArray implements Serializable{
    long cardNum;//卡号
    HashMap<Integer,byte[]> readData;//读取的卡片信息
    HashMap<Integer,byte[]> writeData;//写入的卡片信息

    public CardByteArray() {
    }

    public long getCardNum() {
        return cardNum;
    }

    public void setCardNum(long cardNum) {
        this.cardNum = cardNum;
    }

    public HashMap<Integer, byte[]> getReadData() {
        return readData;
    }

    public void setReadData(HashMap<Integer, byte[]> readData) {
        this.readData = readData;
    }

    public HashMap<Integer, byte[]> getWriteData() {
        return writeData;
    }

    public void setWriteData(HashMap<Integer, byte[]> writeData) {
        this.writeData = writeData;
    }
}
