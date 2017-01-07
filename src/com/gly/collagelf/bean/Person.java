package com.gly.collagelf.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Person extends BmobObject{

	private String nick;//用户昵称
	private String phone;//用户名
	private String pwd;//密码
	private BmobFile head;//用户头像
	private List<String> praiseAction;//当前用户收藏的id活动的集合
	public Person() {
		super();
	}
	public Person(String phone, String pwd) {
		super();
		this.phone = phone;
		this.pwd = pwd;
	}
	public Person(String nick, String phone, String pwd,BmobFile head) {
		super();
		this.nick = nick;
		this.phone = phone;
		this.pwd = pwd;
		this.head = head;
	}
	
	public BmobFile getHead() {
		return head;
	}
	public void setHead(BmobFile head) {
		this.head = head;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	

	
	
	@Override
	public String toString() {
		return "Person [nick=" + nick + ", phone=" + phone + ", pwd=" + pwd
				+ ", head=" + head + ", praiseAction=" + praiseAction + "]";
	}
	public List<String> getPraiseAction() {
		if (praiseAction == null) {
			praiseAction = new ArrayList<String>();
		}
		return praiseAction;
	}
	public void setPraiseAction(List<String> praiseAction) {
		this.praiseAction = praiseAction;
	}
	
}
