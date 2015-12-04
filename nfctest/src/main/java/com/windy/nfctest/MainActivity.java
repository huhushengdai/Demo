package com.windy.nfctest;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.windy.nfctest.bean.CardByteArray;
import com.windy.nfctest.utils.CardManager;
import com.windy.nfctest.utils.ConvertManager;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";

    //-----view
    @ViewInject(R.id.state)
    TextView state;
    @ViewInject(R.id.information)
    TextView info;
    //-----view

    private NfcAdapter nfcAdapter;//Nfc适配器
    private PendingIntent pendingIntent;//作用：当扫描到IC卡后跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        initNfc();
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    private void initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        refreshStatus();
    }

    /**
     * 更新现在状态
     */
    private void refreshStatus() {
        if (nfcAdapter == null) {
            state.setText("设备不支持NFC功能");
        }
        else if (nfcAdapter.isEnabled()) {
            state.setText("NFC已经启动");
        }
        else {
            state.setText("NFC没有开启");
        }
    }

    @OnClick(R.id.setting)
    public void settting(View view){
        startActivityForResult(new Intent(
                android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0){
            refreshStatus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    CardManager.FILTERS, CardManager.TECHLISTS);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CardByteArray cardByteArray = CardManager.readCard(intent);
//        if (cardByteArray!=null){
//            CardInfo cardInfo = ConvertManager.convertData(cardByteArray);
//            if (cardInfo!=null){
//                info.setText(cardInfo.toString());
//            }else{
//                info.setText("cardInfo=null");
//            }
//        }else {
//            info.setText("cardByteArray=null");
//        }
        readCardNum(intent);
    }
    private void readCardNum(Intent intent){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] cardId = tag.getId();
        long cardNum =  ConvertManager.getCardNum(cardId);
        Toast.makeText(this,"卡号："+cardNum,Toast.LENGTH_SHORT).show();
    }
}
