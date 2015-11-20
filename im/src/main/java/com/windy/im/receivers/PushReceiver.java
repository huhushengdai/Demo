package com.windy.im.receivers;

import android.content.Context;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;

import java.util.List;

/**
 * author: wang
 * time: 2015/11/20
 * description:
 */
public class PushReceiver extends PushMessageReceiver{
    private static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onBind(Context context, int errorCode,
                       String appid, String userId, String channelId, String requestId) {
        Log.i(TAG,"onBind——errorCode："+errorCode);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        Log.i(TAG,"onUnbind");
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
        Log.i(TAG,"onSetTags");
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
        Log.i(TAG,"onDelTags");
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
        Log.i(TAG,"onListTags");
    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        Log.i(TAG,"onMessage");
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        Log.i(TAG,"onNotificationClicked");
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        Log.i(TAG,"onNotificationArrived");
    }
}
