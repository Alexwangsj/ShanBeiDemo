package com.shanbei.shanbeidemo.utils;

import com.shanbei.shanbeidemo.http.OkHttpUtil;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alexjie on 2017/12/9.
 */

public class RxJavaUtil {

    public static void get(final String url, final Callback callback) {
        Observable.create(new OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                Response response = OkHttpUtil.getInstance().getSync(url);
                subscriber.onNext(response);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(null,new IOException());
                    }

                    @Override
                    public void onNext(Response response) {
                        try {
                            callback.onResponse(null,response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
