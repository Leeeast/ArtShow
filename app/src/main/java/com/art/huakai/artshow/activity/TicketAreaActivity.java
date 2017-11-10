package com.art.huakai.artshow.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.photoview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketAreaActivity extends BaseActivity {

    public static final String PARAMS_URL = "PARAMS_URL";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pdv_ticket_photo)
    PhotoDraweeView photoDVTicketPhoto;
    private String mPriceDiagram;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.myStatusBar(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_ticket_area;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mPriceDiagram = extras.getString(PARAMS_URL);
        }
    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.theatre_seat_pic);
    }

    @Override
    public void setView() {
        showImagePic(mPriceDiagram);
    }

    public void showImagePic(String imagePath) {


        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(imagePath);
        controller.setOldController(photoDVTicketPhoto.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                Log.e(TAG, "onFinalImageSet: getWidth==" + imageInfo.getWidth() + "--getHeight==" + imageInfo.getHeight());
                photoDVTicketPhoto.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDVTicketPhoto.setController(controller.build());
    }

    /**
     * 返回
     */
    @OnClick(R.id.lly_back)
    public void setlLyBack() {
        finish();
    }
}
