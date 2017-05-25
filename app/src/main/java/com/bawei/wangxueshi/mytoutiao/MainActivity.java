package com.bawei.wangxueshi.mytoutiao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.wangxueshi.mytoutiao.base.BaseAcitivity;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;
import com.bawei.wangxueshi.mytoutiao.fragment.TitleFragmet;
import com.bawei.wangxueshi.mytoutiao.right_left.LeftFragment;
import com.bawei.wangxueshi.mytoutiao.right_left.RightFragment;
import com.bwei.slidingmenu.SlidingMenu;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseAcitivity {
    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public boolean isWhite=true;
    private List<Fragment> listFragment=new ArrayList<Fragment>();
    private SharedPreferences.Editor edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
        initGrayBackgroud();
        //是否订阅
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        SharedPreferences isWhite = getSharedPreferences("isWhite", MODE_PRIVATE);
        edit = isWhite.edit();
        boolean isWhite1 = isWhite.getBoolean("isWhite",true);
        if(isWhite1==false){
            EventBus.getDefault().postSticky(new YeJianEvent(true));
        }


        setContentView(R.layout.activity_main);
        //include 点击的作用
         findViewById(R.id.main_include).findViewById(R.id.pub_title_left_imageview).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 toggle();
             }
         });


        switchFragment(0);

        initLeftRight();

    }
    private void initLeftRight() {
        LeftFragment leftFragment = new LeftFragment();
        setBehindContentView(R.layout.left);
        getSupportFragmentManager().beginTransaction().replace(R.id.left, leftFragment).commit();
        SlidingMenu slidingMenu = getSlidingMenu();
        // 设置slidingmenu滑动的方式
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置阴影的宽度
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置slidingmenu边界的阴影图片
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        //设置右边（二级）侧滑菜单
        RightFragment rightFragment = new RightFragment();
        slidingMenu.setSecondaryMenu(R.layout.right);
        getSupportFragmentManager().beginTransaction().replace(R.id.right, rightFragment).commit();
    }
//添加蒙层
    public void initGrayBackgroud() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams
                (WindowManager.LayoutParams.TYPE_APPLICATION,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSPARENT);
        view = new View(this);
        view.setBackgroundResource(R.color.color_window);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isWhite==false){
            windowManager.removeViewImmediate(view);
        }
        EventBus.getDefault().unregister(this);
    }
    public void loadQQ() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                System.out.println("onStart" + share_media);
            }
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                System.out.println("onComplete" + share_media);
                String uid = map.get("uid");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                System.out.println("iconurl = " + iconurl);
                System.out.println("uid = " + uid);
                System.out.println("name = " + name);
                System.out.println("gender = " + gender);
            }
            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                System.out.println("onError" + share_media);
            }
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                System.out.println("onCancel" + share_media);
            }
        });
    }
    //王学士  微信登录
    public void loadweixin() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                System.out.println("onStart" + share_media);
            }
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                System.out.println("onComplete" + share_media);
                String uid = map.get("uid");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                System.out.println("iconurl = " + iconurl);
                System.out.println("uid = " + uid);
                System.out.println("name = " + name);
                System.out.println("gender = " + gender);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                System.out.println("onError" + share_media);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                System.out.println("onCancel" + share_media);
            }
        });
    }
    //王学士  新浪登录
    public void loadSIna() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.SINA, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                System.out.println("onStart" + share_media);
            }
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                System.out.println("onComplete" + share_media);
                String uid = map.get("uid");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                System.out.println("iconurl = " + iconurl);
                System.out.println("uid = " + uid);
                System.out.println("name = " + name);
                System.out.println("gender = " + gender);
            }
            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                System.out.println("onError" + share_media);
            }
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                System.out.println("onCancel" + share_media);
            }
        });
    }
    //    // 日 夜切换
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainActivityEvent(YeJianEvent event) {
        if (event.isYeJian()) {
            // true 夜
            windowManager.addView(view, layoutParams);
            isWhite=false;
            edit.putBoolean("isWhite",false).commit();
        } else {
            // 日
            windowManager.removeView(view);
            isWhite=true;
            edit.putBoolean("isWhite",true).commit();
        }
        //更改字体颜色
        switchTextViewColor((ViewGroup) getWindow().getDecorView(),event.isYeJian());
        //对所有的控件取出,设置对应的图片
        setView();
        TitleFragmet indexFragment = (TitleFragmet) listFragment.get(0);
        indexFragment.changeMode(event.isYeJian());
    }
    // 更改 控件 背景
    private    void setView(){
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
                textView.setTextColor(getResources().getColor(resouseId));
            }
        }

    }
//添加Fragment
    private void initFragment(Bundle savedInstanceState){
        //防止Fragment混乱
        if(listFragment != null && listFragment.size() > 0){
            listFragment.clear();
        }
        TitleFragmet indexFragment = null ;
//        FriendsFragment friendsFragment = null;
//        MyTalkFragment myTalkFragment = null ;
//        CollectFragment collectFragment = null;
//        ActivitysFragment activitysFragment = null;
//        ShopFragment shopFragment = null;
        if(savedInstanceState != null){
            indexFragment = (TitleFragmet)getSupportFragmentManager().findFragmentByTag("TitleFragmet");
//            friendsFragment = (FriendsFragment)getSupportFragmentManager().findFragmentByTag("FriendsFragment");
//            myTalkFragment = (MyTalkFragment)getSupportFragmentManager().findFragmentByTag("MyTalkFragment");
//            collectFragment = (CollectFragment)getSupportFragmentManager().findFragmentByTag("CollectFragment");
//            activitysFragment = (ActivitysFragment)getSupportFragmentManager().findFragmentByTag("ActivitysFragment");
//            shopFragment = (ShopFragment)getSupportFragmentManager().findFragmentByTag("ShopFragment");
        }
        if(indexFragment == null){
            indexFragment = new TitleFragmet();
        }
//        if(friendsFragment == null){
//            friendsFragment = new FriendsFragment();
//        }
//        if(myTalkFragment == null){
//            myTalkFragment = new MyTalkFragment();
//        }
//        if(collectFragment == null){
//            collectFragment = new CollectFragment();
//        }
//        if(activitysFragment == null){
//            activitysFragment = new ActivitysFragment();
//        }
//        if(shopFragment == null){
//            shopFragment = new ShopFragment();
//        }
//
//        list.add(friendsFragment);
//        list.add(myTalkFragment);
//        list.add(collectFragment);
//        list.add(activitysFragment);
//        list.add(shopFragment);
        listFragment.add(indexFragment);
    }

    private void switchFragment(int pos){

        FragmentManager fragmentManager =  getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        if(!listFragment.get(pos).isAdded()){
            fragmentTransaction.add(R.id.title_fragment, (Fragment) listFragment.get(pos),listFragment.get(pos).getClass().getSimpleName());
        }
        for(int i=0;i<listFragment.size();i++){
            if(i == pos){
                fragmentTransaction.show((Fragment) listFragment.get(pos));
            }else {
                fragmentTransaction.hide((Fragment) listFragment.get(i));
            }
        }
        fragmentTransaction.commit();
    }


}
