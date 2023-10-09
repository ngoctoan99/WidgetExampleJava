package com.pooldashboard.widget;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class WidgetModel implements Serializable {
    private String nameApp ;
    private Integer point ;
    private Drawable image ;

    public WidgetModel(String nameApp, Integer point, Drawable image) {
        this.nameApp = nameApp;
        this.point = point;
        this.image = image;
    }
    public String getNameApp() {
        return nameApp;
    }

    public Integer getPoint() {
        return point;
    }

    public Drawable getImage() {
        return image;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
