package com.gly.collagelf.adapter;

import com.gly.collagelf.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 更多页面中GridView的适配器
 * @author 高留洋
 *
 */
public class MyMoreGridViewAdapter extends BaseAdapter {

	private Activity act;
	private String[] names;
	private int[] imgs;
	private LayoutInflater inflater;
	public MyMoreGridViewAdapter(Activity act, String[] names, int[] imgs) {
		this.act = act;
		this.names = names;
		this.imgs = imgs;
		inflater = LayoutInflater.from(act);
	}

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		if (v == null) {
			v = inflater.inflate(R.layout.more_gridview_item, null);
		}
		ImageView moreIv = (ImageView) v.findViewById(R.id.more_gridview_item_iv);
		TextView moreTv = (TextView) v.findViewById(R.id.more_gridview_item_tv);
		moreIv.setImageResource(imgs[position]);
		moreTv.setText(names[position]);
		return v;
	}

}
