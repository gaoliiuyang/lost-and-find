package com.gly.collagelf.activity;

import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.receiver.SMSCodeReceiver;
import com.gly.collagelf.receiver.SMSCodeReceiver.ISMSCodeListener;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;


public class RegisterAct extends BaseActivity implements BaseInterface,OnClickListener{

	private EditText reg_nick_et,reg_pwd_et,reg_repwd_et,reg_phone_et,reg_code_et;
	private String regNick,regPwd,regRepwd,regPhone,regCode;
	private Button reg_getcode_btn,reg_register_btn;
	private ImageButton reg_back_ibtn;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_register);
		initView();
		initData();
		initOperat();
	}
	@Override
	public void initView() {
		reg_nick_et = (EditText) findViewById(R.id.reg_nick_et);
		reg_pwd_et = (EditText) findViewById(R.id.reg_pwd_et);
		reg_repwd_et = (EditText) findViewById(R.id.reg_repwd_et);
		reg_phone_et = (EditText) findViewById(R.id.reg_phone_et);
		reg_code_et = (EditText) findViewById(R.id.reg_code_et);
		reg_getcode_btn = (Button) findViewById(R.id.reg_getcode_btn);
		reg_register_btn = (Button) findViewById(R.id.reg_register_btn);
		reg_back_ibtn = (ImageButton) findViewById(R.id.reg_back_ibtn);
		reg_getcode_btn.setOnClickListener(this);
		reg_register_btn.setOnClickListener(this);
		reg_back_ibtn.setOnClickListener(this);
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initOperat() {
		//注册成为观察者
		SMSCodeReceiver.setISMSCodeListener(new ISMSCodeListener() {
			//获取到验证码时，回调此方法
			//把验证码设置到文本框中
			@Override
			public void setCode(String code) {
				if (code!=null) {
					reg_code_et.setText(code);
				}
			}
		});
	}
	
	//按钮点击事件的监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//点击返回按钮，结束当前activity，回到上一个activity
		case R.id.reg_back_ibtn:
			finish();
			break;

		//点击获取验证码，按钮进入倒计时，通过Bmob云向手机发送验证码，并验证
		case R.id.reg_getcode_btn:
			//获取输入框中的内容
			regPhone = getTVContent(reg_phone_et);
			//判断手机号码是否合法
			if (!regPhone
					.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
				toast("手机号码不合法！");
			}else {
				//合法时进入倒计时，服务器发送验证码到手机
				//请求发送验证码
				BmobSMS.requestSMSCode(RegisterAct.this, regPhone, "驴友聚",new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if(ex==null){//验证码发送成功
				            Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
				        }
					}
				});
				
				//设置按钮不可点击
				reg_getcode_btn.setClickable(false);
				//倒计时
				CountDownTimer time = new CountDownTimer(60000,1000) {
					
					@Override
					public void onTick(long millisUntilFinished) {
						int s = (int) (millisUntilFinished/1000);
						reg_getcode_btn.setText(s+"s");
					}
					
					//倒计时结束
					@Override
					public void onFinish() {
						reg_getcode_btn.setText("获取验证码");
						reg_getcode_btn.setClickable(true);
					}
				};
				time.start();
			}
			
			break;
		
		//点击注册按钮，验证输入框中内容是否合法，以及通过Bmob云验证是否能够注册
		case R.id.reg_register_btn:
			//关闭广播接受者
			SMSCodeReceiver.setISMSCodeListener(null);
			//获取输入框中的内容
			regNick = getTVContent(reg_nick_et);
			regPwd = getTVContent(reg_pwd_et);
			regRepwd = getTVContent(reg_repwd_et);
			regPhone = getTVContent(reg_phone_et);
			regCode = getTVContent(reg_code_et);
			if (regNick.equals("")) {
				toast("昵称不能为空！");
			}else if(!regPwd.matches("^[0-9a-zA-Z]{6,16}")){
				toast("密码不合法！");
			}else if(!regPwd.equals(regRepwd)){
				toast("两次密码不同，请确认!");
			}else{
				//通过verifySmsCode方式可验证该短信验证码：
				BmobSMS.verifySmsCode(RegisterAct.this,regPhone, regCode, new VerifySMSCodeListener() {

				    @Override
				    public void done(BmobException ex) {
				        // TODO Auto-generated method stub
				        if(ex==null){//短信验证码已验证成功
//				            Log.i("bmob", "验证通过");
				            //判断是否能够注册
				            final Person person = new Person();
							person.setNick(regNick);
							person.setPhone(regPhone);
							person.setPwd(regPwd);
							person.save(new SaveListener<String>() {

								@Override
								public void done(String arg0,
										cn.bmob.v3.exception.BmobException e) {
									if (e == null) {
										toast("注册成功");
										//把电话号码和密码存到缓存对象中
										MyApplication.person = person;
										//取消广播
										SMSCodeReceiver.setISMSCodeListener(null);
										//结束掉自己，回到登陆页面
										finish();
									}else{
										Toast.makeText(RegisterAct.this, "注册失败!", 0).show();
									}
								}
							});
				        }else{
				            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
				        }
				    }
				});
			}
			break;
		}
		
	}

}
