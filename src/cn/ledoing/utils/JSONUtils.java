package cn.ledoing.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import cn.ledoing.activity.R;
import cn.ledoing.bean.Advertisement;
import cn.ledoing.bean.AdvertisementList;
import cn.ledoing.bean.AgeGroup;
import cn.ledoing.bean.AgeGroupList;
import cn.ledoing.bean.AttentionCenter;
import cn.ledoing.bean.BeanDetail;
import cn.ledoing.bean.BeanTask;
import cn.ledoing.bean.CancelReason;
import cn.ledoing.bean.CancelReasonList;
import cn.ledoing.bean.CenterCity;
import cn.ledoing.bean.City;
import cn.ledoing.bean.CityBean;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.CityUser;
import cn.ledoing.bean.ClassAll;
import cn.ledoing.bean.ClassList;
import cn.ledoing.bean.Concern;
import cn.ledoing.bean.ConcernAll;
import cn.ledoing.bean.CoursesRecourd;
import cn.ledoing.bean.CoursesRecourdAll;
import cn.ledoing.bean.DistrictBean;
import cn.ledoing.bean.Institution;
import cn.ledoing.bean.OfflineCourses;
import cn.ledoing.bean.OfflineCoursesAll;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.BeanTaskErrorCode;
import cn.ledoing.bean.DistrictUser;
import cn.ledoing.bean.ErrorCodeBeanDetail;
import cn.ledoing.bean.FindAll;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.FindTitle;
import cn.ledoing.bean.FindTitleList;
import cn.ledoing.bean.HistoryAll;
import cn.ledoing.bean.HistoryList;
import cn.ledoing.bean.LCLogin;
import cn.ledoing.bean.LCMeProduction;
import cn.ledoing.bean.LCMeProductionList;
import cn.ledoing.bean.LDChange;
import cn.ledoing.bean.LDChangeEorror;
import cn.ledoing.bean.LyricBean;
import cn.ledoing.bean.Options;
import cn.ledoing.bean.Praise;
import cn.ledoing.bean.Provice;
import cn.ledoing.bean.ProvinceBean;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.bean.Question;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.bean.TeamShow;
import cn.ledoing.bean.TeamShowList;
import cn.ledoing.bean.Theme;
import cn.ledoing.bean.ThemeGroup;
import cn.ledoing.bean.ThemeType;
import cn.ledoing.bean.TimAll;
import cn.ledoing.bean.TrainingAid;
import cn.ledoing.bean.TrainingAidAll;
import cn.ledoing.bean.UserInfo;
import cn.ledoing.bean.Video;
import cn.ledoing.bean.VideoAll;

public class JSONUtils {

    public static JSONUtils jsonUtils = null;

    public static JSONUtils getInstatce() {
        if (jsonUtils == null) {
            jsonUtils = new JSONUtils();
        }
        return jsonUtils;
    }

