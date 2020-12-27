package com.daluzy.daluzy_project.daluzy_project.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片加载封装类
 */
public class ImageLoadTool {

    public static void imageLoad(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
//        Picasso.get().load(url).into(imageView);
    }
}
