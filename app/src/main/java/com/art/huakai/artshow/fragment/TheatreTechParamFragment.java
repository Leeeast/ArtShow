package com.art.huakai.artshow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.TechParamsAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.GridLayoutItemDecoration;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.art.huakai.artshow.utils.TheatreTechParamsUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class TheatreTechParamFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.edt_theatre_height)
    EditText edtTheatreHeight;
    @BindView(R.id.edt_theatre_width)
    EditText edtTheatreWidth;
    @BindView(R.id.edt_theatre_depth)
    EditText edtTheatreDepth;
    @BindView(R.id.edt_curtain_height)
    EditText edtCurtainHeight;
    @BindView(R.id.edt_curtain_width)
    EditText edtCurtainWidth;
    @BindView(R.id.edt_theatre_dressing)
    EditText edtTheatreDressing;
    @BindView(R.id.edt_theatre_rehearsal_roomnum)
    EditText edtTheatreRehearsalRoomNum;
    @BindView(R.id.edt_theatre_costume_roomnum)
    EditText edtTheatreCostumeRoomnum;
    @BindView(R.id.edt_theatre_prop_roomnum)
    EditText edtTheatrePropRoomNum;

    private ShowProgressDialog showProgressDialog;
    private ArrayList<String> techParams;
    private ArrayList<String> techParamsAdded;
    private TheatreDetailInfo theatreInstance;
    private String[] mStringArray;

    public TheatreTechParamFragment() {
    }

    public static TheatreTechParamFragment newInstance() {
        TheatreTechParamFragment fragment = new TheatreTechParamFragment();
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        showProgressDialog = new ShowProgressDialog(getContext());
        theatreInstance = TheatreDetailInfo.getInstance();
        techParams = new ArrayList<>();
        techParamsAdded = new ArrayList<>();
        mStringArray = getResources().getStringArray(R.array.theatre_tech_param);
        techParams.addAll(Arrays.asList(mStringArray));
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_theatre_tech_param;
    }

    @Override
    public void initView(View rootView) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_technical_parameters);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setView() {
        edtTheatreHeight.setText(theatreInstance.getStageHeight());
        edtTheatreWidth.setText(theatreInstance.getStageWidth());
        edtTheatreDepth.setText(theatreInstance.getStageDepth());
        edtCurtainHeight.setText(theatreInstance.getCurtainHeight());
        edtCurtainWidth.setText(theatreInstance.getCurtainWidth());
        edtTheatreDressing.setText(theatreInstance.getDressingRoomNum());
        edtTheatreRehearsalRoomNum.setText(theatreInstance.getRehearsalRoomNum());
        edtTheatreCostumeRoomnum.setText(theatreInstance.getCostumeRoomNum());
        edtTheatrePropRoomNum.setText(theatreInstance.getPropRoomNum());
        TheatreTechParamsUtil.getTheatreTechParamsAddedList(techParamsAdded);

        TechParamsAdapter techParamsAdapter = new TechParamsAdapter(techParams, techParamsAdded);
        GridLayoutItemDecoration gridLayoutItemDecorationone = new GridLayoutItemDecoration(
                3,
                GridLayoutManager.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen.DIMEN_20PX),
                getResources().getDimensionPixelSize(R.dimen.DIMEN_10PX));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(gridLayoutItemDecorationone);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(techParamsAdapter);

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
        final String stageHeight = edtTheatreHeight.getText().toString().trim();
        final String stageWidth = edtTheatreWidth.getText().toString().trim();
        final String stageDepth = edtTheatreDepth.getText().toString().trim();
        final String curtainHeight = edtCurtainHeight.getText().toString().trim();
        final String curtainWidth = edtCurtainWidth.getText().toString().trim();
        final String stageDressingNum = edtTheatreDressing.getText().toString().trim();
        final String stageRehearsalRoomNum = edtTheatreRehearsalRoomNum.getText().toString().trim();
        final String stagePropRoomNum = edtTheatrePropRoomNum.getText().toString().trim();
        final String stageCostumeRoomNum = edtTheatreCostumeRoomnum.getText().toString().trim();
        final String stageLights = techParamsAdded.contains(mStringArray[0]) ? "1" : "0";
        final String stereoEquipment = techParamsAdded.contains(mStringArray[1]) ? "1" : "0";
        final String broadcastSystem = techParamsAdded.contains(mStringArray[2]) ? "1" : "0";
        final String steeve = techParamsAdded.contains(mStringArray[3]) ? "1" : "0";
        final String musicStage = techParamsAdded.contains(mStringArray[4]) ? "1" : "0";
        final String chorusPlatform = techParamsAdded.contains(mStringArray[5]) ? "1" : "0";
        final String orchestraPit = techParamsAdded.contains(mStringArray[6]) ? "1" : "0";
        final String acousticShroud = techParamsAdded.contains(mStringArray[7]) ? "1" : "0";
        final String bandPlatform = techParamsAdded.contains(mStringArray[8]) ? "1" : "0";
        final String curtainSystem = techParamsAdded.contains(mStringArray[9]) ? "1" : "0";
        final String specialEquipment = techParamsAdded.contains(mStringArray[10]) ? "1" : "0";
        final String projector = techParamsAdded.contains(mStringArray[11]) ? "1" : "0";
        final Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(theatreInstance.getId())) {
            params.put("id", theatreInstance.getId());
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("stageHeight", stageHeight);
        params.put("stageWidth", stageWidth);
        params.put("stageDepth", stageDepth);
        params.put("curtainHeight", curtainHeight);
        params.put("curtainWidth", curtainWidth);
        params.put("dressingRoomNum", stageDressingNum);
        params.put("rehearsalRoomNum", stageRehearsalRoomNum);
        params.put("propRoomNum", stagePropRoomNum);
        params.put("costumeRoomNum", stageCostumeRoomNum);
        params.put("stageLights", stageLights);
        params.put("stereoEquipment", stereoEquipment);
        params.put("broadcastSystem", broadcastSystem);
        params.put("steeve", steeve);//吊杆
        params.put("musicStage", musicStage);//演奏台
        params.put("chorusPlatform", chorusPlatform);//合唱台
        params.put("orchestraPit", orchestraPit);//乐池
        params.put("acousticShroud", acousticShroud);
        params.put("bandPlatform", bandPlatform);
        params.put("curtainSystem", curtainSystem);
        params.put("specialEquipment", specialEquipment);
        params.put("projector", projector);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, Constant.URL_THEATER_EDIT_TECHNICALS, params, 68, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                try {
                    if (isSuccess) {
                        Toast.makeText(getContext(), getString(R.string.tip_theatre_info_commit_success), Toast.LENGTH_SHORT).show();
                        //{"id":"8a999cce5f51904d015f528af2ce0004"}
                        JSONObject jsonObject = new JSONObject(obj);
                        String theatreId = (String) jsonObject.get("id");
                        theatreInstance.setId(theatreId);
                        theatreInstance.setStageHeight(stageHeight);
                        theatreInstance.setStageWidth(stageWidth);
                        theatreInstance.setStageDepth(stageDepth);
                        theatreInstance.setCurtainHeight(curtainHeight);
                        theatreInstance.setCurtainWidth(curtainWidth);
                        theatreInstance.setDressingRoomNum(stageDressingNum);
                        theatreInstance.setRehearsalRoomNum(stageRehearsalRoomNum);
                        theatreInstance.setPropRoomNum(stagePropRoomNum);
                        theatreInstance.setCostumeRoomNum(stageCostumeRoomNum);
                        theatreInstance.setStageLights(stageLights);
                        theatreInstance.setStereoEquipment(stereoEquipment);
                        theatreInstance.setBroadcastSystem(broadcastSystem);
                        theatreInstance.setSteeve(steeve);
                        theatreInstance.setMusicStage(musicStage);
                        theatreInstance.setChorusPlatform(orchestraPit);
                        theatreInstance.setAcousticShroud(acousticShroud);
                        theatreInstance.setBandPlatform(bandPlatform);
                        theatreInstance.setCurtainSystem(curtainSystem);
                        theatreInstance.setSpecialEquipment(specialEquipment);
                        theatreInstance.setProjector(projector);
                        EventBus.getDefault().post(new TheatreInfoChangeEvent());
                    } else {
                        ResponseCodeCheck.showErrorMsg(code);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
