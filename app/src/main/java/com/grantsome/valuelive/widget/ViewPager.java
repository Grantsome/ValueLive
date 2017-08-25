package com.grantsome.valuelive.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.grantsome.valuelive.R;
import com.grantsome.valuelive.utils.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grantsome on 2017/8/17.
 * 轮播图
 */

public class ViewPager extends FrameLayout implements View.OnClickListener{

    private List<String> urlList;

    private Context context;

    private List<View> viewList;

    private android.support.v4.view.ViewPager viewPager;

    private int currentItem;

    private boolean isAutoPlay;

    private MyPagerAdapter adapter;

    private Handler handler = new Handler();

    private OnItemClickListener ItemClickListener;

    private String url;

    public ViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        this.context = context;
        initView();
        initData();
        initUI();
    }

    public ViewPager(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }

    public ViewPager(Context context){
        this(context,null);
    }

    private void initView(){
        viewList = new ArrayList<View>();
        urlList = new ArrayList<>();
    }

    private void initData(){
        urlList.add(Common.VIEW_PAGER_ONE);
        urlList.add(Common.VIEW_PAGER_TWO);
        urlList.add(Common.VIEW_PAGER_THREE);
        urlList.add(Common.VIEW_PAGER_FOUR);
        urlList.add(Common.VIEW_PAGER_Five);
        urlList.add(Common.VIEW_PAGER_Six);
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    private void reset(){
        //初始化的时候clear
        viewList.clear();
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(context).inflate(R.layout.view_pager,this,true);
        viewPager = (android.support.v4.view.ViewPager) view.findViewById(R.id.view_pager);
        int len = urlList.size();
        for(int i=0;i<=len+1;i++){
           View v = LayoutInflater.from(context).inflate(R.layout.item_image,null);
           ImageView imageView = (ImageView) v.findViewById(R.id.image_view);
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           if(i == 0){
               Glide.with(context).load(urlList.get(len-1)).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
           } else if(i ==len+1) {
               Glide.with(context).load(urlList.get(0)).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
           } else {
               Glide.with(context).load(urlList.get(i-1)).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
           }
            v.setOnClickListener(this);
            viewList.add(v);
        }
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        currentItem = 1;
        startPlay();
    }

    private void startPlay(){
        isAutoPlay = true;
        //定时器,设置为3秒的延迟
        handler.postDelayed(task ,3000);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isAutoPlay){
                currentItem = currentItem%(urlList.size()+1)+1;
                if(currentItem == 1){
                    viewPager.setCurrentItem(currentItem,false);
                    handler.post(task);
                }else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task,4000);
                }
            } else {
                handler.postDelayed(task,4000);
            }
        }
    };

    @Override
    public void onClick(View view) {
        if(ItemClickListener != null){
            //由于设置的currentItem的原因,所以这里必须-1才是可以
              adapter.notifyDataSetChanged();
              int i = viewPager.getCurrentItem()-1;
              Log.d("viewpager", "onClick: index"+i);
              if( i != -1){
                 url = urlList.get(viewPager.getCurrentItem()-1);
              }else {
                 url = urlList.get(0);
              }
              ItemClickListener.click(view,url);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup,int i){
            viewGroup.addView(viewList.get(i));
            return viewList.get(i);
        }

        @Override
        public void destroyItem(ViewGroup viewGroup,int i,Object object){
            viewGroup.removeView((View) object);
        }

    }

    class MyOnPageChangeListener implements android.support.v4.view.ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
           switch (state){
               //手指滑动的时候
               //正在滑动
               case 1:
                   isAutoPlay = false;
                   break;
               //默认滑动完毕
               case 2:
                   isAutoPlay = true;
                   break;
               //什么都没有
               case 0:
                   if(viewPager.getCurrentItem() ==0 ){
                       viewPager.setCurrentItem(urlList.size(),false);
                   } else if(viewPager.getCurrentItem() == urlList.size()+1){
                       viewPager.setCurrentItem(1,false);
                   }
                   currentItem =viewPager.getCurrentItem();
                   isAutoPlay = true;
                   break;
           }
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.ItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void click(View v,String url);
    }
}
