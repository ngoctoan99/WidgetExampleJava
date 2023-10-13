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
import com.pooldashboard.widget.ExampleModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAddWidget ,btnAddToList;
    private EditText edtNameApp ;
    public static List<EarnPointModel> listEarnPoint = new ArrayList<>();
    public static List<AppModel> listAppInput = new ArrayList<>();
    ExampleModel exampleModel ;
    String json ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddWidget = findViewById(R.id.btnAddWidget);
        btnAddToList = findViewById(R.id.btnAddToList);
        edtNameApp = findViewById(R.id.edtNameApp);

         json ="{\n" +
                 "  \"totalPools\": 1000,\n" +
                 "  \"totalPoint\": 1000,\n" +
                 "  \"pointToday\": 1000,\n" +
                 "  \"listApp\": [\n" +
                 "    {\n" +
                 "      \"image\": \"https://media.threatpost.com/wp-content/uploads/sites/103/2019/09/26105755/fish-1.jpg\",\n" +
                 "      \"nameApp\": \"app1\",\n" +
                 "      \"packageName\": \"com.example.App\"\n" +
                 "    },\n" +
                 "    {\n" +
                 "      \"image\": \"https://media.threatpost.com/wp-content/uploads/sites/103/2019/09/26105755/fish-1.jpg\",\n" +
                 "      \"nameApp\": \"app2\",\n" +
                 "      \"packageName\": \"com.example.App\"\n" +
                 "    },\n" +
                 "    {\n" +
                 "      \"image\": \"https://media.threatpost.com/wp-content/uploads/sites/103/2019/09/26105755/fish-1.jpg\",\n" +
                 "      \"nameApp\": \"app3\",\n" +
                 "      \"packageName\": \"com.example.App\"\n" +
                 "    },\n" +
                 "    {\n" +
                 "      \"image\": \"https://media.threatpost.com/wp-content/uploads/sites/103/2019/09/26105755/fish-1.jpg\",\n" +
                 "      \"nameApp\": \"app4\",\n" +
                 "      \"packageName\": \"com.example.App\"\n" +
                 "    }\n" +
                 "  ],\n" +
                 "  \"listEarnPointToday\": [\n" +
                 "    {\n" +
                 "      \"nameApp\": \"App1\",\n" +
                 "      \"point\": 1000\n" +
                 "    },\n" +
                 "    {\n" +
                 "      \"nameApp\": \"App1\",\n" +
                 "      \"point\": 1000\n" +
                 "    },\n" +
                 "    {\n" +
                 "      \"nameApp\": \"App1\",\n" +
                 "      \"point\": 1000\n" +
                 "    }\n" +
                 "  ]\n" +
                 "}";

        int[] widgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, DemoAppWidgetProvider.class));
        if (widgetIds.length > 0) {

        } else {
           pinWidget(json);
        }

        btnAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinWidget(json);
            }
        });

    }

    public void pinWidget(String json) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            AppWidgetManager mAppWidgetManager = this.getSystemService(AppWidgetManager.class) ;
            ComponentName myProvider =  new ComponentName( this, DemoAppWidgetProvider.class) ;
            if(mAppWidgetManager.isRequestPinAppWidgetSupported()){
                Intent pinnedWidgetCallbackIntent =  new Intent(this,DemoAppWidgetProvider.class) ;
                PendingIntent successCallback = PendingIntent.getBroadcast(this , 0,pinnedWidgetCallbackIntent,PendingIntent.FLAG_IMMUTABLE);
                mAppWidgetManager.requestPinAppWidget(myProvider,null,successCallback);
                addDataWidget(json);
            }
        }
    }

    public void addDataWidget(String json) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.NAME_DATA_LOCAL2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.KEY_EXAMPLE_MODEL_WIDGET,json);
        editor.apply();

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(this, DemoAppWidgetProvider.class));
        this.sendBroadcast(intent);
    }
}