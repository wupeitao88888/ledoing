package cn.ledoing.bean;

import java.io.Serializable;

public class DistrictBean implements Serializable {
	private String name;
	private String id;

	public DistrictBean() {
		super();
	}

	public DistrictBean(String name, String id) {
		super();
		this.name = name;
		this.id = id;
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


	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + id + "]";
	}

}
