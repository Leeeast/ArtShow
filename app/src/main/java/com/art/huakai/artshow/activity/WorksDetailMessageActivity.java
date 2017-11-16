package com.art.huakai.artshow.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.StaffIntroduceAdapter;
import com.art.huakai.artshow.adapter.TalentDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.dialog.PageLoadingDialog;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.dialog.StaffIntroduceDialog;
import com.art.huakai.artshow.dialog.TakePhoneDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.Staff;
import com.art.huakai.artshow.entity.WorksDetailBean;
import com.art.huakai.artshow.eventbus.ProjectNotifyEvent;
import com.art.huakai.artshow.fragment.ProjectDetailPoltFragment;
import com.art.huakai.artshow.fragment.ProjectDetailRequireFragment;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.listener.PageLoadingListener;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.DateUtil;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.art.huakai.artshow.widget.headerviewpager.HeaderViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class WorksDetailMessageActivity extends BaseActivity implements View.OnClickListener, PageLoadingListener {
    public static final String PARAMS_ID = "PARAMS_ID";
    public static final String PARAMS_ORG = "PARAMS_ORG";
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.sdv)
    ChinaShowImageView sdv;
    @BindView(R.id.tv_theatre_name)
    TextView tvTheatreName;
    @BindView(R.id.tv_theatre_kind)
    TextView tvTheatreKind;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_producers)
    TextView tvProducers;
    @BindView(R.id.tv_actor_number)
    TextView tvActorNumber;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_thratre_size)
    TextView tvThratreSize;
    @BindView(R.id.tv_show_time)
    TextView tvShowTime;
    @BindView(R.id.tv_first_show_time)
    TextView tvFirstShowTime;
    @BindView(R.id.tv_already_show_times)
    TextView tvAlreadyShowTimes;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.stl_dis_tab)
    SlidingTabLayout stlDisTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.ll_make_telephone)
    LinearLayout llMakeTelephone;
    @BindView(R.id.tv_show_useful_time)
    TextView tvShowUsefulTime;
    @BindView(R.id.tv_actor_detail)
    TextView tvActorDetail;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private String mProjectId;
    private WorksDetailBean worksDetailBean;
    private boolean mIsFromOrgan;
    private String URL_TALENT_DETAL;


    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                setData();
            }
        }
    };
    private ShareDialog shareDialog;
    private TakePhoneDialog takePhoneDialog;
    private WbShareHandler mShareHandler;
    private RequestCall requestCall;
    private List<Staff> mStaffs;
    private PageLoadingDialog pageLoadingDialog;


    private void setData() {

        mTabArray = getResources().getStringArray(R.array.work_detail_tab);
        mFragments = new ArrayList<>();
        ProjectDetailPoltFragment worksDetailShowFragment = ProjectDetailPoltFragment.newInstance(worksDetailBean);
        mFragments.add(worksDetailShowFragment);
        StaggerFragment staggerFragment = StaggerFragment.newInstance(worksDetailBean.getPictures());
        mFragments.add(staggerFragment);

        ProjectDetailRequireFragment worksDetailSkillFragment = ProjectDetailRequireFragment.newInstance(worksDetailBean);
        mFragments.add(worksDetailSkillFragment);
        TalentDetailFragmentAdapter disPagerAdapter = new TalentDetailFragmentAdapter(getSupportFragmentManager(), mFragments, mTabArray);

        viewpager.setAdapter(disPagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        stlDisTab.setViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollableLayout.setCurrentScrollableContainer(mFragments.get(position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);


        if (!TextUtils.isEmpty(worksDetailBean.getLogo())) {
            sdv.setImageURI(Uri.parse(worksDetailBean.getLogo()));
        }
        tvTheatreName.setText(worksDetailBean.getTitle());
        tvFee.setText(worksDetailBean.getExpenseDescpt());
        tvUnit.setText(worksDetailBean.getExpenseUnit());
        tvProducers.setText(worksDetailBean.getLinkman());
        tvActorNumber.setText(worksDetailBean.getPeopleNum() + "人");
        tvCity.setText(worksDetailBean.getRegionName());
        tvTheatreKind.setText(worksDetailBean.getClassifyName());
        tvShowTime.setText(worksDetailBean.getShowLast() + "min");
        tvAlreadyShowTimes.setText(worksDetailBean.getViewTimes() + "场");
        try {
            if (0 != worksDetailBean.getPremiereTime() && 0 != worksDetailBean.getPerformanceEndDate()) {
                tvShowUsefulTime.setText(DateUtil.transTime(worksDetailBean.getPerformanceBeginDate() + "", "yyyy.M.d") + "～" + DateUtil.transTime(worksDetailBean.getPerformanceEndDate() + "", "yyyy.M.d"));
            }
        } catch (Exception e) {

        }
//        tvFirstShowTime.setText(worksDetailBean.getPremiereTime() + "");


//        tvShowUsefulTime.setText(worksDetailBean.getPerformanceBeginDate()+"～"+worksDetailBean.getPerformanceEndDate());


        if (worksDetailBean.getStaffs() != null && worksDetailBean.getStaffs().size() > 0) {
            tvActorDetail.setVisibility(View.VISIBLE);
            rcv.setVisibility(View.VISIBLE);
            mStaffs = worksDetailBean.getStaffs();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorksDetailMessageActivity.this, LinearLayoutManager.HORIZONTAL, false);
            final StaffIntroduceAdapter staffIntroduceAdapter = new StaffIntroduceAdapter(WorksDetailMessageActivity.this, mStaffs);
            LinearItemDecoration linearItemDecoration = new LinearItemDecoration((int) getResources().getDimension(R.dimen.DIMEN_14PX));
            staffIntroduceAdapter.setOnItemClickListener(new StaffIntroduceAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                    StaffIntroduceDialog staffIntroduceDialog = StaffIntroduceDialog.newInstence(worksDetailBean.getStaffs().get(position));
                    staffIntroduceDialog.show(getSupportFragmentManager(), "");

                }
            });
            rcv.addItemDecoration(linearItemDecoration);
            rcv.setLayoutManager(linearLayoutManager);
            rcv.setAdapter(staffIntroduceAdapter);

        } else {
            tvActorDetail.setVisibility(View.GONE);
            rcv.setVisibility(View.GONE);
        }


