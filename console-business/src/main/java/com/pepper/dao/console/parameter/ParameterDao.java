package com.pepper.dao.console.parameter;

import org.springframework.data.jpa.repository.Query;
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
	@Query(" from Parameter where code =?1")
	Parameter findByCode(String code);

}
