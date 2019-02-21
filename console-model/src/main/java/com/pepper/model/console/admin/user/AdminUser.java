package com.pepper.model.console.admin.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.pepper.common.emuns.Gender;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseModel;
import com.pepper.core.validator.Insert;
import com.pepper.core.validator.Update;
import com.pepper.model.console.enums.UserType;

/**
 * 用户模型
 *
 * @author mrliu
 *
 */
@Entity()
@Table(name = "t_admin_user")
@DynamicUpdate(true)
public class AdminUser extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -7877606290381395545L;

	/**
	 * 用户姓名
	 */
	@NotBlank(message="用户姓名不能为空",groups={Insert.class,Update.class})
	@Length(min=1,max=30,message="用戶姓名'${validatedValue}'长度在{min}和{max}之间",groups={Insert.class,Update.class})
	@Column(name = "name",length=30,nullable=false)
	private String name;

	/**
	 * 登录帐号
	 */
	@Column(name = "account", unique = true)
	private String account;

	/**
	 * 帐号密码
	 */
	@Column(name = "password", nullable=false,length=32)
	private String password;

	/**
	 * 用户昵称
	 */
	@Column(name = "nick_name",length=32)
	private String nickName;

	/**
	 * 用户手机
	 */
	@Column(name = "mobile",length=20)
	private String mobile;

	/**
	 * 邮箱
	 */
	@Column(name = "email",length=32)
	private String email;

	/**
	 * 头像
	 */
	@Column(name = "head_portrait")
	private String headPortrait;

	/**
	 * 性别
	 */
	@Column(name = "gender")
	private Gender gender;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Status status;

	/**
	 * 用户类型
	 */
	@Column(name = "user_type")
	private UserType userType;

	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_time")
	private Date lastLoginTime;

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public Gender getGender() {
		return gender;
	}


}
