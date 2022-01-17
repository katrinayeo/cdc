package com.example.cdc.Model;

public class CheckCandle {

    private double o;
    private double h;
    private double l;
    private double c;
    private int count = 0;

    public CheckCandle(double o, double h, double l, double c, int count) {
        this.o = o;
        this.h = h;
        this.l = l;
        this.c = c;
        this.count = count;
    }

    public CheckCandle() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
}
