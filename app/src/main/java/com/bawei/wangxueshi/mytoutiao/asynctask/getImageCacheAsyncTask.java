package com.bawei.wangxueshi.mytoutiao.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by Administrator on 2017/5/23.
 */

public class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
    private final Context context;

    public getImageCacheAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected File doInBackground(String... params) {
        String imgUrl =  params[0];
        try {
            return Glide.with(context)
                    .load(imgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        //此path就是对应文件的缓存路径
        String path = result.getPath();

        Bitmap bmp= BitmapFactory.decodeFile(path);
      //  img.setImageBitmap(bmp);

    }
}