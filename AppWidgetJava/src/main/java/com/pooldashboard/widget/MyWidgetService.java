package com.pooldashboard.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CustomRemoteViewFactory(getApplicationContext(),intent);
    }
}
