package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class TimAll implements Serializable {
    private List<ProvinceModel> year;
    private List<CityModel> month;
    private List<DistrictModel> day;

    public List<ProvinceModel> getYear() {
        return year;
    }

    public void setYear(List<ProvinceModel> year) {
        this.year = year;
    }

    public List<CityModel> getMonth() {
        return month;
    }

    public void setMonth(List<CityModel> month) {
        this.month = month;
    }

    public List<DistrictModel> getDay() {
        return day;
    }

    public void setDay(List<DistrictModel> day) {
        this.day = day;
    }

}
