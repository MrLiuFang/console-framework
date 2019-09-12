package com.pepper.model.console.role;

import java.util.List;

import com.pepper.model.console.menu.MenuVo;

public class RoleVo extends Role{

	/**
	 * 
	 */
	private static final long serialVersionUID = -357586659108797091L;
	
	private List<MenuVo> menu;

	public List<MenuVo> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuVo> menu) {
		this.menu = menu;
	}
	
	

}
