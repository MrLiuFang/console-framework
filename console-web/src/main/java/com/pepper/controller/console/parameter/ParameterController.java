package com.pepper.controller.console.parameter;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;

import org.apache.dubbo.config.annotation.Reference;
import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.validator.Validator.Insert;
import com.pepper.core.validator.Validator.Update;
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
@Validated
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
	public String toEdit(@NotBlank(message="请选择要编辑的数据")String id) {
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
	public ResultData add(@Validated(value= {Insert.class}) Parameter parameter,BindingResult bindingResult) {
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
	public ResultData update(@Validated(value= {Update.class}) Parameter parameter,BindingResult bindingResult) {
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
	public ResultData delete(@NotBlank(message="请选择要删除的数据") String id) {
		parameterService.deleteById(id);
		return new ResultData();
	}
}
