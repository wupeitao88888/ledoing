package cn.ledoing.bean;

import java.io.Serializable;

public class LCMeProductionList implements Serializable {
	private String workimg;
	private String workname;
	private String groupsid;
	private String courseid;
	private String imgdate;

	public String getWorkimg() {
		return workimg;
	}

	public void setWorkimg(String workimg) {
		this.workimg = workimg;
	}

	public String getWorkname() {
		return workname;
	}

	public void setWorkname(String workname) {
		this.workname = workname;
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

	public String getImgdate() {
		return imgdate;
	}

	public void setImgdate(String imgdate) {
		this.imgdate = imgdate;
	}

	public LCMeProductionList(String workimg, String workname, String groupsid,
			String courseid, String imgdate) {
		super();
		this.workimg = workimg;
		this.workname = workname;
		this.groupsid = groupsid;
		this.courseid = courseid;
		this.imgdate = imgdate;
	}

	public LCMeProductionList() {
		super();
	}

}
