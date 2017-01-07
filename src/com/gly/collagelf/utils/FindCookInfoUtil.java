package com.gly.collagelf.utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import com.gly.collagelf.bean.Cook;

public class FindCookInfoUtil {

	public static void findCookInfo(
			int type,
			Object data,
			int skip,
			int count,
			final FindCookInfoListener listener){
		BmobQuery<Cook> query = new BmobQuery<Cook>();
		switch (type) {
		case 1:
			//正常查询，主页的下拉刷新
			query.order("-createdAt");
			break;

		case 2://主页的加载更多
			query.setSkip(skip);
			break;
		}
		query.findObjects(new FindListener<Cook>() {
			
			@Override
			public void done(List<Cook> info, BmobException ex) {
				listener.getCookInfo(info,ex);
			}
		});
	}
	
	//观察者模式，谁查询谁实现接口
	public interface FindCookInfoListener{
		void getCookInfo(List<Cook> cookInfos,BmobException ex);
	}
}
