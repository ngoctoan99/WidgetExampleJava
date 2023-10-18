package com.example.widgetmodulejava;
import androidx.appcompat.app.AppCompatActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.example.widgetmodulejava.databinding.ActivityMainBinding;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.pooldashboard.widget.Constant;
import com.pooldashboard.widget.DemoAppWidgetProvider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String json ;
    private ActivityMainBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


        binding.btnChart.setOnClickListener(view -> {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 2));
            entries.add(new Entry(1, 1));
            entries.add(new Entry(2, 6));
            entries.add(new Entry(3, 9));
            entries.add(new Entry(4, 7));
            changeDataChart(entries);
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

    public void changeDataChart(List<Entry> entryList){
        Gson gson = new Gson();
        String json = gson.toJson(entryList);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.NAME_DATA_LOCAL3, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.KEY_DATA_CHART_WIDGET,json);
        editor.apply();
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(this, DemoAppWidgetProvider.class));
        this.sendBroadcast(intent);
    }






    }
