package com.gly.collagelf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;

public class MyselfSettingActivity extends BaseActivity implements BaseInterface,OnClickListener{

	private ImageView myselfSettingBack;
	private EditText oldPwd,newPwd,newRepwd;
	private Button myselfSettingSubmit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myself_setting);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		myselfSettingBack = (ImageView) findViewById(R.id.act_myself_setting_back);
		oldPwd = (EditText) findViewById(R.id.act_myself_setting_oldPwd);
		newPwd = (EditText) findViewById(R.id.act_myself_setting_newPwd);
		newRepwd = (EditText) findViewById(R.id.act_myself_setting_newRepwd);
		myselfSettingSubmit = (Button) findViewById(R.id.act_myself_setting_btn);
	}

	@Override
	public void initData() {
		myselfSettingBack.setOnClickListener(this);
		myselfSettingSubmit.setOnClickListener(this);
	}

	@Override
	public void initOperat() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_myself_setting_back:
			finish();
			break;

		case R.id.act_myself_setting_btn:
			String myOldPwd = oldPwd.getText().toString().trim();
			String myNewPwd = newPwd.getText().toString().trim();
			String myNewRepwd = newRepwd.getText().toString().trim();
			if (myOldPwd.equals("") || myOldPwd == null) {
				toast("原密码不能为空!");
				return;
			}
			if (myNewPwd.equals("") || myNewPwd == null) {
				toast("新密码不能为空!");
				return;
			}
			if (myNewRepwd.equals("") || myNewRepwd == null) {
				toast("确认密码不能为空!");
				return;
			}
			if (!myNewPwd.equals(myNewRepwd)) {
				toast("两次密码不同，请重新输入");
				return;
			}
	
			if (!myOldPwd.equals(MyApplication.person.getPwd())) {
				toast("密码有误！");
				return;
			}
			Person person = new Person();
			person.setPwd(myNewPwd);
			person.update(MyApplication.person.getObjectId(),new UpdateListener() {
			    @Override
			    public void done(BmobException e) {
			        if(e==null){
			            toast("修改密码成功");
			            startActivity(new Intent(MyselfSettingActivity.this, LoginAct.class));
			        }else{
			            toast("修改密码失败:" + e.getMessage());
			        }
			    }
			});
			break;
		}
	}

}
