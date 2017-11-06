package com.art.huakai.artshow.widget.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.okhttp.request.RequestCall;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * @author lvning
 * @version create time:2014-10-29_上午9:56:45
 * @Description 预订日选择
 */
public class CalendarSelectorActivity extends Activity implements View.OnClickListener {
    public static final String TAG = CalendarSelectorActivity.class.getSimpleName();

    /**
     * 可选天数
     */
    public static final String DAYS_OF_SELECT = "days_of_select";
    /**
     * 上次预订日
     */
    public static final String ORDER_DAY = "order_day";
    /**
     * 能否选择
     */
    public static final String SELECT_ENALBE = "SELECT_ENALBE";
    public static final String SELECT_LIST = "SELECT_LIST";
    @BindView(R.id.lly_back)
    LinearLayout llyBack;

    private int daysOfSelect;
    private String orderDay;
    private RecyclerView rcv;
    LinearLayoutManager linearLayoutManager;
    private Myadapter myadapter;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private ArrayList<String> orderDays;
    private boolean mSelectEnable;
    private RequestCall requestCall;
    private ShowProgressDialog showProgressDialog;


//吊起日历选择界面

//	int days = 1000;
//	String order = orderEt.getText().toString();
//
//	Intent i = new Intent(MainActivity.this, CalendarSelectorActivity.class);
//	i.putExtra(yinhe.calendarproject.lib.CalendarSelectorActivity.DAYS_OF_SELECT, days);
//	i.putExtra(yinhe.calendarproject.lib.CalendarSelectorActivity.ORDER_DAY, order);
//	startActivityForResult(i, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycalendar_selector);
        orderDays = getIntent().getStringArrayListExtra(SELECT_LIST);
        showProgressDialog = new ShowProgressDialog(this);
        ButterKnife.bind(this);
        mSelectEnable = getIntent().getBooleanExtra(SELECT_ENALBE, false);
        daysOfSelect = getIntent().getIntExtra(DAYS_OF_SELECT, 30);
        orderDay = getIntent().getStringExtra(ORDER_DAY);
        rcv = (RecyclerView) findViewById(R.id.rcv);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycledViewPool = rcv.getRecycledViewPool();
        myadapter = new Myadapter();
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(myadapter);

        llyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(R.string.theatre_schedule);
        if (mSelectEnable) {
            TextView tvSubTitle = (TextView) findViewById(R.id.tv_subtitle);
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subtitle:
                commitDisableDate();
                break;
        }
    }

    public void commitDisableDate() {
        if (orderDays.size() == 0) {
            Toast.makeText(this, getString(R.string.tip_dangqi_select), Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (String data : orderDays) {
            jsonArray.put(data);
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(TheatreDetailInfo.getInstance().getId())) {
            params.put("id", TheatreDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("disabledDates", jsonArray.toString());
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "sign:" + sign);
        showProgressDialog.show();
        requestCall = RequestUtil.request(true, Constant.URL_THEATER_EDIT_DISABLEDDATES, params, 33, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String projectId = jsonObject.getString("id");
                        TheatreDetailInfo.getInstance().setId(projectId);
                        EventBus.getDefault().post(new TheatreInfoChangeEvent());
                        Toast.makeText(CalendarSelectorActivity.this, getString(R.string.tip_theatre_release), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                }
            }

            @Override
            public void onFailed(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "- id = " + id);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
            }
        });
    }

    class Myadapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(CalendarSelectorActivity.this).inflate(R.layout.calendar, null);
            MyViewholder myViewholder = new MyViewholder(view);


            return myViewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewholder myViewholder = (MyViewholder) holder;
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, position);
            myViewholder.yearAndMonth.setText(c.get(Calendar.YEAR) + CalendarSelectorActivity.this.getString(R.string.year)
                    + (c.get(Calendar.MONTH) + 1) + CalendarSelectorActivity.this.getString(R.string.month));
            myViewholder.calendarGrid.setRecycledViewPool(recycledViewPool);
            String str = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1);
            if (position == 0) {
                CalendarGridAdapter myGridAdapter = new CalendarGridAdapter(CalendarSelectorActivity.this, c, daysOfSelect, orderDays, str, mSelectEnable);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CalendarSelectorActivity.this, 7);
                myViewholder.calendarGrid.setLayoutManager(gridLayoutManager);
                myGridAdapter.setOnItemClickListener(new CalendarGridAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(String str) {

                    }
                });
                myViewholder.calendarGrid.setAdapter(myGridAdapter);
            } else {
                int d = daysOfSelect - CalendarUtils.currentMonthRemainDays() - CalendarUtils.getFlowMonthDays(position - 1);
                CalendarGridAdapter myGridAdapter = new CalendarGridAdapter(CalendarSelectorActivity.this, c, d, orderDays, str, mSelectEnable);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CalendarSelectorActivity.this, 7);
                myViewholder.calendarGrid.setLayoutManager(gridLayoutManager);
                myGridAdapter.setOnItemClickListener(new CalendarGridAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(String str) {

                    }
                });
                myViewholder.calendarGrid.setAdapter(myGridAdapter);
            }


        }

        @Override
        public int getItemCount() {
            return CalendarUtils.throughMonth(Calendar.getInstance(), daysOfSelect) + 1;
        }

    }


    class MyViewholder extends RecyclerView.ViewHolder {

        TextView yearAndMonth;
        RecyclerView calendarGrid;

        //        NoScrollGridView calendarGrid;
        public MyViewholder(View itemView) {
            super(itemView);
            yearAndMonth = (TextView) itemView.findViewById(R.id.tv_year_month);
            calendarGrid = (RecyclerView) itemView.findViewById(R.id.recy);

        }
    }

    public void setOnCalendarOrderListener(OnCalendarOrderListener listener) {
        listener = listener;
    }


    public interface OnCalendarOrderListener {
        void onOrder(String orderInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestCall != null) {
            requestCall.cancel();
        }
    }
}
