package com.example.cdc.Model;

public class Candlestick {

    private long t;
    private double o;
    private double h;
    private double l;
    private double c;
    private double v;

    public Candlestick(long t, double o, double h, double l, double c, double v) {
        this.t = t;
        this.o = o;
        this.h = h;
        this.l = l;
        this.c = c;
        this.v = v;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
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

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public Candlestick() {
    }
}
