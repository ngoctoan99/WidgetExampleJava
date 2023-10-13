package com.pooldashboard.widget;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class EarnPointModel implements Serializable {
    private String nameApp ;
    private Integer point ;

    public EarnPointModel(String nameApp, Integer point) {
        this.nameApp = nameApp;
        this.point = point;
    }
    public String getNameApp() {
        return nameApp;
    }

    public Integer getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "EarnPointModel{" +
                "nameApp='" + nameApp + '\'' +
                ", point=" + point +
                '}';
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

}
