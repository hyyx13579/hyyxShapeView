package com.example.hyyx.hyyxshape.bean;

/**
 * Created by hyyx on 2016/12/23.
 */

public class NuringInfoShapeBean {

    private String time;
    private String value;
    private String hightValue;
    private String lowValue;


    public NuringInfoShapeBean(String value, String time) {
        this.value = value;
        this.time = time;
    }

    public NuringInfoShapeBean(String time, String hightValue, String lowValue) {
        this.time = time;
        this.hightValue = hightValue;
        this.lowValue = lowValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHightValue() {
        return hightValue;
    }

    public void setHightValue(String hightValue) {
        this.hightValue = hightValue;
    }

    public String getLowValue() {
        return lowValue;
    }

    public void setLowValue(String lowValue) {
        this.lowValue = lowValue;
    }
}
