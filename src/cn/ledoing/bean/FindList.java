package cn.ledoing.bean;

import java.io.Serializable;

public class FindList implements Serializable {
	private String groupimg;
	private String groupsid;
	private String courseid;
	private String groupsdate;
	private String praise;
	private String name;
	private String groupimgthumb;

	public String getGroupimgthumb() {
		return groupimgthumb;
	}

	public void setGroupimgthumb(String groupimgthumb) {
		this.groupimgthumb = groupimgthumb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getGroupimg() {
		return groupimg;
	}

	public void setGroupimg(String groupimg) {
		this.groupimg = groupimg;
	}

	public String getGroupsid() {
		return groupsid;
	}

	public void setGroupsid(String groupsid) {
		this.groupsid = groupsid;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getGroupsdate() {
		return groupsdate;
	}

	public void setGroupsdate(String groupsdate) {
		this.groupsdate = groupsdate;
	}

	public FindList(String groupimg, String groupsid, String courseid,
			String groupsdate, String praise, String name, String groupimgthumb) {
		super();
		this.groupimg = groupimg;
		this.groupsid = groupsid;
		this.courseid = courseid;
		this.groupsdate = groupsdate;
		this.praise = praise;
		this.name = name;
		this.groupimgthumb = groupimgthumb;
	}

	public FindList() {
		super();
	}

}
