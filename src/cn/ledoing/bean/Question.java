package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class Question  implements Serializable {
	private String questionid;
	private String start;
	private String end;
	private String mp3url;
	private String questionname;
	private String questionstatus;
	
	public String getQuestionstatus() {
		return questionstatus;
	}

	public void setQuestionstatus(String questionstatus) {
		this.questionstatus = questionstatus;
	}

	private List<Options> listOptions;

	public String getQuestionname() {
		return questionname;
	}

	public void setQuestionname(String questionname) {
		this.questionname = questionname;
	}

	public List<Options> getListOptions() {
		return listOptions;
	}

	public void setListOptions(List<Options> listOptions) {
		this.listOptions = listOptions;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getMp3url() {
		return mp3url;
	}

	public void setMp3url(String mp3url) {
		this.mp3url = mp3url;
	}

	public Question(String questionid, String start, String end, String mp3url,
			String questionname, List<Options> listOptions) {
		super();
		this.questionid = questionid;
		this.start = start;
		this.end = end;
		this.mp3url = mp3url;
		this.questionname = questionname;
		this.listOptions = listOptions;
	}

	public Question() {
		super();
	}

}
