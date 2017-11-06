package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lidonglinag on 2017/11/6.
 */

public class DisabledDatesBean implements Serializable {
    /**
     * theaterId : null
     * date : 1509292800000
     */

    private String theaterId;
    private long date;

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}