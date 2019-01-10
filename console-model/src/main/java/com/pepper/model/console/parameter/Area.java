package com.pepper.model.console.parameter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import com.pepper.core.base.BaseModel;
import com.pepper.model.console.enums.AreaLevelEnum;

/**
 * 模型 CREATE TIME:Thu Apr 19 16:18:53 AWST 2018
 */
@Entity
@Table(name = "t_area")
@DynamicUpdate(true)
public class Area extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7722150931112661268L;
	/**
	 * 地区编码
	 */
	@Column(name = "code")
	private String code;
	/**
	 * 父级编码
	 */
	@Column(name = "parent_code")
	private String parentCode;
	/**
	 * 地区名
	 */
	@Column(name = "area_name")
	private String areaName;
	/**
	 * 层级
	 */
	@Column(name = "level")
	private AreaLevelEnum level;

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getParentCode() {
		return parentCode;
	}

	public void setParentCode(java.lang.String parentCode) {
		this.parentCode = parentCode;
	}

	public java.lang.String getAreaName() {
		return areaName;
	}

	public void setAreaName(java.lang.String areaName) {
		this.areaName = areaName;
	}

	public AreaLevelEnum getLevel() {
		return level;
	}

	public void setLevel(AreaLevelEnum level) {
		this.level = level;
	}
	
	

}
