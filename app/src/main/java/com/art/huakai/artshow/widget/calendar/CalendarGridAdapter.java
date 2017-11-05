package com.art.huakai.artshow.widget.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lining on 17-10-28.
 */
public class CalendarGridAdapter extends RecyclerView.Adapter {

    private final static String TAG = "CalendarGridAdapter";
    private final static int NORMAL = 1;
    private ArrayList<Day> days;
    private Calendar c;
    private Context context;
    private ArrayList<String> orderDays;
    private String currentMoth;
    private OnItemClickListener onItemClickListener;

    //  注意  orderDays 传递数据格式为2017 9 21  currentMoth传递格式为2017 12
    public CalendarGridAdapter(Context context, Calendar c, int passDays, ArrayList<String> orderDays, String currentMoth) {
        this.c = c;
        this.context = context;
        this.orderDays = orderDays;
        this.currentMoth = currentMoth;
        this.days = CalendarUtils.getDaysOfMonth(this.c, passDays, "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_calendar_item, null);
        MyViewholder myViewholder = new MyViewholder(view);
        return myViewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewholder) {
            MyViewholder viewHolder = (MyViewholder) holder;
            viewHolder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        if (!TextUtils.isEmpty(days.get(position).getName())) {
                            String str = currentMoth + "-" + days.get(position).getName();
                            if (!isOrdered(orderDays, str)) {
                                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                                onItemClickListener.onItemClickListener(str);
                            } else {
                                Toast.makeText(context, "已经被预定了", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            viewHolder.rl.setBackgroundColor(0xffffffff);
            viewHolder.view_order.setVisibility(View.INVISIBLE);
            viewHolder.tv_today.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(days.get(position).getName())) {
                if (days.get(position).getType() == Day.DayType.NOT_ENABLE) {
                    viewHolder.rl.setBackgroundColor(0xfffafafa);
                    viewHolder.tv_data.setTextColor(0xffaaa6a2);
                    viewHolder.tv_today.setVisibility(View.INVISIBLE);
                } else if (days.get(position).getType() == Day.DayType.TODAY) {
                    viewHolder.tv_today.setVisibility(View.VISIBLE);
                    viewHolder.tv_data.setTextColor(0xff3b3d33);
                    String str = currentMoth + days.get(position).getName();
                    if (isOrdered(orderDays, str)) {
                        viewHolder.view_order.setVisibility(View.VISIBLE);
                        viewHolder.rl.setBackgroundColor(0xffcccccc);
                        viewHolder.tv_data.setTextColor(0xffffffff);
                    }
                } else {
                    viewHolder.tv_today.setVisibility(View.INVISIBLE);
                    viewHolder.tv_data.setTextColor(0xff3b3d33);
                    String str = currentMoth + days.get(position).getName();
                    if (isOrdered(orderDays, str)) {
                        viewHolder.view_order.setVisibility(View.VISIBLE);
                        viewHolder.rl.setBackgroundColor(0xffcccccc);
                        viewHolder.tv_data.setTextColor(0xffffffff);
                    }
                }
                viewHolder.tv_data.setText(days.get(position).getName());
            } else {
                viewHolder.tv_today.setVisibility(View.INVISIBLE);
                viewHolder.tv_data.setText(days.get(position).getName());
            }
        }

    }


    private boolean isOrdered(ArrayList<String> orderDays, String currentDay) {
        if (orderDays != null && orderDays.size() > 0) {
            for (int i = 0; i < orderDays.size(); i++) {
                if (orderDays.get(i).equals(currentDay)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL;
    }

    class MyViewholder extends RecyclerView.ViewHolder {

        //      现在确定计算规则  首先是不可用的小于今天的数字都是不可用的 同时外加这个数字是不是空的
//      是空的就不需要做任何处理  1：所以最外面加上不为空判断  2：判断是否可用
//      可获取今天日期  将年月数据传递到item判断当前item是不是选中的  是今天和预定显示今天外加预定图标
//      已预定的背景颜色是  #cccccc  今天之前的背景颜色是 #fafafa（另外今天需要显示今天文字）   默认背景颜色是 #ffffff
//      文字颜色设置 默认文字颜色是 #3b3d33  已预定文字颜色是 #ffffff  不可用的文字颜色是 #aaa6a2
        RelativeLayout rl;
        View view_order;
        TextView tv_data;
        TextView tv_today;

        public MyViewholder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            view_order = itemView.findViewById(R.id.view_order);
            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
            tv_today = (TextView) itemView.findViewById(R.id.tv_today);

        }
    }


    public void notifyDatasetChange(ArrayList<Day> days) {
        this.days.clear();
        if (this.days.addAll(days)) {
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(String str);
    }

}
