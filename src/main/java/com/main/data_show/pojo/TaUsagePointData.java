package com.main.data_show.pojo;

import java.util.Date;

public class TaUsagePointData {
    private int pointId;
    private long createTimeInt;
    private Date createTime;
    private String dateShow;
    private String hourShow;
    private String pointData;
    private double pointUsage;

    public double getPointUsage() {
        return pointUsage;
    }
    public void setPointUsage(double pointUsage) {
        this.pointUsage = pointUsage;
    }
    public long getCreateTimeInt() {
        return createTimeInt;
    }

    public void setCreateTimeInt(long createTimeInt) {
        this.createTimeInt = createTimeInt;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPointData() {
        return pointData;
    }

    public void setPointData(String pointData) {
        this.pointData = pointData;
    }

    public String getDateShow() {
        return dateShow;
    }

    public void setDateShow(String dateShow) {
        this.dateShow = dateShow;
    }

    public String getHourShow() {
        return hourShow;
    }

    public void setHourShow(String hourShow) {
        this.hourShow = hourShow;
    }
}
