package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.ClassifyTypeBigAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.ClassifyTypeBean;
import com.art.huakai.artshow.eventbus.NameChangeEvent;
import com.art.huakai.artshow.utils.ACache;
import com.art.huakai.artshow.utils.GsonTools;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.ProvinceBean;
import cn.qqtheme.framework.picker.ProvincePicker;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;

public class ClassifyTypeActivity extends BaseActivity {
    public static final String CLASSIFY_TYPE_CONFIRM = "CLASSIFY_TYPE_CONFIRM";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private ShowProgressDialog showProgressDialog;
    private ACache mACache;
    private ArrayList<ClassifyTypeBean> classifyTypeAdded;


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_classify_type;
    }

    @Override
    public void initData() {
        mACache = ACache.get(this);
        classifyTypeAdded = new ArrayList<>();
        showProgressDialog = new ShowProgressDialog(this);
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.user_ability_type);

        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText(R.string.true_choose);
    }

    @Override
    public void setView() {
        getClassifyTypeData();
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }

    @OnClick(R.id.tv_subtitle)
    public void changeAccountName() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(CLASSIFY_TYPE_CONFIRM, classifyTypeAdded);
        setResult(JumpCode.FLAG_RES_CLASSIFY_TYPE_CONFIRM, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(CLASSIFY_TYPE_CONFIRM, classifyTypeAdded);
        setResult(JumpCode.FLAG_RES_CLASSIFY_TYPE_CONFIRM, intent);
        finish();
    }

    /**
     * 获取类型数据
     */
    public void getClassifyTypeData() {
        //读取缓存
        String timeAddressCache = mACache.getAsString(Constant.TIME_CLASSIFY_CACHE);
        if (TextUtils.isEmpty(timeAddressCache)) {
            timeAddressCache = "0";
        }
        long lastTime = Long.parseLong(timeAddressCache);//得到上次保存最新礼物的时间
        long currentTime = System.currentTimeMillis();
        String addressJson = mACache.getAsString(Constant.CLASSIFY_CACHE);
        if (!TextUtils.isEmpty(addressJson) && currentTime - lastTime <= Constant.TIME_CACHE) {//如果缓存是新鲜的
            setClassifyData(addressJson);
            return;
        }

        Map<String, String> params = new TreeMap<>();
        params.put("type", "talent");
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        showProgressDialog.show();
        RequestUtil.request(false, Constant.URL_GET_CLASSFY_LIST, params, 56, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                long currentTime = System.currentTimeMillis();
                mACache.put(Constant.TIME_CLASSIFY_CACHE, String.valueOf(currentTime));
                mACache.put(Constant.CLASSIFY_CACHE, obj);
                setClassifyData(obj);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 填充类型数据
     */
    public void setClassifyData(String classifyJson) {
        try {
            List<ClassifyTypeBean> classifyTypeBeanList = GsonTools.parseDatas(classifyJson, ClassifyTypeBean.class);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            ClassifyTypeBigAdapter classifyTypeBigAdapter = new ClassifyTypeBigAdapter(classifyTypeBeanList, classifyTypeAdded);
            recyclerView.setAdapter(classifyTypeBigAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
