package cn.ledoing.bean;

import java.util.List;

public class FindAll {
	private String errorCode;
	private String errorMessage;
	private String page;
	private String pagesize;
	private String total_count;
	private List<FindList> list;

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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPagesize() {
		return pagesize;
	}

	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public List<FindList> getList() {
		return list;
	}

	public void setList(List<FindList> list) {
		this.list = list;
	}

	public FindAll(String errorCode, String errorMessage, String page,
			String pagesize, String total_count, List<FindList> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.page = page;
		this.pagesize = pagesize;
		this.total_count = total_count;
		this.list = list;
	}

	public FindAll() {
		super();
	}

}
