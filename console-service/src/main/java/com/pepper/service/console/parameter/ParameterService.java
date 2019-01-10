package com.pepper.service.console.parameter;

import com.pepper.core.base.BaseService;
import com.pepper.model.console.parameter.Parameter;

/**
 * 
 * @author mrliu
 *
 */
public interface ParameterService extends BaseService<Parameter> {

	Parameter findByCode(String code);

	/**
	 * 根据code获取value
	 * 
	 * @param code
	 * @return
	 */
	String findValueByCode(String code);

}
