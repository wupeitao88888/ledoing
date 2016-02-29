package cn.ledoing.bean;

import java.io.Serializable;

public class Options implements Serializable {
	private String answername;
	private String answerimg;
	private String answerid;

	public String getAnswerid() {
		return answerid;
	}

	public void setAnswerid(String answerid) {
		this.answerid = answerid;
	}

	public String getAnswername() {
		return answername;
	}

	public void setAnswername(String answername) {
		this.answername = answername;
	}

	public String getAnswerimg() {
		return answerimg;
	}

	public void setAnswerimg(String answerimg) {
		this.answerimg = answerimg;
	}

	public Options(String answername, String answerimg, String answerid) {
		super();
		this.answername = answername;
		this.answerimg = answerimg;
		this.answerid = answerid;
	}

}
