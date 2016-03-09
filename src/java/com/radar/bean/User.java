package com.radar.bean;

/**
 * @Description:业务用户bean
 * @ClassName:  User 
 * @author: sunshine  
 */
public class User{
	
	private String userId;//当前业务系统主键
	private String userFrom;//
	private String userState;//0，正常；1，删除；2，注销；
	private String extension1;//
	private String extension2;//

	/**
	 * 用户中心主键
	 */
	private String id;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 是否锁定 false表示没有锁住
	 */
	private Boolean locked;
	
	/**
	 * 是否过期 false 表示没有过期
	 */
	private Boolean expire;
	
	/**
	 * -1：认证未通过，0：未认证，1：认证通过，2：待认证
	 */
	private Integer isAuthentication;
	
	/**
	 * 登录起点 1:app，2：website
	 */
	private String from;
	
	/**
	 * app端注册或登录成功后系统生成的用户token
	 */
	private String token;
	
	/**
	 * 用户类型（0：超级管理员、100：区域用户<只能web端登录>、200：机构用户<只能web端登录>、300：会员用户<只能App端登录>、400：咨询人员<负责处理咨询回复>、500：会计<只能App端登录>）
	 */
	private String type;
	
	/**
	 * 设备唯一标识码
	 */
	private String imie;
	
	/**
	 * 用户扩展信息
	 */
	private ExtUser extUser;
	/**
	 * 是否能够发送通知（针对会员）
	 */
	private Boolean hasSend;
	
	public Boolean getHasSend() {
		return hasSend;
	}

	public void setHasSend(Boolean hasSend) {
		this.hasSend = hasSend;
	}

	/**
	 * ios设备Id
	 */
	private String iosToken;
	
	public User() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.userId=id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean isLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean isExpire() {
		return expire;
	}

	public void setExpire(Boolean expire) {
		this.expire = expire;
	}

	public Integer getIsAuthentication() {
		return isAuthentication;
	}

	public void setIsAuthentication(Integer isAuthentication) {
		this.isAuthentication = isAuthentication;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ExtUser getExtUser() {
		return extUser;
	}

	public void setExtUser(ExtUser extUser) {
		this.extUser = extUser;
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getIosToken() {
		return iosToken;
	}

	public void setIosToken(String iosToken) {
		this.iosToken = iosToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		this.id=userId;
	}

	public String getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getExtension1() {
		return extension1;
	}

	public void setExtension1(String extension1) {
		this.extension1 = extension1;
	}

	public String getExtension2() {
		return extension2;
	}

	public void setExtension2(String extension2) {
		this.extension2 = extension2;
	}

	public String getPassword() {
		return password;
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	
}
