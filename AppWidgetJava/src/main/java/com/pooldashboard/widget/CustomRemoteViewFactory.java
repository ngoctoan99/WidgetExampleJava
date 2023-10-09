package com.pooldashboard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class CustomRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    public static ArrayList<WidgetModel> dataList = new ArrayList<>();

    public Context context  ;
    public Intent intent ;

    public CustomRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }


    @Override
    public void onCreate() {
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("Expert-T",1000,ContextCompat.getDrawable(context, R.drawable.expertt)));
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("Expert-T",1000,ContextCompat.getDrawable(context, R.drawable.expertt)));
//        dataList.add(new WidgetModel("PoolsWallet",1000,ContextCompat.getDrawable(context, R.drawable.poolswallet)));
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("PoolsWallet",1000,ContextCompat.getDrawable(context, R.drawable.poolswallet)));
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("PoolsWallet",1000,ContextCompat.getDrawable(context, R.drawable.poolswallet)));
//        dataList.add(new WidgetModel("Expert-T",1000,ContextCompat.getDrawable(context, R.drawable.expertt)));
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("Expert-T",1000,ContextCompat.getDrawable(context, R.drawable.expertt)));
//        dataList.add(new WidgetModel("PoolsWallet",1000,ContextCompat.getDrawable(context, R.drawable.poolswallet)));
//        dataList.add(new WidgetModel("Friendify",1000 , ContextCompat.getDrawable(context, R.drawable.friendify)));
//        dataList.add(new WidgetModel("PoolsWallet",1000,ContextCompat.getDrawable(context, R.drawable.poolswallet)));
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
//        dataList.clear();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_info_point_app);
        remoteViews.setTextViewText(R.id.tvNameApp, dataList.get(i).getNameApp());
        if(dataList.get(i).getImage() != null){
            remoteViews.setImageViewBitmap(R.id.ivIconApp,((BitmapDrawable)dataList.get(i).getImage()).getBitmap());
        }else {
            remoteViews.setImageViewResource(R.id.ivIconApp,R.drawable.baseline_widgets_24);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return Long.valueOf(i);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
