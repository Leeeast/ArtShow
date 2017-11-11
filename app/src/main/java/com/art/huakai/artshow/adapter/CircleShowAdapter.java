package com.art.huakai.artshow.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.huakai.artshow.R;
import com.art.huakai.artshow.activity.EnrollDetailActivity;
import com.art.huakai.artshow.activity.KeywordSearchAllActivity;
import com.art.huakai.artshow.activity.KeywordSearchNewsActivity;
import com.art.huakai.artshow.activity.MainActivity;
import com.art.huakai.artshow.activity.NewsDetailActivity;
import com.art.huakai.artshow.activity.PersonalDetailMessageActivity;
import com.art.huakai.artshow.activity.TheatreDetailMessageActivity;
import com.art.huakai.artshow.activity.WorksDetailMessageActivity;
import com.art.huakai.artshow.base.BaseActivity;
import com.art.huakai.artshow.constant.JumpCode;
import com.art.huakai.artshow.decoration.LinearItemDecoration;
import com.art.huakai.artshow.entity.HomePageDetails;
import com.art.huakai.artshow.fragment.ShowCircleTestFragment;
import com.art.huakai.artshow.utils.AdvertJumpUtil;
import com.art.huakai.artshow.widget.ChinaShowImageView;
import com.art.huakai.artshow.widget.banner.BannerEntity;
import com.art.huakai.artshow.widget.banner.BannerView;
import com.art.huakai.artshow.widget.banner.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 17-10-7.
 */
public class CircleShowAdapter extends  RecyclerView.Adapter{

    private static final String TAG="CircleShowAdapter";

    private HomePageDetails homePageDetails;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private MainActivity mainActivity;
    private ShowCircleTestFragment showCircleTestFragment;
    int typeone=1;
    int typetwo=2;
    int typethree=3;
    int typefour=4;
    int typefive=5;
    int typesix=6;
    int typeseven=7;
    IndustryNewsAdapter industryNewsAdapter;
    CooperationOpportunitiesAdapter cooperationAdapter;
    ExcellentWorksAdapter excellentWorksAdapter;
    RecommendTheaterAdapter recommendTheaterAdapter;
    ProfessionalPersonAdapter professionalPersonAdapter;

