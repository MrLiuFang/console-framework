package com.pepper.model.console.menu;

import java.util.List;

/**
 * 
 * @author mrliu
 *
 */
public class MenuVo extends Menu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2358744802937511142L;
	
	private String pdfUrl;
	
	
	private List<MenuVo> child;

	public List<MenuVo> getChild() {
		return child;
	}

	public void setChild(List<MenuVo> child) {
		this.child = child;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	
	
}
