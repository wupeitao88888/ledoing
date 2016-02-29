package cn.ledoing.bean;

import java.io.Serializable;

public class FindTitleList implements Serializable {
	private String categoryid;
	private String name;

	public FindTitleList(String categoryid, String name) {
		super();
		this.categoryid = categoryid;
		this.name = name;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FindTitleList() {
		super();
	}

}
