package com.pepper.dao.console.parameter;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.parameter.Parameter;

/**
 * 
 * @author mrliu
 *
 * @param <T>
 */

public interface ParameterDao extends BaseDao<Parameter> {

	/**
	 * 根据code查询参数
	 * @param code
	 * @return
	 */
	Parameter findByCode(String code);

}
