package com.gly.collagelf.wxapi;


import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	 //这两个参数在文档中没有找到，自己在代码里面找了会才找到，这两个常量代表了微信返回的消息类型，是对登录的处理还是对分享的处理，登录会在后面介绍到
	  private static final int RETURN_MSG_TYPE_LOGIN = 1;
	  private static final int RETURN_MSG_TYPE_SHARE = 2;

	  private static final String TAG = "WXEntryActivity";

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.act_wxshare);

	    //如果没回调onResp，八成是这句没有写
	    MyApplication.api.handleIntent(getIntent(), this);
	  }
	  
	//微信发送消息给app，app接受并处理的回调函数
	@Override
	public void onReq(BaseReq req) {
		Toast.makeText(this, "aaaaaaaaaa", 0).show();
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	//app发送消息给微信，微信返回的消息回调函数,根据不同的返回码来判断操作是否成功
	@Override
	public void onResp(BaseResp resp) {
		Toast.makeText(this, "bbbbbbbbbb"+resp.errCode, 0).show();
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Toast.makeText(this, "分享成功", 0).show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Toast.makeText(this, "分享失败", 0).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Toast.makeText(this, "分享失败", 0).show();
			break;
		case BaseResp.ErrCode.ERR_BAN:
			Toast.makeText(this, "分享失败....", 0).show();
			break;
		}
  }
	
	private void goToGetMsg() {
		Intent intent = new Intent(this, HomeAdapter.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		Log.i("MyTag", msg.toString());
		/*Intent intent = new Intent(this, ShowFromWXActivity.class);
		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
		startActivity(intent);*/
		finish();
	}
 
}

