package com.gly.collagelf.adapter;

import java.io.File;
import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * GridView的适配器，用来显示活动图片
 * @author 高留洋
 *
 */
public class HomeItemGridViewAdapter extends BaseAdapter {

	private Context context;//上下文
	private List<BmobFile> messagePics;//活动图片的集合
	private GridView messagepicGv;//要显示的控件
	private int screenWidth;//屏幕宽度
	
	public HomeItemGridViewAdapter(Context context,List<BmobFile> messagePics,GridView messagepicGv,int screenWidth) {
		this.messagePics = messagePics;
		this.context = context;
		this.messagepicGv = messagepicGv;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		return messagePics.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		final MyHolder holder;
		if (v == null) {
			v = LinearLayout.inflate(context, R.layout.frag_home_item_actionpic_gridview, null);
			holder = new MyHolder();
			holder.picIv = (ImageView) v.findViewById(R.id.item_iv);
			v.setTag(holder);
		}else {
			holder = (MyHolder) v.getTag();
		}
		
		//如果有3张之内的照片，把GridView的高度设置为90，超过3张，高度设置为180
		if (messagePics.size()<=3) {
			LinearLayout.LayoutParams params = new LayoutParams(screenWidth-20 , (screenWidth-20)/4);
			messagepicGv.setLayoutParams(params);
		}else {
			LinearLayout.LayoutParams params = new LayoutParams(screenWidth-20, (screenWidth-20)/2);
			messagepicGv.setLayoutParams(params);
		}
		//设置图片到GridView上

		//使用ImageLoaderUtil下载图片
		ImageLoader loader = ImageLoaderUtil.getInstance(context);
		DisplayImageOptions options = ImageLoaderUtil.getOpt();
		loader.displayImage(messagePics.get(position).getFileUrl(), holder.picIv, options);
		return v;
	}

	//二级优化
	class MyHolder{
		ImageView picIv;
	}
}
