package com.pooldashboard.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        if(listApp.get(i).getImage() != null){
            try {
                Bitmap bitmap = Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse(listApp.get(i).getImage()))
                        .submit()
                        .get();
                remoteViews.setImageViewBitmap(R.id.ivApp, bitmap);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Intent fillIntent = new Intent();
            fillIntent.putExtra(Constant.KEY_CLICK_LIST_APP_WIDGET, "SSS");
            remoteViews.setOnClickFillInIntent(R.id.ivApp, fillIntent);
        }else {
            remoteViews.setImageViewResource(R.id.ivApp,R.drawable.baseline_widgets_24);
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
