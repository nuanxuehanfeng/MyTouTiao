package com.bawei.wangxueshi.mytoutiao.tools;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by Administrator on 2017/5/24.
 */

public class GlideConfiguration implements GlideModule {
    private static final int MEMORY_MAX_SPACE=(int) (Runtime.getRuntime().maxMemory()/8);

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置加载图片的样式,比默认图片质量好,但占用内存会大点
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    //    builder.setMemoryCache(new LruResourceCache(MEMORY_MAX_SPACE));
      //  builder.setDiskCache(new InternalCacheDiskCacheFactory(context, getDiskFileString(context,"glideCache"), 20*1024*1024));


        //获取内存计算器
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        //获取Glide默认内存缓存大小
        int                  defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        //获取Glide默认图片池大小
        int                  defaultBitmapPoolSize  = calculator.getBitmapPoolSize();
        //将数值修改为之前的1.1倍
        int                  myMemoryCacheSize  = (int) (1.1 * defaultMemoryCacheSize);
        int                  myBitmapPoolSize   = (int) (1.1 * defaultBitmapPoolSize);
        //修改默认值
        builder.setMemoryCache(new LruResourceCache(myMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(myBitmapPoolSize));

        //设置磁盘缓存大小
        int size = 100 * 1024 * 1024;
        //设置磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, size));

    }

//    private String getDiskFileString(Context mContext,String str) {
////        File dirFile=new File(mContext.getCacheDir().getAbsolutePath().toString()+str);
////        System.out.println(mContext.getCacheDir().getAbsolutePath().toString()+str+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
////        System.out.println(mContext.getCacheDir().getAbsolutePath().toString()+str);
////        System.out.println(mContext.getCacheDir().getAbsolutePath().toString()+str);
////        System.out.println(mContext.getCacheDir().getAbsolutePath().toString()+str);
////        File tempFile=new File(dirFile,"bitmaps");
////        if (! tempFile.getParentFile().exists()){
////            boolean mkdirs = tempFile.getParentFile().mkdirs();
////            System.out.println("mkdirs = " + mkdirs);
////        }
////        return tempFile.getAbsolutePath().toString();
//    }
    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}