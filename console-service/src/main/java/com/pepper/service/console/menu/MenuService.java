package com.pepper.service.console.menu;

import java.util.List;
import java.util.Map;

import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseService;
import com.pepper.model.console.menu.Menu;

/**
 * 
 * @author Mr.Liu
 *
 */
public interface MenuService extends BaseService<Menu> {

	/**
	 * 根据角色ID和菜单父节点ID 查找子菜单
	 * 
	 * @param parentId
	 * @param parentId
	 * @return
	 */

	public List<Menu> queryRoleChildMenu(String parentId, String roleId, Status status);

	/**
	 * 根据角色Id查找第一级菜单
	 * 
	 * @param roleId
	 * @return
	 */

	public List<Menu> queryRootMenuByRoleId(String roleId, Status status);

	/**
	 * 根据菜单使用用户类型，菜单状态，菜单作用域,父节点ID 查找菜单（parentId=null 查找第一级菜单,parentId<>null
	 * 查找子节点菜单）
	 * 
	 * @param type
	 * @param status
	 * @param scope
	 * @return
	 */
	public List<Menu> queryMenu(Status status, Scope scope, String parentId);

	/**
	 * 根据作用域获取树list
	 * 
	 * @param searchParameter
	 * @return
	 */
	public List<Menu> getMenuTreeList(Map<String, Object> searchParameter);

	/**
	 * 根据code查询
	 * 
	 * @param code
	 * @return
	 */
	public Menu findByCode(String code);

	/**
	 * 根据parentId查询
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Menu> findByParentId(String parentId);

	/**
	 * 新增menu(菜单)
	 * 
	 * @param menu
	 */
	public void addMenu(Menu menu);

	/**
	 * 修改menu(菜单)
	 * 
	 * @param menu
	 */
	public void updateMenu(Menu menu);

	/**
	 * 删除menu(菜单)
	 * 
	 * @param menu
	 */
	public void deleteMenu(Menu menu);

	public List<Map<String, Object>> findAllToMap();

	/**
	 * 如果是超管，拥有所有的资源
	 * 
	 * @return
	 */
	public List<String> findAllResourceUrl();
}
