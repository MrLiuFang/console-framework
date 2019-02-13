package com.pepper.dao.console.role;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.role.Role;


/**
 * 
 * @author Mr.Liu
 *
 * @param <T>
 */

public interface RoleDao extends BaseDao<Role> {

	/**
	 * 根据用户id查询改用户拥有的请求资源
	 * 
	 * @param userId
	 * @return
	 */
	@Query(" select c.url from RoleUser a, RoleMenu b, Menu c where a.roleId=b.roleId and b.menuId=c.id and a.userId=?1 and c.status=1 ")
	List<String> queryUserAllResources(String userId);

	/**
	 * 根据用户id查询改用户拥有的请求资源
	 * 
	 * @param userId
	 * @return
	 */
	@Query(" select c from RoleUser a, RoleMenu b, Menu c where a.roleId=b.roleId and b.menuId=c.id and a.userId=?1 and c.status=1 ")
	List<Menu> queryUserAllMenu(String userId);

	/**
	 * 根据用户id查询改用户拥有的请求资源（只获取code)
	 * 
	 * @param userId
	 * @return
	 */
	@Query(" select c.code from RoleUser a, RoleMenu b, Menu c where a.roleId=b.roleId and b.menuId=c.id and a.userId=?1 and c.status=1 ")
	List<String> queryUserAllMenuCode(String userId);

	/**
	 * 根据code获取记录
	 * 
	 * @param code
	 * @return
	 */
	Role findByCode(String code);

	/**
	 * 
	 * @Title: findByUserId
	 * @author: drake
	 * @Description: 根据用户id查角色
	 * @param userId
	 * @return: Role
	 */
	@Query(" select r from Role r , RoleUser u where r.id=u.roleId and u.userId = ?1")
	Role findByUserId(String userId);

	/**
	 * 根据name获取记录
	 * 
	 * @param code
	 * @return
	 */
	Role findByName(String name);

}
