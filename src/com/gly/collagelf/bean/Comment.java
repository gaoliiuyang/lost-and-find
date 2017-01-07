package com.gly.collagelf.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject{
	
	private String commentMessageInfoId;
	private String commentUserNick;
	private String commentInfos;
	public Comment(String commentMessageInfoId, String commentUserNick,
			String commentInfos) {
		super();
		this.commentMessageInfoId = commentMessageInfoId;
		this.commentUserNick = commentUserNick;
		this.commentInfos = commentInfos;
	}
	public Comment() {
		super();
	}
	public String getCommentMessageInfoId() {
		return commentMessageInfoId;
	}
	public void setCommentMessageInfoId(String commentMessageInfoId) {
		this.commentMessageInfoId = commentMessageInfoId;
	}
	public String getCommentUserNick() {
		return commentUserNick;
	}
	public void setCommentUserNick(String commentUserNick) {
		this.commentUserNick = commentUserNick;
	}
	public String getCommentInfos() {
		return commentInfos;
	}
	public void setCommentInfos(String commentInfos) {
		this.commentInfos = commentInfos;
	}
	@Override
	public String toString() {
		return "Comment [commentMessageInfoId=" + commentMessageInfoId
				+ ", commentUserNick=" + commentUserNick + ", commentInfos="
				+ commentInfos + "]";
	}

	
}
