package com.pepper.dao.console.role;

import java.util.List;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.role.RoleUser;

/**
 * 
 * @author Mr.Liu
 *
 */

public interface RoleUserDao extends BaseDao<RoleUser> {

//	RoleUser findByUserId(String userId);
	
	List<RoleUser> findByUserId(String userId);

	List<RoleUser> findByRoleId(String roleId);

	/**
	 * 根据用户id和角色id查找记录
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	RoleUser findByRoleIdAndUserId(String roleId, String userId);

}
