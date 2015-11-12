package com.windy.distinguish;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.windy.distinguish.utils.RecogCarNumberThread;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity {
    private Handler handler;
    private ProgressDialog progressDialog;
    private ImageView image;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fun();
        initHandlerListener();
        image = (ImageView) findViewById(R.id.image);
        Bitmap bm = BitmapFactory.decodeFile("/storage/sdcard0/carnumber.jpg");
        image.setImageBitmap(bm);
        text = (TextView) findViewById(R.id.textnumber);
    }

    public void fun(){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.carnumber);
        File myCaptureFile = new File("/storage/sdcard0/carnumber.jpg");
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        // 100表示不进行压缩，70表示压缩率为30%
    }

    /**
     * 点击车牌识别按钮
     * */
    public void getCarNumber(View v){
        progressDialog = ProgressDialog.show(this, "Loading...", "正在识别，请稍后...", true, false);
        progressDialog.show();
        new Thread(new RecogCarNumberThread(this, handler, true)).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 8 && event.getAction() == KeyEvent.ACTION_DOWN) {
            getCarNumber(null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initHandlerListener(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    // 识别成功
                    case 1:
                        progressDialog.dismiss();
                        String carNumber = msg.getData().getString("CarNumber");
                        text.setText(carNumber);
                        break;

                    // 识别失败
                    case 2:
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                        break;

                    // 显示照片
                    case 3:

                        break;

                    default:
                        break;
                }
            }
        };
    }
}
