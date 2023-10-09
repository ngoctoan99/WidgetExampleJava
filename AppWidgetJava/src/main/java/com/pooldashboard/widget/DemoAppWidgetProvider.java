package com.pooldashboard.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;




public class DemoAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//get data  local = json => update list
//                dataList=list local
        for(int i = 0 ; i < appWidgetIds.length ; i++){
            updateUIWidget(appWidgetIds[i],context,appWidgetManager,new RemoteViews(context.getPackageName(),R.layout.example_widget_4_3));
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateUIWidget(Integer appWidgetId , Context context , AppWidgetManager appWidgetManager , RemoteViews remoteViews) {
        Intent intent = new Intent(context , MyWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.lvInfoPoint,intent);
        setViewData(remoteViews, context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void setViewData(RemoteViews views, Context context) {
        views.setImageViewResource(R.id.ivApp1,R.drawable.friendify);
        views.setImageViewResource(R.id.ivApp2,R.drawable.poolswallet);
        views.setImageViewResource(R.id.ivApp3,R.drawable.expertt);
        views.setImageViewResource(R.id.ivApp4,R.drawable.vote);
        views.setImageViewResource(R.id.ivApp5,R.drawable.vote);
        views.setImageViewResource(R.id.ivApp6,R.drawable.friendify);
        views.setImageViewResource(R.id.ivApp7,R.drawable.poolswallet);
        views.setImageViewResource(R.id.ivApp8,R.drawable.expertt);
        views.setImageViewResource(R.id.ivApp9,R.drawable.vote);
        views.setImageViewResource(R.id.ivApp10,R.drawable.vote);
        views.setTextViewText(R.id.tvEarnPointDay,"16000");
        views.setTextViewText(R.id.tvNumberItemApp1,"6");
        views.setTextViewText(R.id.tvNumberItemApp2,"6");
        views.setTextViewText(R.id.tvNumberItemApp3,"4");
        views.setTextViewText(R.id.tvNumberItemApp4,"0");
        views.setTextViewText(R.id.tvTotalApp,"10");
        views.setTextViewText(R.id.tvTotalPoint,"100000");
        views.setOnClickPendingIntent(R.id.ivApp1,createPendingIntentToOpenLink(context,"friendify.playground"));
        views.setOnClickPendingIntent(R.id.ivApp2,createPendingIntentToOpenLink(context,"com.wallet.pools"));
        views.setOnClickPendingIntent(R.id.ivApp3,createPendingIntentToOpenLink(context,"com.expertt.mobile"));
        views.setOnClickPendingIntent(R.id.ivApp4,createPendingIntentToOpenLink(context,"finance.winery.votebattle"));
        views.setOnClickPendingIntent(R.id.ivApp5,createPendingIntentToOpenLink(context,"finance.winery.votebattle"));
        views.setOnClickPendingIntent(R.id.ivPoolDashBoard,createPendingIntentToOpenLink(context,"com.pooldashboard"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent != null){
            String action = intent.getAction();
            if(!TextUtils.isEmpty(action) && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
//get data  local = json => update list
//                dataList=list local
                AppWidgetManager appWidgetManager  =AppWidgetManager.getInstance(context);
                int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(context,DemoAppWidgetProvider.class));
                for (int appWidgetId : appWidgetIds) {
                    updateUIWidget(appWidgetId, context, appWidgetManager, new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvInfoPoint);
                }
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
//        Integer minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
//        Integer minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
//        updateUIWidget(appWidgetId,context,appWidgetManager,getRemoteViews(context,minWidth,minHeight));
    }

//    private RemoteViews getRemoteViews(Context context, Integer minWidth, Integer minHeight) {
//        if(minHeight >= 120){
//            return new RemoteViews(
//                    context.getPackageName(),
//                    R.layout.example_widget_4_3
//            );
//        }else {
//            return new RemoteViews(
//                    context.getPackageName(),
//                    R.layout.example_widget_4_1
//            );
//        }
//    }

    private PendingIntent createPendingIntentToOpenLink(Context context, String packageName) {
        if(checkAppInstall(packageName,context)){
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            return PendingIntent.getActivity(context,0,launchIntent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            Uri linkUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW,linkUri);
            return PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private boolean checkAppInstall(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                pm.getPackageInfo(uri ,PackageManager.PackageInfoFlags.of(Long.valueOf(0)) );
            }else {
                pm.getPackageInfo(uri,0);
            }
            return true ;

        }catch ( PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return false ;
    }

}
