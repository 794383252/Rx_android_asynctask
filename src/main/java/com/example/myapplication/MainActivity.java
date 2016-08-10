package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.utils.*;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 在实际的开发中，activity充当的角色太多了
 * 1：UI主线程负责绘制UI
 * 2：开启主线程负责获取网络数据
 * 3：赋值到控件中
 * 4：逻辑判断等等
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button button;
    private ImageView imageView;
    private String PATH = "http://picm.photophoto.cn/005/008/007/0080071498.jpg";
    downLoadUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        utils = new downLoadUtils();

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //使用http协议获取数据
        utils.downLoadImage(PATH).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<byte[]>() {
            @Override
            public void onCompleted() {
                //在消息发射完成之后执行，一般使用在对话框消失
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(byte[] bytes) {
                Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * Params 启动任务执行的输入参数，比如HTTP请求的URL
     * Progress 后台任务执行的百分比
     * Result 后台执行任务最终返回的结果，比如String
     */
    public class myTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... params) {
            return new byte[0];
        }
    }

}
