package com.art.huakai.artshow.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lining on 2017/10/10.
 */
public class StaggeredGridLayoutItemDecoration extends RecyclerView.ItemDecoration {


    private int space;

    public StaggeredGridLayoutItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left=space/2;
//        outRect.right=space/2;
//        outRect.bottom=space;
//        if(parent.getChildAdapterPosition(view)==0){
//            outRect.top=space;
//        }
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space;
    }
}
