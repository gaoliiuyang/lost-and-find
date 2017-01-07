package com.gly.collagelf.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.CurrItemMessageActivity;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.myview.MyImageView;
import com.gly.collagelf.utils.CountDistanceUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

/**
 * home中ListView上要显示的数据
 * 
 * @author 高留洋
 * 
 */
public class HomeAdapter extends BaseAdapter {
	private ImageLoader loader;
	private Context context;
	private int screenWidth;
	// 数据源
	private List<MessageInfo> allInfos;

	public HomeAdapter(Activity act, List<MessageInfo> allInfos, int screenWidth) {
		this.context = act;
		this.allInfos = allInfos;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		return allInfos.size();
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
	public View getView(final int position, View v, ViewGroup parent) {
		// 一级优化，减少了item项的创建次数
		// 二级优化:减少了获取item中的控件的次数
		// 维护一个ViewHolder对象
		final ViewHolder holder;
		if (v == null) {
			// 如果回收池为null,说明加载第一页数据,没有滑动ListView
			// 绑定item的布局
			v = LayoutInflater.from(context).inflate(R.layout.fragment_item,
					null);
			// 创建ViewHolder对象，并使用ViewHolder获取各个控件
			holder = new ViewHolder();
			// 文本框
			holder.messageUsernameTv = (TextView) v
					.findViewById(R.id.frag_item_username_tv);
			holder.messageTimeTv = (TextView) v
					.findViewById(R.id.frag_item_time_tv);
			holder.messageTypeTv = (TextView) v
					.findViewById(R.id.frag_item_type);
			holder.messageAddressTv = (TextView) v
					.findViewById(R.id.frag_item_address_tv);
			holder.messageDistanceTv = (TextView) v
					.findViewById(R.id.frag_item_distance_tv);
			holder.messageTitleTv = (TextView) v
					.findViewById(R.id.frag_item_title_tv);
			holder.messageDescTv = (TextView) v
					.findViewById(R.id.frag_item_desc_tv);
			// 头像
			holder.messageHead = (MyImageView) v
					.findViewById(R.id.frag_item_headpic_iv);
			// 图片 GridView
			holder.messagePicGv = (GridView) v.findViewById(R.id.frag_item_gv);
			// 收藏
			holder.lovepicIv = (ImageView) v
					.findViewById(R.id.frag_item_lovepic_iv);
			//分享
			holder.sharepicIv = (ImageView) v.findViewById(R.id.frag_item_share_iv);
			// setTag把holder绑定到item项的布局上
			v.setTag(holder);
		} else {
			// 如果回收池不为空，则使用回收池中创建过得item项的布局，并且通过getTag()获取holder中的数据
			holder = (ViewHolder) v.getTag();
		}
		// 把position对应的用户信息给bean对象
		final MessageInfo infos = allInfos.get(position);
		// 放到全局
		MyApplication.putData("allInfos", allInfos);

		/**
		 * 计算距离
		 */
		// 当前位置的经度纬度
		double locationLong = MyApplication.getLongitude();// 当前位置的经度
		double locationLa = MyApplication.getLatitude();// 当前位置的纬度
		double actionLong = infos.getMessageLocation().getLongitude();// 活动的经度
		double actionLa = infos.getMessageLocation().getLatitude();// 活动的纬度
		double distance = CountDistanceUtil.Distance(locationLong, locationLa,
				actionLong, actionLa);
		if (distance < 1000) {
			// 保留两位小数
			DecimalFormat df = new DecimalFormat("#.00");
			String distancekm = df.format(distance);
			holder.messageDistanceTv.setText("距离我" + distancekm + "m");
			// 保存到全局
			MyApplication.putData("distance", distance);
		} else {
			// 保留两位小数
			DecimalFormat df = new DecimalFormat("#.00");
			String distancekm = df.format(distance / 1000.0);
			holder.messageDistanceTv.setText("距离我" + distancekm + "km");
			// 保存到全局
			MyApplication.putData("distance", distancekm);
		}
		// 给文本框设置内容
		holder.messageTimeTv.setText("时间：" + infos.getCreatedAt());
		holder.messageAddressTv.setText("地点：" + infos.getMessageAddress());
		holder.messageTypeTv.setText(infos.getMessageType());
		holder.messageTitleTv.setText("标题：" + infos.getMessageName());
		holder.messageDescTv.setText("详情：" + infos.getMessageDesc());

		// 给GridView设置适配器
		holder.messagePicGv.setAdapter(new HomeItemGridViewAdapter(context,
				infos.getMessagePics(), holder.messagePicGv, screenWidth));

		// 通过infos的getMessageUserId方法找到该用户的个人信息
		FindPersonInfoUtil.findPersonInfo(infos.getMessageUserId(),
				new FindPersonInfoListener() {

					

					@Override
					public void getPersonInfo(List<Person> person) {
						holder.messageUsernameTv.setText(person.get(0).getNick());
						// 使用ImageLoaderUtil下载图片
						loader = ImageLoaderUtil.getInstance(context);
						DisplayImageOptions options = ImageLoaderUtil.getOpt();
						loader.displayImage(person.get(0).getHead().getFileUrl(),
								holder.messageHead, options);
					}
				});
		
		//分享
		holder.sharepicIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!MyApplication.api.isWXAppInstalled()) {
				      Toast.makeText(context, "您还未安装微信客户端", 0).show();
				      return;
				    }
				loader = ImageLoaderUtil.getInstance(context);
				DisplayImageOptions options = ImageLoaderUtil.getOpt();
				loader.loadImage(infos.getMessagePics().get(0).getFileUrl(), options, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View arg1, Bitmap loadedImage) {
						File file = new File("/sdcard/image");
						OutputStream stream;
						try {
							stream = new FileOutputStream(file);
							loadedImage.compress(CompressFormat.PNG, 100, stream );
							Intent intent = new Intent();
							ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
							intent.setComponent(comp);
							intent.setAction(Intent.ACTION_SEND);
							intent.setType("image/*");
							intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
							context.startActivity(intent);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
				
			}
		});
		
		
		boolean flag = true;// 判断收藏,为真时表示未收藏

