package com.bawei.wangxueshi.mytoutiao.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bawei.wangxueshi.mytoutiao.bean.TabTitleBean;
import com.bawei.wangxueshi.mytoutiao.fragment.TabFragment;

import java.util.List;

/**
 * Created by lenovo-pc on 2017/5/9.
 */

public class IndextAdapter extends FragmentPagerAdapter {
    List<TabTitleBean.DataBeanX.DataBean> data;
    private TabFragment tabFragment;

    public IndextAdapter(FragmentManager fm, List<TabTitleBean.DataBeanX.DataBean> data) {
        super(fm);
        this.data = data;
    }
    @Override
    public Fragment getItem(int position) {
        tabFragment = new TabFragment();
        String category = data.get(position).getCategory();
        Bundle bundle = new Bundle();
        bundle.putString("category",category);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public int getCount() {
        return  data==null?0:data.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
      return  data.get(position).getName();
    }
}
