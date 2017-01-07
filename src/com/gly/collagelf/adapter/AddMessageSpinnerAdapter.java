package com.gly.collagelf.adapter;

import com.gly.collagelf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 添加活动的选择活动类型的下拉列表适配器
 * 
 * @author 高留洋
 * 
 */
public class AddMessageSpinnerAdapter extends BaseAdapter {

	// 数据源
	private String[] names ;
	private int[] ivs = { R.drawable.yikatong,R.drawable.shouji, R.drawable.yifu,R.drawable.shipin,R.drawable.qianbao,R.drawable.shu,R.drawable.yaoshi,R.drawable.chongwu,R.drawable.qita};
	// 上下文
	private Context context;
	// 布局填充器
	private LayoutInflater inflater;

	public AddMessageSpinnerAdapter(Context context,String[] names) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.names = names;
	}

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int posititon) {
		return names[posititon];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		if (v == null) {
			v = inflater.inflate(R.layout.spinner_item, null);
		}
		ImageView iv = (ImageView) v.findViewById(R.id.spinner_item_iv);
		TextView tv = (TextView) v.findViewById(R.id.spinner_item_tv);
		iv.setImageResource(ivs[position]);
		tv.setText(names[position]);
		return v;
	}
}
