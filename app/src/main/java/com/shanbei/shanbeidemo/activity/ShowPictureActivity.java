package com.shanbei.shanbeidemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import com.shanbei.shanbeidemo.HomeViewAdapter;
import com.shanbei.shanbeidemo.R;
import com.shanbei.shanbeidemo.utils.CommonUtils;
import com.shanbei.shanbeidemo.utils.PagingScrollHelper;

/**
 * Created by alexjie on 2017/12/6.
 */

public class ShowPictureActivity extends Activity {

    private final String mPicturePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Andorid/data/com.shanbei.shanbeidemo/Images";

    private RecyclerView mHomeView;
    private HomeViewAdapter mHomeViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        init();
    }

    private void init() {
        mHomeView = (RecyclerView) findViewById(R.id.homeview);
        LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mHomeView.setLayoutManager(manager);

        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        scrollHelper.setUpRecycleView(mHomeView);

        mHomeViewAdapter = new HomeViewAdapter();
        if (CommonUtils.isPictureExist(mPicturePath)){
            mHomeViewAdapter.setData(this,false, CommonUtils.getPicture(mPicturePath));
        }else {
            mHomeViewAdapter.setData(this,true, CommonUtils.gerPictureUrl(this));
        }
        mHomeView.setAdapter(mHomeViewAdapter);
    }
}
