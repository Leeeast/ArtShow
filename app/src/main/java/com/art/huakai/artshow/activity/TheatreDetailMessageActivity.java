package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TheatreDetailFragmentAdapter;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.base.HeaderViewPagerFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.dialog.ShareDialog;
import com.art.huakai.artshow.dialog.TakePhoneDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailBean;
import com.art.huakai.artshow.fragment.ErrorFragment;
import com.art.huakai.artshow.fragment.StaggerFragment;
import com.art.huakai.artshow.fragment.TheatreDetailDesFragment;
import com.art.huakai.artshow.fragment.TheatreDetailParamsFragment;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.AnimUtils;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.art.huakai.artshow.widget.calendar.CalendarSelectorActivity;
import com.art.huakai.artshow.widget.headerviewpager.HeaderViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class TheatreDetailMessageActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener {
    public static final String PARAMS_ID = "PARAMS_ID";
    public static final String PARAMS_ORG = "PARAMS_ORG";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sdv)
    ChinaShowImageView sdv;
    @BindView(R.id.tv_theatre_name)
    TextView tvTheatreName;
    @BindView(R.id.tv_theatre_kind)
    TextView tvTheatreKind;

    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_seat_count)
    TextView tvSeatCount;
    @BindView(R.id.ll_check_ticket_area)
    LinearLayout llCheckTicketArea;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.stl_dis_tab)
    SlidingTabLayout stlDisTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.ll_make_telephone)
    LinearLayout llMakeTelephone;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.iv_no_content)
    ImageView ivNoContent;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ll_check_map_area)
    LinearLayout llCheckMapArea;
    @BindView(R.id.ll_check_schedule_area)
    LinearLayout llCheckScheduleArea;

    private String[] mTabArray;
    private ArrayList<HeaderViewPagerFragment> mFragments;
    private Handler handler = new Handler();
    private String theatreId;
    private TheatreDetailBean theatreDetailBean;
    private ShareDialog shareDialog;
    private boolean mIsFromOrgan;
    private String URL_THEATRE_DETAL;
    private TakePhoneDialog takePhoneDialog;
    private WbShareHandler mShareHandler;

    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.VISIBLE);
                ivNoContent.setVisibility(View.GONE);
                setData();

            }
        }
    };
    private RequestCall requestCall;

    private void initPosition() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void setData() {
        mTabArray = getResources().getStringArray(R.array.theatre_detail_tab);
        mFragments = new ArrayList<HeaderViewPagerFragment>();
        TheatreDetailDesFragment theatreDetailTheatreFragment = TheatreDetailDesFragment.newInstance(theatreDetailBean);
        mFragments.add(theatreDetailTheatreFragment);
        if (theatreDetailBean.getPictures() != null && theatreDetailBean.getPictures().size() > 0) {
            StaggerFragment staggerFragment = StaggerFragment.newInstance();
            staggerFragment.setLists(theatreDetailBean.getPictures());
            mFragments.add(staggerFragment);
        } else {
            ErrorFragment errorFragment = ErrorFragment.newInstance();
            mFragments.add(errorFragment);

        }

        TheatreDetailParamsFragment theatreDetailParamsFragment = TheatreDetailParamsFragment.newInstance(theatreDetailBean);
        mFragments.add(theatreDetailParamsFragment);

        TheatreDetailFragmentAdapter disPagerAdapter = new TheatreDetailFragmentAdapter(getSupportFragmentManager(), mFragments, mTabArray);

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

        scrollableLayout.setCurrentScrollableContainer(mFragments.get(0));


        viewpager.setCurrentItem(0);

        if (!TextUtils.isEmpty(theatreDetailBean.getLogo())) {
            sdv.setImageURI(Uri.parse(theatreDetailBean.getLogo()));
        }
        tvTheatreName.setText(theatreDetailBean.getName());

        tvFee.setText(theatreDetailBean.getExpense() + "");
        tvSeatCount.setText(theatreDetailBean.getSeating() + "");
//        tvWeight.setText(talentDetailBean.getWeight());
//        tvHeight.setText(talentDetailBean.getHeight());
//        tvUniversity.setText(talentDetailBean.getSchool());
//        tvOrganize.setText(talentDetailBean.getAgency());
        tvIntroduce.setText(theatreDetailBean.getDescription());
        tvLocation.setText(theatreDetailBean.getRegionName());


    }

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_theatre_detail_message;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            theatreId = extras.getString(PARAMS_ID);
            mIsFromOrgan = extras.getBoolean(PARAMS_ORG, false);
        }
        if (mIsFromOrgan) {
            URL_THEATRE_DETAL = Constant.URL_USER_THEATER_DETAIL;
        } else {
            URL_THEATRE_DETAL = Constant.URL_THEATER_DETAIL;
        }
        getTheatreDetail();
        mShareHandler = ShareDialog.regToWeibo(this);
    }

    @Override
    public void initView() {
        ivRightImg.setImageResource(R.mipmap.icon_share_gray);
        if (mIsFromOrgan) {
            btnEdit.setVisibility(View.VISIBLE);
            llMakeTelephone.setVisibility(View.GONE);
        } else {
            btnEdit.setVisibility(View.GONE);
            llMakeTelephone.setVisibility(View.VISIBLE);
        }
        AnimUtils.rotate(ivLoading);
        ivNoContent.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);
        llCheckMapArea.setOnClickListener(this);
        llCheckScheduleArea.setOnClickListener(this);
        llCheckTicketArea.setOnClickListener(this);
    }

    @Override
    public void setView() {

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
            case R.id.ll_check_map_area:
                Intent intent = new Intent();
                String coordinate = theatreDetailBean.getCoordinate();
                String[] lists = {};
                if (!TextUtils.isEmpty(coordinate)) {
                    lists = coordinate.split(",");
                }
                intent.putExtra("toLatitude", "");
                intent.putExtra("toLongitude", "");
                if (lists != null && lists.length == 2) {
                    intent.putExtra("toLatitude", lists[1]);
                    intent.putExtra("toLongitude", lists[0]);
                }
                intent.putExtra("theatreName", theatreDetailBean.getName());
                intent.putExtra("theatreLocation", theatreDetailBean.getAddress());
                intent.setClass(TheatreDetailMessageActivity.this, NavigationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_check_ticket_area:

                break;
            case R.id.ll_check_schedule_area:
                Intent i = new Intent(TheatreDetailMessageActivity.this, CalendarSelectorActivity.class);
                i.putExtra(CalendarSelectorActivity.DAYS_OF_SELECT, 1000);
                i.putExtra(CalendarSelectorActivity.ORDER_DAY, "");
                i.putExtra(CalendarSelectorActivity.SELECT_ENALBE, false);
                startActivity(i);

                break;

        }


    }

    private void getTheatreDetail() {

        Map<String, String> params = new TreeMap<>();
        params.put("id", theatreId);
        if (mIsFromOrgan) {
            params.put("userId", LocalUserInfo.getInstance().getId());
            params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        }
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        Log.i(TAG, "getRepertoryClassify: " + params.toString());
        requestCall = RequestUtil.request(true, URL_THEATRE_DETAL, params, 130, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                if (isSuccess) {
                    if (!TextUtils.isEmpty(obj)) {
                        Log.i(TAG, "onSuccess: obj=" + obj);
                        Gson gson = new Gson();
                        try {
                            theatreDetailBean = gson.fromJson(obj, TheatreDetailBean.class);
                            if (theatreDetailBean != null) {
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
                }
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                ivLoading.setVisibility(View.GONE);
                rlContent.setVisibility(View.GONE);
                ivNoContent.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.fly_right_img)
    public void shareProject() {
        if (shareDialog == null) {
            String title = theatreDetailBean == null ? getString(R.string.app_name) : theatreDetailBean.getName();
            String shareLink = theatreDetailBean == null ? getString(R.string.share_main_url) : theatreDetailBean.getShareLink();
            shareDialog = ShareDialog.newInstence(title, shareLink);
            shareDialog.setShareHandler(mShareHandler);
        }
        shareDialog.show(getSupportFragmentManager(), "SHARE.DIALOG");
    }


    @OnClick(R.id.btn_edit)
    public void jump2TheatreActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(TheatreEditActivity.PARAMS_NEW_CREATE, false);
        invokActivity(TheatreDetailMessageActivity.this, TheatreEditActivity.class, bundle, JumpCode.FLAG_REQ_THEATRE_EDIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
        if (TextUtils.isEmpty(theatreDetailBean.getLinkTel())) {
            showToast(getString(R.string.tip_linkdata_error));
            return;
        }
        if (takePhoneDialog == null) {
            takePhoneDialog = TakePhoneDialog.newInstence(theatreDetailBean.getLinkman(), theatreDetailBean.getLinkTel());
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
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息

                amapLocation.getDistrict();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                Toast.makeText(TheatreDetailMessageActivity.this, amapLocation.toString(), Toast.LENGTH_SHORT).show();
                mlocationClient.stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(TheatreDetailMessageActivity.this, amapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        if (requestCall != null) {
            requestCall.cancel();
            requestCall = null;
        }
    }
}
