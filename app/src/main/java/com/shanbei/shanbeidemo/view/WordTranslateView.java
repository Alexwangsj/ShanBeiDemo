package com.shanbei.shanbeidemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanbei.shanbeidemo.R;
import com.shanbei.shanbeidemo.utils.Define.WordTranslateInfo;

/**
 * Created by alexjie on 2017/12/6.
 */

public class WordTranslateView extends RelativeLayout {

    private TextView mWordTv;
    private TextView mSoundMarkTv;
    private TextView mContentTv;
    private ImageView mTrumpetImgv;


    public WordTranslateView(Context context) {
        super(context);
        initView(context);
    }

    public WordTranslateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WordTranslateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_word_translate, this, true);
        mWordTv = (TextView) findViewById(R.id.wordTv);
        mSoundMarkTv = (TextView) findViewById(R.id.soundMarkTv);
        mContentTv = (TextView) findViewById(R.id.contentTv);
        mTrumpetImgv = (ImageView) findViewById(R.id.trumpetImgv);
    }

    public void setData(WordTranslateInfo info) {
        if (null != info) {
            mWordTv.setText(info.content);
            mSoundMarkTv.setText("英 [" + info.soundMark+"]");
            mContentTv.setText(info.definition);
        }
    }

    public void setTrumpetClickListener(OnClickListener listener){
        if (null != listener){
            mTrumpetImgv.setOnClickListener(listener);
        }
    }
}
