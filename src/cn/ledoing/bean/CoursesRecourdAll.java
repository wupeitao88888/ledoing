package cn.ledoing.bean;

import java.util.List;

public class CoursesRecourdAll {
    private String errorCode;
    private String errorMessage;
    private String page;
    private String pagesize;
    private String total_count;
   private String total_use;
    private String total_hour;
    private List<CoursesRecourd> list;

    public String getTotal_use() {
        return total_use;
    }

    public void setTotal_use(String total_use) {
        this.total_use = total_use;
    }

    public String getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(String total_hour) {
        this.total_hour = total_hour;
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

    public List<CoursesRecourd> getList() {
        return list;
    }

    public void setList(List<CoursesRecourd> list) {
        this.list = list;
    }

    public CoursesRecourdAll(String errorCode, String errorMessage, String page, String pagesize, String total_count, String total_use, String total_hour, List<CoursesRecourd> list) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.page = page;
        this.pagesize = pagesize;
        this.total_count = total_count;
        this.total_use = total_use;
        this.total_hour = total_hour;
        this.list = list;
    }

    public CoursesRecourdAll() {
        super();
    }

}
