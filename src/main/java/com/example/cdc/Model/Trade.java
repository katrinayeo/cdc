package com.example.cdc.Model;

public class Trade {

    private double p;
    private double q;
    private String s;
    private long d;
    private long t;
    private long dataTime;
    private String i;

    public Trade(double p, double q, String s, long d, long t, long dataTime, String i) {
        this.p = p;
        this.q = q;
        this.s = s;
        this.d = d;
        this.t = t;
        this.dataTime = dataTime;
        this.i = i;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public long getD() {
        return d;
    }

    public void setD(long d) {
        this.d = d;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public long getDataTime() {
        return dataTime;
    }

    public void setDataTime(long dataTime) {
        this.dataTime = dataTime;
    }

    public Trade() {
    }
}