//        if (!TextUtils.isEmpty(talentDetailBean.getLogo())) {
//            talentsPic.setImageURI(Uri.parse(talentDetailBean.getLogo()));
//        }
//        tvName.setText(talentDetailBean.getName());
//        String major = "";
//        if (talentDetailBean.getClassifyNames() != null && talentDetailBean.getClassifyNames().size() > 0) {
//            for (int i = 0; i < talentDetailBean.getClassifyNames().size(); i++) {
//                if (i == 0) {
//                    major = talentDetailBean.getClassifyNames().get(i);
//                } else {
//                    major = major + "/" + talentDetailBean.getClassifyNames().get(i);
//                }
//            }
//        }
//        tvMajor.setText(major);
//        tvAge.setText(talentDetailBean.getAge() + "");
//        tvWeight.setText(talentDetailBean.getWeight());
//        tvHeight.setText(talentDetailBean.getHeight());
//        tvUniversity.setText(talentDetailBean.getSchool());
//        tvOrganize.setText(talentDetailBean.getAgency());
        tvIntroduce.setText(worksDetailBean.getDescription());


    }


    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_works_detail_message;
    }

    @Override
    public void initData() {
        pageLoadingDialog = new PageLoadingDialog(this);
        pageLoadingDialog.setPageLoadingListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            mProjectId = extras.getString(PARAMS_ID);
            mIsFromOrgan = extras.getBoolean(PARAMS_ORG, false);
        }
        if (mIsFromOrgan) {
            URL_TALENT_DETAL = Constant.URL_USER_REPERTORY_DETAIL;
        } else {
            URL_TALENT_DETAL = Constant.URL_REPERTORY_DETAIL;
        }
        getWorkDetail();
        mShareHandler = ShareDialog.regToWeibo(this);
    }

    @Override
    public void initView() {
    }

    @Override
    public void setView() {
        ivRightImg.setImageResource(R.mipmap.icon_share_gray);
        if (mIsFromOrgan) {
            btnEdit.setVisibility(View.VISIBLE);
            llMakeTelephone.setVisibility(View.GONE);
        } else {
            btnEdit.setVisibility(View.GONE);
            llMakeTelephone.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private void getWorkDetail() {
        Map<String, String> params = new TreeMap<>();
        params.put("id", mProjectId);
        if (mIsFromOrgan) {
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        }
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.i(TAG, "getRepertoryClassify: " + params.toString());
        if (!pageLoadingDialog.isShowing() && !this.isFinishing()) {
            pageLoadingDialog.show();
        }
        requestCall = RequestUtil.request(true, URL_TALENT_DETAL, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (pageLoadingDialog.isShowing()) {
                        pageLoadingDialog.dismiss();
                    }
                    if (!TextUtils.isEmpty(obj)) {
                        Log.i(TAG, "onSuccess: obj=" + obj);
                        Gson gson = new Gson();
                        try {
                            worksDetailBean = gson.fromJson(obj, WorksDetailBean.class);
                            if (worksDetailBean != null) {
                                uiHandler.sendEmptyMessage(0);
                                return;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess: 数据解析异常");
                        }
                    } else {
                        Log.e(TAG, "onSuccess: 数据为空");
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                    pageLoadingDialog.showErrorLoading();
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                pageLoadingDialog.showErrorLoading();
            }
        });
    }

    @OnClick(R.id.fly_right_img)
    public void shareProject() {
        if (shareDialog == null) {
            String title = worksDetailBean == null ? getString(R.string.app_name) : worksDetailBean.getTitle();
            String shareLink = worksDetailBean == null ? getString(R.string.share_main_url) : worksDetailBean.getShareLink();
            shareDialog = ShareDialog.newInstence(title, shareLink);
            shareDialog.setShareHandler(mShareHandler);
        }
        shareDialog.show(getSupportFragmentManager(), "SHARE.DIALOG");
    }

    @OnClick(R.id.btn_edit)
    public void jump2ProjectEdit() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProjectEditActivity.PARAMS_NEW, false);
        invokActivity(this, ProjectEditActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_EDIT);
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void back() {
        finish();
    }

    /**
     * 打电话
     */
    @OnClick(R.id.ll_make_telephone)
    public void callPhone() {
        if (TextUtils.isEmpty(worksDetailBean.getLinkTel())) {
            showToast(getString(R.string.tip_linkdata_error));
            return;
        }
        if (takePhoneDialog == null) {
            takePhoneDialog = TakePhoneDialog.newInstence(worksDetailBean.getLinkman(), worksDetailBean.getLinkTel());
        }
        takePhoneDialog.show(getSupportFragmentManager(), "TAKEPHONE.DIALOG");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareDialog != null) {
            mShareHandler.doResultIntent(intent, shareDialog);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestCall != null) {
            requestCall.cancel();
            requestCall = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLogin(ProjectNotifyEvent event) {
        if (event == null) {
            return;
        }
        if (this.isFinishing()) {
            return;
        }
        try {
            ProjectDetailInfo p = ProjectDetailInfo.getInstance();
            switch (event.getActionCode()) {
                case ProjectNotifyEvent.NOTIFY_AVATAR:
                    sdv.setImageURI(p.getLogo());
                    break;
                case ProjectNotifyEvent.NOTIFY_BASE_INFO:
                    tvTheatreName.setText(p.getTitle());
                    tvFee.setText(p.getExpense() + "");
                    tvProducers.setText(p.getLinkman());
                    tvActorNumber.setText(p.getPeopleNum() + "人");
                    tvCity.setText(p.getRegionName());

                    if (Integer.valueOf(p.getSeatingRequir()) <= 400) {
                        tvThratreSize.setText("小剧场");
                    } else if (400 < Integer.valueOf(p.getSeatingRequir()) && Integer.valueOf(p.getSeatingRequir()) <= 800) {
                        tvThratreSize.setText("中剧场");
                    } else if (800 < Integer.valueOf(p.getSeatingRequir()) && Integer.valueOf(p.getSeatingRequir()) <= 1500) {
                        tvThratreSize.setText("大剧场");
                    } else if (1500 < Integer.valueOf(p.getSeatingRequir())) {
                        tvThratreSize.setText("超大剧场");
                    }
                    tvShowTime.setText(p.getShowLast() + "min");
                    tvFirstShowTime.setText(DateUtil.transTime(p.getPremiereTime() + "", "yyyy.M.d"));
                    tvAlreadyShowTimes.setText(p.getViewTimes() + "场");
                    tvShowUsefulTime.setText(
                            DateUtil.transTime(p.getPerformanceBeginDate() + "", "yyyy.M.d") + "～" +
                                    DateUtil.transTime(p.getPerformanceEndDate() + "", "yyyy.M.d"));

                    break;
                case ProjectNotifyEvent.NOTIFY_INTRODUCE:
                    tvIntroduce.setText(p.getDescription());
                    break;
                case ProjectNotifyEvent.NOTIFY_INTRODUCE_CREATE:
                    mStaffs.clear();
                    mStaffs.addAll(p.getStaffs());
                    rcv.getAdapter().notifyDataSetChanged();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK && shareDialog != null) {
                Tencent.handleResultData(data, shareDialog);
            }
        }
    }

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void onRetry() {
        getWorkDetail();
    }
}
