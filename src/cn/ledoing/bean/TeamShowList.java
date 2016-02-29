package cn.ledoing.bean;

public class TeamShowList {
	private String userid;
	private String username;
	private String workimg;
	private String workimgthumb;
	private String groupsid;
	private String courseid;
	private String imgdate;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWorkimg() {
		return workimg;
	}

	public void setWorkimg(String workimg) {
		this.workimg = workimg;
	}

	public String getWorkimgthumb() {
		return workimgthumb;
	}

	public void setWorkimgthumb(String workimgthumb) {
		this.workimgthumb = workimgthumb;
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

	public TeamShowList(String userid, String username, String workimg,
			String workimgthumb, String groupsid, String courseid,
			String imgdate) {
		super();
		this.userid = userid;
		this.username = username;
		this.workimg = workimg;
		this.workimgthumb = workimgthumb;
		this.groupsid = groupsid;
		this.courseid = courseid;
		this.imgdate = imgdate;
	}

	public TeamShowList() {
		super();
	}

}
