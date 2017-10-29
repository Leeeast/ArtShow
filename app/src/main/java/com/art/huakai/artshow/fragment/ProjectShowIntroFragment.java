package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.LoginUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ProjectShowIntroFragment extends BaseFragment {

    @BindView(R.id.edt_introduce)
    EditText edtIntroduce;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;

    private ShowProgressDialog showProgressDialog;
    private String mDescription;

    public ProjectShowIntroFragment() {
    }

    public static ProjectShowIntroFragment newInstance() {
        ProjectShowIntroFragment fragment = new ProjectShowIntroFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        mDescription = ProjectDetailInfo.getInstance().getPlot();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_project_show_intro;
    }

    @Override
    public void initView(View rootView) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.project_show_introduce);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
        if (!TextUtils.isEmpty(mDescription)) {
            edtIntroduce.setText(mDescription);
        }
    }

    @OnClick(R.id.lly_back)
    public void back() {
        getActivity().finish();
    }

    /**
     * 确认信息
     */
    @OnClick(R.id.tv_subtitle)
    public void confirmInfo() {
        changeResumeDescription();
    }

    /**
     * 修改简历个人介绍
     */
    public void changeResumeDescription() {
        //判断是否登录
        if (!LoginUtil.checkUserLogin(getContext(), true)) {
            return;
        }
        if (TextUtils.isEmpty(LocalUserInfo.getInstance().getId()) ||
                TextUtils.isEmpty(LocalUserInfo.getInstance().getAccessToken())) {
            Toast.makeText(getContext(), getString(R.string.tip_data_error), Toast.LENGTH_SHORT).show();
            return;
        }
        mDescription = edtIntroduce.getText().toString().trim();
        if (TextUtils.isEmpty(mDescription)) {
            Toast.makeText(getContext(), getString(R.string.tip_project_plot_input), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(ProjectDetailInfo.getInstance().getId())) {
            params.put("id", ProjectDetailInfo.getInstance().getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("plot", mDescription);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        showProgressDialog.show();
        RequestUtil.request(true, Constant.URL_REPERTORY_EDIT_PLOT, params, 81, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                if (isSuccess) {
                    try {
                        showToast(getString(R.string.tip_project_plot_input_change_suc));
                        JSONObject jsonObject = new JSONObject(obj);
                        String projectId = jsonObject.getString("id");
                        ProjectDetailInfo.getInstance().setId(projectId);
                        ProjectDetailInfo.getInstance().setPlot(mDescription);
                        EventBus.getDefault().post(new ProjectInfoChangeEvent());
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
}