		// 使用holder对象，可以直接使用ActionPraiseViewHolder的属性
		final MessagePraiseViewHolder messageHolder = new MessagePraiseViewHolder();
		messageHolder.infos = infos;
		messageHolder.flag = flag;

		// 显示用户当前活动的收藏状态
		List<String> praiseMessages = MyApplication.person.getPraiseAction();
		if (praiseMessages.contains(messageHolder.infos.getObjectId())) {
			holder.lovepicIv.setImageResource(R.drawable.love3);
			messageHolder.flag = false;
		} else {
			holder.lovepicIv.setImageResource(R.drawable.love0);
			messageHolder.flag = true;
		}

		// 收藏
		holder.lovepicIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				messageCollect(holder.lovepicIv);
			}

			/**
			 * 收藏和取消收藏操作
			 * 
			 * @param lovepicIv
			 */
			private void messageCollect(final ImageView lovepicIv) {
				if (messageHolder.flag) {// 未收藏状态

					// 修改活动表的收藏数据，把收藏此活动的用户的id存下来
					MessageInfo messageInfo = new MessageInfo();
					// 原子计数器
					messageInfo.increment("loveCount"); // 分数递增1
					MyApplication.putData("loveCount",
							messageInfo.getLoveCount());
					messageInfo.add("praiseUsers",
							MyApplication.person.getObjectId());
					messageInfo.update(messageHolder.infos.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										// 改变显示的收藏的图片
										lovepicIv
												.setImageResource(R.drawable.love3);
									}
								}
							});
					// 修改用户表中的收藏数据，把当前活动的id存到表中
					Person person = new Person();
					person.add("praiseAction",
							messageHolder.infos.getObjectId());
					person.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException arg0) {

								}
							});
					// 修改收藏状态
					messageHolder.flag = false;
				} else {// 已收藏状态
					// 修改活动表的收藏数据，把收藏此活动的用户的id清除掉
					MessageInfo messageInfo = new MessageInfo();
					// 原子计数器
					messageInfo.increment("loveCount", -1); // 分数递减少1
					MyApplication.putData("loveCount",
							messageInfo.getLoveCount());
					ArrayList<String> removePersonId = new ArrayList<String>();
					removePersonId.add(MyApplication.person.getObjectId());
					messageInfo.removeAll("praiseUsers", removePersonId);
					messageInfo.update(messageHolder.infos.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException e) {
									if (e == null) {
										lovepicIv
												.setImageResource(R.drawable.love0);
									} else {
										Log.i("bmob",
												"收藏删除用户Id失败：" + e.getMessage());
									}
								}
							});
					// 修改用户表中的收藏数据，把当前活动的id从表中移除
					Person rePerson = new Person();
					ArrayList<String> removeMessageIds = new ArrayList<String>();
					removeMessageIds.add(messageHolder.infos.getObjectId());
					rePerson.removeAll("praiseAction", removeMessageIds);
					rePerson.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException e) {
									if (e == null) {
										Log.i("bmob", "成功");
									} else {
										Log.i("bmob",
												"收藏删除活动Id失败：" + e.getMessage());
									}
								}
							});
					// 修改收藏状态
					messageHolder.flag = true;
				}
			}
		});
		// v.setOnClickListener(new MyOnClickListener(position,v));
		return v;
	}

	/*
	 * class MyOnClickListener implements OnClickListener{
	 * 
	 * private int index; private View v; MyOnClickListener(int index,View v){
	 * this.index = index; this.v = v; }
	 * 
	 * @Override public void onClick(View arg0) { Intent intent = new
	 * Intent(context, CurrItemMessageActivity.class); intent.putExtra("index",
	 * index); context.startActivity(intent); }
	 * 
	 * }
	 */

	// 收藏所需的对象信息
	class MessagePraiseViewHolder {
		MessageInfo infos;
		boolean flag;
	}

	// 设置一个容器:用来存放item中的每一个控件
	class ViewHolder {
		MyImageView messageHead;
		TextView messageTitleTv, messageTypeTv, messageDescTv, messageTimeTv,
				messageAddressTv, messageUsernameTv, messageDistanceTv;
		ImageView messagepicIv, lovepicIv,sharepicIv;
		GridView messagePicGv;
	}

	/**
	 * 向外暴露更新adapter的数据源的方法
	 * 
	 * @param allInfos
	 */
	public void updateInfos(List<MessageInfo> allInfos) {
		this.allInfos = allInfos;
		notifyDataSetChanged();
	}
}
