package com.shanbei.shanbeidemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shanbei.shanbeidemo.utils.RxJavaUtil;
import com.shanbei.shanbeidemo.utils.CommonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by alexjie on 2017/12/9.
 */

public class NetImageView extends android.support.v7.widget.AppCompatImageView {

    public NetImageView(Context context) {
        super(context);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNetSrc(String url, final int fail, final ImageView view) {
        RxJavaUtil.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.setImageResource(fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response && response.isSuccessful()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = response.body().byteStream().read(buffer)) > -1 ) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    InputStream showStream = new ByteArrayInputStream(baos.toByteArray());
                    Bitmap bitmap = BitmapFactory.decodeStream(showStream);
                    response.body().close();
                    view.setImageBitmap(bitmap);

                    InputStream localStream = new ByteArrayInputStream(baos.toByteArray());
                    CommonUtils.savePicture(localStream);
                } else {
                    view.setImageResource(fail);
                }
            }
        });
    }
}
