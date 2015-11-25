package com.windy.nfctest.callback;

import com.windy.nfctest.bean.CardByteArray;

/**
 * author: wang
 * time: 2015/9/29
 * description:
 */
public interface ReadBack {
    public void callBack(CardByteArray cardByte);
}
