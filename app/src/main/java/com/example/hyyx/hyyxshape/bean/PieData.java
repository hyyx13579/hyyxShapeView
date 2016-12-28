package com.example.hyyx.hyyxshape.bean;

/**
 * Created by hyyx on 2016/12/13.
 */

public class PieData {


    public PieData(float value, int color) {
        this.value = value;
        this.color = color;
    }

    private float value;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
