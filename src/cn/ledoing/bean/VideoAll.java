package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class VideoAll implements Serializable {
	private String errorCode;
	private String errorMessage;
	private String total_count;
	private String taskstate;
	private String lock;
	private List<Video> list;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public List<Video> getList() {
		return list;
	}

	public void setList(List<Video> list) {
		this.list = list;
	}

	public String getTaskstate() {
		return taskstate;
	}

	public void setTaskstate(String taskstate) {
		this.taskstate = taskstate;
	}

	public String getLock() {
		return lock;
	}

	public VideoAll(String errorCode, String errorMessage, String total_count,
			String taskstate, String lock, List<Video> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.total_count = total_count;
		this.taskstate = taskstate;
		this.lock = lock;
		this.list = list;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public VideoAll() {
		super();
	}

}
