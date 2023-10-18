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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;


import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class DemoAppWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_POST = "actionPost";

    public static List<Entry> listDataChart = new ArrayList<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateUIWidget(appWidgetId, context, appWidgetManager);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateUIWidget(Integer appWidgetId , Context context , AppWidgetManager appWidgetManager ) {
        if(getDataExample(context) != null){
            dataList.clear();
            dataList.addAll(getDataExample(context).listEarnPointToday);
            listApp.clear();
            listApp.addAll(getDataExample(context).listApp);
        }
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        // Get min width and height.
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        RemoteViews remoteViews = null;


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
        setUpChart(context,remoteViews);
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
                    assert packageName != null;
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    assert launchIntent != null;
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

     ExampleModel getDataExample(Context context){
        Gson gson = new Gson() ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.NAME_DATA_LOCAL2, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Constant.KEY_EXAMPLE_MODEL_WIDGET, "");
        Type type = new TypeToken<ExampleModel>() {}.getType();

         return gson.fromJson(json, type);
    }
    List<Entry> getDataChart(Context context){
        Gson gson = new Gson() ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.NAME_DATA_LOCAL3, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Constant.KEY_DATA_CHART_WIDGET, "");
        Type type = new TypeToken<List<Entry>>() {}.getType();

        return gson.fromJson(json, type);
    }
    private PendingIntent createPendingIntentToOpenLink(Context context, String packageName) {
        Intent launchIntent= new Intent(context, ItemAppClickBroadcastReceiver.class);
        launchIntent.putExtra(EXTRA_PACKAGE_NAME, packageName);
        int uniqueRequestCode = (int) System.currentTimeMillis();

        return PendingIntent.getBroadcast(
                context,
                uniqueRequestCode,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private boolean checkAppInstall(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                pm.getPackageInfo(uri ,PackageManager.PackageInfoFlags.of(0L) );
            }else {
                pm.getPackageInfo(uri,0);
            }
            return true ;

        }catch ( PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return false ;
    }

    private void setUpChart(Context context , RemoteViews remoteViews){
        if(getDataChart(context) != null){
            listDataChart.clear();
            listDataChart.addAll(getDataChart(context));
        }

        if(listDataChart.isEmpty()){
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 4));
            entries.add(new Entry(1, 8));
            entries.add(new Entry(2, 6));
            entries.add(new Entry(3, 2));
            entries.add(new Entry(4, 7));
            entries.add(new Entry(5, 3));
            entries.add(new Entry(6, 8));
            entries.add(new Entry(7, 6));
            entries.add(new Entry(8, 2));
            entries.add(new Entry(9, 7));
            entries.add(new Entry(10, 4));
            entries.add(new Entry(11, 8));
            entries.add(new Entry(12, 6));
            entries.add(new Entry(13, 2));
            entries.add(new Entry(14, 4));
            entries.add(new Entry(15, 8));
            entries.add(new Entry(16, 6));
            entries.add(new Entry(17, 2));
            entries.add(new Entry(18, 7));
            entries.add(new Entry(19, 3));
            entries.add(new Entry(20, 8));
            entries.add(new Entry(21, 6));
            entries.add(new Entry(22, 2));
            entries.add(new Entry(23, 7));
            listDataChart.addAll(entries);
        }
        List<String> listLabelXChart = new ArrayList<>();
        listLabelXChart.add("00:00");
        listLabelXChart.add("01:00");
        listLabelXChart.add("02:00");
        listLabelXChart.add("03:00");
        listLabelXChart.add("04:00");
        listLabelXChart.add("05:00");
        listLabelXChart.add("06:00");
        listLabelXChart.add("07:00");
        listLabelXChart.add("08:00");
        listLabelXChart.add("09:00");
        listLabelXChart.add("10:00");
        listLabelXChart.add("11:00");
        listLabelXChart.add("12:00");
        listLabelXChart.add("13:00");
        listLabelXChart.add("14:00");
        listLabelXChart.add("15:00");
        listLabelXChart.add("16:00");
        listLabelXChart.add("17:00");
        listLabelXChart.add("18:00");
        listLabelXChart.add("19:00");
        listLabelXChart.add("20:00");
        listLabelXChart.add("21:00");
        listLabelXChart.add("22:00");
        listLabelXChart.add("23:00");
        LineChart lineChart = new LineChart(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widths = displayMetrics.widthPixels;
        lineChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(com.pooldashboard.widget.R.dimen.dimen200dp)));
        lineChart.getLegend().setEnabled(false);
        lineChart.setBackgroundColor(Color.BLACK);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        LineDataSet dataSet = new LineDataSet(listDataChart, "");
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);

        dataSet.setColor(Color.MAGENTA);
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        dataSet.setLineWidth(3f);
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(false);

        yAxis.setLabelCount(5,true);
        yAxis.setAxisLineWidth(2f);
        xAxis.setLabelCount(7,true);
        xAxis.setAxisLineWidth(2f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int position = (int) value;
                return listLabelXChart.get(position);
            }
        });
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return "$ " + (int)value + ".00";
            }
        });
        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_color);
            dataSet.setFillDrawable(drawable);
        }
        else {
            dataSet.setFillColor(Color.BLACK);
        }
        lineChart.measure(widths,(int)context.getResources().getDimension(com.pooldashboard.widget.R.dimen.dimen200dp));
        lineChart.layout(0,0,widths,(int)context.getResources().getDimension(com.pooldashboard.widget.R.dimen.dimen200dp));

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        int width = lineChart.getWidth();
        int height = lineChart.getHeight();
        Log.d("toan","Size : " + width +" /" +height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        lineChart.draw(canvas);
        remoteViews.setImageViewBitmap(R.id.ivChart,bitmap);
    }
}
