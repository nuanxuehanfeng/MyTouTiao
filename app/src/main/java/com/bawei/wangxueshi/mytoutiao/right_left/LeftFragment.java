package com.bawei.wangxueshi.mytoutiao.right_left;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.wangxueshi.mytoutiao.MainActivity;
import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.activity.city.LiXianActivity;
import com.bawei.wangxueshi.mytoutiao.activity.city.ManyLoadActivity;
import com.bawei.wangxueshi.mytoutiao.activity.city.SetingActivity;
import com.bawei.wangxueshi.mytoutiao.base.BaseFragment;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/5/22.
 */

public class LeftFragment extends BaseFragment {


    private ImageView tengxunweibo;
    private ImageView qq;
    private ImageView weibo;
    private TextView manyload;
    private Button lixianrb;
    private CheckBox yejianbt;
    private Button shexhiru;
    private View view;
    private int theme ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sildingmenu_left, container, false);
        inintView(view);

        return view;
    }

    //王学士   初始化视图
    private void inintView(final View view) {
        lixianrb = (Button) view.findViewById(R.id.slipngmeun_left_lixian_rb);
        tengxunweibo = (ImageView) view.findViewById(R.id.silpingmenu_rigth_tengxun_weibo);
        qq = (ImageView) view.findViewById(R.id.silpingmenu_rigth_QQ);
        weibo = (ImageView) view.findViewById(R.id.silpingmenu_rigth_weibo);
        manyload = (TextView) view.findViewById(R.id.silpingmenu_rigth_manyload);
        shexhiru = (Button) view.findViewById(R.id.slipngmeun_left_shezhi_rb);
        yejianbt = (CheckBox) view.findViewById(R.id.slipngmeun_left_yejian_rb);


        //夜间模式
        yejianbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                EventBus.getDefault().postSticky(new YeJianEvent(isChecked));
            }
        });
        shexhiru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SetingActivity.class));
            }
        });
        lixianrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LiXianActivity.class));
            }
        });
        manyload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ManyLoadActivity.class);
                startActivity(intent);
            }
        });
        //三种登录
        tengxunweibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("weibo\" = " + "weibo");
                MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.loadweixin();

            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("weibo\" = " + "qq");
                MainActivity mainActivity2 = (MainActivity) getActivity();
                mainActivity2.loadQQ();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("weibo\" = " + "weibo11");
                MainActivity mainActivity3 = (MainActivity) getActivity();
                mainActivity3.loadSIna();
            }
        });


    }



    @Override
    public void onPause() {
        super.onPause();
    }
}
