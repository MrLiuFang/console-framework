package com.pepper.service.console.area;

import java.util.List;

import com.pepper.core.base.BaseService;
import com.pepper.model.console.parameter.Area;


/**
 * 地区
 * 
 * @author weber
 *
 * @param <Area>
 */
public interface AreaService extends BaseService<Area> {

	/**
	 * 根据code获取记录
	 * 
	 * @param code
	 * @return
	 */
	Area findByCode(String code);

	/**
	 * 根据areaName获取记录，通过名字查找是有可能重复的，比如“北京市”
	 * 
	 * @param areaName
	 * @return
	 */
	List<Area> findByAreaName(String areaName);

	/**
	 * 根据编码和级别获取记录，主要是校验新增或修改的时候，指定用户指定的父级节点是否存在。
	 * 
	 * @param code
	 * @param level
	 * @return
	 */
	Area findByCodeAndLevel(String code, Integer level);

	/**
	 * 根据地区code获取它的下级地区
	 * 
	 * @param code
	 * @return
	 */
	List<Area> getAreaChilds(String code);

	/**
	 * 根据地区等级获取记录
	 * 
	 * @param level
	 * @return
	 */
	List<Area> findByLevel(Integer level);

}
