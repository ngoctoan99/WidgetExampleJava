package com.example.widgetmodulejava;

import static com.pooldashboard.widget.CustomRemoteViewFactory.dataList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pooldashboard.widget.DemoAppWidgetProvider;
import com.pooldashboard.widget.WidgetModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAddWidget ,btnAddToList;
    private EditText edtNameApp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddWidget = findViewById(R.id.btnAddWidget);
        btnAddToList = findViewById(R.id.btnAddToList);
        edtNameApp = findViewById(R.id.edtNameApp);

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
            WidgetModel element = new WidgetModel(edtNameApp.getText().toString(), 1000, null);
            Log.d("data send :  ", element.toString());
            List<WidgetModel> listWidget = new ArrayList<>();
            listWidget.add(element);
            dataList.addAll(listWidget);
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.setComponent(new ComponentName(this, DemoAppWidgetProvider.class));
            this.sendBroadcast(intent);
        }
    }
}