package com.pepper.service.console.role;

import java.util.List;

import com.pepper.core.base.BaseService;
import com.pepper.model.console.role.RoleMenu;

/**
 * 
 * @author Mr.Liu
 *
 */
public interface RoleMenuService extends BaseService<RoleMenu> {

	/**
	 * 根据菜单ID删除角色关联的菜单
	 * 
	 * @param menuId
	 * @return
	 */
	public void deleteByMenuId(String menuId);

	/**
	 * 根据角色ID删除角色关联的菜单
	 * 
	 * @param menuId
	 * @return
	 */
	public void deleteByRoleId(String roleId);

	/**
	 * 根据角色id和菜单id获取对象
	 * 
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public RoleMenu findByRoleAndMenu(String roleId, String menuId);

	/**
	 * 根据角色id返回menuId，格式List<String>
	 * 
	 * @param roleId
	 */
	public List<String> findMenuIdsByRoleId(String roleId);
	
	/**
	 * 根据菜单id查找是否绑定角色
	 * 
	 * @param menuId
	 * @return
	 */
	public List<RoleMenu> findByMenuId(String menuId);
	
	/**
	 * 根据
	 * @param roleId
	 * @return
	 */
	public List<RoleMenu> findByRoleId(String roleId);

}
