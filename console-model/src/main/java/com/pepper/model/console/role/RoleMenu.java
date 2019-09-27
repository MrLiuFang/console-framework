package com.pepper.model.console.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import com.pepper.core.base.BaseModel;


@Entity()
@Table(name = "t_role_menu")
@DynamicUpdate(true)
public class RoleMenu extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3856559544740129673L;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id", nullable = false)
	private String roleId;

	/**
	 * 采单ID
	 */
	@Column(name = "menu_id", nullable = false)
	private String menuId;
	
	@Column(name = "must_exist")
	private Boolean mustExist;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
