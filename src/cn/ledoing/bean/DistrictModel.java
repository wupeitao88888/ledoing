package cn.ledoing.bean;

import java.io.Serializable;

public class DistrictModel implements Serializable {
	private String name;
	private String id;
	private String pinyin;
	private String division_type;
	private String parent_id;

	private String xingzhengdaima;
	private String show_order;
	private String status;
	private String update_time;
	private String alias;
	private String create_time;
	private String initials;

	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String id, String pinyin,
			String division_type, String parent_id, String xingzhengdaima,
			String show_order, String status, String update_time, String alias,
			String create_time, String initials) {
		super();
		this.name = name;
		this.id = id;
		this.pinyin = pinyin;
		this.division_type = division_type;
		this.parent_id = parent_id;
		this.xingzhengdaima = xingzhengdaima;
		this.show_order = show_order;
		this.status = status;
		this.update_time = update_time;
		this.alias = alias;
		this.create_time = create_time;
		this.initials = initials;
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

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getDivision_type() {
		return division_type;
	}

	public void setDivision_type(String division_type) {
		this.division_type = division_type;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getXingzhengdaima() {
		return xingzhengdaima;
	}

	public void setXingzhengdaima(String xingzhengdaima) {
		this.xingzhengdaima = xingzhengdaima;
	}

	public String getShow_order() {
		return show_order;
	}

	public void setShow_order(String show_order) {
		this.show_order = show_order;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + id + "]";
	}

}
