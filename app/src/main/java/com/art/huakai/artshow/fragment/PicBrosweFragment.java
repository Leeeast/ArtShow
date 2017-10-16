package com.art.huakai.artshow.fragment;


import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseFragment;
import com.art.huakai.artshow.widget.MultiTouchViewPager;
import com.art.huakai.artshow.widget.photoview.PhotoDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 我Fragment,没有登录状态下，底部推荐内容
 * Created by lidongliang on 2017/9/27.
 */
public class PicBrosweFragment extends BaseFragment {


    @BindView(R.id.view_pager)
    MultiTouchViewPager viewPager;
    @BindView(R.id.ll)
    LinearLayout ll;
    private ArrayList<String> paths = new ArrayList<String>();
    private boolean isSdPic = false;
    ArrayList<View> views = new ArrayList<View>();
    private int lastPosition = 0;


    public PicBrosweFragment() {
        // Required empty public constructor
    }

    public static PicBrosweFragment newInstance() {
        PicBrosweFragment fragment = new PicBrosweFragment();
        return fragment;
    }


//  初始化上述数据：
    @Override
    public void initData(@Nullable Bundle bundle) {

        paths.add("1");
        paths.add("1");
        paths.add("1");

        for(int i=0;i<paths.size();i++){
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(25,25);
            layoutParams.leftMargin=15;
            View viewone=new View(getContext());
            viewone.setLayoutParams(layoutParams);
            if(lastPosition==i){
                viewone.setBackgroundColor(0xffff0000);
            }else{
                viewone.setBackgroundColor(0xff0000ff);
            }
            ll.addView(viewone);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                if(lastPosition!=-1){
                    View view=ll.getChildAt(lastPosition);
                    view.setBackgroundColor(0xff0000ff);
                }
                View view=ll.getChildAt(position);
                view.setBackgroundColor(0xffff0000);
                lastPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(new DraweePagerAdapter());
        viewPager.setCurrentItem(0);

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_pic_browse;
    }


    @Override
    public void initView(View rootView) {

    }


    @Override
    public void setView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    public class DraweePagerAdapter extends PagerAdapter {

        @Override public int getCount() {
            return paths.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(final ViewGroup viewGroup, int position) {
            final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
//            controller.setUri(Uri.parse("res:///" + mDrawables[position]));
            controller.setUri(Uri.parse("res:///" +R.mipmap.account_type_n));
//            controller.setUri(Uri.parse("file://" + paths.get(position)));
            Log.e("mainactivity", "instantiateItem: path==" +paths.get(position));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    Log.e("mainactivity==", "onFinalImageSet: getWidth==" +imageInfo.getWidth()+"--getHeight=="+imageInfo.getHeight());
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
//                    photoDraweeView.update(500, 500);
                }
            });

//            photoDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            photoDraweeView.setController(controller.build());
            try {
                Log.e("mainactivity", "instantiateItem: 111111111111111");
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return photoDraweeView;
        }
    }


}
