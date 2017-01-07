package com.gly.collagelf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
/**
 * 接受短信服务，通过观察者模式监听短信
 * @author 高留洋
 * 1 短信接受者
 * 2 清单文件中注册
 */
public class SMSCodeReceiver extends BroadcastReceiver {

	//接收到短信的回调方法
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Object[] objs = (Object[]) bundle.get("pdus");
		for (int i = 0; i < objs.length; i++) {
			byte[] bytes = (byte[]) objs[i];
			SmsMessage message = SmsMessage.createFromPdu(bytes);
			String data = message.getMessageBody();
			Log.i("MyTag", data);
			String codeData = data.substring(6, 12);
			Log.i("MyTag", codeData);
			getCodeSetCode(codeData);
		}
	}
	
	//声明一个接口
	public interface ISMSCodeListener{
		void setCode(String code);
	}

	//全局维护这个接口对象
	public static ISMSCodeListener listener;
	
	//注册观察者
	public static void setISMSCodeListener(ISMSCodeListener listener ){
		SMSCodeReceiver.listener = listener;
	}
	
	//获取并设置验证码
	public void getCodeSetCode(String code){
		if (listener!=null) {
			listener.setCode(code);
		}
	}
}
