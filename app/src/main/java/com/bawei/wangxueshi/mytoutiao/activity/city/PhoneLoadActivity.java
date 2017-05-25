package com.bawei.wangxueshi.mytoutiao.activity.city;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

//王学士 手机号登录
public class PhoneLoadActivity extends Activity {
    private EditText phone;
    private EditText psw;
    private Button load;
    public boolean isWhite=true;
    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGrayBackgroud();
        setContentView(R.layout.activity_phone_load);
        //是否订阅
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initView();

    }
    private void initView() {
        phone = (EditText) findViewById(R.id.phoneload_et_phone);
        psw = (EditText) findViewById(R.id.phoneload_et_psw);
        load = (Button) findViewById(R.id.phoneload_bt_load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path="http://qhb.2dyt.com/Bwei/login";
                String ph = phone.getText().toString().trim();
                String psw1 = psw.getText().toString().trim();
                loadget(path,ph,psw1,"1503d");
            }
        });
    }

    private void loadget(String path,String username,String psw,String postkey){
        RequestParams requestParams = new RequestParams(path);
        requestParams.addQueryStringParameter("postkey",postkey);
        requestParams.addQueryStringParameter("username",username);
        requestParams.addQueryStringParameter("password",psw);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result = " + result);
                Toast.makeText(PhoneLoadActivity.this,result,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
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
    //    // 日 夜切换
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMainActivityEvent(YeJianEvent event) {
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