    public CircleShowAdapter(Context context, HomePageDetails homePageDetails, MainActivity mainActivity, ShowCircleTestFragment showCircleTestFragment) {
        this.homePageDetails = homePageDetails;
        this.mContext = context;
        this.mainActivity=mainActivity;
        this.showCircleTestFragment=showCircleTestFragment;
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType=="+viewType );
            View view=null;
            if(viewType==1){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_banner_item, parent, false);
                TypeBannerViewHolder typeOneViewHolder = new TypeBannerViewHolder(view);
                return typeOneViewHolder;
            }else if(viewType==2){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_news_item, parent, false);
                TypeNewsViewHolder typeOneViewHolder = new TypeNewsViewHolder(view);
                return typeOneViewHolder;
            }else if(viewType==3){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_cooperate_item, parent, false);
                TypeCooperateViewHolder typeOneViewHolder = new TypeCooperateViewHolder(view);
                return typeOneViewHolder;
            }else if(viewType==4){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_work_item, parent, false);
                TypeWorkViewHolder typeOneViewHolder = new TypeWorkViewHolder(view);
                return typeOneViewHolder;
            }else if(viewType==5){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_theatre_item, parent, false);
                TypeTheatreViewHolder typeOneViewHolder = new TypeTheatreViewHolder(view);
                return typeOneViewHolder;
            }else if(viewType==6){
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_talent_item, parent, false);
                TypeTalentViewHolder typeOneViewHolder = new TypeTalentViewHolder(view);
                return typeOneViewHolder;
            }else {
                view = LayoutInflater.from(mContext).inflate(R.layout.show_circle_ad_item, parent, false);
                TypeAdViewHolder typeOneViewHolder = new TypeAdViewHolder(view);
                return typeOneViewHolder;
            }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       if(holder instanceof TypeBannerViewHolder ){
           TypeBannerViewHolder typeBannerViewHolder= (TypeBannerViewHolder) holder;
           List<BannerEntity> entities = new ArrayList<>();
           if (homePageDetails.getBanners() != null && homePageDetails.getBanners().size() > 0) {
               for (int i = 0; i < homePageDetails.getBanners().size(); i++) {
                   entities.add(new BannerEntity(0, homePageDetails.getBanners().get(i).getLogo(), "image"));
               }
           } else {
               entities.add(new BannerEntity(0, "http://logo", "image"));
           }
           typeBannerViewHolder.bannerView.setEntities(entities);
           typeBannerViewHolder.bannerView.setOnBannerClickListener(new OnBannerClickListener() {
               @Override
               public void onClick(int position) {
//                   Toast.makeText(getContext(), "position==" + position, Toast.LENGTH_SHORT).show();
                   if(homePageDetails.getBanners()!=null&&homePageDetails.getBanners().size()>position&&homePageDetails.getBanners().get(position)!=null){
                       AdvertJumpUtil.invoke((BaseActivity)mainActivity,mContext,homePageDetails.getBanners().get(position));
                   }
               }
           });
           if (homePageDetails.getBanners().size() > 1) {
               typeBannerViewHolder.bannerView.startAutoScroll();
           }
           typeBannerViewHolder.search.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(mContext, KeywordSearchAllActivity.class);
                   mContext.startActivity(intent);
               }
           });
       }else if(holder instanceof TypeNewsViewHolder ){
           if(industryNewsAdapter!=null)return;
           Log.d(TAG, "onBindViewHolder: 11111");
           TypeNewsViewHolder typeNewsViewHolder= (TypeNewsViewHolder) holder;
           if(homePageDetails.getNewses() != null && homePageDetails.getNewses().size() > 0){
               LinearLayoutManager industryNewsLayoutManager;
               industryNewsAdapter = new IndustryNewsAdapter(mContext, homePageDetails.getNewses());
               industryNewsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
               industryNewsAdapter.setOnItemClickListener(new IndustryNewsAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClickListener(int position) {
                       Bundle bundle = new Bundle();
                       bundle.putString(NewsDetailActivity.PARAMS_NEWS_ID, homePageDetails.getNewses().get(position).getId());
                       showCircleTestFragment.invokActivity(mContext, NewsDetailActivity.class, bundle, JumpCode.FLAG_REQ_NEWS_DETAIL);
                   }
               });
               typeNewsViewHolder.rcv_one.setNestedScrollingEnabled(false);
//              gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
               LinearItemDecoration itemDecoration = new LinearItemDecoration(10);
               typeNewsViewHolder.rcv_one.removeItemDecoration(itemDecoration);
               typeNewsViewHolder.rcv_one.addItemDecoration(itemDecoration);
               typeNewsViewHolder.rcv_one.setLayoutManager(industryNewsLayoutManager);
               typeNewsViewHolder.rcv_one.setAdapter(industryNewsAdapter);
               typeNewsViewHolder.tv_one_whole.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(mContext, KeywordSearchNewsActivity.class);
                       intent.putExtra("searchType", "news");
                       intent.putExtra("keyword", "*");
                       mContext.startActivity(intent);
                   }
               });

           }else{
               RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(0,0);
               typeNewsViewHolder.itemView.setLayoutParams(layoutParams);
           }

       }else if(holder instanceof TypeCooperateViewHolder){
           if(cooperationAdapter!=null)return;
           Log.d(TAG, "onBindViewHolder: 22222");
           TypeCooperateViewHolder typeCooperateViewHolder= (TypeCooperateViewHolder) holder;
           if (homePageDetails.getEnrolls() != null && homePageDetails.getEnrolls().size() > 0){
                   cooperationAdapter = new CooperationOpportunitiesAdapter(mContext, homePageDetails.getEnrolls());
               LinearLayoutManager cooperationLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
               typeCooperateViewHolder.rcv_two.setNestedScrollingEnabled(false);
               LinearItemDecoration cooperationItemDecoration = new LinearItemDecoration(10);
               cooperationAdapter.setOnItemClickListener(new CooperationOpportunitiesAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClickListener(int position) {
                       Log.e(TAG, "onItemClickListener: position==" + position);
                       if(homePageDetails.getNewses()!=null&&homePageDetails.getNewses().size()>position&&homePageDetails.getNewses().get(position)!=null){
                           Bundle bundle = new Bundle();
                           bundle.putSerializable("PARAMS_ENROLL_ID", homePageDetails.getNewses().get(position).getId());
                           showCircleTestFragment.invokActivity(mContext, EnrollDetailActivity.class, bundle, JumpCode.FLAG_REQ_ENROLL_DETAIL);
                       }
                   }
               });
               typeCooperateViewHolder.rcv_two.removeItemDecoration(cooperationItemDecoration);
               typeCooperateViewHolder.rcv_two.addItemDecoration(cooperationItemDecoration);
               typeCooperateViewHolder.rcv_two.setLayoutManager(cooperationLayoutManager);
               typeCooperateViewHolder.rcv_two.setAdapter(cooperationAdapter);
               typeCooperateViewHolder.tv_two_whole.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mainActivity.setWholeItemPosition(2);
                       mainActivity.setCheckId(R.id.rdobtn_collaborate);
                   }
               });
           }else{
               RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(0,0);
               typeCooperateViewHolder.itemView.setLayoutParams(layoutParams);
           }
       }else if(holder instanceof TypeWorkViewHolder){
           if(excellentWorksAdapter!=null)return;
           Log.d(TAG, "onBindViewHolder: 33333");
           TypeWorkViewHolder typeCooperateViewHolder= (TypeWorkViewHolder) holder;
           if (homePageDetails.getRepertorys() != null && homePageDetails.getRepertorys().size() > 0){
                excellentWorksAdapter = new ExcellentWorksAdapter(mContext, homePageDetails.getRepertorys());
               LinearLayoutManager worksLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
               typeCooperateViewHolder.rcv_three.setNestedScrollingEnabled(false);
               LinearItemDecoration worksItemDecorationThree = new LinearItemDecoration(10);
               excellentWorksAdapter.setOnItemClickListener(new ExcellentWorksAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClickListener(int position) {
                       Log.e(TAG, "onItemClickListener: position==" + position);
                       Bundle bundle = new Bundle();
                       bundle.putString(WorksDetailMessageActivity.PARAMS_ID, homePageDetails.getRepertorys().get(position).getId());
                       showCircleTestFragment.invokActivity(mContext, WorksDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PROJECT);
                   }
               });
               typeCooperateViewHolder.rcv_three.removeItemDecoration(worksItemDecorationThree);
               typeCooperateViewHolder.rcv_three.addItemDecoration(worksItemDecorationThree);
               typeCooperateViewHolder.rcv_three.setLayoutManager(worksLayoutManager);
               typeCooperateViewHolder.rcv_three.setAdapter(excellentWorksAdapter);
               typeCooperateViewHolder.tv_three_whole.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mainActivity.setWholeItemPosition(3);
                       mainActivity.setCheckId(R.id.rdobtn_discover);
                   }
               });
           }else{
               RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(0,0);
               typeCooperateViewHolder.itemView.setLayoutParams(layoutParams);
           }
       }else if(holder instanceof TypeTheatreViewHolder){
           if(recommendTheaterAdapter!=null){
               return;
           }
           Log.d(TAG, "onBindViewHolder: 44444");
           TypeTheatreViewHolder typeTheatreViewHolder= (TypeTheatreViewHolder) holder;
           if (homePageDetails.getTheaters() != null && homePageDetails.getTheaters().size() > 0){

               recommendTheaterAdapter = new RecommendTheaterAdapter(mContext, homePageDetails.getTheaters());
               LinearLayoutManager theatreLayoutManagerFour = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
               typeTheatreViewHolder.rcv_four.setNestedScrollingEnabled(false);
               LinearItemDecoration theatreItemDecoration = new LinearItemDecoration(10);
               recommendTheaterAdapter.setOnItemClickListener(new RecommendTheaterAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClickListener(int position) {
                       Log.e(TAG, "onItemClickListener: position==" + position);
                       Bundle bundle = new Bundle();
                       bundle.putString(TheatreDetailMessageActivity.PARAMS_ID, homePageDetails.getTheaters().get(position).getId());
                       bundle.putBoolean(TheatreDetailMessageActivity.PARAMS_ORG, false);
                       showCircleTestFragment.invokActivity(mContext, TheatreDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_THEATRE);
                   }
               });
               typeTheatreViewHolder.rcv_four.removeItemDecoration(theatreItemDecoration);
               typeTheatreViewHolder.rcv_four.addItemDecoration(theatreItemDecoration);
               typeTheatreViewHolder.rcv_four.setLayoutManager(theatreLayoutManagerFour);
               typeTheatreViewHolder.rcv_four.setAdapter(recommendTheaterAdapter);
               typeTheatreViewHolder.tv_four_whole.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mainActivity.setWholeItemPosition(4);
                       mainActivity.setCheckId(R.id.rdobtn_discover);
                   }
               });
           }else{
               RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(0,0);
               typeTheatreViewHolder.itemView.setLayoutParams(layoutParams);
           }
       }else if(holder instanceof TypeTalentViewHolder){
           if(professionalPersonAdapter!=null)return;
           Log.d(TAG, "onBindViewHolder: 55555");
           TypeTalentViewHolder typeTalentViewHolder= (TypeTalentViewHolder) holder;
           if (homePageDetails.getTalents() != null && homePageDetails.getTalents().size() > 0){
                professionalPersonAdapter = new ProfessionalPersonAdapter(mContext, homePageDetails.getTalents());
               LinearLayoutManager professionalLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
               typeTalentViewHolder.rcv_five.setNestedScrollingEnabled(false);
               LinearItemDecoration professionalItemDecoration = new LinearItemDecoration(10);
               professionalPersonAdapter.setOnItemClickListener(new ProfessionalPersonAdapter.OnItemClickListener() {
                   @Override
                   public void onItemClickListener(int position) {
                       Log.e(TAG, "onItemClickListener: position" + position);
                       Bundle bundle = new Bundle();
                       bundle.putString(PersonalDetailMessageActivity.PARAMS_ID, homePageDetails.getTalents().get(position).getId());
                       showCircleTestFragment.invokActivity(mContext, PersonalDetailMessageActivity.class, bundle, JumpCode.FLAG_REQ_DETAIL_PERSONAL);
                   }
               });
               typeTalentViewHolder.rcv_five.removeItemDecoration(professionalItemDecoration);
               typeTalentViewHolder.rcv_five.addItemDecoration(professionalItemDecoration);
               typeTalentViewHolder.rcv_five.setLayoutManager(professionalLayoutManager);
               typeTalentViewHolder.rcv_five.setAdapter(professionalPersonAdapter);
               typeTalentViewHolder.tv_five_whole.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mainActivity.setWholeItemPosition(5);
                       mainActivity.setCheckId(R.id.rdobtn_discover);
                   }
               });
           }else{
               RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(0,0);
               typeTalentViewHolder.itemView.setLayoutParams(layoutParams);
           }
       }else if(holder instanceof TypeAdViewHolder){
           TypeAdViewHolder typeAdViewHolder= (TypeAdViewHolder) holder;
           if(homePageDetails.getAdvert()!=null&&!TextUtils.isEmpty(homePageDetails.getAdvert().getLogo())){
               typeAdViewHolder.csiv.setImageURI(Uri.parse(homePageDetails.getAdvert().getLogo()));
           }
           typeAdViewHolder.csiv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (homePageDetails != null && homePageDetails.getAdvert() != null) {
                       AdvertJumpUtil.invoke(mainActivity, mContext, homePageDetails.getAdvert());
                   }
               }
           });
       }

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return typeone;
        }else if(position==1){
            return typetwo;
        }else if(position==2){
            return typethree;
        }else if(position==3){
            return typefour;
        }else if(position==4){
            return typefive;
        }else if(position==5){
            return typesix;
        }else{
            return typeseven;
        }
    }

    public class TypeBannerViewHolder extends RecyclerView.ViewHolder {
        BannerView bannerView;
        LinearLayout search;
        public TypeBannerViewHolder(View itemView) {
            super(itemView);
            bannerView= (BannerView) itemView.findViewById(R.id.banner);
            search= (LinearLayout) itemView.findViewById(R.id.search);
        }
    }

    public class TypeNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_one_whole;
        RecyclerView rcv_one;
        public TypeNewsViewHolder(View itemView) {
            super(itemView);
            tv_one_whole= (TextView) itemView.findViewById(R.id.tv_one_whole);
            rcv_one= (RecyclerView) itemView.findViewById(R.id.rcv_one);
        }
    }

    public class TypeCooperateViewHolder extends RecyclerView.ViewHolder {
        TextView tv_two_whole;
        RecyclerView rcv_two;
        public TypeCooperateViewHolder(View itemView) {
            super(itemView);
            tv_two_whole= (TextView) itemView.findViewById(R.id.tv_two_whole);
            rcv_two= (RecyclerView) itemView.findViewById(R.id.rcv_two);
        }
    }

    public class TypeWorkViewHolder extends RecyclerView.ViewHolder {
        TextView tv_three_whole;
        RecyclerView rcv_three;
        public TypeWorkViewHolder(View itemView) {
            super(itemView);
            tv_three_whole= (TextView) itemView.findViewById(R.id.tv_three_whole);
            rcv_three= (RecyclerView) itemView.findViewById(R.id.rcv_three);
        }
    }

    public class TypeTheatreViewHolder extends RecyclerView.ViewHolder {
        TextView tv_four_whole;
        RecyclerView rcv_four;
        public TypeTheatreViewHolder(View itemView) {
            super(itemView);
            tv_four_whole= (TextView) itemView.findViewById(R.id.tv_four_whole);
            rcv_four= (RecyclerView) itemView.findViewById(R.id.rcv_four);
        }
    }

    public class TypeTalentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_five_whole;
        RecyclerView rcv_five;
        public TypeTalentViewHolder(View itemView) {
            super(itemView);
            tv_five_whole= (TextView) itemView.findViewById(R.id.tv_five_whole);
            rcv_five= (RecyclerView) itemView.findViewById(R.id.rcv_five);
        }
    }


    public class TypeAdViewHolder extends RecyclerView.ViewHolder {
        ChinaShowImageView csiv;
        public TypeAdViewHolder(View itemView) {
            super(itemView);
            csiv= (ChinaShowImageView) itemView.findViewById(R.id.csiv);

        }
    }




    public interface  OnItemClickListener{
        void onItemClickListener(int position);
    }

//    public void add(ArrayList<Theatre> theatres){
//        int lastIndex = this.list.size();
//        if (this.list.addAll(theatres)) {
//            notifyItemRangeInserted(lastIndex, list.size());
//        }
//    }

//    public void notifyDataSetChange(ArrayList<Theatre> theatres){
//        list.clear();
//        if(this.list.addAll(theatres)){
//            notifyDataSetChanged();
//        }
//    }


}