    // 登陆和注册
    public LCLogin getLogin(String readFrom, String lc_login_number) {

        LCLogin login = null;
        if (TextUtils.isEmpty(readFrom)) {
            login = new LCLogin();
            login.setErrorCode("1");
            login.setErrorMessage("数据为空");
            return login;
        }
        try {
            String loadConvert = loadConvert(readFrom);
            L.e("登陆注册：" + loadConvert);
            JSONObject jo = new JSONObject(loadConvert);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "错误：1001");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String userid = jsonObject.optString("userid", "");
                String username = jsonObject.optString("username",
                        lc_login_number);
                String userpic = jsonObject.optString("userpic", "");
                String realname = jsonObject.optString("realname", "");
                String birthday = jsonObject.optString("birthday", "");
                String sex = jsonObject.optString("sex", "");
                JSONObject provincial = jsonObject.optJSONObject("province");
                Provice provice = new Provice(provincial.optString("name"),provincial.optString("id"));
                JSONObject city = jsonObject.optJSONObject("city");
                CityUser cityUser = new CityUser(city.optString("name"),city.optString("id"));
                JSONObject district = jsonObject.optJSONObject("district");
                DistrictUser districtUser = new DistrictUser(district.optString("name"),district.optString("id"));
                String mobile = jsonObject.optString("mobile", "");
                String email = jsonObject.optString("email", "");
                String token = jsonObject.optString("token", "");
                String attestation_status = jsonObject.optString(
                        "attestation_status", "0");
                String is_member = jsonObject.optJSONObject("amount").optString("is_member");
                login = new LCLogin(errorCode, errorMessage, userid, username,
                        userpic, realname, birthday, sex, provice, cityUser,
                        districtUser, mobile, email, token, attestation_status,is_member);
                return login;
            } else {
                login = new LCLogin();
                login.setErrorCode(errorCode);
                login.setErrorMessage(errorMessage);
                return login;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            login = new LCLogin();
            login.setErrorCode("1");
            login.setErrorMessage("数据异常");
            return login;
        }
    }

    // 个人信息获取
    public UserInfo getUserInfo(String theString) {

        UserInfo uInfo = null;
        if (TextUtils.isEmpty(theString)) {
            uInfo = new UserInfo();
            uInfo.setErrorCode("1");
            uInfo.setErrorMessage("数据为空");
            return uInfo;
        }
        String readFrom = loadConvert(theString);
        L.e("个人信息:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String userid = jsonObject.optString("userid", "");
                String username = jsonObject.optString("username", "");
                String realname = jsonObject.optString("realname", "");
                String birthday = jsonObject.optString("birthday", "");
                String mobile = jsonObject.optString("mobile", "");
                String sex = jsonObject.optString("sex", "");
                String provincial = jsonObject.optString("province", "");
                String city = jsonObject.optString("city", "");
                String cid = jsonObject.optString("cid", "");
                String did = jsonObject.optString("did", "");
                String pid = jsonObject.optString("pid", "");
                String district = jsonObject.optString("district", "");
                String userpic = jsonObject.optString("userpic", "");
                String adddate = jsonObject.optString("adddate", "");
                uInfo = new UserInfo(errorCode, errorMessage, userid, username,
                        realname, birthday, mobile, sex, provincial, city,
                        district, userpic, adddate, cid, did, pid);
                return uInfo;
            } else {
                uInfo = new UserInfo();
                uInfo.setErrorCode(errorCode);
                uInfo.setErrorMessage(errorMessage);
                return uInfo;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            uInfo = new UserInfo();
            uInfo.setErrorCode("1");
            uInfo.setErrorMessage("数据为空");
            return uInfo;
        }

    }

    // 获取年龄组
    public AgeGroup getAgeGroup(String theString) {
        AgeGroup ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new AgeGroup();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取年龄组:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<AgeGroupList> list = new ArrayList<AgeGroupList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String agegroupid = jObject.optString("agegroupid", "");
                    String agegroupname = jObject.optString("agegroupname", "");
                    AgeGroupList agl = new AgeGroupList(agegroupid,
                            agegroupname);
                    list.add(agl);
                }
                ag = new AgeGroup(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new AgeGroup();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new AgeGroup();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    ;

    // 课程列表
    public ClassAll getClassAll(String theString) {
        ClassAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new ClassAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取课程列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<ClassList> list = new ArrayList<ClassList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String courseid = jObject.optString("courseid", "");
                    String coursename = jObject.optString("coursename", "");
                    String videothumbnail = jObject.optString("videothumbnail",
                            "");

                    ClassList agl = new ClassList(courseid, coursename,
                            videothumbnail);
                    list.add(agl);
                }

                ag = new ClassAll(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new ClassAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new ClassAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }


    // 线下课程列表
    public OfflineCoursesAll getOfflineCoursesAll(String theString) {
        OfflineCoursesAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new OfflineCoursesAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取课程列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");

            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");

                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<OfflineCourses> list = new ArrayList<OfflineCourses>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("id",
                            "");
                    String create_at = jObject.optString("create_at", "");
                    String price = jObject.optString("price", "");
                    String status = jObject.optString("status",
                            "");
                    String teacher_name = jObject.optString("teacher_name",
                            "");
                    String base_name = jObject.optString("base_name",
                            "");
                    String start_time = jObject.optString("start_time",
                            "");
                    String institution = jObject.optString("institution",
                            "");
                    String cancelable = jObject.optString("cancelable",
                            "");
                    String course_name = jObject.optString("course_name",
                            "");

                    OfflineCourses agl = new OfflineCourses(id, create_at, price, status, teacher_name, base_name, start_time, institution, cancelable, course_name);
                    list.add(agl);
                }

                ag = new OfflineCoursesAll(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new OfflineCoursesAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new OfflineCoursesAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }

    // 课程列表
    public CoursesRecourdAll getCoursesRecordAll(String theString) {
        CoursesRecourdAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new CoursesRecourdAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取课程列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");

            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");

                String total_count = jsonObject.optString("total_count", "");
                String month = jsonObject.optString("month", "");
                String total_use = jsonObject.optString("total_use", "");
                String total_hour = jsonObject.optString("total_hour", "");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<CoursesRecourd> list = new ArrayList<CoursesRecourd>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String week = jObject.optString("week", "");
                    String day = jObject.optString("day", "");
                    String time = jObject.optString("time",
                            "");
                    String realname = jObject.optString("realname",
                            "");
                    String hour = jObject.optString("hour",
                            "");
                    String isfavour = jObject.optString("isfavour",
                            "");
                    String id = jObject.optString("id",
                            "");

                    CoursesRecourd agl = new CoursesRecourd(day, week, time, hour, realname, isfavour, id, month, total_use, total_hour);
                    list.add(agl);
                }

                ag = new CoursesRecourdAll(errorCode, errorMessage, "", "", total_count, total_use, total_hour, list);
                return ag;
            } else {
                ag = new CoursesRecourdAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new CoursesRecourdAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }

    // 获取发现分组
    public FindTitle getFindTitle(String theString) {
        FindTitle ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new FindTitle();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取课程列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<FindTitleList> list = new ArrayList<FindTitleList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String categoryid = jObject.optString("categoryid", "");
                    String name = jObject.optString("name", "");

                    FindTitleList ftl = new FindTitleList(categoryid, name);
                    list.add(ftl);
                }
                ag = new FindTitle(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new FindTitle();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new FindTitle();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    public FindAll getFindAll(String theString) {
        FindAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new FindAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取团队列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<FindList> list = new ArrayList<FindList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String groupimg = jObject.optString("groupimg", "");

                    String groupimgthumb = jObject.optString("groupimgthumb",
                            "");
                    String groupsid = jObject.optString("groupsid", "");
                    String courseid = jObject.optString("courseid", "");
                    String groupsdate = jObject.optString("groupsdate", "");
                    String praise = jObject.optString("praise", "");
                    String name = jObject.optString("name", "");
//                    String groupimgthumb = "..";
//                    String groupsid = "..";
//                    String courseid = "..";
//                    String groupsdate = "..";
//                    String praise ="..";
//                    String name ="..";
//                    String groupimg ="http://image.photophoto.cn/nm-6/018/030/0180300244.jpg";
                    FindList ftl = new FindList(groupimg, groupsid, courseid,
                            groupsdate, praise, name, groupimgthumb);
                    list.add(ftl);
                }
                ag = new FindAll(errorCode, errorMessage, "", "",
                        total_count, list);
                return ag;
            } else {
                ag = new FindAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new FindAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    public Praise getPraise(String theString) {
        Praise ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new Praise();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("点赞:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                // JSONObject jsonObject = jo.getJSONObject("data");
                String number = jo.optString("data", "");

                ag = new Praise(errorCode, errorMessage, number);
                return ag;
            } else {
                ag = new Praise();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new Praise();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    // 团队作品展示
    public TeamShow getTeamShow(String theString) {
        TeamShow ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new TeamShow();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取团队作品:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<TeamShowList> list = new ArrayList<TeamShowList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String userid = jObject.optString("userid", "");
                    String username = jObject.optString("username", "");
                    String workimg = jObject.optString("workimg", "");
                    String workimgthumb = jObject.optString("workimgthumb", "");
                    String courseid = jObject.optString("courseid", "");
                    String groupsid = jObject.optString("groupsid", "");
                    String imgdate = jObject.optString("imgdate", "");
                    TeamShowList ftl = new TeamShowList(userid, username,
                            workimg, workimgthumb, groupsid, courseid, imgdate);
                    list.add(ftl);
                }
                ag = new TeamShow(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new TeamShow();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new TeamShow();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    // 个人作品
    public LCMeProduction getLCMeProduction(String theString) {
        LCMeProduction ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new LCMeProduction();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("获取团队列表:" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");

                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<LCMeProductionList> list = new ArrayList<LCMeProductionList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String workimg = jObject.optString("workimg", "");
                    String workname = jObject.optString("workname", "");
                    String groupsid = jObject.optString("groupsid", "");
                    String courseid = jObject.optString("courseid", "");
                    String imgdate = jObject.optString("imgdate", "");

                    LCMeProductionList ftl = new LCMeProductionList(workimg,
                            workname, groupsid, courseid, imgdate);
                    list.add(ftl);
                }
                ag = new LCMeProduction(errorCode, errorMessage, total_count,
                        list);
                return ag;
            } else {
                ag = new LCMeProduction();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new LCMeProduction();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    // 获取城市
    public City getCity(String theString) {
        City ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new City();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);

        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONArray jsonArray = jo.getJSONArray("data");
                List<ProvinceModel> list = new ArrayList<ProvinceModel>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("id", "");
                    String division_name = jObject.optString("division_name",
                            "");
                    String division_type = jObject.optString("division_type",
                            "");
                    String parent_id = jObject.optString("parent_id", "");
                    String pinyin = jObject.optString("pinyin", "");
                    String xingzhengdaima = jObject.optString("xingzhengdaima",
                            "");
                    String show_order = jObject.optString("show_order", "");
                    String status = jObject.optString("status", "");
                    String update_time = jObject.optString("update_time", "");
                    String alias = jObject.optString("alias", "");
                    String create_time = jObject.optString("create_time", "");
                    String initials = jObject.optString("initials", "");
                    JSONArray cityChilds = jObject.getJSONArray("childs");
                    List<CityModel> listCityModel = new ArrayList<CityModel>();
                    if (cityChilds.length() == 0) {

                        DistrictModel dm = new DistrictModel(division_name, id,
                                pinyin, division_type, parent_id,
                                xingzhengdaima, show_order, status,
                                update_time, alias, create_time, "");
                        List<DistrictModel> listDistrictModel = new ArrayList<DistrictModel>();
                        listDistrictModel.add(dm);
                        CityModel cm = new CityModel(division_name, id,
                                division_name, pinyin, division_type,
                                parent_id, xingzhengdaima, show_order, status,
                                update_time, alias, create_time, initials,
                                listDistrictModel);
                        listCityModel.add(cm);
                        ProvinceModel pm = new ProvinceModel(id, division_name,
                                pinyin, division_type, parent_id,
                                xingzhengdaima, show_order, status,
                                update_time, alias, create_time, initials,
                                listCityModel);
                        list.add(pm);
                        continue;
                    }

                    for (int p = 0; p < cityChilds.length(); p++) {
                        JSONObject cityObject = cityChilds.getJSONObject(p);
                        String cityId = cityObject.optString("id", "");
                        String cityName = cityObject.optString("division_name",
                                "");
                        String cityPinyin = cityObject.optString("pinyin", "");
                        String cityDivision_type = cityObject.optString(
                                "division_type", "");
                        String cityParent_id = cityObject.optString(
                                "parent_id", "");
                        String cityXingzhengdaima = cityObject.optString(
                                "xingzhengdaima", "");
                        String cityShow_order = cityObject.optString(
                                "show_order", "");
                        String cityStatus = cityObject.optString("status", "");
                        String cityUpdate_time = cityObject.optString(
                                "update_time", "");
                        String cityAlias = cityObject.optString("alias", "");
                        String cityCreate_time = cityObject.optString(
                                "create_time", "");
                        String cityInitials = cityObject.optString("initials",
                                "");
                        JSONArray districtChilds = cityObject
                                .getJSONArray("childs");
                        List<DistrictModel> listDistrictModel = new ArrayList<DistrictModel>();
                        if (districtChilds.length() == 0) {
                            DistrictModel dm = new DistrictModel(cityName,
                                    cityId, cityPinyin, cityDivision_type,
                                    cityParent_id, cityXingzhengdaima,
                                    cityShow_order, cityStatus,
                                    cityUpdate_time, cityAlias,
                                    cityCreate_time, "");
                            listDistrictModel.add(dm);
                            CityModel cm = new CityModel(cityName, cityId,
                                    cityName, cityPinyin, cityDivision_type,
                                    cityParent_id, cityXingzhengdaima,
                                    cityShow_order, cityStatus,
                                    cityUpdate_time, cityAlias,
                                    cityCreate_time, cityInitials,
                                    listDistrictModel);

                            listCityModel.add(cm);
                            ProvinceModel pm = new ProvinceModel(id,
                                    division_name, pinyin, division_type,
                                    parent_id, xingzhengdaima, show_order,
                                    status, update_time, alias, create_time,
                                    initials, listCityModel);
                            list.add(pm);
                            continue;
                        }

                        for (int q = 0; q < districtChilds.length(); q++) {
                            JSONObject districtObject = districtChilds
                                    .getJSONObject(q);
                            String districtId = districtObject.optString("id",
                                    "");
                            String districtName = districtObject.optString(
                                    "division_name", "");
                            String districtPinyin = districtObject.optString(
                                    "pinyin", "");
                            String districtDivision_type = districtObject
                                    .optString("division_type", "");
                            String districtParent_id = districtObject
                                    .optString("parent_id", "");
                            String districtXingzhengdaima = districtObject
                                    .optString("xingzhengdaima", "");
                            String districtShow_order = districtObject
                                    .optString("show_order", "");
                            String districtStatus = districtObject.optString(
                                    "status", "");
                            String districtUpdate_time = districtObject
                                    .optString("update_time", "");
                            String districtAlias = districtObject.optString(
                                    "alias", "");
                            String districtCreate_time = districtObject
                                    .optString("create_time", "");
                            String districtInitials = districtObject.optString(
                                    "initials", "");

                            DistrictModel dm = new DistrictModel(districtName,
                                    districtId, districtPinyin,
                                    districtDivision_type, districtParent_id,
                                    districtXingzhengdaima, districtShow_order,
                                    districtStatus, districtUpdate_time,
                                    districtAlias, districtCreate_time,
                                    districtInitials);
                            listDistrictModel.add(dm);
                        }

                        CityModel cm = new CityModel(cityName, cityId,
                                cityName, cityPinyin, cityDivision_type,
                                cityParent_id, cityXingzhengdaima,
                                cityShow_order, cityStatus, cityUpdate_time,
                                cityAlias, cityCreate_time, cityInitials,
                                listDistrictModel);

                        listCityModel.add(cm);
                    }

                    ProvinceModel pm = new ProvinceModel(id, division_name,
                            pinyin, division_type, parent_id, xingzhengdaima,
                            show_order, status, update_time, alias,
                            create_time, initials, listCityModel);
                    list.add(pm);
                }
                ag = new City(errorCode, errorMessage, list);
                return ag;
            } else {
                ag = new City();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new City();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    public TimAll getTimeAll() {
        TimAll ag = null;
        Calendar mCalendar = Calendar.getInstance();


        List<ProvinceModel> year = new ArrayList<ProvinceModel>();
        for (int i = 2000; i < mCalendar.get(Calendar.YEAR) + 1; i++) {
            String id = i + "";
            String division_name = i + "";
            String division_type = "";
            String parent_id = "";
            String pinyin = "";
            String xingzhengdaima = "";
            String show_order = "";
            String status = "";
            String update_time = "";
            String alias = "";
            String create_time = "";
            String initials = "";

            ProvinceModel pm = new ProvinceModel(id, division_name, pinyin,
                    division_type, parent_id, xingzhengdaima, show_order,
                    status, update_time, alias, create_time, initials, null);
            year.add(pm);
        }
        List<CityModel> month = new ArrayList<CityModel>();
        for (int p = 1; p < 13; p++) {
            String cityId = p + "";
            String cityName = p + "";
            String cityPinyin = "";
            String cityDivision_type = "";
            String cityParent_id = "";
            String cityXingzhengdaima = "";
            String cityShow_order = "";
            String cityStatus = "";
            String cityUpdate_time = "";
            String cityAlias = "";
            String cityCreate_time = "";
            String cityInitials = "";
            CityModel cm = new CityModel(cityName, cityId, cityName,
                    cityPinyin, cityDivision_type, cityParent_id,
                    cityXingzhengdaima, cityShow_order, cityStatus,
                    cityUpdate_time, cityAlias, cityCreate_time, cityInitials,
                    null);
            month.add(cm);
        }
        List<DistrictModel> day = new ArrayList<DistrictModel>();

        for (int q = 1; q < 32; q++) {
            String districtId = q + "";
            String districtName = q + "";
            String districtPinyin = "";
            String districtDivision_type = "";
            String districtParent_id = "";
            String districtXingzhengdaima = "";
            String districtShow_order = "";
            String districtStatus = "";
            String districtUpdate_time = "";
            String districtAlias = "";
            String districtCreate_time = "";
            String districtInitials = "";
            DistrictModel dm = new DistrictModel(districtName, districtId,
                    districtPinyin, districtDivision_type, districtParent_id,
                    districtXingzhengdaima, districtShow_order, districtStatus,
                    districtUpdate_time, districtAlias, districtCreate_time,
                    districtInitials);
            day.add(dm);
        }
        ag = new TimAll();
        ag.setDay(day);
        ag.setMonth(month);
        ag.setYear(year);
        return ag;
    }


    public VideoAll getVideoAll(String theString) {

        VideoAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new VideoAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = AbStrUtil.format(loadConvert(theString));
        L.e(readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                String taskstate = jsonObject.optString("taskstate", "0");
//                String taskstate="2";
                String lock = jsonObject.optString("lock", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<Video> list = new ArrayList<Video>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String videoid = jObject.optString("videoid", "");
                    String videoname = jObject.optString("videoname", "");
                    String videourl = jObject.optString("videourl", "");
                    String viewstate = jObject.optString("viewstate", "");
//                    String viewstate=2+"";
                    String videotime = jObject.optString("videotime", "");
                    String video_intro = jObject.optString("video_intro", "");
                    String note = jObject.optString("note", "");
                    String lockV = jObject.optString("lock", "");
                    String lastplaydate = jObject
                            .optString("lastplaytime", "0");
                    if (TextUtils.isEmpty(lastplaydate)
                            & "null".equals(lastplaydate)) {
                        lastplaydate = 0 + "";
                    }
                    if ("null".equals(lastplaydate)) {
                        lastplaydate = 0 + "";
                    }
                    JSONArray captionlist = jObject.getJSONArray("captionlist");
                    List<LyricBean> listLyricBean = new ArrayList<LyricBean>();
                    for (int p = 0; p < captionlist.length(); p++) {
                        JSONObject lyObject = captionlist.getJSONObject(p);
                        String indexid = lyObject.optString("indexid", "0");
                        String start_time = getTimeJson(lyObject.optString(
                                "start_time", "")) + "";
                        String end_time = getTimeJson(lyObject.optString(
                                "end_time", "")) + "";
                        String text = lyObject.optString("text", "");
                        LyricBean lb = new LyricBean(text, start_time,
                                end_time, indexid);
                        listLyricBean.add(lb);
                    }
                    JSONArray question = jObject.getJSONArray("question");
                    List<Question> listQuestion = new ArrayList<Question>();

                    for (int q = 0; q < question.length(); q++) {
                        JSONObject qObject = question.getJSONObject(q);
                        String questionid = qObject
                                .optString("questionid", "0");
                        String start = getTimeJson(qObject.optString("start",
                                "")) + "";
                        String end = getTimeJson(qObject.optString("end", ""))
                                + "";
                        String mp3url = qObject.optString("mp3url", "");
                        String questionname = qObject.optString("questionname",
                                "");
                        JSONArray answer = qObject.getJSONArray("answer");
                        List<Options> listOptions = new ArrayList<Options>();
                        L.e(questionname + "/" + start + "/" + end + "/"
                                + answer.length());
                        for (int y = 0; y < answer.length(); y++) {
                            JSONObject opObject = answer.getJSONObject(y);
                            String answername = opObject.optString(
                                    "answername", "0");
                            String answerimg = opObject.optString("answerimg",
                                    "0");
                            String answerid = opObject
                                    .optString("answerid", "");
                            Options op = new Options(answername, answerimg,
                                    answerid);
                            listOptions.add(op);
                        }
                        Question qtion = new Question(questionid, start, end,
                                mp3url, questionname, listOptions);
                        qtion.setQuestionstatus("0");
                        listQuestion.add(qtion);
                    }
                    Video video = new Video(videoid, videoname, videourl,
                            viewstate, lastplaydate, videotime, video_intro,
                            listLyricBean, listQuestion, note);
                    video.setLock(lockV);
                    list.add(video);
                }
                ag = new VideoAll(errorCode, errorMessage, total_count,
                        taskstate, lock, list);
                return ag;
            } else {
                ag = new VideoAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ag = new VideoAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据异常");
            return ag;
        }
    }

    public int getTimeJson(String s) {
        int lastIndexOf = s.lastIndexOf(",");
        String substring;
        if (lastIndexOf > -1) {
            substring = s.substring(0, lastIndexOf);
        } else {
            substring = s;
        }
        String replaceAll = substring.replaceAll(" ", "");
        int time = getTime(replaceAll);
        return time;
    }

    public int getTime(String s) {
        int time = 0;
        try {
            int lastIndexOf = s.lastIndexOf(":");
            String ss_string = s.substring(lastIndexOf + 1, s.length());
            int ss = Integer.parseInt(ss_string);
            String mm_string = s.substring(0, lastIndexOf);
            int indexOf = mm_string.indexOf(":");
            if (indexOf == -1) {
                int mm = Integer.parseInt(mm_string);
                time = mm * 60 + ss;
            } else {
                String mm_hstring = mm_string.substring(indexOf + 1,
                        mm_string.length());
                int mm = Integer.parseInt(mm_hstring);

                String hh_string = mm_string.substring(0, indexOf);
                int hh = Integer.parseInt(hh_string);
                time = hh * 60 * 60 + mm * 60 + ss;
            }
            return time;
        } catch (Exception e) {
            return time;
        }
    }

    // get历史记录
    public HistoryAll getHistoryAll(String theString) {
        HistoryAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new HistoryAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e(readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<HistoryList> list = new ArrayList<HistoryList>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String courseid = jObject.optString("courseid", "");
                    String coursename = jObject.optString("coursename", "");
                    String videothumbnail = jObject.optString("videothumbnail",
                            "");
                    String taskstatus = jObject.optString("taskstatus", "");
                    HistoryList hl = new HistoryList(courseid, coursename,
                            videothumbnail, taskstatus);
                    list.add(hl);
                }
                ag = new HistoryAll(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new HistoryAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new HistoryAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }

    // 获取首页
    public Theme getTheme(String theString) {
        Theme ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new Theme();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e(theString);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count", "");

                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<ThemeGroup> list = new ArrayList<ThemeGroup>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String categoryid = jObject.optString("categoryid", "");
                    String name = jObject.optString("name", "");
                    String baseimg = jObject.optString("baseimg", "");
                    String grouplock = jObject.optString("lock", "");

                    JSONArray vObject = jObject.getJSONArray("videolist");
                    List<ThemeType> listtype = new ArrayList<ThemeType>();
                    for (int y = 0; y < vObject.length(); y++) {
                        JSONObject jsonObject2 = vObject.getJSONObject(y);
                        String agegroupid = jsonObject2.optString("agegroupid",
                                "");
                        String agegroupname = jsonObject2.optString(
                                "agegroupname", "");
                        String coursename = jsonObject2.optString("coursename",
                                "");
                        String courseimg = jsonObject2.optString("courseimg",
                                "");
                        String courseid = jsonObject2.optString("courseid", "");
                        String lock = jsonObject2.optString("lock", "");
                        String price = jsonObject2.optString("price", "");
                        String isbuy = jsonObject2.optString("isbuy", "");
                        String oldprice = jsonObject2.optString("old_price", "");
                        ThemeType tt = new ThemeType(agegroupid,
                                agegroupname, coursename, courseid, courseimg,
                                lock);
                        tt.setIsbuy(isbuy);
                        tt.setPrice(price);
                        tt.setOldPrice(oldprice);
                        listtype.add(tt);
                    }
                    int mColor = getColor(i);
                    ThemeGroup tg = new ThemeGroup(categoryid, name, baseimg, grouplock, mColor, listtype);
                    list.add(tg);
                }
                ag = new Theme(errorCode, errorMessage, total_count, list);
                return ag;
            } else {
                ag = new Theme();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new Theme();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }

    public BeanTaskErrorCode getBeanTask(String theString, Context context) {
        BeanTaskErrorCode errorCode = null;
        if (TextUtils.isEmpty(theString)) {
            errorCode = new BeanTaskErrorCode();
            errorCode.setErrorCode("1");
            errorCode.setErrorMessage("数据为空");
            return errorCode;
        }
        String readFrom = loadConvert(theString);
        L.e(readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCodel = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");

            if ("0".equals(errorCodel)) {
                JSONObject jsonObject = jo.getJSONObject("data");
//                JSONObject total_count = jsonObject.getJSONObject("total_count");
                JSONArray beanTasklist = jsonObject.getJSONArray("list");
                List<BeanTask> btList = new ArrayList<BeanTask>();
                for (int i = 0; i < beanTasklist.length(); i++) {
                    JSONObject btask = beanTasklist.getJSONObject(i);
                    String id = btask.optString("id", "");
                    String name = btask.optString("name", "");
                    String status = btask.optString("status", "");
                    String number = btask.optString("number", "");
                    BeanTask beanTask = new BeanTask(id, name, number, status);
                    btList.add(beanTask);
                }
                errorCode = new BeanTaskErrorCode();
                errorCode.setErrorCode(errorCodel);
                errorCode.setErrorMessage(errorMessage);
                errorCode.setList(btList);
            } else {
                errorCode = new BeanTaskErrorCode();
                errorCode.setErrorCode("1");
                errorCode.setErrorMessage(context.getString(R.string.no_date));
                return errorCode;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            errorCode = new BeanTaskErrorCode();
            errorCode.setErrorCode("1");
            errorCode.setErrorMessage(context.getString(R.string.no_date));
            return errorCode;
        }
        return errorCode;
    }


    public ErrorCodeBeanDetail getBeanDetail(String theString, Context context) {
        ErrorCodeBeanDetail eCode = null;
        if (TextUtils.isEmpty(theString)) {
            eCode = new ErrorCodeBeanDetail();
            eCode.setErrorCode("1");
            eCode.setErrorMessage(context.getString(R.string.no_date));
            return eCode;
        }
        String readFrom = loadConvert(theString);
        L.e(readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCodel = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");

            if ("0".equals(errorCodel)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String total_count = jsonObject.optString("total_count");
                JSONArray beanTasklist = jsonObject.getJSONArray("list");
                List<BeanDetail> btList = new ArrayList<BeanDetail>();
                for (int i = 0; i < beanTasklist.length(); i++) {
                    JSONObject btask = beanTasklist.getJSONObject(i);
                    String name = btask.optString("name", "");
                    String status = btask.optString("status", "");
                    String count = btask.optString("count", "");
                    String current_balance = btask.optString("current_balance", "");
                    String time = btask.optString("time", "");
                    String remark = btask.optString("remark", "");
                    BeanDetail beanTask = new BeanDetail(current_balance, time, count, name, remark, status);
                    btList.add(beanTask);
                }
                eCode = new ErrorCodeBeanDetail();
                eCode.setErrorCode(errorCodel);
                eCode.setErrorMessage(errorMessage);
                eCode.setTotal_count(total_count);
                eCode.setList(btList);
            } else {
                eCode = new ErrorCodeBeanDetail();
                eCode.setErrorCode("1");
                eCode.setErrorMessage(context.getString(R.string.no_date));
                return eCode;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            eCode = new ErrorCodeBeanDetail();
            eCode.setErrorCode("1");
            eCode.setErrorMessage(context.getString(R.string.no_date));
            return eCode;
        }
        return eCode;


    }


    public int getColor(int i) {
        int color = 1;
        if (i < 7) {
//            Android ad = new Android(i + "", i + 1 + "");
//            System.out.println(ad.getName() + "/" + ad.getTest());
            color = i + 1;

        } else {

            int j = (i - 7) / 6;
            if (j % 2 == 0) {

                int k = (i - 7) % 6;
                color = 6 - k;
//                Android ad = new Android(i + "", 6 - k + "");
//                System.out.println(ad.getName() + "/" + ad.getTest());
            } else {
                int k = (i - 7) % 6 + 2;
                color = k;
//                Android ad = new Android(i + "", k + "");
//                System.out.println(ad.getName() + "/" + ad.getTest());

            }
        }

        return color;
    }


    public TrainingAidAll getAidsperson(String theString) {
        TrainingAidAll ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new TrainingAidAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e(theString);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<TrainingAid> list = new ArrayList<TrainingAid>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String aidsname = jObject.optString("aidsname", "");
                    String aidsid = jObject.optString("aidsid", "");
                    String status = jObject.optString("status", "");

                    TrainingAid ta = new TrainingAid(aidsname, aidsid, status);
                    list.add(ta);
                }
                ag = new TrainingAidAll(errorCode, errorMessage, list);
                return ag;
            } else {
                ag = new TrainingAidAll();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new TrainingAidAll();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
    }

    /**
     * 获得乐豆余额
     *
     * @param content
     * @return balance
     */
    public String getLeBeanCountOver(String content, Context context) {
        String balance = "";
        if (TextUtils.isEmpty(content)) {
            return balance;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                JSONObject jsonArray = jsonObject.getJSONObject("list");
                balance = jsonArray.optString("balance", "");
                return balance;
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            return balance;
        }
        return balance;

    }


    /**
     *  关注（取消关注）
     *
     * @param content
     * @return balance
     */
    public String getConcernOver(String content, Context context) {
        String errorCode = null;
        if (TextUtils.isEmpty(content)) {
            return errorCode;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if(!"0".equals(errorCode)){
                Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show();
            }
            return errorCode;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            return errorCode;
        }
    }

    /**
     * 获得关注中心列表
     *
     * @param content
     * @return balance
     */
    public ConcernAll getConcernAllOver(String content, Context context) {
        ConcernAll concernAll = null;
        if (TextUtils.isEmpty(content)) {
            concernAll = new ConcernAll();
            return concernAll;
        }
        String readFrom = loadConvert(content);
        L.e("获得关注中心列表:"+content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            JSONObject jsonObject = jo.getJSONObject("data");
            String total_count = jsonObject.optString("total_count");
            if ("0".equals(errorCode)) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<AttentionCenter> list = new ArrayList<AttentionCenter>();
                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String ins_id = jObject.optString("ins_id");
                    String ins_name = jObject.optString("ins_name");
                    String face_pic = jObject.optString("face_pic");
                    String score = jObject.optString("score");
                    String ins_addr = jObject.optString("ins_addr");
                    String distance = jObject.optString("distance");
                    String comment_num = jObject.optString("comment_num");
                    String report_num = jObject.optString("report_num");
//                    AttentionCenter attentionCenter=new AttentionCenter(ins_name,face_pic,ins_addr,score,comment_num,distance,report_num,"true");
//                    attentionCenter.setCenterId(ins_id);
//                    list.add(attentionCenter);
                }
                concernAll = new ConcernAll(errorCode, errorMessage,total_count,list);
            } else {
                concernAll = new ConcernAll(errorCode, errorMessage,total_count,null);
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
            return concernAll;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            concernAll = new ConcernAll();
            L.d(e.getMessage());
            return concernAll;
        }
    }

    /**
     * 获取乐豆兑换列表
     *
     * @param theString
     * @param context
     * @return
     */
    public LDChangeEorror getLDChangeOver(String theString, Context context) {
        LDChangeEorror errorCode = null;
        if (TextUtils.isEmpty(theString)) {
            errorCode = new LDChangeEorror();
            errorCode.setErrorCode("1");
            errorCode.setErrorMessage("数据为空");
            return errorCode;
        }
        String readFrom = loadConvert(theString);
        L.e(readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCodel = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");

            if ("0".equals(errorCodel)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                JSONArray beanTasklist = jsonObject.getJSONArray("list");
                List<LDChange> btList = new ArrayList<LDChange>();
                for (int i = 0; i < beanTasklist.length(); i++) {
                    JSONObject changeObject = beanTasklist.getJSONObject(i);
                    int id = changeObject.optInt("id");
                    String code = changeObject.optString("code", "");
                    String state = changeObject.optString("state", "");
                    String name = changeObject.optString("name", "");
                    String begin_time = changeObject.optString("begin_time", "");
                    String end_time = changeObject.optString("end_time", "");
                    String reward = changeObject.optString("reward", "");
                    LDChange beanChange = new LDChange(id, code, state, name, begin_time, end_time, reward);
                    btList.add(beanChange);
                }
                errorCode = new LDChangeEorror();
                errorCode.setErrorCode(errorCodel);
                errorCode.setErrorMessage(errorMessage);
                errorCode.setLdChange(btList);
            } else {
                errorCode = new LDChangeEorror();
                errorCode.setErrorCode("1");
                errorCode.setErrorMessage(context.getString(R.string.no_date));
                return errorCode;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            errorCode = new LDChangeEorror();
            errorCode.setErrorCode("1");
            errorCode.setErrorMessage(context.getString(R.string.no_date));
            return errorCode;
        }
        return errorCode;
    }


    /**
     * 兑换乐豆券
     *
     * @param content
     * @return balance
     */
    public SingleHttpBean getLDVoucherOver(String content, Context context) {
        SingleHttpBean singlehttp = new SingleHttpBean();
        if (TextUtils.isEmpty(content)) {
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            singlehttp.setErrorCode(errorCode);
            singlehttp.setErrorMessage(errorMessage);
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                JSONObject jsonArray = jsonObject.getJSONObject("list");
                String reward = jsonArray.optString("reward", "");
                singlehttp.setReward(reward);
                return singlehttp;
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        return singlehttp;
    }

    /**
     * 获得乐豆换礼物连接
     *
     * @param content
     * @return balance
     */
    public String getLeBeanGiftOver(String content) {
        String url = "";
        if (TextUtils.isEmpty(content)) {
            return url;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            url = jo.optString("url", "");
            return url;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            return url;
        }
    }


    /**
     * 获取用户账户余额
     *
     * @param content
     * @return balance
     */
    public SingleHttpBean getUserHoursOver(String content, Context context) {
        SingleHttpBean singlehttp = new SingleHttpBean();
        if (TextUtils.isEmpty(content)) {
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            singlehttp.setErrorCode(errorCode);
            singlehttp.setErrorMessage(errorMessage);
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                String course_hours = jsonObject.optString("course_hours", "");
                String give_course_hours = jsonObject.optString("give_course_hours", "");
                String money_amount = jsonObject.optString("money_amount", "");
                String give_money_amount = jsonObject.optString("give_money_amount", "");
                String is_mumber = jsonObject.optString("is_member", "");
                singlehttp.setCourse_hours(course_hours);
                singlehttp.setGive_course_hours(give_course_hours);
                singlehttp.setMoney_amount(money_amount);
                singlehttp.setGive_money_amount(give_money_amount);
                singlehttp.setIs_mumber(is_mumber);
                return singlehttp;
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        return singlehttp;
    }

    public SingleHttpBean getUserFeedBack(String content, Context context) {
        SingleHttpBean singlehttp = new SingleHttpBean();
        if (TextUtils.isEmpty(content)) {
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            singlehttp.setErrorCode(errorCode);
            singlehttp.setErrorMessage(errorMessage);
            if ("0".equals(errorCode)) {
                return singlehttp;
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        return singlehttp;
    }
    public Advertisement getAdvertisement(String content, Context context) {
        Advertisement singlehttp = new Advertisement();
        if (TextUtils.isEmpty(content)) {
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            singlehttp.setErrorCode(errorCode);
            singlehttp.setErrorMessage(errorMessage);
            if ("0".equals(errorCode)) {
                List<AdvertisementList> list=new ArrayList<AdvertisementList>();
                JSONObject data = jo.optJSONObject("data");
                String total_count = data.optString("total_count");
                JSONArray datalist = data.optJSONArray("list");
                for(int i=0;i<datalist.length();i++){
                    JSONObject jsonObject = datalist.getJSONObject(i);
                    String ads_name = jsonObject.optString("ads_name");
                    String ads_type = jsonObject.optString("ads_type");
                    String ads_url = jsonObject.optString("ads_url");
                    String media_url = jsonObject.optString("media_url");
//                    String media_url ="http://pic25.nipic.com/20121209/9252150_194258033000_2.jpg";
                    String ads_content = jsonObject.optString("ads_content");
                    String start_time = jsonObject.optString("start_time");
                    String end_time = jsonObject.optString("end_time");
                    AdvertisementList alist=new AdvertisementList(ads_name,ads_type,ads_url,media_url,ads_content,start_time,end_time);
//                    AdvertisementList alist=new AdvertisementList("","","",media_url,"","","");
                    list.add(alist);
                }
                singlehttp.setTotal_count(total_count);
                singlehttp.setList(list);
                return singlehttp;
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            singlehttp.setErrorCode("1");
            singlehttp.setErrorMessage(context.getString(R.string.no_date));
            return singlehttp;
        }
        return singlehttp;
    }

    public CancelReason getCancelReasonOver(String content, Context context) {
        List<CancelReasonList> listReason = new ArrayList<CancelReasonList>();
        CancelReason cancelReason = new CancelReason();
        if (TextUtils.isEmpty(content)) {
            cancelReason.setErrorCode("1");
            cancelReason.setErrorMessage(context.getString(R.string.no_date));
            return cancelReason;
        }
        String readFrom = loadConvert(content);
        L.d(content);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            cancelReason.setErrorCode(errorCode);
            cancelReason.setErrorMessage(errorMessage);
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo.getJSONObject("data");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<OfflineCourses> list = new ArrayList<OfflineCourses>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("id",
                            "");
                    String reason = jObject.optString("reason", "");

                    CancelReasonList cancelReasonList = new CancelReasonList(id, reason);
                    listReason.add(cancelReasonList);
                }
                cancelReason.setCancelReasonList(listReason);
            } else {
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            L.d(e.getMessage());
            cancelReason.setErrorCode("1");
            cancelReason.setErrorMessage(context.getString(R.string.no_date));
            return cancelReason;
        }
        return cancelReason;
    }


    // public
    // 转换unicode
    public String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private String SDPath = Environment.getExternalStorageDirectory() + "/";
    private final String fileName = "city.txt";
    //
    // /**
    // * <pre>
    // * 方法说明： 写入缓存日志
    // * 编写日期: 2015年4月24日
    // * 编写人员: 吴培涛
    // *
    // </pre>
    // *
    // * @param fileName
    // * 文件名
    // * @param write_str
    // * 写入的字符
    // * @return
    // */
    // public void writeSDFile(String fileName, String write_str) {
    // File fe = new File(SDPath);
    // if (!fe.exists()) {
    // fe.mkdirs();
    // }
    // try {
    // File file = new File(SDPath + fileName);
    //
    // FileOutputStream fos = new FileOutputStream(file);
    //
    // byte[] bytes = write_str.getBytes();
    // Log.e("完成1", "***************" + bytes);
    // fos.write(bytes);
    // fos.flush();
    // fos.close();
    // Log.e("完成", "***************");
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // }
    // }


    //得到city文件数据列表
    public String getFromAssets(Context mContext, String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    mContext.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            return Result = "{\"errorCode\":1,\"errorMessage\":[\"错误\"],\"data\":[]}";
        }
    }

    // 获取线上
    public CenterCity getCityOnline(String theString) {
        CenterCity ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("+++" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONArray jsonArray = jo.getJSONArray("data");
                List<ProvinceBean> list = new ArrayList<ProvinceBean>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("pid", "");
                    String name = jObject.optString("pname", "");
                    JSONArray cityChilds = jObject.getJSONArray("city_list");
                    List<CityBean> listCityModel = new ArrayList<CityBean>();
                    if (cityChilds.length() == 0) {

                        DistrictBean dm = new DistrictBean(name, id);
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        listDistrictModel.add(dm);
                        CityBean cm = new CityBean(name, id, listDistrictModel);
                        listCityModel.add(cm);
                        ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                        list.add(pm);
                        continue;
                    }

                    for (int p = 0; p < cityChilds.length(); p++) {
                        JSONObject cityObject = cityChilds.getJSONObject(p);
                        String cityId = cityObject.optString("cid", "");
                        String cityName = cityObject.optString("cname",
                                "");
                        JSONArray districtChilds = cityObject
                                .getJSONArray("district_list");
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        if (districtChilds.length() == 0) {
                            DistrictBean dm = new DistrictBean(cityName,
                                    cityId);
                            listDistrictModel.add(dm);
                            CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                            listCityModel.add(cm);
                            ProvinceBean pm = new ProvinceBean(cityId,
                                    cityName, listCityModel);
                            list.add(pm);
                            continue;
                        }

                        for (int q = 0; q < districtChilds.length(); q++) {
                            JSONObject districtObject = districtChilds
                                    .getJSONObject(q);
                            String districtId = districtObject.optString("did",
                                    "");
                            String districtName = districtObject.optString(
                                    "dname", "");
                            DistrictBean dm = new DistrictBean(districtName,
                                    districtId);
                            listDistrictModel.add(dm);
                        }

                        CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                        listCityModel.add(cm);
                    }

                    ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                    list.add(pm);
                }
                ag = new CenterCity(errorCode, errorMessage, list);
                return ag;
            } else {
                ag = new CenterCity();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }

    // 获取家中心城市线上
    public CenterCity getCenterCityOnline(String theString) {
        CenterCity ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);
        L.e("+++" + readFrom);
        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONArray jsonArray = jo.getJSONArray("data");
                List<ProvinceBean> list = new ArrayList<ProvinceBean>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("id", "");
                    String name = jObject.optString("division_name", "");
                    JSONArray cityChilds = jObject.getJSONArray("childs");
                    List<CityBean> listCityModel = new ArrayList<CityBean>();
                    if (cityChilds.length() == 0) {

                        DistrictBean dm = new DistrictBean(name, id);
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        listDistrictModel.add(dm);
                        CityBean cm = new CityBean(name, id, listDistrictModel);
                        listCityModel.add(cm);
                        ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                        list.add(pm);
                        continue;
                    }

                    for (int p = 0; p < cityChilds.length(); p++) {
                        JSONObject cityObject = cityChilds.getJSONObject(p);
                        String cityId = cityObject.optString("id", "");
                        String cityName = cityObject.optString("division_name",
                                "");
                        JSONArray districtChilds = cityObject
                                .getJSONArray("childs");
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        if (districtChilds.length() == 0) {
                            DistrictBean dm = new DistrictBean(cityName,
                                    cityId);
                            listDistrictModel.add(dm);
                            CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                            listCityModel.add(cm);
                            ProvinceBean pm = new ProvinceBean(cityId,
                                    cityName, listCityModel);
                            list.add(pm);
                            continue;
                        }

                        for (int q = 0; q < districtChilds.length(); q++) {
                            JSONObject districtObject = districtChilds
                                    .getJSONObject(q);
                            String districtId = districtObject.optString("id",
                                    "");
                            String districtName = districtObject.optString(
                                    "division_name", "");
                            DistrictBean dm = new DistrictBean(districtName,
                                    districtId);
                            listDistrictModel.add(dm);
                        }

                        CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                        listCityModel.add(cm);
                    }

                    ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                    list.add(pm);
                }
                ag = new CenterCity(errorCode, errorMessage, list);
                return ag;
            } else {
                ag = new CenterCity();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }
}