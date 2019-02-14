package com.pepper.controller.console.area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.exception.BusinessException;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.parameter.Area;
import com.pepper.model.console.parameter.vo.AreaVO;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.area.AreaService;

/**
 * 地区
 *
 * @author weber
 *
 */
@Controller
@RequestMapping("/admin/area")
public class AreaController extends BaseControllerImpl {

	@Reference
	AreaService areaService;

	@Resource
	ConsoleAuthorize consoleAuthorize;

	/**
	 * 页面框架
	 *
	 * @return
	 */
	@RequestMapping(value = "/index")
	@Authorize
	public String index() {
		return "Area/support_area";
	}

	/**
	 * 页面数据
	 *
	 * @return
	 */
	@RequestMapping(value = "/list")
	@Authorize
	@ResponseBody
	public Object list() {
		Pager<Area> pager = new Pager<Area>();
		pager = areaService.findNavigator(pager);
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
		return "Area/support_area_add";
	}

	/**
	 * 修改页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@Authorize
	public String toEdit(String id) {
		request.setAttribute("Area", areaService.findById(id));
		return "Area/support_area_update";
	}

	/**
	 * 查看页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/toView")
	@Authorize
	public String toView(String id) {
		request.setAttribute("Area", areaService.findById(id));
		request.setAttribute("action", "view");
		return "Area/support_area_update";
	}

	/**
	 * 新增提交
	 *
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/add")
	@Authorize
	@ResponseBody
	public ResultData add(Area area) {
		area.setCreateDate(new Date());
		AdminUser currentUser = (AdminUser) this.consoleAuthorize.getCurrentUser();
		area.setCreateUser(currentUser.getId());
		areaService.save(area);
		return new ResultData().setLoadUrl("/admin/Area/index");
	}

	/**
	 * 修改提交
	 *
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/update")
	@Authorize
	@ResponseBody
	public ResultData update(Area area) {
		area.setUpdateDate(new Date());
		AdminUser currentUser = (AdminUser) this.consoleAuthorize.getCurrentUser();
		area.setUpdateUser(currentUser.getId());
		areaService.update(area);
		return new ResultData().setLoadUrl("/admin/Area/index");
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
		areaService.deleteById(id);
		return new ResultData();
	}

	/**
	 * 检测分类是否已经存在
	 *
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/checkAreaExist")
	@ResponseBody
	public Object checkAreaExist(String code) {
		Area area = null;
		if (org.springframework.util.StringUtils.hasText(code)) {
			area = areaService.findByCode(code);
		}
		if (area!=null) {
			return new ResultData().setData("exist", true);
		} else {
			return new ResultData().setData("exist", false);
		}
	}

	/**
	 * 检测父级地区是否已经存在
	 *
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/checkParentAreaExist")
	@ResponseBody
	public Object checkParentAreaExist(String parentCode, Integer level) {
		Area area = null;
		if (org.springframework.util.StringUtils.hasText(parentCode)  &&  level !=null) {
			area = areaService.findByCodeAndLevel(parentCode, level - 1);
		}
		if (area!=null) {
			return new ResultData().setData("exist", true);
		} else {
			return new ResultData().setData("exist", false);
		}
	}

	/**
	 * 根据地区code获取它的下级地区
	 *
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getAreaChilds")
	@ResponseBody
	public Object getAreaChilds(String code) {
		List<Area> Areas = areaService.getAreaChilds(code);
		return new ResultData().setData("list", Areas);
	}


	/**
	 * 根据地区code获取它的下级地区
	 *
	 * @param selectedCode
	 * @return
	 */
	@RequestMapping(value = "/getAll")
	@ResponseBody
	public Object getAll(String selectedCodeStr) {
		List<String> selectedCodeList=new ArrayList<>();
		if(StringUtils.isNotBlank(selectedCodeStr)) {
			selectedCodeList=Arrays.asList(selectedCodeStr.split(","));
		}
		HashMap<String, Object> map=new HashMap<>();
		map.put("code", 0);
		map.put("msg","success");
		map.put("data", getMenuTree(selectedCodeList));
		return map;
	}


	private List<AreaVO> getMenuTree(List<String> selectedCode) {
		List<Area> allList=areaService.findAll();
		List<AreaVO> reList = new ArrayList<>();
		List<Area> oneList = getList(allList,"0");
		for (Area one : oneList) {
			AreaVO oneVO = toVO(one, selectedCode);
			List<Area> twoList = getList(allList,one.getCode());
			if (twoList.size() > 0) {
				for (Area two : twoList) {
					AreaVO twoVO = toVO(two, selectedCode);
					List<Area> threeList =  getList(allList,two.getCode());
					if (threeList.size() > 0) {
						for (Area three : threeList) {
							AreaVO threeVO = toVO(three, selectedCode);
							twoVO.getChildren().add(threeVO);
						}
					}
					oneVO.getChildren().add(twoVO);
				}
			}
			reList.add(oneVO);
		}
		return reList;
	}

	private List<Area> getList(List<Area> allList,String code){
		List<Area> reList=new ArrayList<>();
		for(Area area:allList) {
			if(code.equals(area.getParentCode())){
				reList.add(area);
			}
		}
		return reList;
	}



	private AreaVO toVO(Area area, List<String> selectedCode) {
		AreaVO vo = new AreaVO();
		vo.setName(area.getAreaName());
		vo.setValue(area.getCode());
		vo.setSelected(selected(area.getCode(), selectedCode)?"selected":"");
		return vo;
	}

	private boolean selected(String code, List<String> selectedCode) {
		if (selectedCode != null) {
			for (String select : selectedCode) {
				if (code.trim().equals(select.trim())) {
					return true;
				}
			}
		}
		return false;
	}


}
