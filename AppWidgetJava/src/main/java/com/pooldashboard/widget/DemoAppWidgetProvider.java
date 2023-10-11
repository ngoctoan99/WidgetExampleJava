package com.pooldashboard.widget;

import static com.pooldashboard.widget.ListEarnPointRemoteViewFactory.dataList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class DemoAppWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_POST = "actionPost";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if(getDataLocal(context)!= null){
            dataList.clear();
            dataList.addAll(getDataLocal(context)) ;
        }
        for (int appWidgetId : appWidgetIds) {
            updateUIWidget(appWidgetId, context, appWidgetManager, new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3));
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateUIWidget(Integer appWidgetId , Context context , AppWidgetManager appWidgetManager , RemoteViews remoteViews) {
        // list earn point to day
        Intent intentListEarnPoint = new Intent(context , ListEarnPointWidgetService.class);
        intentListEarnPoint.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.lvInfoPoint,intentListEarnPoint);
        // list app widget
        Intent intentListApp = new Intent(context , ListAppWidgetService.class);
        intentListApp.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(R.id.gVListApp,intentListApp);

        Intent postIntent = new Intent(context, DemoAppWidgetProvider.class);
        postIntent.setAction(ACTION_POST);
        PendingIntent postPendingIntent = PendingIntent.getBroadcast(context,
                0, postIntent, PendingIntent.FLAG_IMMUTABLE);


        remoteViews.setPendingIntentTemplate(R.id.gVListApp, postPendingIntent);

        setViewData(remoteViews, context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void setViewData(RemoteViews views, Context context) {

        views.setTextViewText(R.id.tvEarnPointDay,"16000");
        views.setTextViewText(R.id.tvTotalApp,"10");
        views.setTextViewText(R.id.tvTotalPoint,"100000");
        views.setOnClickPendingIntent(R.id.ivPoolDashBoard,createPendingIntentToOpenLink(context,"com.pooldashboard"));
        views.setInt(R.id.rlEarnPoint,"setBackgroundResource",R.drawable.bg_ll_stroke_black_corner8);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent != null){
            String action = intent.getAction();
            if(!TextUtils.isEmpty(action) && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
               if(getDataLocal(context) != null){
                   dataList.clear();
                   dataList.addAll(getDataLocal(context)) ;
               }
                AppWidgetManager appWidgetManager  =AppWidgetManager.getInstance(context);
                int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(context,DemoAppWidgetProvider.class));
                for (int appWidgetId : appWidgetIds) {
                    updateUIWidget(appWidgetId, context, appWidgetManager, new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvInfoPoint);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.gVListApp);
                }
            }
            if(ACTION_POST.equals(intent.getAction())){
                String packageName= intent.getStringExtra(Constant.KEY_CLICK_LIST_APP_WIDGET);
                Log.d("onReceive","clicked" + packageName.toString());
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
    }


    private List<EarnPointModel> getDataLocal(Context context){
        Gson gson = new Gson() ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.NAME_DATA_LOCAL, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Constant.KEY_LIST_EARN_TODAY, "");
        Type type = new TypeToken<List<EarnPointModel>>() {}.getType();
        List<EarnPointModel> listEarnPoint = gson.fromJson(json, type);
        return listEarnPoint ;
    }
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
