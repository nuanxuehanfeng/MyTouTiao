package com.bawei.wangxueshi.mytoutiao.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bawei.wangxueshi.mytoutiao.IApplication;
import com.bawei.wangxueshi.mytoutiao.R;
import com.bawei.wangxueshi.mytoutiao.bean.ShuJuBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.hp.hpl.sparta.Text;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TabFragmentListViewAdapter extends BaseAdapter {
    Context context;
    List<ShuJuBean.DataBean>  list;
    ImageView   imageViewHuncun;
   DbManager db;

    public TabFragmentListViewAdapter(Context context, List<ShuJuBean.DataBean> list) {
        db=x.getDb(IApplication.getDaoConfig());
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShuJuBean.DataBean dataBean = list.get(position);



        //判断是否为视频
        boolean has_video = dataBean.isHas_video();
        //判断是否为一个中图
        boolean has_image = dataBean.isHas_image();
        //三个小图
        List<ShuJuBean.DataBean.ImageListBean> image_list = dataBean.getImage_list();
        //获得中图
        ShuJuBean.DataBean.MiddleImageBean middle_image = dataBean.getMiddle_image();
        //视屏

        if(has_video){
            return getVideo(position, convertView);
        }
        else{
            //中等图片一个
           if(has_image){

               return getImageModdle(position, convertView);
           } 
            else{
               //三个图片的  集合没有弄清
               if(image_list!=null&&image_list.size()==3){

                   return getImageSmall(position, convertView, image_list);
               }
               else{
                   //没有图片
                   return getImageNoNo(position, convertView);
                //   return getImageNo(convertView);

               }



           }
        }
    }

    @NonNull
    private View getImageNoNo(int position, View convertView) {
        HolderImageNo holderImageNo=null;
        View view6=null;
        if(view6==null){
            holderImageNo=new HolderImageNo();
            view6=View.inflate(context, R.layout.tabfragment_adapter_image_no,null);
            holderImageNo.item_media_name= (TextView) view6.findViewById(R.id.tabfragment_adapter_image_no_tv_source);
            holderImageNo.item_title= (TextView) view6.findViewById(R.id.tabfragment_adapter_image_no_tv_title);
            view6.setTag(holderImageNo);
        }
        else{
            holderImageNo=(HolderImageNo) view6.getTag();
        }
        holderImageNo.item_title.setText(list.get(position).getTitle());
        holderImageNo.item_media_name.setText(list.get(position).getMedia_name());
        //图片的加载
        return  view6;
    }
    @NonNull
    private View getImageSmall(int position, View convertView, List<ShuJuBean.DataBean.ImageListBean> image_list) {
        HolderImageSmall holderImageSmall=null;
        View view5=null;
        if(view5==null){
            holderImageSmall=new HolderImageSmall();
            view5=View.inflate(context, R.layout.tabfragment_adapter_image_small,null);
            holderImageSmall.item_media_name= (TextView) view5.findViewById(R.id.tabfragment_adapter_image_small_tv_source);
            holderImageSmall.item_title= (TextView) view5.findViewById(R.id.tabfragment_adapter_image_small_tv_title);
            holderImageSmall.ivLeft= (ImageView) view5.findViewById(R.id.tabfragment_adapter_image_small_iv_left);
            holderImageSmall.ivRight= (ImageView) view5.findViewById(R.id.tabfragment_adapter_image_small_iv_right);
            holderImageSmall.ivModdle= (ImageView) view5.findViewById(R.id.tabfragment_adapter_image_small_iv_moddle);
            view5.setTag(holderImageSmall);
        }
        else{
            holderImageSmall= (HolderImageSmall) view5.getTag();
        }
        holderImageSmall.item_title.setText(list.get(position).getTitle());
        holderImageSmall.item_media_name.setText(list.get(position).getMedia_name());
        //图片的加载
        loadImage(image_list.get(0).getUrl(),holderImageSmall.ivLeft);
        loadImage(image_list.get(1).getUrl(),holderImageSmall.ivModdle);
        loadImage(image_list.get(2).getUrl(),holderImageSmall.ivRight);
        return  view5;
    }

    @NonNull
    private View getImageModdle(int position, View convertView) {
        HolderImageModdle holderImageModdle=null;
        View view2=null;
        if(view2==null){
            holderImageModdle=new HolderImageModdle();
            view2=View.inflate(context, R.layout.tabfragment_adapter_image_moddle,null);
            holderImageModdle.item_media_name= (TextView) view2.findViewById(R.id.tabfragment_adapter_image_moddle_tv_source);
            holderImageModdle.item_title= (TextView) view2.findViewById(R.id.tabfragment_adapter_image_moddle_tv_title);
            holderImageModdle.ivModdle= (ImageView) view2.findViewById(R.id.tabfragment_adapter_image_moddle_iv);
            view2.setTag(holderImageModdle);
        }
        else{
            holderImageModdle= (HolderImageModdle) view2.getTag();
        }
        holderImageModdle.item_title.setText(list.get(position).getTitle());
        holderImageModdle.item_media_name.setText(list.get(position).getMedia_name());
        //图片的加载
        ShuJuBean.DataBean.MiddleImageBean middle_image = list.get(position).getMiddle_image();
           System.out.println("middle_image.getUrl() = " + middle_image.getUrl());
        if(!(middle_image==null)){
        loadImage(list.get(position).getMiddle_image().getUrl(),holderImageModdle.ivModdle);
        }
        return  view2;
    }

    @NonNull
    private View getVideo(int position, View convertView) {
        HolderVideo holderVideo=null;
        View view1=null;
        if(view1==null){
            holderVideo=new HolderVideo();
            view1=View.inflate(context, R.layout.tabfragment_adapter_video,null);
            holderVideo.item_media_name= (TextView) view1.findViewById(R.id.tabfragment_adapter_video_source);
            holderVideo.item_title= (TextView) view1.findViewById(R.id.tabfragment_adapter_video_title);
            holderVideo.video= (JCVideoPlayerStandard) view1.findViewById(R.id.tabfragment_adapter_video_video);
           holderVideo.larg_image= (ImageView) view1.findViewById(R.id.tabfragment_adapter_video_iv);
            view1.setTag(holderVideo);
        }
        else{
            holderVideo= (HolderVideo) view1.getTag();
        }
        holderVideo.item_title.setText(list.get(position).getTitle());
        holderVideo.item_media_name.setText(list.get(position).getMedia_name());
        //图片的加载

        //判断下不为空
        List<ShuJuBean.DataBean.LargeImageListBean> large_image_list = list.get(position).getLarge_image_list();


        if(null!=large_image_list){
            loadImage(list.get(position).getLarge_image_list().get(0).getUrl(),holderVideo.larg_image);}

     //   holderVideo.video.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "");


      //  holderVideo.video.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4",JCVideoPlayer.SCREEN_LAYOUT_LIST);
        //这个控件不能加载 网络资源
        holderVideo.video.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",JCVideoPlayer.SCREEN_LAYOUT_LIST);
        //加载图片作为视频的显示图片



        return  view1;
    }

    //图片缓存  以及加载
    private void loadImage(String path,ImageView imageView){
      //  new getImageCacheAsyncTask(context).execute(path,imageView);
       // imageView.setTag(tag1,path);

         Glide.with(context).load(path).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(imageView);
    }
    //视频的view
    class HolderVideo{
        //新闻标题
        TextView item_title;
        //新闻源
        TextView item_media_name;
        //大图片
        ImageView larg_image;
        JCVideoPlayerStandard video;
    }
    //无图片的
    class HolderImageNo{
        //新闻标题
        TextView item_title;
        //新闻源
        TextView item_media_name;
    }
    //中等图片一
    class HolderImageModdle{
        //新闻标题
        TextView item_title;
        //新闻源
        TextView item_media_name;
        //中等图片
        ImageView ivModdle;
    }
    //三个图片的
    class HolderImageSmall{
        //新闻标题
        TextView item_title;
        //新闻源
        TextView item_media_name;
        //中等图片
        ImageView ivLeft;
        //中等图片
        ImageView ivModdle;
        //中等图片
        ImageView ivRight;
    }


}
