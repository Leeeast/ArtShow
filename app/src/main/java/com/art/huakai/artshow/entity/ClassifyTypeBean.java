package com.art.huakai.artshow.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lidongliang on 2017/10/22.
 */

public class ClassifyTypeBean implements Parcelable {
    private int id;
    private String name;
    private List<ClassifyTypeBean> children;

    protected ClassifyTypeBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        children = in.createTypedArrayList(ClassifyTypeBean.CREATOR);
    }

    public static final Creator<ClassifyTypeBean> CREATOR = new Creator<ClassifyTypeBean>() {
        @Override
        public ClassifyTypeBean createFromParcel(Parcel in) {
            return new ClassifyTypeBean(in);
        }

        @Override
        public ClassifyTypeBean[] newArray(int size) {
            return new ClassifyTypeBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ClassifyTypeBean> getChildren() {
        return children;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(children);
    }
}
