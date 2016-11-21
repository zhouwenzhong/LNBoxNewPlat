package com.lianyao.ftfbox.domain;

public class Contact {

	private int id;
	
	private String mobile; //手机号
	
	private String nickname; //昵称
	
	private String name; //称呼
	
	private String uid;
	
	private String tid;
	
	private int imgTag = 1;
	
	private String createDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getImgTag() {
		return imgTag;
	}

	public void setImgTag(int imgTag) {
		this.imgTag = imgTag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
