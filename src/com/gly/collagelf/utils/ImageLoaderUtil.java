package com.gly.collagelf.utils;


import java.io.File;

import android.content.Context;

import com.gly.collagelf.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 加载和缓存图片的工具类,此处用于加载和缓存用户头像
 * @author 高留洋
 *
 */
public class ImageLoaderUtil {
private  static	ImageLoader loader;
private  static  ImageLoaderConfiguration.Builder confbuilder;
private   static ImageLoaderConfiguration conf;
private   static DisplayImageOptions.Builder optbuilder;
private  static DisplayImageOptions opt;
//返回注册号的imageloader对象
	public static  ImageLoader getInstance(Context context)
	{
	
		loader=ImageLoader.getInstance();
		confbuilder=new ImageLoaderConfiguration.Builder(context);
		File file=new File("/mnt/sdcard/lf/imageloader");
		//制定sdcard缓存的路径
		confbuilder.discCache(new UnlimitedDiscCache(file));
		//缓存的图片个数
		confbuilder.discCacheFileCount(100);
		//缓存的最大容量
		confbuilder.discCacheSize(1024*1024*10*10);
		conf=confbuilder.build();
		loader.init(conf);
		return loader;
	}
	//返回显示图片使得opt
	public   static DisplayImageOptions getOpt()
	{
		optbuilder=new DisplayImageOptions.Builder();
		//uri 加载默认图片
		optbuilder.showImageForEmptyUri(R.drawable.logo);
		//图片获取失败 加载的默认图片
		optbuilder.showImageOnFail(R.drawable.ic_launcher);
		optbuilder.cacheInMemory(true);//做内存缓存
		optbuilder.cacheOnDisc(true);//做硬盘缓存
		opt=optbuilder.build();
		return opt;
	}
}
