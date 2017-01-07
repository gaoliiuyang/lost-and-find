package com.gly.collagelf.activity;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人中心
 * 
 * @author 高留洋
 * 
 */
public class MySelfActivity extends BaseActivity implements BaseInterface,
		OnClickListener {

	private ImageButton mineBack;
	private ImageView mineHead;
	private TextView mineNick, minePhone;
	
	// 加载和缓存头像
	private ImageLoader mImageLoader;
	private BmobFile userhead;// 用户头像
	// 保存用户头像
	public File savePersonHeadFile = new File("sdcard/myhead.jpg");
	//更多
	private LinearLayout mineSetting,minelove,mineCook,mineXsh,mineSt,mineZy,minePxh,mineKy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myself);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		mineBack = (ImageButton) findViewById(R.id.frag_mine_back);
		mineHead = (ImageView) findViewById(R.id.frag_mine_head);
		mineNick = (TextView) findViewById(R.id.frag_mine_nick);
		minePhone = (TextView) findViewById(R.id.frag_mine_phone);
		mineBack.setOnClickListener(this);
		mineHead.setOnClickListener(this);
		
		mineSetting = (LinearLayout) findViewById(R.id.act_mine_setting);
		mineSetting.setOnClickListener(this);
		minelove = (LinearLayout) findViewById(R.id.act_mine_love);
		minelove.setOnClickListener(this);
		mineCook = (LinearLayout) findViewById(R.id.act_mine_cook);
		mineCook.setOnClickListener(this);
		mineXsh = (LinearLayout) findViewById(R.id.act_mine_xueshenghui);
		mineXsh.setOnClickListener(this);
		mineSt = (LinearLayout) findViewById(R.id.act_mine_shetuan);
		mineSt.setOnClickListener(this);
		mineZy = (LinearLayout) findViewById(R.id.act_mine_zhiyuan);
		mineZy.setOnClickListener(this);
		minePxh = (LinearLayout) findViewById(R.id.act_mine_pingxihu);
		minePxh.setOnClickListener(this);
		mineKy = (LinearLayout) findViewById(R.id.act_mine_kaoyan);
		mineKy.setOnClickListener(this);
	}

	@Override
	public void initData() {

		// 获取图片加载和缓存的操作对象
		mImageLoader = ImageLoaderUtil.getInstance(this);
		// 调用从服务器获取数据的工具类，注册观察者，从服务器拿到需要的数据（用户头像）
		FindPersonInfoUtil.findPersonInfo(MyApplication.person.getObjectId(),
				new FindPersonInfoListener() {

					@Override
					public void getPersonInfo(List<Person> person) {
						if (person != null) {
							log("用户的ID是："+MyApplication.person.toString());
							mineNick.setText("昵称："+person.get(0).getNick());
							minePhone.setText("账户："+person.get(0).getPhone());
							// 获取头像并设置到控件上
							String uri = person.get(0).getHead().getFileUrl();
							DisplayImageOptions options = ImageLoaderUtil
									.getOpt();
							/**
							 * 参数一：图片的URI 参数二：显示图片的控件 参数三：图片的属性
							 */
							mImageLoader.displayImage(uri, mineHead, options);
						}
					}
				});
	}

	@Override
	public void initOperat() {

	}

	// 点击事件的监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag_mine_back:
			finish();
			break;
		case R.id.frag_mine_head:

			// 点击头像，弹出对话框让用户选择拍照或者进入相册
			AlertDialog.Builder builder = new Builder(this);
			builder.setNegativeButton("从相册选取",
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击"从相册选取"，进入相册选择并剪切图片
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							// 类型
							intent.setType("image/*");
							// 剪切
							intent.putExtra("crop", "true");
							// 剪切比例
							intent.putExtra("aspectX", 1);
							intent.putExtra("aspectY", 1);
							// 剪切的像素点
							intent.putExtra("outputX", 300);
							intent.putExtra("outputY", 300);

							intent.putExtra("scale", true);

							intent.putExtra("return-data", false);
							// 存放的位置
							intent.putExtra("output",
									Uri.fromFile(savePersonHeadFile));
							startActivityForResult(intent, 200);
						}
					});
			builder.setPositiveButton("拍照选取",
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE, null);
							// 存放的位置
							intent.putExtra("output",
									Uri.fromFile(savePersonHeadFile));
							startActivityForResult(intent, 300);
						}
					});
			builder.show();
			break;
		case R.id.act_mine_setting://个人设置
			startActivity(new Intent(this, MyselfSettingActivity.class));
			break;
		case R.id.act_mine_love://我的收藏
			startActivity(new Intent(this, MyselfLoveActivity.class));
			break;
		case R.id.act_mine_cook://附近美食
			startActivity(new Intent(this, NearCookActivity.class));
			break;
		case R.id.act_mine_xueshenghui:
			toast("暂未开通!");
			break;
		case R.id.act_mine_shetuan:
			toast("暂未开通!");
			break;
		case R.id.act_mine_pingxihu:
			toast("暂未开通!");
			break;
		case R.id.act_mine_kaoyan:
			toast("暂未开通!");
			break;
		case R.id.act_mine_zhiyuan:
			toast("暂未开通!");
			break;
		}
	}

	// 头像获取的回调方法
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 200:
			userHeadUpLoading();
			break;
		case 300:
			
			//对照相机拍的照片进行剪切
			Intent intent = new Intent("com.android.camera.action.CROP");
		    intent.setDataAndType(Uri.fromFile(savePersonHeadFile), "image/*");
		    intent.putExtra("crop", "true");
		    intent.putExtra("aspectX", 1);
		    intent.putExtra("aspectY", 1);
		    intent.putExtra("outputX", 300);
		    intent.putExtra("outputY", 300);
		    intent.putExtra("scale", true);
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(savePersonHeadFile));
		    intent.putExtra("return-data", false);
		    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		    intent.putExtra("noFaceDetection", true); // no face detection
		    startActivityForResult(intent, 400);
			break;
		case 400:
			if (Uri.fromFile(savePersonHeadFile)!=null&&data!=null) {
				userHeadUpLoading();
			}else {
				toast("头像更新失败!");
			}
			break;
		}
	}

	/**
	 * 选完头像回来，上传到服务器,跟新用户信息
	 */
	public void userHeadUpLoading() {
		final ProgressDialog upLoadingProDialog = showProDialog(false, null,
				null);
//		toast("头像已经选中完毕");
		final Person person = new Person();
		person.setHead(new BmobFile(savePersonHeadFile));
		person.getHead().upload(new UploadFileListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					// 头像上传成功
//					toast("上传成功");
					person.update(MyApplication.person.getObjectId(),
							new UpdateListener() {

								@Override
								public void done(BmobException ex) {
									if (ex == null) {
										upLoadingProDialog.dismiss();
//										toast("用户信息更新成功");
										Bitmap bitmap = BitmapFactory
												.decodeFile(savePersonHeadFile
														.getAbsolutePath());
										mineHead.setImageBitmap(bitmap);
										person.setHead(new BmobFile(
												savePersonHeadFile));
									}
								}
							});
				}
			}
		});
	}

}
