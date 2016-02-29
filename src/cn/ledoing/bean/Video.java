package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {
	private String videoid;
	private String videoname;
	private String videourl;
	private String viewstate;
	private String lastplaydate;
	private String playing;
	private String videotime;
	private String video_intro;
	private String note;
	private String lock;
	private List<LyricBean> listLyricBean;
	private List<Question> listQuestion;

	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getVideotime() {
		return videotime;
	}

	public void setVideotime(String videotime) {
		this.videotime = videotime;
	}

	public String getVideo_intro() {
		return video_intro;
	}

	public void setVideo_intro(String video_intro) {
		this.video_intro = video_intro;
	}

	public String getPlaying() {
		return playing;
	}

	public void setPlaying(String playing) {
		this.playing = playing;
	}

	public String getLastplaydate() {
		return lastplaydate;
	}

	public void setLastplaydate(String lastplaydate) {
		this.lastplaydate = lastplaydate;
	}

	public String getVideoid() {
		return videoid;
	}

	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}

	public String getVideoname() {
		return videoname;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getViewstate() {
		return viewstate;
	}

	public void setViewstate(String viewstate) {
		this.viewstate = viewstate;
	}

	public List<LyricBean> getListLyricBean() {
		return listLyricBean;
	}

	public void setListLyricBean(List<LyricBean> listLyricBean) {
		this.listLyricBean = listLyricBean;
	}

	public List<Question> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<Question> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public Video(String videoid, String videoname, String videourl,
			String viewstate, String lastplaydate, String videotime,
			String video_intro, List<LyricBean> listLyricBean,
			List<Question> listQuestion, String note) {
		super();
		this.videoid = videoid;
		this.videoname = videoname;
		this.videourl = videourl;
		this.viewstate = viewstate;
		this.lastplaydate = lastplaydate;
		this.videotime = videotime;
		this.video_intro = video_intro;
		this.listLyricBean = listLyricBean;
		this.listQuestion = listQuestion;
		this.note = note;
	}

	public Video() {
		super();
	}

}
