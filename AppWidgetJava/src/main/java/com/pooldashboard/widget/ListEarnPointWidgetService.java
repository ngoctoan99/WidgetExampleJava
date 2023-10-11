package com.pooldashboard.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListEarnPointWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListEarnPointRemoteViewFactory(getApplicationContext(),intent);
    }
}
