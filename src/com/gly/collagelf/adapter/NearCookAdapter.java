package com.gly.collagelf.adapter;
import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter.ViewHolder;
import com.gly.collagelf.bean.Cook;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 附近美食的Adapter
 * @author 高留洋
 *
 */
public class NearCookAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Cook> allInfos;
	private ImageLoader loader;
	public NearCookAdapter(Context context,List<Cook> allInfos) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.allInfos = allInfos;
	}

	@Override
	public int getCount() {
		return allInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return allInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		CookHolder cookHolder;
		if (v == null) {
			v = inflater.inflate(R.layout.cook_item, null);
			cookHolder = new CookHolder();
			cookHolder.cookPic = (ImageView) v.findViewById(R.id.cook_pic);
			cookHolder.cookTitle = (TextView) v.findViewById(R.id.cook_title);
			cookHolder.cookMoney = (TextView) v.findViewById(R.id.cook_money);
			cookHolder.cookAddress = (TextView) v.findViewById(R.id.cook_address);
			v.setTag(cookHolder);
		}else {
			cookHolder = (CookHolder) v.getTag();
		}
		Cook cookInfo = allInfos.get(position);
		cookHolder.cookTitle.setText(cookInfo.getCookTitle());
		cookHolder.cookMoney.setText("人均￥"+cookInfo.getCookMoney());
		cookHolder.cookAddress.setText("地址："+cookInfo.getCookAddress());
		loader = ImageLoaderUtil.getInstance(context);
		DisplayImageOptions options = ImageLoaderUtil.getOpt();
		loader.displayImage(cookInfo.getCookPic().getFileUrl(),
				cookHolder.cookPic, options);
		return v;
	}

	class CookHolder{
		ImageView cookPic;
		TextView cookTitle,cookMoney,cookAddress;
	}
	/**
	 * 向外暴露更新adapter的数据源的方法
	 * 
	 * @param allInfos
	 */
	public void updateInfos(List<Cook> allInfos) {
		this.allInfos = allInfos;
		notifyDataSetChanged();
	}
}
