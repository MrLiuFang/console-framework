package com.pepper.model.console.admin.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.pepper.common.emuns.Gender;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseModel;
import com.pepper.core.validator.Validator.Insert;
import com.pepper.core.validator.Validator.Update;
import com.pepper.model.console.enums.UserType;

/**
 * 用户模型
 *
 * @author mrliu
 *
 */
//@ScriptAssert()
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
//	@Size(min=3,max=30,message="用戶姓名'${validatedValue}'请输入{min}至{max}个字符",groups={Insert.class,Update.class})
	@Size(min=3,max=30,message="用戶姓名请输入{min}至{max}个字符",groups={Insert.class,Update.class})
	@Column(name = "name")
	private String name;

	/**
	 * 登录帐号
	 */
	@NotBlank(message="用户帐号不能为空",groups={Insert.class})
	@Size(min=3,max=30,message="用戶帐号请输入{min}至{max}个字符",groups={Insert.class})
	@Column(name = "account", unique = true,length=30)
	private String account;

	/**
	 * 帐号密码
	 */
//	@NotBlank(message="密码不能为空",groups={Insert.class})
	@Column(name = "password", nullable=false,length=32)
	private String password;

	/**
	 * 用户昵称
	 */
	@Size(min=3,max=30,message="用戶昵称请输入{min}至{max}个字符",groups={Insert.class})
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * 用户手机
	 */
	@NotBlank(message="电话号码不能为空",groups={Insert.class,Update.class})
	@Size(min=3,max=30,message="电话号码请输入{min}至{max}个字符",groups={Insert.class,Update.class})
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 邮箱
	 */
	@Email(message="请输入正确的邮箱",groups={Insert.class,Update.class})
	@NotBlank(message="邮箱不能为空",groups={Insert.class,Update.class})
	@Column(name = "email")
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
	
	@Column(name = "department_id")
	private String departmentId;
	
	@Column(name = "is_work")
	private Boolean isWork;
	
	@Column(name = "is_manager")
	private Boolean isManager;
	
	@Column(name = "department_group_id")
	private String departmentGroupId;

	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_time")
	private Date lastLoginTime;
	
	@Column(name = "is_never_expire")
	private Boolean isNeverExpire;
	
	@Column(name = "update_password_date")
	private Date updatePasswordDate;
	
	@Column(name = "automatic_logut_date")
	private Date automaticLogOutDate;
	
	private String userNo;

	

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

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

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Boolean getIsWork() {
		return isWork;
	}

	public void setIsWork(Boolean isWork) {
		this.isWork = isWork;
	}

	public Boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}

	public String getDepartmentGroupId() {
		return departmentGroupId;
	}

	public void setDepartmentGroupId(String departmentGroupId) {
		this.departmentGroupId = departmentGroupId;
	}

	public Boolean getIsNeverExpire() {
		return isNeverExpire;
	}

	public void setIsNeverExpire(Boolean isNeverExpire) {
		this.isNeverExpire = isNeverExpire;
	}

	public Date getUpdatePasswordDate() {
		return updatePasswordDate;
	}

	public void setUpdatePasswordDate(Date updatePasswordDate) {
		this.updatePasswordDate = updatePasswordDate;
	}

	public Date getAutomaticLogOutDate() {
		return automaticLogOutDate;
	}

	public void setAutomaticLogOutDate(Date automaticLogOutDate) {
		this.automaticLogOutDate = automaticLogOutDate;
	}

	
}
