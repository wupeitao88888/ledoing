package cn.ledoing.bean;

public class HistoryList {
	private String courseid;
	private String coursename;
	private String videothumbnail;
	private String taskstatus;

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

	public String getTaskstatus() {
		return taskstatus;
	}

	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}

	public HistoryList(String courseid, String coursename,
			String videothumbnail, String taskstatus) {
		super();
		this.courseid = courseid;
		this.coursename = coursename;
		this.videothumbnail = videothumbnail;
		this.taskstatus = taskstatus;
	}

}
