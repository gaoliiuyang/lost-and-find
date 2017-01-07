package com.gly.collagelf.activity;

import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;

import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class LoginAct extends BaseActivity implements BaseInterface,
		OnClickListener {

	private EditText login_phone_et, login_pwd_et;
	private String loginPhone, loginPwd;
	private Button login_login_btn;
	private TextView login_register_tv;
	private SharedPreferences preferences;
	private ProgressDialog loginDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		login_phone_et = (EditText) findViewById(R.id.login_phone_et);
		login_pwd_et = (EditText) findViewById(R.id.login_pwd_et);
		login_login_btn = (Button) findViewById(R.id.login_login_btn);
		login_register_tv = (TextView) findViewById(R.id.login_register_tv);

		// 给两个按钮添加点击事件
		login_login_btn.setOnClickListener(this);
		login_register_tv.setOnClickListener(this);

	}

	@Override
	public void initData() {

		//创建SharedPreferences对象，用来存储用户的用户名和密码
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		if (preferences!=null) {
			login_phone_et.setText(preferences.getString("phone", null));
			login_pwd_et.setText(preferences.getString("pwd", null));
		}
	}

	@Override
	public void initOperat() {
		
	}

	// 点击事件的监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login_btn:
			//log("登录之前的peroson值:"+MyApplication.person.toString());
			loginDialog = showProDialog(false, null, null);
			// 获取输入框中的内容
			loginPhone = getTVContent(login_phone_et);
			loginPwd = getTVContent(login_pwd_et);
			// 查询所有的用户名和密码
			BmobQuery<Person> queryAll = new BmobQuery<Person>();

			queryAll.findObjects(new FindListener<Person>() {

				@Override
				public void done(List<Person> list, BmobException e) {
					if (e == null) {
						Boolean flag = false;
						for (int i = 0; i < list.size(); i++) {
							if (loginPhone.equals(list.get(i).getPhone())
									&& loginPwd.equals(list.get(i).getPwd())) {
								flag = true;
								//把当前登陆成功的用户的信息对象保存到缓存中
								MyApplication.person = list.get(i);
//								log("list.get(i):"+list.get(i).toString());
//								log("MyApplication.person:"+MyApplication.person.toString());
								//把用户登陆成功的用户名和密码保存到SharedPreferences中
								Editor editor = preferences.edit();
								editor.putString("phone", loginPhone);
								editor.putString("pwd", loginPwd);
								editor.commit();
								break;
							}
						}
						if (flag) {
							Toast.makeText(LoginAct.this, "登录成功", 0).show();
							loginDialog.dismiss();
							Intent intent = new Intent(LoginAct.this,
									HomeActivity.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(LoginAct.this, "用户名或密码错误，请重新输入!", 0)
									.show();
							loginDialog.dismiss();
							// login_phone_et.setText("");
							// login_pwd_et.setText("");
						}
					} else {
						Toast.makeText(LoginAct.this, e.getMessage(), 0).show();
					}
				}
			});
			break;

		case R.id.login_register_tv:
			startActivity(new Intent(this, RegisterAct.class));
			break;
		}
	}

	// 当从注册页面回来时会调用，负责把缓存中的数据回显到登陆输入框中
	@Override
	protected void onRestart() {
		super.onRestart();
		Person person = MyApplication.person;
		if (person != null) {
			login_phone_et.setText(person.getPhone());
			login_pwd_et.setText(person.getPwd());
		}
	}

}
