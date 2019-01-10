package com.pepper.service.console.role;

import java.util.List;

import com.pepper.core.Pager;
import com.pepper.core.base.BaseService;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.role.Role;

/**
 * 
 * @author Mr.Liu
 *
 */
public interface RoleService extends BaseService<Role> {

	/**
	 * 根据code获取记录
	 * 
	 * @param asText
	 * @return
	 */
	Role findByCode(String code);

	/**
	 * 删除角色，同时删除角色菜单中间表记录
	 * 
	 * @param id
	 */
	void deleteRole(String id);

	/**
	 * 更新role的资源，先删除原来role的资源，然后插入的资源
	 * 
	 * @param roleId
	 * @param resourceIds
	 */
	void updateRoleMenu(String roleId, String resourceIds);

	/**
	 * 保存角色，同时保存角色资源
	 * 
	 * @param role
	 * @param resourceIds
	 */
	Role saveRole(Role role, String resourceIds);

	/**
	 * 修改角色，同时修改角色资源
	 * 
	 * @param role
	 * @param resourceIds
	 */
	void updateRole(Role role, String resourceIds);

	/**
	 * 根据用户获取所有资源权限
	 * 
	 * @param userId
	 * @return
	 */
	List<String> queryUserAllResources(String userId);

	/**
	 * 根据用户获取所有资源权限
	 * 
	 * @param userId
	 * @return
	 */
	List<Menu> queryUserAllMenu(String userId);

	/**
	 * 根据用户获取所有资源权限（只获取code）
	 * 
	 * @param userId
	 * @return
	 */
	List<String> queryUserAllMenuCode(String userId);

	/**
	 * 
	 * @Title: findByUserId
	 * @author: drake
	 * @Description: 根据用户id获取所属角色
	 * @param userId
	 * @return: Role
	 */
	Role findByUserId(String userId);

	/**
	 * 根据角色名获取记录
	 * 
	 * @param name
	 * @return
	 */
	Role findByName(String name);

	/**
	 * 使用sql拼接的方式获取列表
	 * 
	 * @param page
	 * @param pageSize
	 * @param searchParameter
	 * @param sortParameter
	 * @param returnType
	 * @return
	 */
	Pager<Role> list(Pager<Role> pager);
}
