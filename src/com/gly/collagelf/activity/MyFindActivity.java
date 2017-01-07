package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;

/**
 * 我的招领页面
 * 
 * @author 高留洋
 * 
 */
public class MyFindActivity extends BaseActivity implements BaseInterface {

	private ImageView findBack;
	private LinearLayout beishangLL;
	private ListView findLv;
	private int screenWidth;// 屏幕宽度
	private List<MessageInfo> myFindMessageInfos = new ArrayList<MessageInfo>();
	private HomeAdapter myFindHomeAdapter;// 适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myfind);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		findBack = (ImageView) findViewById(R.id.act_myfind_back_iv);
		findLv = (ListView) findViewById(R.id.act_myfind_lv);
		beishangLL = (LinearLayout) findViewById(R.id.act_myfind_beishang_ll);
	}

	@Override
	public void initData() {

		// 返回到上一页
		findBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//长按删除
		findLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new Builder(MyFindActivity.this);
				builder.setMessage("确定要删除吗？");
				builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MessageInfo messageInfos = new MessageInfo();
						messageInfos.setObjectId(myFindMessageInfos.get(position).getObjectId());
						messageInfos.delete(new UpdateListener() {

						    @Override
						    public void done(BmobException e) {
						        if(e==null){
						            Log.i("bmob","成功");
						            initOperat();
						        }else{
						            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
						        }
						    }
						});
					}
				});
				builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MyFindActivity.this, "你已放弃本次操作！", 0).show();
					}
				});
				builder.show();
				return true;
			}
		});
	}

	@Override
	public void initOperat() {
		// ListView适配器
		myFindHomeAdapter = new HomeAdapter(this, myFindMessageInfos,
				screenWidth);
		findLv.setAdapter(myFindHomeAdapter);
		
		// --and条件1,当前用户id
		BmobQuery<MessageInfo> eq1 = new BmobQuery<MessageInfo>();
		eq1.addWhereEqualTo("messageUserId", MyApplication.person.getObjectId());
		// --and条件2,类型为“失物”
		BmobQuery<MessageInfo> eq2 = new BmobQuery<MessageInfo>();
		eq2.addWhereEqualTo("messageType", "招领");
		// 最后组装完整的and条件
		List<BmobQuery<MessageInfo>> andQuerys = new ArrayList<BmobQuery<MessageInfo>>();
		andQuerys.add(eq1);
		andQuerys.add(eq2);
		// 查询符合整个and条件的人
		BmobQuery<MessageInfo> query = new BmobQuery<MessageInfo>();
		query.and(andQuerys);
		query.order("-createdAt");
		query.findObjects(new FindListener<MessageInfo>() {
			@Override
			public void done(List<MessageInfo> messageInfos, BmobException e) {
				if (e == null) {
					if (messageInfos.size()==0) {
						beishangLL.setVisibility(View.VISIBLE);
						findLv.setVisibility(View.GONE);
					}else {
						// 把从服务器拿到的数据给全局信息集合
						myFindMessageInfos = messageInfos;
						// 跟新适配器的数据源
						myFindHomeAdapter.updateInfos(myFindMessageInfos);
					}
				} else {
					Log.i("bmob",
							"失败：" + e.getMessage() + "," + e.getErrorCode());
				}
			}
		});

	}

}
