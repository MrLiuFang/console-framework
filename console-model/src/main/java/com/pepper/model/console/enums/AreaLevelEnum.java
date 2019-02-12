package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 省市区级别枚举
 * 
 * @author weber
 *
 */
public enum AreaLevelEnum implements IEnum {
	PROVINCE(0, "省"), CITY(1, "市"), BUTCHER(2, "区"), STREET(3, "街道");

	private final int key;

	private final String desc;

	private AreaLevelEnum(int key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public Integer getKey() {
		return key;
	}

	@Override
	public String getName() {
		return this.toString();
	}
	
	@Override
	@JsonValue
	public String getDesc(){
		return desc;
	}

}
