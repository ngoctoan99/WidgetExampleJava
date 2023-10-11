package com.example.widgetmodulejava;

import static com.pooldashboard.widget.ListAppRemoteViewFactory.listApp;
import static com.pooldashboard.widget.ListEarnPointRemoteViewFactory.dataList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pooldashboard.widget.AppModel;
import com.pooldashboard.widget.Constant;
import com.pooldashboard.widget.DemoAppWidgetProvider;
import com.pooldashboard.widget.EarnPointModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAddWidget ,btnAddToList;
    private EditText edtNameApp ;
    public static List<EarnPointModel> listEarnPoint = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddWidget = findViewById(R.id.btnAddWidget);
        btnAddToList = findViewById(R.id.btnAddToList);
        edtNameApp = findViewById(R.id.edtNameApp);
        initViewListApp();
        int[] widgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, DemoAppWidgetProvider.class));
        if (widgetIds.length > 0) {

        } else {
           pinWidget();
        }
        btnAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinWidget();
            }
        });

        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToList();
            }
        });
    }

    public void pinWidget() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            AppWidgetManager mAppWidgetManager = this.getSystemService(AppWidgetManager.class) ;
            ComponentName myProvider =  new ComponentName( this, DemoAppWidgetProvider.class) ;
            if(mAppWidgetManager.isRequestPinAppWidgetSupported()){
                Intent pinnedWidgetCallbackIntent =  new Intent(this,DemoAppWidgetProvider.class) ;
                PendingIntent successCallback = PendingIntent.getBroadcast(this , 0,pinnedWidgetCallbackIntent,PendingIntent.FLAG_IMMUTABLE);
                mAppWidgetManager.requestPinAppWidget(myProvider,null,successCallback);
            }
        }
    }



    public void addToList(){
        if(edtNameApp.getText().length() >= 0) {
            EarnPointModel element = new EarnPointModel(edtNameApp.getText().toString(), 1000, null);
            listEarnPoint.add(element);
            Gson gson = new Gson();
            String jsonListEarnToDay = gson.toJson(listEarnPoint);

            SharedPreferences sharedPreferences = getSharedPreferences(Constant.NAME_DATA_LOCAL, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constant.KEY_LIST_EARN_TODAY, jsonListEarnToDay);
            editor.apply();

            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.setComponent(new ComponentName(this, DemoAppWidgetProvider.class));
            this.sendBroadcast(intent);
        }
    }

    public void initViewListApp(){
        List<AppModel> listAppInput = new ArrayList<>();
        listAppInput.add(new AppModel("https://i.ibb.co/YtYjjc3/POOLS-Betting.png","App1","com.example"));
        listAppInput.add(new AppModel("https://i.ibb.co/CbzS19j/Frame-1000003687.png","App2","com.example"));
        listAppInput.add(new AppModel("https://i.ibb.co/QMpvsjk/Frame-1000003681.png","App3","com.example"));
        listAppInput.add(new AppModel("https://i.ibb.co/DWjmXjp/Frame-1000003688.png","App4","com.example"));

        listApp.addAll(listAppInput) ;
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(this, DemoAppWidgetProvider.class));
        this.sendBroadcast(intent);
    }
}