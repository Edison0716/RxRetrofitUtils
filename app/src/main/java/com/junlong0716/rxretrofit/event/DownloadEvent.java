package com.junlong0716.rxretrofit.event;

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/9/20.
 */

public class DownloadEvent {
    private String what;
    private int percent;

    public DownloadEvent(String what, int percent) {
        this.what = what;
        this.percent = percent;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
