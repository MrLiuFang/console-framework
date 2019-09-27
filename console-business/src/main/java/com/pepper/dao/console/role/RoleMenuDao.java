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

	List<RoleMenu> findByMenuId(String menuId);

	List<RoleMenu> findByRoleId(String roleId);
	
	
	List<RoleMenu> findByRoleIdAndMustExistIsTrue(String roleId);

	/**
	 * 根据角色id和菜单id获取对象
	 * 
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	RoleMenu findOneByRoleIdAndMenuId(String roleId, String menuId);

	/**
	 * 根据角色id返回menuId，格式List<String>
	 * 
	 * @param roleId
	 */
	@Query(" select menuId from RoleMenu where roleId=?1")
	List<String> findMenuIdsByRoleId(String roleId);

}
