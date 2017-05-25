package com.bawei.wangxueshi.mytoutiao.activity.city;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//王学士，注册界面
public class ManyLoadActivity extends Activity {

    private Button phoneLoad;
    private Button zhuce;
    private ImageView fanhui;

    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public boolean isWhite=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGrayBackgroud();
        //是否订阅
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
       setContentView(R.layout.activity_zhu_ce);
        initView();
    }
    private void initView() {
        phoneLoad = (Button) findViewById(R.id.zhuce_bt_phoneload);
        zhuce = (Button) findViewById(R.id.zhuce_bt_zhucenow);
        fanhui = (ImageView) findViewById(R.id.zhuce_tv_fanhui);

        phoneLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ManyLoadActivity.this,PhoneLoadActivity.class);
                startActivity(intent);
                finish();
            }
        });
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManyLoadActivity.this,PhoneZhuceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
  }
    //日夜
    public void initGrayBackgroud() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams
                (WindowManager.LayoutParams.TYPE_APPLICATION,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSPARENT);
        view = new View(this);

        view.setBackgroundResource(R.color.color_window);

    }
    //    // 日 夜切换
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMainActivityEvent(YeJianEvent event) {
      //  initGrayBackgroud();
        if (event.isYeJian()) {
            // true 夜
            windowManager.addView(view, layoutParams);
            isWhite=false;
        } else {
            // 日
            windowManager.removeViewImmediate(view);
            isWhite=true;
        }
        //更改字体颜色
        switchTextViewColor((ViewGroup) getWindow().getDecorView(),event.isYeJian());}
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isWhite==false){
            windowManager.removeViewImmediate(view);
        }
        EventBus.getDefault().unregister(this);
    }

}
