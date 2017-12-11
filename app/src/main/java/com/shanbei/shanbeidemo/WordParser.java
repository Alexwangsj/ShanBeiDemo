package com.shanbei.shanbeidemo;

import android.text.TextUtils;

import com.shanbei.shanbeidemo.utils.Define.WordTranslateInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexjie on 2017/12/6.
 */

public class WordParser {

    public static final int SUCCESS = 0;

    public static WordTranslateInfo parse(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                WordTranslateInfo info = new WordTranslateInfo();
                JSONObject json = new JSONObject(data);
                if (SUCCESS == json.optInt("status_code")) {
                    JSONObject dataJson = json.optJSONObject("data");
                    if (null != dataJson && dataJson.length() > 0) {
                        info.content = dataJson.optString("content");
                        info.definition = dataJson.optString("definition");
                        info.soundMark = dataJson.optString("pronunciation");
                        info.audio = dataJson.optString("uk_audio");
                        return info;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;

    }


}
