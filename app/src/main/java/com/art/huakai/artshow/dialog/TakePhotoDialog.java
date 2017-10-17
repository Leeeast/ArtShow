package com.art.huakai.artshow.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseDialogFragment;
import com.art.huakai.artshow.config.PhotoConfig;
import com.art.huakai.artshow.fragment.PerfectInfoFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 通用类型选择框
 * Created by lidongliang on 2017/10/1.
 */

public class TakePhotoDialog extends BaseDialogFragment implements View.OnClickListener {
    private CallBack mCallBack;

    public static TakePhotoDialog newInstence() {
        TakePhotoDialog typeConfirmDialog = new TakePhotoDialog();
        return typeConfirmDialog;
    }


    public interface CallBack {
        void onTakePhoto(DialogFragment dialogFragment);

        void onAlbuml(DialogFragment dialogFragment);
    }

    public void setOnCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        if (bundle != null) {
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_take_photo;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        rootView.findViewById(R.id.btn_photo_album).setOnClickListener(this);
        rootView.findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (mCallBack != null) {
                    mCallBack.onTakePhoto(this);
                }
                break;
            case R.id.btn_photo_album:
                if (mCallBack != null) {
                    mCallBack.onAlbuml(this);
                }
                break;
            case R.id.btn_cancle:
                this.dismiss();
                break;
        }
    }

    @Override
    public void setView() {

    }

    /**
     * Fragment中拍照
     *
     * @param fragment
     */
    public static void takePhoto(Fragment fragment, List<LocalMedia> selectList) {
        // 单独拍照
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(PhotoConfig.MAX_SELECT_NUM)
                .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)//单选&多选
                .previewImage(false)//是否显示预览
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .compressGrade(Luban.THIRD_GEAR)
                .isCamera(false)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                .glideOverride(160, 160)
                .withAspectRatio(0, 0)
                .hideBottomControls(true)
                .isGif(false)//是否现实Gif图片
                .freeStyleCropEnabled(true)
                .circleDimmedLayer(false)
                .showCropFrame(true)
                .showCropGrid(true)
                .openClickSound(PhotoConfig.VOICE)
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * Fragment中相册选择
     *
     * @param fragment
     */
    public static void photoAlbum(Fragment fragment, List<LocalMedia> selectList) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)
                .maxSelectNum(PhotoConfig.MAX_SELECT_NUM)
                .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)//单选&多选
                .previewImage(false)//是否显示预览
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .compressGrade(Luban.THIRD_GEAR)
                .isCamera(false)
                .enableCrop(true)
                .compress(true)
                .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                .glideOverride(160, 160)
                .withAspectRatio(0, 0)
                .hideBottomControls(true)
                .isGif(false)//是否现实Gif图片
                .freeStyleCropEnabled(true)
                .circleDimmedLayer(false)
                .showCropFrame(true)
                .showCropGrid(true)
                .openClickSound(PhotoConfig.VOICE)
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
