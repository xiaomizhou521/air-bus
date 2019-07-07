package com.main.data_show.pojo;

import java.util.Date;

public class TaPoint {
    private int pointId;
    private String pointName;
    private String remarksName;
    private String pointType;
    private String pointUnit;
    private String blockNo;
    private int state;
    private Date initDate;
    private int initUser;
    private Date modDate;
    private int modUser;

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getRemarksName() {
        return remarksName;
    }

    public void setRemarksName(String remarksName) {
        this.remarksName = remarksName;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public int getInitUser() {
        return initUser;
    }

    public void setInitUser(int initUser) {
        this.initUser = initUser;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public int getModUser() {
        return modUser;
    }

    public void setModUser(int modUser) {
        this.modUser = modUser;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getPointUnit() {
        return pointUnit;
    }

    public void setPointUnit(String pointUnit) {
        this.pointUnit = pointUnit;
    }
}
