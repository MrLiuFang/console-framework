
package com.pepper.service.console.parameter.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.dao.console.parameter.ParameterDao;
import com.pepper.model.console.parameter.Parameter;
import com.pepper.service.console.parameter.ParameterService;

/** @author mrliu @param <T> */
@Service(interfaceClass = ParameterService.class)
public class ParameterServiceImpl extends BaseServiceImpl<Parameter> implements ParameterService{

	@Resource
	private ParameterDao parameterDao;

	@Override
	public Parameter findByCode(String code) {
		return parameterDao.findByCode(code);
	}

	@Override
	public String findValueByCode(String code) {
		Parameter parameter = parameterDao.findByCode(code);
		if (parameter!=null) {
			return parameter.getValue();
		}
		return "";
	}

}
