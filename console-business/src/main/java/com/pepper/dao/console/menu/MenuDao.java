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

	@Query(value = " SELECT t1.* FROM t_menu t1 join t_role_menu t2 on t1.id = t2.menu_id  WHERE t1.parent_id= ?1 "
			+ " AND t2.role_id = ?2 AND status =?3 order by t1.sort asc ", nativeQuery = true)
	List<Menu> queryRoleChildMenu(String parentMenuId, String roleId, Integer status);

	@Query(value = " SELECT t2.* FROM t_role_menu t1 JOIN t_menu t2 ON t1.menu_id = t2.id WHERE t1.role_id = ?1 "
			+ " AND t2.parent_id = '0' AND t2.status = ?2 order by t2.sort asc", nativeQuery = true)
	List<Menu> queryRoleRootMenuByRoleId(String roleId, Integer status);

	@Query(value = " SELECT t3.* FROM t_role_user t1 JOIN t_role_menu t2 ON t1.role_id = t2.role_id "
			+ " JOIN t_menu t3 ON t2.menu_id = t3.id WHERE t1.user_id = ?1 ", nativeQuery = true)
	List<Menu> queryUserRoleMenu(String userId);

	/**
	 * 根据code查询
	 * 
	 * @param code
	 * @return
	 */
	@Query("from Menu where code =?1")
	Menu findByCode(String code);

	/**
	 * 根据parentId查询
	 * 
	 * @param parentId
	 * @return
	 */
	@Query(value = "from Menu where parentId =?1")
	List<Menu> findByParentId(String parentId);

	/**
	 * 根据parentId查询除了id记录外，还有没有其他子记录
	 * 
	 * @param parentId
	 * @param id
	 * @return
	 */
	@Query(value = "from Menu where parentId =?1 and id <> ?2 ")
	List<Menu> findByParentId(String parentId, String id);

	/**
	 * 获取所有
	 * 
	 * @return
	 */
	@Query(value = "select * from t_menu ", nativeQuery = true)
	List<Map<String, Object>> findAllToMap();

	/**
	 * 如果是超管，拥有所有的资源
	 * 
	 * @return
	 */
	@Query("select url from Menu ")
	List<String> findAllResourceUrl();

}
