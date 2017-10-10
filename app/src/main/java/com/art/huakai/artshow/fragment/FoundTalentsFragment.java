package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.LookingWorksAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.widget.SmartRecyclerview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoundTalentsFragment extends BaseFragment implements View.OnClickListener, SmartRecyclerview.LoadingListener {


    @BindView(R.id.iv_choose_price)
    ImageView ivChoosePrice;
    @BindView(R.id.iv_choose_number)
    ImageView ivChooseNumber;
    @BindView(R.id.iv_real_choose)
    ImageView ivRealChoose;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    SmartRecyclerview recyclerView;
    private ArrayList<String> list;
    private LookingWorksAdapter lookingWorksAdapter;
    private LinearLayoutManager linearlayoutManager;
    private LinearItemDecoration linearItemDecoration;

    public FoundTalentsFragment() {
        // Required empty public constructor
    }

    public static FoundTalentsFragment newInstance() {
        FoundTalentsFragment fragment = new FoundTalentsFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_found_talents;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void setView() {
        ivChoosePrice.setOnClickListener(this);
        ivChooseNumber.setOnClickListener(this);
        ivRealChoose.setOnClickListener(this);

        linearlayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        linearItemDecoration=new LinearItemDecoration((int) getContext().getResources().getDimension(R.dimen.DIMEN_14PX));
        recyclerView.setLoadingListener(this);
        recyclerView.setLayoutManager(linearlayoutManager);
//        recyclerView.addItemDecoration(linearItemDecoration);
        list = new ArrayList<String>();
        lookingWorksAdapter = new LookingWorksAdapter(getContext(), list);
        recyclerView.setAdapter(lookingWorksAdapter);
        lookingWorksAdapter.setOnItemClickListener(new LookingWorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

                Toast.makeText(getContext(), "itemclick", Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView.refresh();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: v.getId==" + v.getId());
        switch (v.getId()) {

            case R.id.iv_choose_number:

                Toast.makeText(getContext(), "iv_choose_number", Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_choose_price:

                Toast.makeText(getContext(), "iv_choose_price", Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_real_choose:

                Toast.makeText(getContext(), "iv_real_choose", Toast.LENGTH_SHORT).show();

                break;

        }

    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                list.clear();
                for (int i = 0; i < 50; i++) {
                    list.add("文字" + i);
                }
                recyclerView.refreshComplete();
            }

        }, 2000);

    }

    @Override
    public void onLoadMore() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < 30; i++) {
                    list.add("文字" + i);
                }
                recyclerView.loadMoreComplete();
            }

        }, 2000);

    }
}
