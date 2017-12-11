package com.shanbei.shanbeidemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shanbei.shanbeidemo.R;

import java.io.File;

public class MainActivity extends Activity implements OnClickListener{

    private Button mSearchWord;
    private Button mShowPicture;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Andorid/data/com.shanbei.shanbeidemo/Images";

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void init() {
        mSearchWord = (Button) findViewById(R.id.searchWorld);
        mShowPicture = (Button) findViewById(R.id.showPicture);

        mSearchWord.setOnClickListener(this);
        mShowPicture.setOnClickListener(this);

        File file = new File(path);
        if (!file.exists()) {
            this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchWorld:
                Intent searchIntent = new Intent(this,SearchWordActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.showPicture:
                Intent showIntent = new Intent(this,ShowPictureActivity.class);
                startActivity(showIntent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(path);
                        if (!file.exists()) {
                            Log.d("jie", "path1 create:" + file.mkdirs());
                        }
                    }
                    break;
                }
        }

    }


    }
