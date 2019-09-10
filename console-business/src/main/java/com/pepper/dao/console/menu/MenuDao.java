package com.pepper.dao.console.menu;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseDao;
import com.pepper.model.console.menu.Menu;


/**
 * 
 * @author Mr.Liu
 *
 * @param <T>
 */
public interface MenuDao extends BaseDao<Menu> {

	@Query(value = " SELECT t1 FROM Menu t1 join RoleMenu t2 on t1.id = t2.menuId  WHERE t1.parentId= ?1 "
			+ " AND t2.roleId = ?2 AND t1.status =?3 order by t1.sort asc ")
	List<Menu> queryRoleChildMenu(String parentMenuId, String roleId, Status status);

	@Query(value = " SELECT t2 FROM RoleMenu t1 JOIN Menu t2 ON t1.menuId = t2.id WHERE t1.roleId = ?1 "
			+ " AND t2.parentId = '0' AND t2.status = ?2 order by t2.sort asc")
	List<Menu> queryRoleRootMenuByRoleId(String roleId, Status status);

	@Query(value = " SELECT t3 FROM RoleUser t1 JOIN RoleMenu t2 ON t1.roleId = t2.roleId "
			+ " JOIN Menu t3 ON t2.menuId = t3.id WHERE t1.userId = ?1 ")
	List<Menu> queryUserRoleMenu(String userId);

	/**
	 * 根据code查询
	 * 
	 * @param code
	 * @return
	 */
	Menu findOneByCode(String code);
	
	Menu findOneByUrl(String url);

	/**
	 * 根据parentId查询
	 * 
	 * @param parentId
	 * @return
	 */
	List<Menu> findByParentId(String parentId);

	/**
	 * 根据parentId查询除了id记录外，还有没有其他子记录
	 * 
	 * @param parentId
	 * @param id
	 * @return
	 */
	List<Menu> findByParentIdAndIdNot(String parentId, String id);

	/**
	 * 获取所有
	 * 
	 * @return
	 */
	@Query(value = "from Menu ")
	List<Map<String, Object>> findAllToMap();

	/**
	 * 如果是超管，拥有所有的资源
	 * 
	 * @return
	 */
	@Query("select url from Menu ")
	List<String> findAllResourceUrl();

}
