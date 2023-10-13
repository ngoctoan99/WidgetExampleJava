package com.pooldashboard.widget;

import java.io.Serializable;
import java.util.List;

public class ExampleModel implements Serializable {
    Integer totalPools ;
    Integer totalPoint;
    Integer pointToday ;
    List<AppModel> listApp ;
    List<EarnPointModel> listEarnPointToday ;

    @Override
    public String toString() {
        return "ExampleModel{" +
                "totalPools=" + totalPools +
                ", totalPoint=" + totalPoint +
                ", pointToDay=" + pointToday +
                ", listApp=" + listApp +
                ", listEarnPointToday=" + listEarnPointToday +
                '}';
    }

    public ExampleModel(Integer totalPools, Integer totalPoints, Integer pointToDay, List<AppModel> listApp, List<EarnPointModel> listEarnPointToday) {
        this.totalPools = totalPools;
        this.totalPoint = totalPoints;
        this.pointToday = pointToDay;
        this.listEarnPointToday = listEarnPointToday;
        this.listApp = listApp;
    }

    public Integer getTotalPools() {
        return totalPools;
    }

    public void setTotalPools(Integer totalPools) {
        this.totalPools = totalPools;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getPointToDay() {
        return pointToday;
    }

    public void setPointToDay(Integer pointToDay) {
        this.pointToday = pointToDay;
    }

    public List<EarnPointModel> getListEarnPointToday() {
        return listEarnPointToday;
    }

    public void setListEarnPointToday(List<EarnPointModel> listEarnPointToday) {
        this.listEarnPointToday = listEarnPointToday;
    }

    public List<AppModel> getListAppModel() {
        return listApp;
    }

    public void setListAppModel(List<AppModel> listApp) {
        this.listApp = listApp;
    }
}
