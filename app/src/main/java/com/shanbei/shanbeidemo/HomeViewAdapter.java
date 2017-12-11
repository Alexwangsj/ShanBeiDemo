package com.shanbei.shanbeidemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.shanbei.shanbeidemo.view.NetImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexjie on 2017/12/9.
 */

public class HomeViewAdapter extends Adapter {

    private Context mContext;
    private boolean mIsNetSource = false;
    private List<String> mPictureData;

    public void setData(Context context,boolean isNetSource,List<String> data){
        mContext = context;
        mIsNetSource = isNetSource;
        mPictureData =data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NetImageView netImageView = new NetImageView(mContext);
        netImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        netImageView.setScaleType(ScaleType.FIT_XY);
        return new NetImageViewHolder(netImageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mIsNetSource){
            ((NetImageView)holder.itemView).setNetSrc(mPictureData.get(position),R.mipmap.common_default, (ImageView) holder.itemView);
        }else {
            String filePath = mPictureData.get(position);
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ((NetImageView)holder.itemView).setImageBitmap(bm);
        }
    }

    @Override
    public int getItemCount() {
        if (null != mPictureData){
            return mPictureData.size();
        }
        return 0;
    }

    private class NetImageViewHolder extends ViewHolder{
        public NetImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
