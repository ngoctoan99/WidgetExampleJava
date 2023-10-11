package com.pooldashboard.widget;

import java.io.Serializable;

public class AppModel implements Serializable {
    String image ;
    String nameApp;
    String packageName;

    public AppModel(String image, String nameApp, String packageName) {
        this.image = image;
        this.nameApp = nameApp;
        this.packageName = packageName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
