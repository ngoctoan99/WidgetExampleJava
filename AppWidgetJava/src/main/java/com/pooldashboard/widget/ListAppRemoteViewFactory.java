package com.pooldashboard.widget;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListAppRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{
    public static ArrayList<AppModel> listApp = new ArrayList<>();
    public Context context  ;
    public Intent intent ;

    public ListAppRemoteViewFactory(Context context, Intent intent) {
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
        listApp.clear();
    }

    @Override
    public int getCount() {
        return listApp.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_app_widget);
//        displayImageApp(listApp.get(i).getNameApp(),remoteViews);
        if(listApp.get(i).getImage() != null){
            try {
                Bitmap bitmap = Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse(listApp.get(i).getImage()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit()
                        .get();
                remoteViews.setImageViewBitmap(R.id.ivApp, bitmap);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }else {
            remoteViews.setImageViewResource(R.id.ivApp,R.drawable.baseline_widgets_24);
        }
        Intent fillIntent = new Intent();
        fillIntent.putExtra(Constant.KEY_CLICK_LIST_APP_WIDGET, listApp.get(i).getPackageName());
        remoteViews.setOnClickFillInIntent(R.id.ivApp, fillIntent);
        return remoteViews;
    }

    private void displayImageApp(String name , RemoteViews remoteViews) {
        Log.d("displayImageApp:" , name);
        switch (name){
            case "app1":
                remoteViews.setImageViewResource(R.id.ivApp,R.drawable.icon_app_1);
                break;
            case "app2":
                remoteViews.setImageViewResource(R.id.ivApp,R.drawable.icon_app_2);
                break;
            case "app3":
                remoteViews.setImageViewResource(R.id.ivApp,R.drawable.icon_app_3);
                break;
            case "app4":
                remoteViews.setImageViewResource(R.id.ivApp,R.drawable.icon_app_4);
                break;
            default:
                remoteViews.setImageViewResource(R.id.ivApp,R.drawable.baseline_widgets_24);
                break;
        }
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
        return (long) i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
