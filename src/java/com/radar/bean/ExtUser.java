package com.radar.bean;

import java.io.Serializable;
import java.util.Date;
public class ExtUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 全名
	 */
	private String fullName;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 邮件
	 */
	private String email;
	
	/**
	 * 办公电话
	 */
	private String mobile;
	
	/**
	 * 是否对其他会员隐藏办公电话
	 */
	private String hideMobile;
	
	/**
	 * 社交账号
	 */
	private String socialAccount;
	
	/**
	 * 最后登录时间
	 */
	private Date lastActiveTime;
	
	/**
	 * 注册时间
	 */
	private Date registerTime;
	
	/**
	 * 支付账号
	 */
	private String alipayAccount;
	
	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 头像对应的biz_image表中记录
	 */
	private Image avatarImage;
	
	/**
	 * 个人简介
	 */
	private String biography;
	
	/**
	 * 用户状态 （在线与离线）
	 */
	private Boolean state;
	
	/**
	 * 职位
	 */
	private String position;
	/**
	 * 身份证
	 */
	private String idCard;
	/**
	 * 会计档案号
	 */
	private String accountantArchivesNo;
	/**
	 * 用户所属机构
	 */
	private String orgId;
	/**
	 * 岗位描述
	 */
	private String description;
	/**
	 * 专业
	 */
	private String professional;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSocialAccount() {
		return socialAccount;
	}

	public void setSocialAccount(String socialAccount) {
		this.socialAccount = socialAccount;
	}

	public Date getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAccountantArchivesNo() {
		return accountantArchivesNo;
	}

	public void setAccountantArchivesNo(String accountantArchivesNo) {
		this.accountantArchivesNo = accountantArchivesNo;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getHideMobile() {
		return hideMobile;
	}

	public void setHideMobile(String hideMobile) {
		this.hideMobile = hideMobile;
	}

	public Image getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(Image avatarImage) {
		this.avatarImage = avatarImage;
	}
	
}
