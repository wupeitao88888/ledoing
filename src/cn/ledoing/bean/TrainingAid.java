package cn.ledoing.bean;

import java.io.Serializable;

public class TrainingAid implements Serializable {
	private String aidsname;
	private String aidsid;
	private String selected;

	public String getAidsname() {
		return aidsname;
	}

	public void setAidsname(String aidsname) {
		this.aidsname = aidsname;
	}

	public String getAidsid() {
		return aidsid;
	}

	public void setAidsid(String aidsid) {
		this.aidsid = aidsid;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public TrainingAid(String aidsname, String aidsid, String selected) {
		super();
		this.aidsname = aidsname;
		this.aidsid = aidsid;
		this.selected = selected;
	}

}
