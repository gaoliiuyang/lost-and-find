package com.gly.collagelf.utils;

import java.util.List;

import com.gly.collagelf.bean.MessageInfo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 从服务器获取活动的信息
 * @author 高留洋
 *
 */
public class FindMessageInfoUtil {

	/**
	 * 查询活动的信息
	 * 
	 * @param type 类型,1、默认查询10条数据，正常从服务器获取数据/下拉刷新
	 *                 2、使用参数3，查询count条数据,忽略skip条数据/加载更多
	 *                 3、结合参数2，查询一条数据(参数2，用户id)
	 *                 4、结合参数2，查询多条数据(参数2，用户选择的type)
	 *                 
	 * @param data
	 * @param skip  跳过多少条数据
	 * @param count  需要查询的数据总条数
	 * @param listener
	 */
	public static void findMessageInfos(
			int type,
			Object data,
			int skip,
			int count,
			final FindMessageInfoListener listener){
		BmobQuery<MessageInfo> query = new BmobQuery<MessageInfo>();
		switch (type) {
		case 1:
			//正常查询，主页的下拉刷新
//			query.setLimit(10);
			query.order("-createdAt");
			break;

		case 2://主页的加载更多
			query.setSkip(skip);
			break;
			
		case 3://查询指定id的活动
			query.addWhereEqualTo("objectId", data);
			
			break;
		case 4://按物品类型查询
			query.addWhereEqualTo("messageGoodsType", data);
			query.order("-createdAt");
			break;
		case 5://失物或者招领查询
			query.addWhereEqualTo("messageType", data);
			query.order("-createdAt");
			break;
		case 6://失物或者招领中的下拉刷新
			query.addWhereEqualTo("messageType", data);
			query.order("-createdAt");
			break;
		case 7://失物或者招领中的加载更多
			query.addWhereEqualTo("messageType", data);
			query.setSkip(skip);
			query.order("-createdAt");
			break;
		}
		query.findObjects(new FindListener<MessageInfo>() {
			
			@Override
			public void done(List<MessageInfo> info, BmobException ex) {
				listener.getMessageInfo(info,ex);
			}
		});
	}
	
	//观察者模式，谁查询谁实现接口
	public interface FindMessageInfoListener{
		void getMessageInfo(List<MessageInfo> messageInfos,BmobException ex);
	}
}
