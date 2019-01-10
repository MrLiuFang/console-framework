package com.pepper.model.console.parameter.vo;

import java.util.ArrayList;
import java.util.List;

public class AreaVO {
	private String name;
	private String value;
	private String selected="";//selected
	private List<AreaVO> children=new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<AreaVO> getChildren() {
		return children;
	}
	public void setChildren(List<AreaVO> children) {
		this.children = children;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}

}
