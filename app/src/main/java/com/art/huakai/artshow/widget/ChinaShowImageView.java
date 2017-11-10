package com.art.huakai.artshow.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;


import com.art.huakai.artshow.R;
import com.art.huakai.artshow.config.AnimConfig;
import com.art.huakai.artshow.utils.PxToDpUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


/**
 * Created by lining on 16-6-8.
 */
public class ChinaShowImageView extends SimpleDraweeView {
    private static final String TAG = "GitvImageView";

    private PipelineDraweeControllerBuilder mPipelineDraweeControllerBuilder;
    private ImageLoadListener mImageLoadListener;
    private String mUriString;
    private int height;
    public interface ImageLoadListener {
        void onSucc();

        void onFail(Throwable throwable);
    }

    public ChinaShowImageView(Context context) {
        this(context, null);
    }

    public ChinaShowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChinaShowImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChinaShowImageView);
        int loadingRef = typedArray.getResourceId(R.styleable.ChinaShowImageView_loadingRes, -1);
        setFadeDuration(AnimConfig.DEFAULT_ANIM_DURATION);
        if(loadingRef != -1) {
            setPlaceholderImage(getResources().getDrawable(loadingRef), ScalingUtils.ScaleType.FIT_XY);
            setFailureImage(getResources().getDrawable(loadingRef), ScalingUtils.ScaleType.FIT_XY);
        } else {
            setPlaceholderImage(getResources().getDrawable(R.drawable.dynamic_item_loading), ScalingUtils.ScaleType.FIT_XY);
            setFailureImage(getResources().getDrawable(R.drawable.dynamic_item_loading), ScalingUtils.ScaleType.FIT_XY);
        }
        initDefaultControllerListener();
        typedArray.recycle();
    }

//  设置图片加载api
    @Override
    public void setImageURI(Uri uri) {
        if (uri == null) return;
        if (mUriString != null && mUriString.equals(uri.toString())) return;
        mUriString = uri.toString();
        if (mPipelineDraweeControllerBuilder != null) {
            if(mUriString.endsWith(".gif")){
                setController(mPipelineDraweeControllerBuilder.setUri(uri).setAutoPlayAnimations(true).build());
            } else {
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                        .setResizeOptions(new ResizeOptions(100,100))
//                        .build();
//                setController(mPipelineDraweeControllerBuilder.setImageRequest(request).build());
                setController(mPipelineDraweeControllerBuilder.setUri(uri).build());

            }

        } else {
            super.setImageURI(uri);
        }
    }


    public void setSpecificSizeImageUrl(Uri uri,int width,int height){
        if (uri == null) return;
        if (mUriString != null && mUriString.equals(uri.toString())) return;
        mUriString = uri.toString();
        if (mPipelineDraweeControllerBuilder != null) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setResizeOptions(new ResizeOptions(width,height))
                        .build();
                setController(mPipelineDraweeControllerBuilder.setImageRequest(request).build());
                setController(mPipelineDraweeControllerBuilder.setUri(uri).build());
        } else {
            super.setImageURI(uri);
        }
    }





    public void setImageLoadListener(ImageLoadListener imageLoadListener) {
        this.mImageLoadListener = imageLoadListener;
    }

    public void setFadeDuration(int durationMs) {
        getHierarchy().setFadeDuration(durationMs);
    }

    public void setPlaceholderImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        getHierarchy().setPlaceholderImage(drawable, scaleType);
    }

    public void setFailureImage(Drawable drawable, ScalingUtils.ScaleType scaleType) {
        getHierarchy().setFailureImage(drawable, scaleType);
    }

    private void initDefaultControllerListener() {
        mPipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (mImageLoadListener != null) mImageLoadListener.onSucc();
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        if (mImageLoadListener != null) mImageLoadListener.onFail(throwable);
                        mUriString = null;
                    }
                });
    }

//  可以返回图片尺寸大小
    public  void setImage(Uri uri, final Context context, final ChinaShowImageView view){
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                Log.e("getHeight===", "" + imageInfo.getHeight());
                Log.e("getWidth===", "" + imageInfo.getWidth());
                final int width = PxToDpUtils.getNumber(imageInfo.getWidth(), context, PxToDpUtils.width);
                height = PxToDpUtils.getNumber(imageInfo.getHeight(), context, PxToDpUtils.height);

                if(height>PxToDpUtils.getNumber(96,context,PxToDpUtils.height)){
                    height=PxToDpUtils.getNumber(96,context,PxToDpUtils.height);
                }
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
                lp.setMargins(0, 10, 0, 0);
                view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
                view.setLayoutParams(lp);
            }
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

            }
            @Override
            public void onFailure(String id, Throwable throwable) {
                Log.e("logo==","onFailure");
            }
        };

        DraweeController controller =
                Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(uri).build();
        view.setController(controller);
    }

    public  void setImage(Uri uri, final Context context, final ImgScaleResultListener imgScaleResultListener,final ChinaShowImageView view){
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                Log.e("logogetHeight===", "" + imageInfo.getHeight());
                Log.e("logogetWidth===", "" + imageInfo.getWidth());

                imgScaleResultListener.imgSize(imageInfo.getWidth(),imageInfo.getHeight());

            }
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

            }
            @Override
            public void onFailure(String id, Throwable throwable) {
                Log.e("logo==","onFailure");
                imgScaleResultListener.imgSize(0,0);
            }
        };

        DraweeController controller =
                Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(uri).build();
        view.setController(controller);
    }
    public interface ImgScaleResultListener{
        void imgSize(int width, int height);
    }

}
