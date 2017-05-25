package com.bawei.wangxueshi.mytoutiao.activity.city;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.adapter.LiXianListviewAdapter;
import com.bawei.wangxueshi.mytoutiao.bean.TabTitleBean;
import com.bawei.wangxueshi.mytoutiao.bean.YeJianEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class LiXianActivity extends Activity {
    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    public boolean isWhite=true;
    private LiXianListviewAdapter lianxiDoemListviewAdapter;
    private ListView listView;
    private Gson gson = new Gson();
    private List<TabTitleBean.DataBeanX.DataBean> tabdata = new ArrayList<>();
    String path0 = "http://ic.snssdk.com/2/article/v25/stream/?category=";
    //吕文静
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixian);
        initGrayBackgroud();
        //是否订阅
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        init();
        get();
    }

    //吕文静下载页面
    private void init() {
        listView = (ListView) findViewById(R.id.download_listview);
        ImageView download_fanhui = (ImageView) findViewById(R.id.download_fanhui);
        download_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tuijian_tv = (TextView) findViewById(R.id.download_xiazaiwenjian);
        tuijian_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LiXianActivity.this);
                    builder.setTitle("移动网络离线下载会使用较多流量，是否继续？");
                    builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getData();
                            Toast.makeText(LiXianActivity.this, "好了", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tabdata.get(position).setDefault_add(1);
//            }
//        });


    }

    //吕文静网络请求
    private void get() {
        String pathTitle = "http://ic.snssdk.com/article/category/get/v2/?iid=2939228904";
        RequestParams entity = new RequestParams(pathTitle);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TabTitleBean tabTitle = gson.fromJson(result, TabTitleBean.class);
                tabdata.addAll(tabTitle.getData().getData());
                lianxiDoemListviewAdapter = new LiXianListviewAdapter(LiXianActivity.this, tabdata);
                listView.setAdapter(lianxiDoemListviewAdapter);
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

    //牛琼琼  和获取数据加载入数据库
    private void getData() {
        for (TabTitleBean.DataBeanX.DataBean bean:tabdata) {
            int default_add = bean.getDefault_add();
            if (default_add == 1) {
                String category = bean.getCategory();
                String path = path0 + category;
                get1(path);
            }
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                RequestParams rp = new RequestParams("http://ic.snssdk.com/2/article/v25/stream/?count=20&min_behot_time=1455521444&bd_city=北京市&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000");
//
//                x.http().get(rp, new Callback.CommonCallback<String>() {
//
//
//                    @Override
//                    public void onSuccess(String result) {
//                        //  System.out.println("result = ============" + result);
//
//                        Gson gson = new Gson();
//
//                        TuiJianBean tt = gson.fromJson(result, TuiJianBean.class);
//
//                        try {
//                            for (TabTitle.DataBeanX.DataBean bean:tabdata){
//                                int default_add = bean.getDefault_add();
//                                if(default_add==1){
//                                    String category = bean.getCategory();
//                                  String path=path0+category;
//
//
//
//
//                                }
//
//                            }
//
//
//
//                            x.getDb(IApplication.initDB()).save(tt.getData());
//                            //    System.out.println(" =成功================= ");
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
//            }
//        }).start();
//

    }
//王学士 离线下载
    private void get1(String path11) {

        RequestParams entity = new RequestParams(path11);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();

//                TuiJianBean tt = gson.fromJson(result, TuiJianBean.class);
//                try {
//                    x.getDb(IApplication.daoConfig1).delete(TuiJianBean.DataBean.class);
//
//                    for (TuiJianBean.DataBean bean:tt.getData()){
//                        x.getDb(IApplication.getDaoConfig()).save(bean);
//                    }
//
//                    System.out.println("\"保存\" = " + "保存");
//
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//

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