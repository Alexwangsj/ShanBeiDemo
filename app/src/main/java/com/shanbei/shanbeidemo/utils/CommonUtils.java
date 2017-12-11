package com.shanbei.shanbeidemo.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.shanbei.shanbeidemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexjie on 2017/12/9.
 */

public class CommonUtils {

    public static boolean isPictureExist(String path) {
        File dirFile = new File(path);
        if (dirFile.exists() && dirFile.isDirectory() && dirFile.list().length > 0) {
            return true;
        }
        return false;
    }

    public static List<String> getPicture(String path) {
        File dirFile = new File(path);
        if (dirFile.exists() && dirFile.isDirectory()) {
            List<String> pictures = new ArrayList<>();
            File files[] = dirFile.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".bmp")
                            || fileName.endsWith(".gif")
                            || fileName.endsWith(".png")) {
                        if (path.endsWith(File.separator)) {
                            pictures.add(path + file.getName());
                        } else {
                            pictures.add(path + File.separator + file.getName());
                        }
                    }
                }
            }
            return pictures;
        }
        return null;
    }

    public static List<String> gerPictureUrl(Context context) {
        List<String> pictures = new ArrayList<>();
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url0));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url1));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url2));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url3));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url4));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url5));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url6));
        pictures.add(context.getResources().getString(R.string.shan_bei_net_pic_url7));
        return pictures;
    }

    public static void savePicture(InputStream is) {

        FileOutputStream fos = null;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Andorid/data/com.shanbei.shanbeidemo/Images/";

        try {
            String name = path + System.currentTimeMillis() + ".jpg";
            File file = new File(name);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void getEachWord(Context context, TextView textview, Callback cb) {
        Spannable spans = (Spannable) textview.getText();
        Integer[] indices = getIndices(textview.getText().toString().trim(), ' ');
        int start = 0;
        int end = 0;
        for (int i = 0; i <= indices.length; i++) {
            ClickableSpan clickspan = getClickableSpan(cb);
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickspan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
        textview.setHighlightColor(context.getResources().getColor(R.color.wathet));
    }

    private static ClickableSpan getClickableSpan(final Callback cb) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String word = tv.getText().subSequence(tv.getSelectionStart(), tv.getSelectionEnd()).toString();
                cb.onClick(word);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    public static Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }

    public interface Callback{
        void onClick(String text);
    }


}
