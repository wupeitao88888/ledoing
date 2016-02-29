package cn.ledoing.bean;

public class AgeGroupList {
	private String agegroupid;
	private String agegroupname;

	public String getAgegroupid() {
		return agegroupid;
	}

	public void setAgegroupid(String agegroupid) {
		this.agegroupid = agegroupid;
	}

	public String getAgegroupname() {
		return agegroupname;
	}

	public void setAgegroupname(String agegroupname) {
		this.agegroupname = agegroupname;
	}

	public AgeGroupList(String agegroupid, String agegroupname) {
		super();
		this.agegroupid = agegroupid;
		this.agegroupname = agegroupname;
	}

	public AgeGroupList() {
		super();
	}

}
