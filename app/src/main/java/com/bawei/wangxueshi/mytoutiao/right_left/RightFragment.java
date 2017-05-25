package com.bawei.wangxueshi.mytoutiao.right_left;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.base.BaseFragment;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/5/22.
 */

public class RightFragment extends BaseFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sildingmenu_rigth, container, false);
        EventBus.getDefault().register(this);
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(YeJianEvent event) {

//        if(event.isYeJian()){
//            //夜间模式
//            view.setBackgroundColor(getResources().getColor(R.color.backgroundColor_night));
//
//        }
//        //白天模式
//        else{
//            view.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
//        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
