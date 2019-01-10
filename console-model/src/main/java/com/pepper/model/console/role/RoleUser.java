package com.pepper.model.console.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;
import com.pepper.core.base.BaseModel;


@Entity()
@Table(name = "t_role_user")
@DynamicUpdate(true)
public class RoleUser extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3817944444112799554L;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id",nullable=false)
	private String roleId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id",nullable=false)
	private String userId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	private String roleCode;

	@Transient
	private String adminUserAccount;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getAdminUserAccount() {
		return adminUserAccount;
	}

	public void setAdminUserAccount(String adminUserAccount) {
		this.adminUserAccount = adminUserAccount;
	}

}
