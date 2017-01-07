package com.gly.collagelf.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.AddMessageSpinnerAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baidumap.BaiduMapActivity;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.utils.CropPictureUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 添加失物或者招领信息的具体页面
 * 
 * @author 高留洋
 * 
 */ 
public class AddMessageActivity extends BaseActivity implements BaseInterface,
		OnClickListener {

	// 返回和提交按钮
	private ImageView addBack, addUp;
	// 标题，详情，电话号码控件
	private EditText addTitle, addDesc, addUsername, addPhone;
	// 下拉选择控件
	private Spinner addSpinner;
	private String addType;// 放用户选择的类型
	private String[] names = {"一卡通", "手机", "衣服" ,"饰品","钱包","书本","钥匙","宠物","其他"};// 数据源
	// 存放图片的控件
	private LinearLayout linearTop, linearButtom;
	private ImageView addPic;
	private List<Bitmap> pics = new ArrayList<Bitmap>();
	private String[] uploadPicsPaths;// 要上传的图片的数组，批量上传
	private int width;// 图片的宽高
	private int height;
	// 活动图片的缓存路径
	private File actionPic = new File("sdcard/uploadPic.jpg");
	// 选择地点控件
	private TextView selectMap, finallyMap;
	// 用户选择的地点的经纬度
	private double actionLong;// 经度
	private double actionLa;// 纬度
	private String city;// 用户选择的地点所在的城市

	private ProgressDialog uploadProDialog;// 活动上传时的进度条对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_message);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		// 输入框
		addTitle = (EditText) findViewById(R.id.act_add_title);
		addDesc = (EditText) findViewById(R.id.act_add_message);
		addUsername = (EditText) findViewById(R.id.act_add_username);
		addPhone = (EditText) findViewById(R.id.act_add_phone);

		// 返回和提交按钮
		addBack = (ImageView) findViewById(R.id.act_add_back);
		addUp = (ImageView) findViewById(R.id.act_add_up);
		addBack.setOnClickListener(this);
		addUp.setOnClickListener(this);

		// 类型的下拉框
		addSpinner = (Spinner) findViewById(R.id.act_add_spinner);

		// 选择图片
		linearTop = (LinearLayout) findViewById(R.id.act_linearTop);
		linearButtom = (LinearLayout) findViewById(R.id.act_linearBottom);

		// 地图
		selectMap = (TextView) findViewById(R.id.act_add_selectmap_tv);
		finallyMap = (TextView) findViewById(R.id.act_add_finallymap_tv);
		finallyMap.setText(MyApplication.getAddress());
		actionLong = MyApplication.getLongitude();
		actionLa = MyApplication.getLatitude();
		selectMap.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// 下拉列表spinner
		addSpinner.setAdapter(new AddMessageSpinnerAdapter(this, names));

		addSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 记录用户选择的类型
				addType = names[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	@Override
	public void initOperat() {

		/**
		 * 上传图片
		 */
		// 获取屏幕的宽度
		width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		height = width / 3 * 2;
		// 动态的添加控件
		addPic = new ImageView(this);
		addPic.setLayoutParams(new LayoutParams(width, height));
		addPic.setImageResource(R.drawable.gather_send_img_add);

		// 给linearLayout动态的添加图片
		linearTop.addView(addPic);

		// 给添加按钮添加点击事件，点击添加，跳到相册选择图片
		addPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 使用工具类处理跳转和剪切
				CropPictureUtil.cropPicture(AddMessageActivity.this,
						Uri.fromFile(actionPic));
			}
		});
	}

	// 按钮点击监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.act_add_back:
			finish();
			// 清空缓存中的图片
			actionPic.delete();
			// 清空集合中的图片
			pics.clear();
			break;

		// 提交
		case R.id.act_add_up:
			uploadProDialog = showProDialog(false, null, "正在上传...");
			publishMessage();
			// 清空缓存中的图片
			actionPic.delete();
			// 清空集合中的图片
			pics.clear();
			break;
		// 地图
		case R.id.act_add_selectmap_tv:
			startActivityForResult(new Intent(this, BaiduMapActivity.class),
					555);
			break;
		}
	}

	/**
	 * 发布信息
	 */
	private void publishMessage() {
		final String messageTitle = addTitle.getText().toString().trim();
		final String messageGoodsType = addType;
		final String messageType = (String) MyApplication.getDate(true, "messageType");
		final String messageDesc = addDesc.getText().toString().trim();
		final String messageUsername = addUsername.getText().toString().trim();
		final String messagePhone = addPhone.getText().toString().trim();
		final String messageAddress = finallyMap.getText().toString().trim();
		if (messageTitle == null || messageTitle.equals("")) {
			toast("标题不能为空！");
			uploadProDialog.dismiss();
			return;
		}
		if (messageDesc == null || messageDesc.equals("")) {
			toast("详情不能为空！");
			uploadProDialog.dismiss();
			return;
		}
		if (messageUsername == null || messageUsername.equals("")) {
			toast("联系人不能为空！");
			uploadProDialog.dismiss();
			return;
		}
		if (!messagePhone
				.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
			toast("手机号码不合法！");
			uploadProDialog.dismiss();
			return;
		}
		// 批量上传图片
		// 获取图片位置的数组
		uploadPicsPaths = new String[pics.size()];
		// 把集合中的图片存放到本地文件中
		for (int i = 0; i < pics.size(); i++) {
			File path = new File("sdcard/messagepic/upload");
			if (!path.exists()) {
				path.mkdirs();
			}
			File filePath = new File(path, "message" + i + ".jpg");
			try {
				pics.get(i).compress(CompressFormat.JPEG, 100,
						new FileOutputStream(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 把图片的路径放入到数组中
			uploadPicsPaths[i] = filePath.getAbsolutePath();
		}

		if (uploadPicsPaths.length < 1) {
			toast("请选择图片！");
			uploadProDialog.dismiss();
			return;
		}

		// 批量上传图片到服务器
		BmobFile.uploadBatch(uploadPicsPaths, new UploadBatchListener() {

			@Override
			public void onSuccess(List<BmobFile> bmobFiles, List<String> strings) {
				// 上传成功
				if (bmobFiles.size() < uploadPicsPaths.length) {
					return;
				}
				// toast("图片上传成功");
				MessageInfo messageInfo = new MessageInfo();
				messageInfo.setMessageName(messageTitle);// 信息标题
				messageInfo.setMessageType(messageType);// 信息的类型
				messageInfo.setMessageGoodsType(messageGoodsType);//物品的类型
				messageInfo.setMessageDesc(messageDesc);// 信息详情
				messageInfo.setMessageUsername(messageUsername);
				messageInfo.setMessagePhone(messagePhone);
				messageInfo.setMessageAddress(messageAddress);// 信息的地址
				messageInfo.setMessageUserId(MyApplication.person.getObjectId());// 发布信息的用户的Id
				// 信息的地点信息(经纬度)
				messageInfo.setMessageLocation(new BmobGeoPoint(actionLong,
						actionLa));
				messageInfo.setMessagePics(bmobFiles);// 信息的图片

				// 用户填写的信息提交到服务器的ActionInfo表中
				messageInfo.save(new SaveListener<String>() {

					@Override
					public void done(String arg0, BmobException ex) {
						if (ex == null) {
							toast("添加活动信息成功");
							uploadProDialog.dismiss();
							//取消定位
							MyApplication.mLocationClient.stop();
							finish();
						}
					}
				});
			}

			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	/**
	 * 选择地图时，把用户选择的地点回显到地图的文本框中 进入相册选择图片并剪切后，把处理后的图片显示到控件上
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 选择地图
		if (resultCode == 555) {
			String name = data.getStringExtra("name");
			city = data.getStringExtra("city");
			actionLong = data.getDoubleExtra("actionLong", 0);
			actionLa = data.getDoubleExtra("actionLa", 0);
			finallyMap.setText(name);
			//取消定位
//			MyApplication.mLocationClient.stop();
		}
		// 剪切后的图片显示到控件上
		if (requestCode == 200) {
			if (data != null) {
				showPicture();
			}
		}
	}

	/**
	 * 把图片显示到布局中
	 */
	private void showPicture() {

		// 往list中添加图片
		Bitmap bitmap = BitmapFactory.decodeFile(actionPic.getAbsolutePath());
		pics.add(bitmap);
		// 判断是否能放入
		if (pics.size() < 3) {// 放入第1、2张图片
			linearTop.removeAllViews();
			for (int i = 0; i < pics.size(); i++) {
				Bitmap currBitmap = pics.get(i);
				// 动态的添加控件
				ImageView imageView = new ImageView(AddMessageActivity.this);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currBitmap);
				linearTop.addView(imageView);

			}
			linearTop.addView(addPic);
		} else if (pics.size() == 3) {// 当放入第三张时，把添加的按钮清除
			linearTop.removeViewAt(2);
			Bitmap currBitmap = pics.get(2);
			ImageView imageView = new ImageView(AddMessageActivity.this);
			imageView.setLayoutParams(new LayoutParams(width, height));
			imageView.setImageBitmap(currBitmap);
			linearTop.addView(imageView);
			linearButtom.addView(addPic);
		} else if (pics.size() < 6) {// 添加第4、5张照片
			linearButtom.removeAllViews();
			for (int i = 3; i < pics.size(); i++) {
				Bitmap currBitmap = pics.get(i);
				// 动态的添加控件
				ImageView imageView = new ImageView(AddMessageActivity.this);
				imageView.setLayoutParams(new LayoutParams(width, height));
				imageView.setImageBitmap(currBitmap);
				linearButtom.addView(imageView);

			}
			linearButtom.addView(addPic);
		} else if (pics.size() == 6) {// 添加第六张时，把添加的按钮清除
			linearButtom.removeViewAt(2);
			Bitmap currBitmap = pics.get(5);
			ImageView imageView = new ImageView(AddMessageActivity.this);
			imageView.setLayoutParams(new LayoutParams(width, height));
			imageView.setImageBitmap(currBitmap);
			linearButtom.addView(imageView);
		}
	}
}
