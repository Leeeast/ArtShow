package com.art.huakai.artshow.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.art.huakai.artshow.activity.ADWebActivity;
import com.art.huakai.artshow.activity.EnrollDetailActivity;
import com.art.huakai.artshow.activity.NewsDetailActivity;
import com.art.huakai.artshow.activity.PersonalDetailMessageActivity;
import com.art.huakai.artshow.activity.TheatreDetailMessageActivity;
import com.art.huakai.artshow.activity.WorksDetailMessageActivity;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.entity.AdvertBean;

/**
 * Created by lidongliang on 2017/11/5.
 */

public class AdvertJumpUtil {
    public static void invoke(BaseActivity baseActivity, Context context, AdvertBean mAdvert) {
        if (mAdvert == null || TextUtils.isEmpty(mAdvert.getTarget())) {
            return;
        }
        if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_THEATER)) {
            String target = mAdvert.getTarget();
            String id = target.split("//")[1];
            Bundle bundle = new Bundle();
            bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, id);
            baseActivity.invokActivity(context, PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PERSONAL);
        } else if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_TALENT)) {
            String target = mAdvert.getTarget();
            String id = target.split("//")[1];
            Bundle bundle = new Bundle();
            bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, id);
            bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, false);
            baseActivity.invokActivity(context, TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
        } else if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_REPERTORY)) {
            String target = mAdvert.getTarget();
            String id = target.split("//")[1];
            Bundle bundle = new Bundle();
            bundle.putString(WorksDetailMessageActivity.PARAMS_ID, id);
            baseActivity.invokActivity(context, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
        } else if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_ENROLL)) {
            String target = mAdvert.getTarget();
            String id = target.split("//")[1];
            Bundle bundle = new Bundle();
            bundle.putSerializable(EnrollDetailActivity.PARAMS_ENROLL_ID, id);
            baseActivity.invokActivity(context, EnrollDetailActivity.class, bundle, JumpCode.FLAG_REQ_ENROLL_DETAIL);
        } else if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_NEWS)) {
            String target = mAdvert.getTarget();
            String newsId = target.split("//")[1];
            Bundle bundle = new Bundle();
            bundle.putSerializable(NewsDetailActivity.PARAMS_NEWS_ID, newsId);
            baseActivity.invokActivity(context, NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);
        } else if (mAdvert.getTarget().startsWith(Constant.AD_TYPE_HTTP)) {
            Bundle bundle = new Bundle();
            bundle.putString(ADWebActivity.PARAMS_URL, mAdvert.getTarget());
            baseActivity.invokActivity(context, ADWebActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_WEBSITE);
        }
    }
}
