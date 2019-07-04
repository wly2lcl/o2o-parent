package com.parent.o2o.model.user;

import java.io.Serializable;
import java.util.Date;

import com.parent.o2o.model.BaseModel;

public class UserInfo extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3276735136456599879L;
	//用户ID
	private Long userId;
	//用户代码
	private String userCode;
	//用户名
	private String userName;
	//用户密码
	private String userPwd;
	//用户创建时间
	private Date createTime;
	
	public Long getUserId() {
		return userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
