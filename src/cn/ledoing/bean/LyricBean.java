package cn.ledoing.bean;

import java.io.Serializable;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年5月21日
 * 编写人员:	 吴培涛
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class LyricBean implements Serializable {
	private String text;
	private String time;
	private String endtime;
	private String index;
	private String isred;

	public LyricBean(String text, String time, String endtime, String index,
			String isred) {
		super();
		this.text = text;
		this.time = time;
		this.endtime = endtime;
		this.index = index;
		this.isred = isred;
	}

	public String getIsred() {
		return isred;
	}

	public void setIsred(String isred) {
		this.isred = isred;
	}

	public LyricBean(String text, String time, String endtime, String index) {
		super();
		this.text = text;
		this.time = time;
		this.endtime = endtime;
		this.index = index;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public LyricBean(String text, String time) {
		super();
		this.text = text;
		this.time = time;
	}

}
