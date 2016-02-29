package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class CityBean implements Serializable {
	private String name;
	private String id;

	private List<DistrictBean> districtList;

	public CityBean() {
		super();
	}

	public CityBean(String name, String id, List<DistrictBean> districtList) {
		super();
		this.name = name;
		this.id = id;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public List<DistrictBean> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictBean> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}

}
