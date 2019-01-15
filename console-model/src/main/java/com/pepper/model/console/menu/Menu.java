package com.pepper.model.console.menu;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseModel;
import com.pepper.model.console.enums.Level;
import com.pepper.model.console.enums.MenuType;

@Entity()
@Table(name = "t_menu")
@DynamicUpdate(true)
public class Menu extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3276956501879763573L;

	/**
	 * 采单名称
	 */
	@Column(name = "name" ,nullable=false)
	private String name;

	/**
	 * 采单编码
	 */
	@Column(name = "code" ,nullable=false,unique=true,length=200)
	private String code;

	/**
	 * url
	 */
	@Column(name = "url" ,unique=true,nullable=false,length=200)
	private String url;

	/**
	 * 父节点ID
	 */
	@Column(name = "parent_id")
	private String parentId;

	/**
	 * 排序
	 */
	@Column(name = "sort",nullable=false)
	private Integer sort;

	/**
	 * 作用域
	 */
	@Column(name = "scope",nullable=false)
	private Scope scope;

	/**
	 * 状态
	 */
	@Column(name = "status",nullable=false)
	private Status status;

	/**
	 * 备注
	 */
	@Column(name = "remarks",length=500)
	private String remarks;

	/**
	 * 采单级别
	 */
	@Column(name = "level",nullable=false)
	private Level level;

	/**
	 * 是否叶子节点
	 */
	@Column(name = "is_leaf",nullable=false)
	private Boolean isLeaf;

	/**
	 * 采单类型
	 */
	@Column(name = "menu_type",nullable=false)
	private MenuType menuType;

	/**
	 * 子节点(采单)
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	private List<Menu> childs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public MenuType getMenuType() {
		return menuType;
	}

	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}

	public List<Menu> getChilds() {
		return childs;
	}

	public void setChilds(List<Menu> childs) {
		this.childs = childs;
	}
	
	

}