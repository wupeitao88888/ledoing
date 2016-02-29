package cn.ledoing.bean;

import java.io.Serializable;

public class LCLogin implements Serializable {
    private String errorCode;
    private String errorMessage;
    private String userid;
    private String username;
    private String userpic;
    private String realname;
    private String birthday;
    private String sex;// 0是男生1是女生
    private Provice provincial;// 省
    private CityUser city;
    private DistrictUser district;// 区
    private String mobile;
    private String email;
    private String is_member;//是否会员   0 不是   1是
    // 邮箱地址
    private String token;
    private String attestation_status;// 认证状态0：未认证 1：已认证 2：审核中 3：

    public void setIs_member(String is_member) {
        this.is_member = is_member;
    }

    public String getIs_member() {

        return is_member;
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

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Provice getProvincial() {
        return provincial;
    }

    public CityUser getCity() {
        return city;
    }

    public void setProvincial(Provice provincial) {
        this.provincial = provincial;
    }

    public void setCity(CityUser city) {
        this.city = city;
    }

    public void setDistrict(DistrictUser district) {
        this.district = district;
    }

    public DistrictUser getDistrict() {
        return district;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAttestation_status() {
        return attestation_status;
    }

    public void setAttestation_status(String attestation_status) {
        this.attestation_status = attestation_status;
    }


    public LCLogin(String errorCode, String errorMessage, String userid, String username, String userpic, String realname, String birthday, String sex, Provice provincial, CityUser city, DistrictUser district, String mobile, String email, String token, String attestation_status,String is_member) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.userid = userid;
        this.username = username;
        this.userpic = userpic;
        this.realname = realname;
        this.birthday = birthday;
        this.sex = sex;
        this.provincial = provincial;
        this.city = city;
        this.district = district;
        this.mobile = mobile;
        this.email = email;
        this.token = token;
        this.attestation_status = attestation_status;
        this.is_member = is_member;
    }

    public LCLogin() {
        super();
    }

}
