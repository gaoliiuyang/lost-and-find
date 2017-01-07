package com.gly.collagelf.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
/**
 * 用户发布的信息
 * @author 高留洋
 *
 */
public class MessageInfo extends BmobObject{

	private String messageType;//活动类型  用户选择 ````
	private String messageTitle;//活动名称   用户输入 ````
	private String messageUserId;//活动发起用户Id 用来查询用户表  系统自动获取填入
	private String messageUsername;//联系人
	private String messagePhone;//联系电话
	private String messageGoodsType;//物品的类型
	
	private String messageAddress;//活动地点   用户选择/输入 
	private String messageDesc;//活动详细介绍  用户输入 ````
	private BmobGeoPoint messageLocation;//活动经纬度   用户选择/输入
	private List<String> praiseUsers;//记录点赞的用户名  //系统录入
//	private List<UserMsg> msgs;//用户评论内容   //系统录入
	private List<BmobFile> messagePics;//活动图片    //用户上传````
	private Boolean flag;// 当前活动状态(是否开始)   系统录入
	private Integer loveCount;//活动被收藏的次数，Integer对象类型（不要用int类型）
	public MessageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageInfo(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageName() {
		return messageTitle;
	}
	public void setMessageName(String messageName) {
		this.messageTitle = messageName;
	}
	public String getMessageUserId() {
		return messageUserId;
	}
	public void setMessageUserId(String messageUserId) {
		this.messageUserId = messageUserId;
	}
	public String getMessageAddress() {
		return messageAddress;
	}
	public void setMessageAddress(String messageAddress) {
		this.messageAddress = messageAddress;
	}
	public String getMessageDesc() {
		return messageDesc;
	}
	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
	public BmobGeoPoint getMessageLocation() {
		return messageLocation;
	}
	public void setMessageLocation(BmobGeoPoint messageLocation) {
		this.messageLocation = messageLocation;
	}
	public List<String> getPraiseUsers() {
		return praiseUsers;
	}
	public void setPraiseUsers(List<String> praiseUsers) {
		this.praiseUsers = praiseUsers;
	}
	public List<BmobFile> getMessagePics() {
		return messagePics;
	}
	public void setMessagePics(List<BmobFile> messagePics) {
		this.messagePics = messagePics;
	}
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public Integer getLoveCount() {
		return loveCount;
	}
	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}
	@Override
	public String toString() {
		return "MessageInfo [messageType=" + messageType + ", messageTitle="
				+ messageTitle + ", messageUserId=" + messageUserId
				+ ", messageUsername=" + messageUsername + ", messagePhone="
				+ messagePhone + ", messageGoodsType=" + messageGoodsType
				+ ", messageAddress=" + messageAddress + ", messageDesc="
				+ messageDesc + ", messageLocation=" + messageLocation
				+ ", praiseUsers=" + praiseUsers + ", messagePics="
				+ messagePics + ", flag=" + flag + ", loveCount=" + loveCount
				+ "]";
	}
	public String getMessageUsername() {
		return messageUsername;
	}
	public void setMessageUsername(String messageUsername) {
		this.messageUsername = messageUsername;
	}
	public String getMessagePhone() {
		return messagePhone;
	}
	public void setMessagePhone(String messagePhone) {
		this.messagePhone = messagePhone;
	}
	public String getMessageGoodsType() {
		return messageGoodsType;
	}
	public void setMessageGoodsType(String messageGoodsType) {
		this.messageGoodsType = messageGoodsType;
	}
	
}
