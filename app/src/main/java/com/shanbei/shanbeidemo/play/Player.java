package com.shanbei.shanbeidemo.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import com.shanbei.shanbeidemo.http.OkHttpUtil;

import java.io.IOException;

/**
 * Created by alexjie on 2017/12/7.
 */

public class Player implements OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private static Player mInstance;
    public static Player getInstance() {
        if (null == mInstance) {
            synchronized (OkHttpUtil.class) {
                if (null == mInstance) {
                    mInstance = new Player();
                }
            }
        }
        return mInstance;
    }

    private Player(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mediaPlayer.setOnPreparedListener(this);
    }

    public void play(String url){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
