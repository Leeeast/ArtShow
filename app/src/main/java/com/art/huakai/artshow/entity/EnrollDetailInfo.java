package com.art.huakai.artshow.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lidongliang on 2017/10/18.
 */

public class EnrollDetailInfo implements Serializable {
    public List<RepertorysBean> enrolledAll;
    public List<RepertorysBean> enrolledAdopt;
    public EnrollDetail enroll;
}
