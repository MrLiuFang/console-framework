package com.pepper.dao.console.area;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.parameter.Area;

/**
 * 地区
 * 
 * @author weber
 *
 */
public interface AreaDao extends BaseDao<Area> {

	/**
	 * 根据code获取记录
	 * 
	 * @param code
	 * @return
	 */
	@Query(" from Area where code=?1 ")
	Area findByCode(String code);

	/**
	 * 根据areaName获取记录，通过名字查找是有可能重复的，比如“北京市”
	 * 
	 * @param areaName
	 * @return
	 */
	@Query(" from Area where areaName=?1 ")
	List<Area> findByAreaName(String areaName);

	/**
	 * 根据编码和级别获取记录，主要是校验新增或修改的时候，指定用户指定的父级节点是否存在。
	 * 
	 * @param code
	 * @param level
	 * @return
	 */
	@Query(" from Area where code=?1 and level=?2 ")
	Area findByCodeAndLevel(String code, Integer level);

	/**
	 * 根据地区code获取它的下级地区
	 * 
	 * @param code
	 * @return
	 */
	@Query(" from Area where parentCode=?1 ")
	List<Area> getAreaChilds(String code);

	/**
	 * 根据地区等级获取记录
	 * 
	 * @param level
	 * @return
	 */
	@Query(" from Area where level=?1 ")
	List<Area> findByLevel(Integer level);

}
