package cn.ledoing.bean;

import java.io.Serializable;

public class ClassList implements Serializable {
	private String courseid;
	private String coursename;
	private String videothumbnail;

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getVideothumbnail() {
		return videothumbnail;
	}

	public void setVideothumbnail(String videothumbnail) {
		this.videothumbnail = videothumbnail;
	}

	public ClassList(String courseid, String coursename, String videothumbnail) {
		super();
		this.courseid = courseid;
		this.coursename = coursename;
		this.videothumbnail = videothumbnail;
	}

	public ClassList() {
		super();
	}

}
