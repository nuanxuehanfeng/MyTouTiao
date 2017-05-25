package com.bawei.wangxueshi.mytoutiao.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bawei.wangxueshi.mytoutiao.IApplication;
import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.adapter.TabFragmentListViewAdapter;
import com.bawei.wangxueshi.mytoutiao.base.BaseFragment;
import com.bawei.wangxueshi.mytoutiao.bean.ShuJuBean;
import com.bawei.wangxueshi.mytoutiao.tools.NetTools;
import com.bwei.springview.container.DefaultFooter;
import com.bwei.springview.container.DefaultHeader;

import com.bwei.springview.widget.SpringView;
import com.google.gson.Gson;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2017/5/22.
 */

public class TabFragment extends BaseFragment {
    private View view;
    private SpringView springView;
    private ListView listView;
    private String path0 = "http://ic.snssdk.com/2/article/v25/stream/?category=";
    private String category;
    private Gson gson;
    private TabFragmentListViewAdapter tabFragmentListViewAdapter;
    private String page0 = "&min_behot_time=";
    private   List<ShuJuBean.DataBean>  listData=new ArrayList<>();
    private long time = System.currentTimeMillis();
    private DbManager dbManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tabfragment,container,false);

        dbManager = x.getDb(IApplication.getDaoConfig());
        initView(view);
        Bundle arguments = getArguments();
        category = arguments.getString("category");
        //System.out.println("category = " + category);
        gson = new Gson();
        return view;

    }
//初始化视图
    private void initView(View view) {
        springView = (SpringView) view.findViewById(R.id.tabfragment_springview);
        listView = (ListView) view.findViewById(R.id.tabfragment_listivew);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setType(SpringView.Type.FOLLOW);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean networkAvailable = NetTools.isNetworkAvailable(getActivity());
        if(networkAvailable){
            //有网络时 ，进行网络访问
            hasNetGet();
        }
        else{
            //没有网络时
            try {
                //判断是那种类型categary
                List<ShuJuBean.DataBean> all = dbManager.findAll(ShuJuBean.DataBean.class);
                for(ShuJuBean.DataBean bean:all){
                    if(category.equals(bean.getCategory1())){
                        System.out.println("bean.getCategory1() =获取数据 " + bean.getCategory1());
                        listData.add(bean); 
                    }
                }
                System.out.println("\"测试一下缓存\"+all.size() = " + "测试一下缓存"+all.size());
               // listData.addAll(all);
                tabFragmentListViewAdapter= new TabFragmentListViewAdapter(getActivity(), listData);
                listView.setAdapter(tabFragmentListViewAdapter);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        //刷新和加载
        springView.setListener(new SpringView.OnFreshListener() {
            //刷新方法
            @Override
            public void onRefresh() {
                time =System.currentTimeMillis();
                getShuju(path0 + category + page0 + time, 1);
                springView.onFinishFreshAndLoad();
            }
            //加载更多方法
            @Override
            public void onLoadmore() {
                time-=10000000;
                getShuju(path0 + category + page0 + time, 2);
                springView.onFinishFreshAndLoad();
            }
        });
    }

    private void hasNetGet() {
        //城市
        if(category.equals("news_local")){
            path0="http://ic.snssdk.com/2/article/v25/stream/?category=news_local\n" +
                    "&count=10\n" +
                    "&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82\n" +
                    "&bd_latitude=40.049317\n" +
                    "&bd_longitude=116.296499\n" +
                    "&bd_loc_time=1455521401&loc_mode=10\n" +
                    "&user_city=%E5%8C%97%E4%BA%AC&lac=4527&cid=28883\n" +
                    "&iid=3642583580\n" +
                    "&device_id=11131669133\n" +
                    "&ac=wifi\n" +
                    "&channel=baidu\n" +
                    "&aid=1\n" +
                    "&app_name=news_article\n" +
                    "&version_code=460\n" +
                    "&device_platform=android\n" +
                    "&device_type=SCH-I919U\n" +
                    "&os_api=19\n" +
                    "&os_version=4.4.2\n" +
                    "&uuid=285592931621751\n" +
                    "&openudid=AC9E172CE2490000";

        }
        //段子
        if(category.equals("essay_joke")){
            path0="http://ic.snssdk.com/2/article/v25/stream/?category=essay_joke";
            getShuju(path0,0);
        }
        //美女
        if(category.equals("essay_joke")){
            path0="http://ic.snssdk.com/2/article/v25/stream/?category=image_ppmm";
            getShuju(path0,0);
        }
        //趣图
        if(category.equals("image_funny")){
            path0="http://ic.snssdk.com/2/article/v25/stream/?category=image_funny";

            getShuju(path0,0);
        }


        //默认有网络,获得数据
        getShuju(path0 + category + page0 + time, 0);
    }

    public void getShuju(final String path, final int type) {
        RequestParams entity = new RequestParams(path);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //第一次请求
                if (type == 0) {
                    ShuJuBean shuJuBean = gson.fromJson(result, ShuJuBean.class);
                    List<ShuJuBean.DataBean> data = shuJuBean.getData();
                    //缓存入库
                    hunCunRuKu(data);
                    listData.addAll(data);
                    tabFragmentListViewAdapter= new TabFragmentListViewAdapter(getActivity(), listData);
                    listView.setAdapter(tabFragmentListViewAdapter);
                    //进行判断 如果是城市 则添加头
                   if (category.equals("news_local")) {
                     TextView  tvHead = new TextView(getActivity());
                        ViewGroup.LayoutParams para=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tvHead.setLayoutParams(para);
                        tvHead.setGravity(Gravity.CENTER);
                        tvHead.setText("点击选择其他城市");
                        tvHead.setTextColor(Color.BLACK);
                       listView.addHeaderView(tvHead);
                        tvHead.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //  startActivity(new Intent(getActivity(), CtriyActivity.class));
                            }
                        });
                    }
                }
                //刷新
                else if (type == 1) {
                    ShuJuBean shuJuBean = gson.fromJson(result, ShuJuBean.class);
                    List<ShuJuBean.DataBean> data = shuJuBean.getData();
                    //缓存入库
                    hunCunRuKu(data);
                    listData.clear();
                    listData.addAll(data);
                    tabFragmentListViewAdapter.notifyDataSetChanged();
                }
                //下拉加载
                else if (type == 2) {
                    ShuJuBean shuJuBean = gson.fromJson(result, ShuJuBean.class);
                    List<ShuJuBean.DataBean> data = shuJuBean.getData();
                    //缓存入库
                    hunCunRuKu(data);
                    listData.addAll(listData.size(), data);
                    tabFragmentListViewAdapter.notifyDataSetChanged();
                }
            }

            private void hunCunRuKu(List<ShuJuBean.DataBean> data) {
                //先清空数据库，在缓存入库
                try {


                    //数据数据库
                    dbManager.delete(ShuJuBean.DataBean.class, WhereBuilder.b("category1", "=", category));
                 //   dbManager.delete(ShuJuBean.DataBean.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                //缓存入数据库
                for(ShuJuBean.DataBean bean:data){
                    try {
                    bean.setCategory1(category);
                    System.out.println("bean.getCategory1() 保存数据= " + bean.getCategory1());

                        dbManager.save(bean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
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

    //关于视频的
    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
