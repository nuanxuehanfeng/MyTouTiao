package com.bawei.wangxueshi.mytoutiao.fragment;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bawei.wangxueshi.mytoutiao.IApplication;
import com.bawei.wangxueshi.mytoutiao.MainActivity;
import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.activity.city.ChannelActivity;
import com.bawei.wangxueshi.mytoutiao.adapter.IndextAdapter;
import com.bawei.wangxueshi.mytoutiao.base.BaseFragment;
import com.bawei.wangxueshi.mytoutiao.bean.TabTitleBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo-pc on 2017/5/9.  弘扬
 */

public class TitleFragmet extends BaseFragment {
     //private List<HttpBean.DataBeanX.DataBean> userChannelList;
    private View view;
    private Gson gson;
    private List<TabTitleBean.DataBeanX.DataBean> userData=new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView ivjia;
    private IndextAdapter adapter;
    private DbManager dbManager;
    private IApplication application;
    public boolean isOne=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.titlefragmet, container, false);
        ivjia = (ImageView) view.findViewById(R.id.titlefragment_iv);
        DbManager.DaoConfig daoConfig = IApplication.getDaoConfig();
        dbManager = x.getDb(daoConfig);
        //获取控件
        tabLayout = (TabLayout) view.findViewById(R.id.table);
        viewPager = (ViewPager)  view.findViewById(R.id.viewpager);
        //订阅
        EventBus.getDefault().register(this);
        ivjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调转 tab选项；
              startActivity(new Intent(getActivity(), ChannelActivity.class));
            }
        });
        gson = new Gson();

//        //获取适配器
        adapter = new IndextAdapter(getChildFragmentManager(),userData);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
//        try {
//            dbManager.delete(TabTitleBean.DataBeanX.DataBean.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        boolean shuJuKu = getShuJuKu();

        //数据库没有内容 时，从网络获取
        if(shuJuKu==false){
            getTitle();
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
        }
        else{
            //有内容 就不管了
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
            System.out.println("shuJuKu = " + shuJuKu);
        }
        //绑定aa
        tabLayout.setupWithViewPager(viewPager);
//字体颜色
        tabLayout.setTabTextColors(getResources().getColor(R.color.hui), getResources().getColor(R.color.colorAccent));
//指示器颜色
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.hui));
//模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//监听事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //从网络获取数据，并添加到数据库中
    public void getTitle() {
        String pathTitle="http://ic.snssdk.com/article/category/get/v2/?iid=2939228904";
        RequestParams entity=new RequestParams(pathTitle);
        entity.setConnectTimeout(5000);
        entity.setReadTimeout(5000);
        x.http().get(entity,new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
               TabTitleBean tabTitle = gson.fromJson(result, TabTitleBean.class);
                List<TabTitleBean.DataBeanX.DataBean> yuanData = tabTitle.getData().getData();
                //1  的顺序
                int k=0;
                //0 的顺序
                int j=0;
                for(int i=0;i<yuanData.size();i++){
                    if(yuanData.get(i).getDefault_add()==1){
                        k++;
                        yuanData.get(i).setOrderId(k);
                    }
                    else{
                        j++;
                        yuanData.get(i).setOrderId(j);
                    }
                }
                try {
                    //设置完成后 添加数据库
                    dbManager.save(yuanData);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                try {
                    //从数据库查找
                    List<TabTitleBean.DataBeanX.DataBean> all = dbManager.findAll(TabTitleBean.DataBeanX.DataBean.class);
                    for(TabTitleBean.DataBeanX.DataBean bean:all){
                        if(bean.getDefault_add()==1){
                            userData.add(bean);
                        }
                    }
//                    adapter = new IndextAdapter(getChildFragmentManager(),userData);
//                   viewPager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (DbException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
    //、随着动画、更新tablayout中的数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainActivityEvent(String a) {
        userData.clear();
        try {
            List<TabTitleBean.DataBeanX.DataBean> all = dbManager.findAll(TabTitleBean.DataBeanX.DataBean.class);
            for(TabTitleBean.DataBeanX.DataBean bean:all){
                if(bean.getDefault_add()==1){
                userData.add(bean); }
            }
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }


    }
//从数据库获取内容
    public boolean getShuJuKu() {
        try {
            List<TabTitleBean.DataBeanX.DataBean> all = dbManager.findAll(TabTitleBean.DataBeanX.DataBean.class);
            if(all==null||all.size()==0){
                return  false;
            }
            else{
                for(TabTitleBean.DataBeanX.DataBean bean:all){
                    if(bean.getDefault_add()==1){
                        userData.add(bean);
                    }
                }
                System.out.println("userData = " + userData.size());


                adapter.notifyDataSetChanged();

                return  true;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return  false;
    }
    /**
     * 切换夜间模式
     * @param white
     */
    public void changeMode(boolean white) {
        MainActivity activity = (MainActivity) getActivity();
     //   switchTextViewColor((ViewGroup)activity.getWindow().getDecorView(),white);

        //true 为黑天
        if(white){
            setNightMode();
        }else {
            setWhiteMode();
        }
    }
    // 改变tablayout 字颜色 下标颜色
    private void setWhiteMode(){

        tabLayout.setBackgroundColor(getResources().getColor(R.color.backgroundColor));

        //字体颜色
        tabLayout.setTabTextColors(getResources().getColor(R.color.hui), getResources().getColor(R.color.colorAccent));
//指示器颜色
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.hui));
    }
    private void setNightMode(){
        tabLayout.setBackgroundColor(getResources().getColor(R.color.color_window));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.title_color));
        tabLayout.setTabTextColors(getResources().getColor(R.color.iblack),getResources().getColor(R.color.title_color));
    }
    /**
     * 遍历出所有的textView设置对应的颜色
     * @param view
     */
    public void switchTextViewColor(ViewGroup view,boolean white) {

//        getChildCount 获取ViewGroup下view的个数
//        view.getChildAt(i) 根据下标获取对应的子view
        for (int i = 0; i < view.getChildCount(); i++) {
            if (view.getChildAt(i) instanceof ViewGroup) {
                switchTextViewColor((ViewGroup) view.getChildAt(i),white);
            } else if (view.getChildAt(i) instanceof TextView) {
                //设置颜色
                int resouseId ;
                TextView textView = (TextView) view.getChildAt(i);
                if(white){
                    resouseId =R.color.textColor_night;
                }else {
                    resouseId = R.color.textColor;
                }
                //textView.setTextColor(resouseId);
                textView.setTextColor(getResources().getColor(resouseId));
            }
        }
    //    adapter.notifyDataSetChanged();

    }


}
