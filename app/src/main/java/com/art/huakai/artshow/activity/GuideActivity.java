package com.art.huakai.artshow.activity;

import android.content.Intent;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.entity.DataProvider;
import com.art.huakai.artshow.utils.statusBar.ImmerseStatusBar;
import com.art.huakai.artshow.widget.SimpleGuideBanner;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.DepthTransformer;

public class GuideActivity extends BaseActivity {

    private SimpleGuideBanner sgBanner;

    @Override
    public void immerseStatusBar() {
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_guide;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        sgBanner = (SimpleGuideBanner) findViewById(R.id.sgbanner);
    }

    @Override
    public void setView() {
        sgBanner.setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(DepthTransformer.class)
                .barPadding(0, 10, 0, 10)
                .setSource(DataProvider.geUsertGuides())
                .startScroll();

        sgBanner.setOnJumpClickL(new SimpleGuideBanner.OnJumpClickL() {
            @Override
            public void onJumpClick() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
