package com.windy.distinguish.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;

import com.wintone.plateid.RecogService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * author: wang
 * time: 2015/11/2
 * description:
 *      压缩照片保存到本地,后识别车牌号
 */
public class RecogCarNumberThread extends Thread{
    private Handler hanlder;
    private Context context;
    private String filePath;

    public RecogCarNumberThread(Context context, Handler hanlder, boolean bType) {
        this.context =context;
        this.hanlder = hanlder;
    }

    @Override
    public void run() {
        filePath = "/storage/sdcard0/carnumber.jpg";

        // 压缩图片
        Bitmap bitmap = compressBySize(filePath, 480, 800);

        try {
            saveFile(bitmap, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 识别车牌
        RecogCarNumberUtils recogUtil = new RecogCarNumberUtils(context, hanlder);
        Intent recogIntent = new Intent(context, RecogService.class);
        context.bindService(recogIntent, recogUtil, Service.BIND_AUTO_CREATE);
    }

    // 压缩图片尺寸
    public Bitmap compressBySize(String pathName, int targetWidth,
                                 int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    // 存储进SD卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }

        // 改变照片方向
        int height=bm.getHeight();
        int width=bm.getWidth();
        Matrix matrix=new Matrix();
        matrix.postRotate(0);
        bm= Bitmap.createBitmap(bm, 0, 0,width, height, matrix, true);

        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }
}
