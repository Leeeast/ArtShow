package com.art.huakai.artshow.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.adapter.StaggeredGridImageAdapter;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.config.PhotoConfig;
import com.art.huakai.artshow.constant.Constant;
import com.art.huakai.artshow.decoration.StaggeredGridLayoutItemDecoration;
import com.art.huakai.artshow.dialog.ShowProgressDialog;
import com.art.huakai.artshow.entity.LocalUserInfo;
import com.art.huakai.artshow.entity.PicturesBean;
import com.art.huakai.artshow.entity.ProjectDetailInfo;
import com.art.huakai.artshow.entity.TalentDetailInfo;
import com.art.huakai.artshow.entity.TheatreDetailInfo;
import com.art.huakai.artshow.eventbus.ProjectInfoChangeEvent;
import com.art.huakai.artshow.eventbus.TalentInfoChangeEvent;
import com.art.huakai.artshow.eventbus.TheatreInfoChangeEvent;
import com.art.huakai.artshow.utils.LogUtil;
import com.art.huakai.artshow.utils.RequestUtil;
import com.art.huakai.artshow.utils.ResponseCodeCheck;
import com.art.huakai.artshow.utils.SignUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class PhotoUploadFragment extends BaseFragment {
    private static final String PARAMS_TYPE = "PARAMS_TYPE";
    public static final String TYPE_TALENT = "TYPE_TALENT";
    public static final String TYPE_THEATRE = "TYPE_THEATRE";
    public static final String TYPE_PROJECT = "TYPE_PROJECT";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubtitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ShowProgressDialog showProgressDialog;
    private String mDescription;
    private String mUploadType;
    private List<LocalMedia> selectList = new ArrayList<>();
    private JSONArray mJSONArray = new JSONArray();
    private StaggeredGridImageAdapter adapter;
    private int maxPicCount;
    private int index = 0;
    private boolean isUploadSuc = false;
    private String mCommitPicUrl;
    private String typeId;

    public PhotoUploadFragment() {
    }

    public static PhotoUploadFragment newInstance(String type) {
        PhotoUploadFragment fragment = new PhotoUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
            mUploadType = bundle.getString(PARAMS_TYPE, TYPE_TALENT);
        }
        showProgressDialog = new ShowProgressDialog(getContext());
        mDescription = TalentDetailInfo.getInstance().getDescription();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_photo_upload;
    }

    @Override
    public void initView(View rootView) {
        String title = "";
        switch (mUploadType) {
            case TYPE_THEATRE:
                title = getString(R.string.theatre_pic);
                mCommitPicUrl = Constant.URL_THEATER_EDIT_PICTURES;
                typeId = TheatreDetailInfo.getInstance().getId();
                break;
            case TYPE_TALENT:
                title = getString(R.string.resume_photo);
                mCommitPicUrl = Constant.URL_TALENT_EDIT_PICTURES;
                typeId = TalentDetailInfo.getInstance().getId();
                break;
            case TYPE_PROJECT:
                title = getString(R.string.project_photo);
                mCommitPicUrl = Constant.URL_REPERTORY_EDIT_PICTURES;
                typeId = ProjectDetailInfo.getInstance().getId();
                break;
        }
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        tvSubtitle.setVisibility(View.VISIBLE);

        StaggeredGridLayoutManager sGLayManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sGLayManager);
        recyclerView.addItemDecoration(new StaggeredGridLayoutItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.DIMEN_15PX)));
        adapter = new StaggeredGridImageAdapter(getContext(), onAddPicClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setView() {
        adapter.setOnItemClickListener(new StaggeredGridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(PhotoUploadFragment.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PhotoUploadFragment.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PhotoUploadFragment.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
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
        for (int i = 0; i < mJSONArray.length(); i++) {
            mJSONArray.remove(i);
        }
        maxPicCount = selectList.size();
        index = 0;
        uploadMultiPhoto();
    }

    /**
     * 上传图片
     */
    public void uploadMultiPhoto() {
        if (isUploadSuc) {
            showToast(getString(R.string.pic_loading_success));
            return;
        }
        String path = "";
        String uploadText = String.format(getString(R.string.loading_upload), index + 1, maxPicCount);
        if (selectList.size() > 0) {
            LocalMedia localMedia = selectList.get(index);
            int mimeType = localMedia.getMimeType();
            if (localMedia.isCut() && !localMedia.isCompressed()) {
                // 裁剪过
                path = localMedia.getCutPath();
            } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = localMedia.getCompressPath();
            } else {
                // 原图
                path = localMedia.getPath();
            }
        } else {
            showToast(getString(R.string.tip_theatre_select_pic));
            return;
        }
        LogUtil.i(TAG, "path = " + path);
        if (!showProgressDialog.isShowing()) {
            showProgressDialog.show();
        }
        showProgressDialog.setLoadingText(uploadText);
        RequestUtil.uploadLoadFile(Constant.URL_UPLOAD_FILE, path, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (isSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String picUrl = jsonObject.getString("url");
                        mJSONArray.put(picUrl);
                        if (selectList.size() > ++index) {
                            uploadMultiPhoto();
                        } else {
                            showProgressDialog.setLoadingText(getString(R.string.loading));
                            commitPic(mJSONArray.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (showProgressDialog.isShowing()) {
                            showProgressDialog.dismiss();
                        }
                    }
                } else {
                    ResponseCodeCheck.showErrorMsg(code);
                    if (showProgressDialog.isShowing()) {
                        showProgressDialog.dismiss();
                    }
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

    /**
     * 提交图片
     */
    public void commitPic(String picJson) {
        Map<String, String> params = new TreeMap<>();
        if (!TextUtils.isEmpty(typeId)) {
            params.put("id", typeId);
        }
        params.put("userId", LocalUserInfo.getInstance().getId());
        params.put("accessToken", LocalUserInfo.getInstance().getAccessToken());
        params.put("pictures", picJson);
        String sign = SignUtil.getSign(params);
        params.put("sign", sign);
        LogUtil.i(TAG, "params = " + params);
        RequestUtil.request(true, mCommitPicUrl, params, 66, new RequestUtil.RequestListener() {
            @Override
            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
                LogUtil.i(TAG, obj);
                if (showProgressDialog.isShowing()) {
                    showProgressDialog.dismiss();
                }
                isUploadSuc = true;
                showToast(getString(R.string.tip_theatre_pic_upload));
                try {
                    //{"id":"8a999cce5f5da93b015f5f338d0a0020"}
                    JSONObject jsonObject = new JSONObject(obj);
                    String typeId = jsonObject.getString("id");
                    switch (mUploadType) {
                        case TYPE_THEATRE:
                            TheatreDetailInfo.getInstance().setId(typeId);
                            ArrayList<PicturesBean> picturesTheatre = new ArrayList<>();
                            for (int i = 0; i < mJSONArray.length(); i++) {
                                PicturesBean picturesBean = new PicturesBean();
                                picturesBean.setLargeUrl(String.valueOf(mJSONArray.get(i)));
                                picturesTheatre.add(picturesBean);
                            }
                            TheatreDetailInfo.getInstance().setPictures(picturesTheatre);
                            EventBus.getDefault().post(new TheatreInfoChangeEvent());
                            break;
                        case TYPE_TALENT:
                            TalentDetailInfo.getInstance().setId(typeId);
                            ArrayList<PicturesBean> picturesTalent = new ArrayList<>();
                            for (int i = 0; i < mJSONArray.length(); i++) {
                                PicturesBean picturesBean = new PicturesBean();
                                picturesBean.setLargeUrl(String.valueOf(mJSONArray.get(i)));
                                picturesTalent.add(picturesBean);
                            }
                            TalentDetailInfo.getInstance().setPictures(picturesTalent);
                            EventBus.getDefault().post(new TalentInfoChangeEvent());
                            break;
                        case TYPE_PROJECT:
                            ProjectDetailInfo.getInstance().setId(typeId);
                            ArrayList<PicturesBean> picturesProject = new ArrayList<>();
                            for (int i = 0; i < mJSONArray.length(); i++) {
                                PicturesBean picturesBean = new PicturesBean();
                                picturesBean.setLargeUrl(String.valueOf(mJSONArray.get(i)));
                                picturesProject.add(picturesBean);
                            }
                            ProjectDetailInfo.getInstance().setPictures(picturesProject);
                            EventBus.getDefault().post(new ProjectInfoChangeEvent());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    break;
            }
        }
    }

    private StaggeredGridImageAdapter.onAddPicClickListener onAddPicClickListener = new StaggeredGridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(PhotoUploadFragment.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(PhotoConfig.MAX_SELECT_NUM)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.videoMaxSecond(15)
                    //.videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };
}
