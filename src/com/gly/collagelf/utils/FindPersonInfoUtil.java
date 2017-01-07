package com.gly.collagelf.utils;

import java.util.List;

import com.gly.collagelf.bean.Person;

import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 使用观察者模式
 * 从服务器获取用户信息的工具类
 * @author 高留洋
 *
 */
public class FindPersonInfoUtil {

	public static void findPersonInfo(String personObjectId,final FindPersonInfoListener listener){
		BmobQuery<Person> query = new BmobQuery<Person>();
//		Log.i("bmob","哈哈哈，personObjectId："+personObjectId.toString());
/*		query.getObject(personObjectId, new QueryListener<Person>() {

		    @Override
		    public void done(Person person, BmobException e) {
		        if(e==null){
		        	listener.getPersonInfo(person);
		        	
		        	 Log.i("bmob","find user success："+person.toString());
		        	 for (int i = 0; i < person.getPraiseAction().size(); i++) {
		        		 Log.i("bmob", person.getPraiseAction().get(i).toString());	
					}
		        }else{
		            Log.i("bmob","查询用户失败。。。："+e.getMessage()+","+e.getErrorCode());
		        }
		    }

		});*/
		query.addWhereEqualTo("objectId", personObjectId);
		query.findObjects(new FindListener<Person>() {
		    @Override
		    public void done(List<Person> person,BmobException e) {
		        if(e==null){
		        	listener.getPersonInfo(person);
//		            toast("查询用户成功:"+object.size());
		            Log.i("bmob","find user success："+person.size()+person.toString());
		        }else{
		            //toast("更新用户信息失败:" + e.getMessage());
		        	Log.i("bmob","更新用户信息失败："+e.getMessage());
		        }
		    }
		});
	}
	
	//观察者模式
	public interface FindPersonInfoListener{
		void getPersonInfo(List<Person> person);
	}
}
