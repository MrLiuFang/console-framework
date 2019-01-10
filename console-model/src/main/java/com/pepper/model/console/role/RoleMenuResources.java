package com.pepper.model.console.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import com.pepper.core.base.BaseModel;


@Entity()
@Table(name = "t_role_menu_resources")
@DynamicUpdate(true)
public class RoleMenuResources extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -1037479084779351037L;

	/**
	 * 角色采单ID
	 */
	@Column(name = "role_menu_id",nullable=false)
	private String roleMenuId;

	/**
	 * 采单资源ID
	 */
	@Column(name = "menu_resources_id",nullable=false)
	private String menuResourcesId;

	public String getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleMenuId(String roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public String getMenuResourcesId() {
		return menuResourcesId;
	}

	public void setMenuResourcesId(String menuResourcesId) {
		this.menuResourcesId = menuResourcesId;
	}

}
