package com.art.huakai.artshow.entity;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/2.
 */
public abstract class SmartMultiEntity implements Serializable {
    private int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
