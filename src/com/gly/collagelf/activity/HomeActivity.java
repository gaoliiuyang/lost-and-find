package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.FragmentViewPagerAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.fragment.FindFragment;
import com.gly.collagelf.fragment.HomeFragment;
import com.gly.collagelf.fragment.LostFragment;
import com.gly.collagelf.fragment.MoreFragment;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;
import com.gly.collagelf.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends FragmentActivity implements BaseInterface,
		OnClickListener {

	/**
	 * 定义所用到的布局中的资源
	 */
	// 有关DrawerLayout里面内容的控件
	private DrawerLayout dl;
	private LinearLayout left, center;
	private TextView title_tv;// 标题中的文字
	/**
	 * 定义ViewPager里面的内容的控件
	 */
	// 选项卡
	private ViewPager pager;
	private LinearLayout[] linears = new LinearLayout[4];
	private int linearIds[] = { R.id.home_ll, R.id.lost_ll, R.id.find_ll,R.id.more_ll };
	private ImageView imgs[] = new ImageView[4];
	private int imgIds[] = { R.id.home_iv, R.id.lost_iv, R.id.find_iv,R.id.more_iv };
	private TextView tvs[] = new TextView[4];
	private int tvIds[] = { R.id.home_tv, R.id.lost_tv, R.id.find_tv,R.id.more_tv };
	// 创建Fragment集合
	private List<Fragment> fragments = new ArrayList<Fragment>();
	// 定义ImageView开关状态要显示的图片
	int imgGuan[] = { R.drawable.home1, R.drawable.lost1, R.drawable.ling1,R.drawable.more };
	int imgKai[] = { R.drawable.home4, R.drawable.lost4, R.drawable.find4,R.drawable.more2 };

	// 用于双击退出判断
	private boolean backFlag = false;

	// 加载和缓存头像
	private ImageLoader mImageLoader;

	// 用户信息
	private ImageView leftHeadIv, centerHeadIv;
	private TextView nickTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_home);
		Log.i("MyTag", "首页中的person值："+MyApplication.person.toString());
		initView();
		initData();
		initOperat();
		initDlOpers();
	}

	/**
	 * 抽屉的操作，产生动画效果
	 */
	private void initDlOpers() {
		dl.setDrawerListener(new DrawerListener() {
			float fromX = 1.0f;
			float fromY = 1.0f;

			@Override
			public void onDrawerStateChanged(int arg0) {
				getPersonInfo();
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				if (arg0 == left) {
					center.setX(200 * (arg1 * 100.0f) / 100.0f);
					// title.setX(200 * (arg1 * 100.0f) / 100.0f);
					// 创建缩放动画
					float toX = 1 - (arg1 * 0.4f);// 0.6-1
					ScaleAnimation scaleAnimation = new ScaleAnimation(fromX,
							toX, fromY, toX, Animation.RELATIVE_TO_PARENT,
							0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
					scaleAnimation.setFillAfter(true);
					scaleAnimation.setDuration(2);
					fromX = toX;
					fromY = toX;
					center.startAnimation(scaleAnimation);
				}
			}

			@Override
			public void onDrawerOpened(View arg0) {
				getPersonInfo();
			}

			@Override
			public void onDrawerClosed(View arg0) {
				getPersonInfo();
			}
		});
	}

	/**
	 * 点击头像调出抽屉
	 * 
	 * @param v
	 */
	public void MyClick(View v) {
		if (dl.isDrawerOpen(Gravity.LEFT) || dl.isDrawerOpen(Gravity.RIGHT)) {
			dl.closeDrawers();
		} else {
			dl.openDrawer(Gravity.LEFT);
		}

	}

	/**
	 * DrawerLayout左侧抽屉中的控件的点击事件
	 * 
	 * @param v
	 */
	public void MyselfClick(View v) {
		switch (v.getId()) {
		case R.id.mylost_tv:
			startActivity(new Intent(this, MyLostActivity.class));
			break;
		case R.id.myfind_tv:
			startActivity(new Intent(this, MyFindActivity.class));
			break;
		case R.id.myselfinfo_tv:
			startActivity(new Intent(this, MySelfActivity.class));
			break;
		case R.id.mycancel_tv:
//			Toast.makeText(this, "请先登录", 0).show();
			AlertDialog.Builder builder = new Builder(this);
			builder.setMessage("确定要注销吗？");
			builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					Log.i("MyTag", "注销中peroson值："+MyApplication.person.toString());
					MyApplication.person = null;
					startActivity(new Intent(HomeActivity.this, LoginAct.class));
				}
			});
			builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(HomeActivity.this, "你已放弃本次操作！", 0).show();
				}
			});
			builder.show();
			break;
		}
	}

	/**
	 * 控件的初始化
	 */
	@Override
	public void initView() {
		// 用户信息
		leftHeadIv = (ImageView) findViewById(R.id.act_home_lefthead_iv);
		centerHeadIv = (ImageView) findViewById(R.id.act_home_centerhead_iv);
		nickTv = (TextView) findViewById(R.id.act_home_nick);
		
		// DrawerLayout
		dl = (DrawerLayout) findViewById(R.id.dl);
		left = (LinearLayout) findViewById(R.id.left);
		center = (LinearLayout) findViewById(R.id.center);
		title_tv = (TextView) findViewById(R.id.title_tv);

		pager = (ViewPager) findViewById(R.id.pager);
		// 定义三个fragment
		fragments.add(new HomeFragment());
		fragments.add(new LostFragment());
		fragments.add(new FindFragment());
		fragments.add(new MoreFragment());
		for (int i = 0; i < 4; i++) {
			linears[i] = (LinearLayout) findViewById(linearIds[i]);
			imgs[i] = (ImageView) findViewById(imgIds[i]);
			tvs[i] = (TextView) findViewById(tvIds[i]);
			// 给三个选项卡设置监听
			linears[i].setOnClickListener(this);
		}
		// 默认显示选项卡中的第一个项
		imgs[0].setImageResource(imgKai[0]);
		tvs[0].setTextColor(Color.parseColor("#d81e06"));
	}

	/**
	 * 给控件设置数据
	 */
	@Override
	public void initData() {
		// 给ViewPager设置是适配器，把fragment显示到ViewPager上(要继承FragmentPagerAdapter)
		pager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
	}

	/**
	 * 控件的操作
	 */
	
	@Override
	public void initOperat() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			// ViewPager的item被选中时，更改选项卡中的图片和字体颜色
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					changes(position);
					break;
				case 1:
					changes(position);
					break;
				case 2:
					changes(position);
					break;
				case 3:
					changes(position);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		getPersonInfo();
	}

	/**
	 * 调用从服务器获取数据的工具类，注册观察者，从服务器拿到需要的数据（用户头像）
	 */
	public void getPersonInfo() {
		// 获取图片加载和缓存的操作对象
//		Log.i("MyTag", "服务器拿到需要的数据中peroson值："+MyApplication.person.getObjectId());
		mImageLoader = ImageLoaderUtil.getInstance(this);
		FindPersonInfoUtil.findPersonInfo(MyApplication.person.getObjectId(),
				new FindPersonInfoListener() {

					@Override
					public void getPersonInfo(List<Person> person) {
						if (person != null) {
							nickTv.setText(person.get(0).getNick());
//							Log.i("MyTag", "nick的值："+person.get(0).getNick());
							// 获取头像并设置到控件上
							String uri = person.get(0).getHead().getFileUrl();
							DisplayImageOptions options = ImageLoaderUtil
									.getOpt();
							/**
							 * 参数一：图片的URI 参数二：显示图片的控件 参数三：图片的属性
							 */
							mImageLoader.displayImage(uri, leftHeadIv, options);
							mImageLoader.displayImage(uri, centerHeadIv,
									options);
						}else{
							Log.i("MyTag", "nick的值vxxxxxxxxxxxxxxxxxxxx");
						}
					}
				});
	}

	// 点击选项卡中的选项，触发事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_ll:
			// 改变ViewPager当前显示的页
			pager.setCurrentItem(0);
			changes(0);
			break;
		case R.id.lost_ll:
			pager.setCurrentItem(1);
			changes(1);
			break;
		case R.id.find_ll:
			pager.setCurrentItem(2);
			changes(2);
			break;
		case R.id.more_ll:
			pager.setCurrentItem(3);
			changes(3);
			break;
		}
	}

	// 改变选项卡中的图片和字体颜色
	private void changes(int position) {
		for (int i = 0; i < 4; i++) {
			if (i == position) {
				imgs[i].setImageResource(imgKai[i]);
				tvs[i].setTextColor(Color.parseColor("#d81e06"));
				title_tv.setText(tvs[position].getText());
			} else {
				imgs[i].setImageResource(imgGuan[i]);
				tvs[i].setTextColor(Color.parseColor("#707070"));
			}
		}
	}

	// 双击退出
	@Override
	public void onBackPressed() {
		if (backFlag) {
			// 退出
			
			super.onBackPressed();
		} else {
			// 单击一次提示信息
			Toast.makeText(this, "双击退出", 0).show();
			backFlag = true;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 3秒之后，修改flag的状态
					backFlag = false;
				};
			}.start();
		}
	}

	/**
	 * 选择头像完成，返回到主页面，刷新主页面
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		dl.closeDrawer(left);
	}
}
