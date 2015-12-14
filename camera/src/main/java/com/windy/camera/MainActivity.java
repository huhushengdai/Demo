package com.windy.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private View layout;
    private Camera camera;
    private Camera.Parameters parameters = null;

    Bundle bundle = null; // 声明一个Bundle对象，用来存储数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = this.findViewById(R.id.buttonLayout);

        SurfaceView surfaceView = (SurfaceView) this
                .findViewById(R.id.surfaceView);
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(1280, 760); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
    }

    /**
     * 按钮被点击触发的事件
     *
     * @param v
     */
    public void btnOnclick(View v) {
        if (camera != null) {
            switch (v.getId()) {
                case R.id.takepicture:
                    // 拍照
                    camera.takePicture(null, null, new MyPictureCallback());
                    break;
            }
        }
    }

    /**
     * 图片被点击触发的时间
     *
     * @param v
     */
    public void imageClick(View v) {
        if (v.getId() == R.id.scalePic) {

            if (bundle == null) {
                Toast.makeText(getApplicationContext(), "拍照",
                        Toast.LENGTH_SHORT).show();
            } else {
//                Intent intent = new Intent(this, ShowPicActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);

//                Toast.makeText(getApplicationContext(), "跳转",
//                        Toast.LENGTH_SHORT).show();
            }
        }
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    Toast.makeText(MainActivity.this,"聚焦",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final class MyPictureCallback implements PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                bundle = new Bundle();
                bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
                saveToSDCard(data); // 保存图片到sd卡中
                Toast.makeText(getApplicationContext(), "成功",
                        Toast.LENGTH_SHORT).show();
                camera.startPreview(); // 拍完照后，重新开始预览

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     * @param data
     * @throws IOException
     */
    public static void saveToSDCard(byte[] data) throws IOException {
//        Bitmap bitmap = Bitmap.createBitmap(data,);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory()
                + "/finger/");
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流
    }


    private final class SurfaceCallback implements Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            parameters = camera.getParameters(); // 获取各项参数
            List<Camera.Size> list = parameters.getSupportedPictureSizes();
            Camera.Size size = list.get(0);
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
////            1280x720
            Camera.Size preSize =  parameters.getSupportedPreviewSizes().get(0);
            parameters.setPreviewSize(preSize.width, preSize.height); // 设置预览大小
//            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(size.width,size.height); // 设置保存的图片尺寸
            parameters.setRotation(90);
//            parameters.setJpegQuality(80); // 设置照片质量
            camera.setParameters(parameters);
            //------------------------------
//            Log.e(TAG,parameters.flatten());
            print(parameters.flatten());
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
//                camera.getParameters().setPictureSize(1920, 1080); // 设置保存的图片尺寸
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(MainActivity.this));
                camera.startPreview(); // 开始预览
//                camera.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        if (success){
//                            Toast.makeText(MainActivity.this,"聚焦",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    /**
     * 点击手机屏幕是，显示两个按钮
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                layout.setVisibility(ViewGroup.VISIBLE); // 设置视图可见
                break;
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA: // 按下拍照按钮
                if (camera != null && event.getRepeatCount() == 0) {
                    // 拍照
                    //注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
                    //，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
                    camera.takePicture(null, null, new MyPictureCallback());
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
//                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 0;
//                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 270;
//                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 180;
//                degree = 270;
                break;
        }
        return degree;
    }

    public void print(String text){
        String[] info = text.split(";");
//        String[] print = null;
        for (String str: info ) {
//            print = str.split("=");
            Log.e(TAG,str);
        }
    }
    /*
           * 旋转图片
           * @param angle
           * @param bitmap
           * @return Bitmap
           */
//    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
//        //旋转图片 动作
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        System.out.println("angle2=" + angle);
//        // 创建新的图片
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        return resizedBitmap;
//    }
}
