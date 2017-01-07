package com.gly.collagelf.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Cook extends BmobObject{

	private BmobFile cookPic;
	private String cookTitle;
	private String cookMoney;
	private String cookAddress;
	public Cook() {
		super();
	}
	public BmobFile getCookPic() {
		return cookPic;
	}
	public void setCookPic(BmobFile cookPic) {
		this.cookPic = cookPic;
	}
	public String getCookTitle() {
		return cookTitle;
	}
	public void setCookTitle(String cookTitle) {
		this.cookTitle = cookTitle;
	}
	public String getCookMoney() {
		return cookMoney;
	}
	public void setCookMoney(String cookMoney) {
		this.cookMoney = cookMoney;
	}
	public String getCookAddress() {
		return cookAddress;
	}
	public void setCookAddress(String cookAddress) {
		this.cookAddress = cookAddress;
	}
	@Override
	public String toString() {
		return "Cook [cookPic=" + cookPic + ", cookTitle=" + cookTitle
				+ ", cookMoney=" + cookMoney + ", cookAddress=" + cookAddress
				+ "]";
	}
	
}
