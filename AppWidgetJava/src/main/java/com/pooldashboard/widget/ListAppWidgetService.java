package com.pooldashboard.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListAppWidgetService  extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListAppRemoteViewFactory(getApplicationContext(),intent);
    }
}
