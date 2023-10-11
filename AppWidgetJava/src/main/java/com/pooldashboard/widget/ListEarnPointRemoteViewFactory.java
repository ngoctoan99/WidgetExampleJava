package com.pooldashboard.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class ListEarnPointRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    public static ArrayList<EarnPointModel> dataList = new ArrayList<>();

    public Context context  ;
    public Intent intent ;

    public ListEarnPointRemoteViewFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_info_point_app);
        remoteViews.setTextViewText(R.id.tvNameApp, dataList.get(i).getNameApp());
        remoteViews.setTextViewText(R.id.tvPoint, dataList.get(i).getPoint().toString());
//        if(dataList.get(i).getImage() != null){
//            remoteViews.setImageViewBitmap(R.id.ivIconApp,((BitmapDrawable)dataList.get(i).getImage()).getBitmap());
//        }else {
//            remoteViews.setImageViewResource(R.id.ivIconApp,R.drawable.baseline_widgets_24);
//        }
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
