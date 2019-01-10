package com.pepper.dao.console.role;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.role.RoleUser;

/**
 * 
 * @author Mr.Liu
 *
 */

public interface RoleUserDao extends BaseDao<RoleUser> {

	@Query(" from RoleUser where userId = ?1 ")
	RoleUser findRoleUserByUserId(String userId);

	@Query(" from RoleUser where roleId = ?1 ")
	List<RoleUser> findByRoleId(String roleId);

	/**
	 * 根据用户id和角色id查找记录
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	@Query(" from RoleUser where roleId=?1 and userId=?2")
	RoleUser findByRoleIdAndUserId(String roleId, String userId);

}
