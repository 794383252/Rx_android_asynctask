package com.example.myapplication.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/8/10.
 */
public class downLoadUtils {

    private OkHttpClient client;

    public downLoadUtils() {
        client = new OkHttpClient();
    }

    /**
     * 声明一个被观察者对象作为结果返回
     * @param path
     * @return
     */
    public Observable<byte[]> downLoadImage(String path) {
        //异步任务在这里编写,本身就是内部类，有继承关系，返回结果很难
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    //访问网络操作
                    Request request = new Request.Builder().url(path).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (response.isSuccessful()) {
                                byte[] data = response.body().bytes();
                                if (data != null) {
                                    subscriber.onNext(data);
                                }
                            }
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }
}
