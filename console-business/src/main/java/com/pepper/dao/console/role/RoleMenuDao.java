package com.pepper.dao.console.role;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.role.RoleMenu;


/**
 * 
 * @author Mr.Liu
 *
 */

public interface RoleMenuDao extends BaseDao<RoleMenu> {

	@Query(" from RoleMenu where menuId=?1")
	List<RoleMenu> findByMenuId(String menuId);

	@Query(" from RoleMenu where roleId=?1")
	List<RoleMenu> findByRoleId(String roleId);

	/**
	 * 根据角色id和菜单id获取对象
	 * 
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	@Query(" from RoleMenu where roleId = ?1 and menuId=?2 ")
	RoleMenu findByRoleAndMenu(String roleId, String menuId);

	/**
	 * 根据角色id返回menuId，格式List<String>
	 * 
	 * @param roleId
	 */
	@Query(" select menuId from RoleMenu where roleId=?1")
	List<String> findMenuIdsByRoleId(String roleId);

}
