package com.pooldashboard.widget;

import static com.pooldashboard.widget.Constant.EXTRA_PACKAGE_NAME;
import static com.pooldashboard.widget.ListAppRemoteViewFactory.listApp;
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
import android.util.SizeF;
import android.widget.RemoteViews;

import androidx.collection.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;


public class DemoAppWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_POST = "actionPost";
    private static Boolean smallView = true ;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        Log.d("HHH", "onUpdate");
//        if(getDataExample(context) != null){
//            dataList.clear();
//            listApp.clear();
//            dataList.addAll(getDataExample(context).listEarnPointToday);
//            listApp.addAll(getDataExample(context).listApp);
//        }
        for (int appWidgetId : appWidgetIds) {
            updateUIWidget(appWidgetId, context, appWidgetManager);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateUIWidget(Integer appWidgetId , Context context , AppWidgetManager appWidgetManager ) {
        if(getDataExample(context) != null){
            dataList.clear();
            listApp.clear();
            dataList.addAll(getDataExample(context).listEarnPointToday);
            listApp.addAll(getDataExample(context).listApp);
        }
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        // Get min width and height.
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        RemoteViews remoteViews = null;

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
//            // Get the new sizes.
//            ArrayList<SizeF> sizes =
//                    options.getParcelableArrayList(AppWidgetManager.OPTION_APPWIDGET_SIZES);
//            // Check that the list of sizes is provided by the launcher.
//            if (sizes == null || sizes.isEmpty()) {
//                return;
//            }
//            // Map the sizes to the RemoteViews that you want.
//            Map<SizeF, RemoteViews> viewMapping = new ArrayMap<>();
//            for (SizeF size : sizes) {
//                viewMapping.put(size, createRemoteViews(size,context));
//            }
//            remoteViews = new RemoteViews(viewMapping);
//        }else {
//            if(getCellsForSize(minHeight) == 2){
//                remoteViews=   new RemoteViews(context.getPackageName(), R.layout.example_app_widget_min_height);
//            }
//            else {
//                remoteViews=   new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3);
//            }
//        }

        if(getCellsForSize(minHeight) == 2){
            remoteViews=   new RemoteViews(context.getPackageName(), R.layout.example_app_widget_min_height);
        }
        else {
            remoteViews=   new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3);
        }
        Log.d("HHH", minHeight +"minHeight of updateUIWidget");
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
                0, postIntent, PendingIntent.FLAG_UPDATE_CURRENT |PendingIntent.FLAG_MUTABLE);

        remoteViews.setPendingIntentTemplate(R.id.gVListApp, postPendingIntent);

        setViewData(remoteViews, context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void setViewData(RemoteViews views, Context context) {

        views.setTextViewText(R.id.tvEarnPointDay,getDataExample(context).getPointToDay().toString()+"");
        views.setTextViewText(R.id.tvTotalApp,getDataExample(context).getTotalPools().toString()+"");
        views.setTextViewText(R.id.tvTotalPoint,getDataExample(context).getTotalPoint().toString()+"");
        views.setOnClickPendingIntent(R.id.ivPoolDashBoard,createPendingIntentToOpenLink(context,"com.pooldashboard"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent != null){
            String action = intent.getAction();
            if(!TextUtils.isEmpty(action) && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
//                if(getDataExample(context) != null){
//                    dataList.clear();
//                    listApp.clear();
//                    dataList.addAll(getDataExample(context).listEarnPointToday);
//                    listApp.addAll(getDataExample(context).listApp);
//                }
                AppWidgetManager appWidgetManager  =AppWidgetManager.getInstance(context);
                int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(context,DemoAppWidgetProvider.class));
                for (int appWidgetId : appWidgetIds) {
                    updateUIWidget(appWidgetId, context, appWidgetManager);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvInfoPoint);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.gVListApp);
                }
            }
            if(ACTION_POST.equals(intent.getAction())){
                String packageName= intent.getStringExtra(Constant.KEY_CLICK_LIST_APP_WIDGET);
                if(checkAppInstall(packageName,context)){
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent);
                }else {
                    Uri linkUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
                    Intent intentCHPlay = new Intent(Intent.ACTION_VIEW,linkUri);
                    intentCHPlay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentCHPlay);
                }
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        if(getDataExample(context) != null){
            dataList.clear();
            listApp.clear();
            dataList.addAll(getDataExample(context).listEarnPointToday);
            listApp.addAll(getDataExample(context).listApp);
        }
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        // Get min width and height.
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        updateUIWidget(appWidgetId, context, appWidgetManager);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            ++n;
        }
        return n - 1;
    }
    private RemoteViews createRemoteViews(SizeF size, Context context) {
        RemoteViews remoteViews  = null ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            RemoteViews smallView =
                    new RemoteViews(context.getPackageName(), R.layout.example_app_widget_min_height);
            RemoteViews normalView =
                    new RemoteViews(context.getPackageName(), R.layout.example_widget_4_3);


            Map<SizeF, RemoteViews> viewMapping = new ArrayMap<>();
            viewMapping.put(new SizeF(300f, 90f), smallView);
            viewMapping.put(new SizeF(300f, 100f), normalView);
            remoteViews = new RemoteViews(viewMapping);
        }
        return remoteViews ;

    }

     ExampleModel getDataExample(Context context){
        Gson gson = new Gson() ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.NAME_DATA_LOCAL2, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Constant.KEY_EXAMPLE_MODEL_WIDGET, "");
        Type type = new TypeToken<ExampleModel>() {}.getType();

        ExampleModel exampleModel = gson.fromJson(json, type);
        return exampleModel ;
    }
    private PendingIntent createPendingIntentToOpenLink(Context context, String packageName) {
        Intent launchIntent= new Intent(context, ItemAppClickBroadcastReceiver.class);
        launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
        int uniqueRequestCode = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                uniqueRequestCode,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return pendingIntent;
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
