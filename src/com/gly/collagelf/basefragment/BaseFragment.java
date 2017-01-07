package com.gly.collagelf.basefragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
/**
 * fragment的父类
 * @author 高留洋
 *
 */
public class BaseFragment extends Fragment {

	public Activity act;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = activity;
	}

	/**
	 * 提示信息
	 * @param str
	 */
	public void toast(String str){
		Toast.makeText(act, str, 0).show();
	}
	
	/**
	 * 在LogCat中打印相关信息，用于调试
	 * @param str
	 */
	public void log(String str){
		Log.i("MyTag", str);
	}
	
	/**
	 * 获取TextView中用户输入的内容
	 * @param tv
	 * @return  内容
	 */
	public String getTVContent(TextView tv){
		String str = tv.getText().toString().trim();
		if (str == null) {
			str = "";
			return str;
		}
		return str;
	}
	
	/**
	 * 进度条对话框
	 * @param isCancel 是否可取消
	 * @param title  标题
	 * @param message 内容
	 * @return  进度条对话框对象
	 */
	public ProgressDialog showProDialog(boolean isCancel,CharSequence title,CharSequence message){
		ProgressDialog pDialog = new ProgressDialog(act);
		pDialog.setCancelable(isCancel);//设置是否可取消
		pDialog.setTitle(title);//设置标题
		pDialog.setMessage(message);//设置显示的内容
		pDialog.show();
		return pDialog;
	}
}
