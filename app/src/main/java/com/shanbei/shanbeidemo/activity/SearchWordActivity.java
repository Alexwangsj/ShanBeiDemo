package com.shanbei.shanbeidemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.shanbei.shanbeidemo.WordParser;
import com.shanbei.shanbeidemo.utils.CommonUtils;
import com.shanbei.shanbeidemo.utils.Define;
import com.shanbei.shanbeidemo.R;
import com.shanbei.shanbeidemo.utils.Define.WordTranslateInfo;
import com.shanbei.shanbeidemo.http.OkHttpUtil;
import com.shanbei.shanbeidemo.play.Player;
import com.shanbei.shanbeidemo.utils.TextJustificationHelper;
import com.shanbei.shanbeidemo.view.WordTranslateView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by alexjie on 2017/12/6.
 */

public class SearchWordActivity extends Activity {

    public static final int SUCCESS = 200;

    private TextView mActicleTv;
    private ScrollView mActicleSv;
    private WordTranslateView mWordTranslateView;

    private Handler mHandler;
    private WordTranslateInfo mTranslateInfo;

    private AlphaAnimation mHideAnimation;
    private AlphaAnimation mShowAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);

        init();
    }

    private void init() {

        mActicleSv = (ScrollView) findViewById(R.id.acticleSv);
        mWordTranslateView = (WordTranslateView) findViewById(R.id.wordTranslateView);
        mActicleTv = new TextView(this);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width = dm.widthPixels;
        mActicleTv.setText(Define.article, BufferType.SPANNABLE);
        mActicleTv.setLineSpacing(9, 1.0f);
        mActicleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15 * (float) width / 320f);
        TextJustificationHelper.justify(mActicleTv, width);

        mActicleTv.setScroller(new Scroller(this));
        LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        infoLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mActicleSv.addView(mActicleTv, infoLayoutParams);

        CommonUtils.getEachWord(this, mActicleTv, new CommonUtils.Callback() {
            @Override
            public void onClick(String text) {
                requestWordInfo(text);
            }
        });
        mActicleTv.setMovementMethod(LinkMovementMethod.getInstance());

        mWordTranslateView.setTrumpetClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mTranslateInfo && !TextUtils.isEmpty(mTranslateInfo.audio)) {
                    Player.getInstance().play(mTranslateInfo.audio);
                }
            }
        });


        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                if (null != msg && SUCCESS == msg.arg1) {
                    mWordTranslateView.setData((WordTranslateInfo) msg.obj);
                    if (View.INVISIBLE == mWordTranslateView.getVisibility()) {
                        setShowAnimation(mWordTranslateView, 200);
                    }
                } else {
                    if (View.VISIBLE == mWordTranslateView.getVisibility()) {
                        setHideAnimation(mWordTranslateView, 200);
                    }
                }
            }
        };
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                mWordTranslateView.getVisibility() == View.VISIBLE) {
            setHideAnimation(mWordTranslateView, 200);
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 请求单词信息
     * @param word
     */
    private void requestWordInfo(String word) {
        if (TextUtils.isEmpty(word)) {
            return;
        }
        String url = getResources().getString(R.string.shan_bei_url) + word;
        OkHttpUtil.getInstance().getAsync(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendMessage(new Message());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response && response.isSuccessful()) {
                    String result = response.body().string();
                    mTranslateInfo = WordParser.parse(result);
                    if (null != mTranslateInfo) {
                        Message msg = new Message();
                        msg.arg1 = SUCCESS;
                        msg.obj = mTranslateInfo;
                        mHandler.sendMessage(msg);
                    } else {
                        mHandler.sendMessage(new Message());
                    }
                } else {
                    mHandler.sendMessage(new Message());
                }
            }
        });
    }

    /**
     * View渐隐动画效果
     */
    public void setHideAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public void setShowAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(mShowAnimation);
    }
}
