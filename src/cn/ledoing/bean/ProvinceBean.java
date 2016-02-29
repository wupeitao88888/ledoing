package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class ProvinceBean implements Serializable {
	private String pid;
	private String pname;
	private List<CityBean> cityList;

	public ProvinceBean() {
		super();
	}

	public ProvinceBean(String id, String name, List<CityBean> cityList) {
		super();
		this.pid = id;
		this.pname = name;
		this.cityList = cityList;
	}

	public String getId() {
		return pid;
	}

	public void setId(String id) {
		this.pid = id;
	}

	public String getName() {
		return pname;
	}

	public void setName(String name) {
		this.pname = name;
	}


	public List<CityBean> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityBean> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + pname + ", cityList=" + cityList + "]";
	}

}
