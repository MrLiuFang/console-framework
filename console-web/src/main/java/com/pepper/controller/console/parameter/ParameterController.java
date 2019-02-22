package com.pepper.controller.console.parameter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.model.console.parameter.Parameter;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.parameter.ParameterService;


/**
 * 参数管理
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping("/console/parameter")
public class ParameterController extends BaseControllerImpl implements BaseController{

	@Reference
	ParameterService parameterService;

	/**
	 * 页面框架
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index")
	@Authorize
	public String index() {
		return "parameter/parameter";
	}

	/**
	 * 列表数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@Authorize
	@ResponseBody
	public Pager<Parameter> list() {
		Pager<Parameter> pager = new Pager<Parameter>();
		pager = parameterService.findNavigator(pager);
		return pager;
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	@Authorize
	public String toAdd() {
		return "parameter/parameter_add";
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@Authorize
	public String toEdit(String id) {
		request.setAttribute("parameter", parameterService.findById(id));
		return "parameter/parameter_update";
	}

	/**
	 * 新增提交
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	@Authorize
	@ResponseBody
	public ResultData add(Parameter parameter) {
		parameterService.save(parameter);
		return new ResultData().setLoadUrl("/console/parameter/index");
	}

	/**
	 * 修改提交
	 * 
	 * @return
	 */
	@RequestMapping(value = "/update")
	@Authorize
	@ResponseBody
	public ResultData update(Parameter parameter) {
		parameterService.update(parameter);
		return new ResultData().setLoadUrl("/console/parameter/index");
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@Authorize
	@ResponseBody
	public ResultData delete(String id) {
		parameterService.deleteById(id);
		return new ResultData();
	}
}
