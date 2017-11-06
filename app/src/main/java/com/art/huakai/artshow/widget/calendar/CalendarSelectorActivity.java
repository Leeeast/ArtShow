package com.art.huakai.artshow.widget.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author lvning
 * @version create time:2014-10-29_上午9:56:45
 * @Description 预订日选择
 */
public class CalendarSelectorActivity extends Activity {

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
        ButterKnife.bind(this);
        orderDays.add("2017-11-1");
        orderDays.add("2017-11-8");
        orderDays.add("2017-11-19");
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


}
