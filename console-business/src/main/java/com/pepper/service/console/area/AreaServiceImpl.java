
package com.pepper.service.console.area;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.dao.console.area.AreaDao;
import com.pepper.model.console.parameter.Area;

/**
 * 地区
 *
 * @author weber
 *
 */
@Service(interfaceClass = AreaService.class)
public class AreaServiceImpl extends BaseServiceImpl<Area> implements AreaService {

	@Resource
	private AreaDao supportAreaDao;

	@Override
	public Area findByCode(String code) {
		return supportAreaDao.findByCode(code);
	}

	@Override
	public List<Area> findByAreaName(String areaName) {
		return supportAreaDao.findByAreaName(areaName);
	}

	@Override
	public Area findByCodeAndLevel(String code, Integer level) {
		return supportAreaDao.findByCodeAndLevel(code, level);
	}

	@Override
	public List<Area> getAreaChilds(String code) {
		return supportAreaDao.getAreaChilds(code);
	}

	@Override
	public List<Area> findByLevel(Integer level) {
		return supportAreaDao.findByLevel(level);
	}

}
