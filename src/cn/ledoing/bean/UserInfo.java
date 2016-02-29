package cn.ledoing.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private String errorCode;
	private String errorMessage;

	private String userid;
	private String username;
	private String realname;
	private String birthday;
	private String mobile;
	private String sex;
	private String provincial;
	private String city;
	private String district;
	private String userpic;
	private String adddate;
	private String cid;
	private String did;
	private String pid;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAdddate() {
		return adddate;
	}

	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}

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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvincial() {
		return provincial;
	}

	public void setProvincial(String provincial) {
		this.provincial = provincial;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public UserInfo(String errorCode, String errorMessage, String userid,
			String username, String realname, String birthday, String mobile,
			String sex, String provincial, String city, String district,
			String userpic, String adddate, String cid, String did, String pid) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.userid = userid;
		this.username = username;
		this.realname = realname;
		this.birthday = birthday;
		this.mobile = mobile;
		this.sex = sex;
		this.provincial = provincial;
		this.city = city;
		this.district = district;
		this.userpic = userpic;
		this.adddate = adddate;
		this.cid = cid;
		this.did = did;
		this.pid = pid;
	}

	public UserInfo() {
		super();
	}

}
