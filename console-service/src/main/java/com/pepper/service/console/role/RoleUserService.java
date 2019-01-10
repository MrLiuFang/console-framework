package com.pepper.service.console.role;

import java.util.List;

import com.pepper.core.base.BaseService;
import com.pepper.model.console.role.RoleUser;

/**
 * 
 * @author Mr.Liu
 *
 */
public interface RoleUserService extends BaseService<RoleUser> {

	/**
	 * 删除用户所关联的角色
	 * 
	 * @param userId
	 * @return
	 */
	public void deleteRoleUserByUserId(String userId);

	/**
	 * 根据用户id查询对应角色
	 * 
	 * @param userId
	 * @return
	 */
	public RoleUser findByUserId(String userId);

	/**
	 * 根据用户id和角色id查找记录
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public RoleUser findByRoleIdAndUserId(String roleId, String userId);

	/**
	 * 根据角色查询记录
	 * 
	 * @param id
	 * @return
	 */
	public List<RoleUser> findByRoleId(String id);

	/**
	 * 根据用户ID获取用户角色
	 * @param userId
	 * @return
	 */
	public RoleUser findRoleUserByUserId(String userId);
}
