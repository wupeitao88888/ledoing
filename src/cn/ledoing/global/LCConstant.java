/**
 * ****************************************************************************
 * Copyright (c) 2015 by ehoo Corporation all right reserved.
 * 2015-3-30
 * <p/>
 * *****************************************************************************
 */
package cn.ledoing.global;

import com.umeng.analytics.AnalyticsConfig;

import cn.ledoing.bean.LCLogin;

/**
 * <pre>
 * 业务名:
 * 功能说明:
 * 编写日期:	2015-3-30
 * 编写人员:	 吴培涛
 *
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class LCConstant {
    /**
     * http
     */
//        public static final String URL = "http://api.lechome.com";// 线上
    public static final String URL = "http://api.ledoedu.com";// 测试
    public static final String URL_API = "/api/v3/";
    public static final String USER_REGISTER = "user-register";//用户注册
    public static final String UPDATE_USER_INFO = "user-update-info";//更新用户信息
    public static final String USER_LOGIN = "user-login";//用户登录
    public static final String USER_INFO = "user-info-temp";//用户信息
    public static final String USER_UPLOAD_AVATAR = "user-upload-avatar";//上传用户头像
    public static final String USER_RESET_PASSWORD = "user-reset-password";//重置密码
    public static final String USER_AIDS_LIST = "user-aids-list";// 获取用户教具
    public static final String USER_UPDATE_AIDS = "user-update-aids";//  更新用户教具
    public static final String USER_CHANGE_MOBILE = "user-change-mobile";//   修改手机号
    public static final String USER_WORK_LIST = "user-works-list";//  个人作品列表
    public static final String USER_WORKS_UPLOAD = "user-upload-works";//  个人作品上传
    public static final String USER_LOGOUT = "user-logout";// 注销登录
    public static final String USER_CODE = "user-code";// 发送验证码
    public static final String TEAM_WORK_LIST = "team-works-list";// 团队作品列表
    public static final String TEAM_WORK_SHOW = "team-works-show";//团队作品展示
    public static final String TEAM_WORK_SHOW_PRAISE = "user-team-works-praise";//团队作品点赞
    public static final String USER_RECEIVE_TASK = "user-receive-task";//用户领取任务
    public static final String USER_FINISH_TASK = "user-finish-task";//用户去完成任务
    public static final String USER_PLAY_RECORD = "user-play-record";//用户观看视频的记录
    public static final String USER_QUESTION_ANSWER = "user-question-answer";//记录用户回答问题
    public static final String USER_TASK_LIST = "user-task-list";//个人任务列表(学习记录)
    public static final String COURSE_BASE_LIST = "course-base-list";// 首页课程列表
    public static final String COURSE_TASK_LIST = "course-task-list";// 任务视频列表
    //    public static final String COURSE_BASE_NAME_LIST= "course-base-name-list";//  课程名称列表   1.2取消，暂时没用到
    public static final String LEDOU_USER_BALANCE = "ledou-user-balance";// 乐豆余额
    public static final String LEDOU_USER_TASK_LIST = "ledou-user-task-list";// 乐豆任务列表
    public static final String LEDOU_USER_COUPON_LIST = "ledou-user-coupon-list";// 用户乐豆兑换券列表
    public static final String LEDOU_EXCHANGE = "ledou-exchange";//  兑换乐豆（兑换吗及兑换券）
    public static final String LEDOU_BUY_COURSE = "ledou-buy-course";//  用户购买课程(乐豆)
    public static final String LEDOU_USER_ORDER_LIST = "ledou-user-order-list";//  用户乐豆账单
    public static final String GET_DUIBA_URL = "get-duiba-url";//  获取兑吧url地址
    public static final String VERSION_VERSIONUPDATE = "/version/versionupdate";//  获取版本
    public static final String VERSION_QRCODE = "/version/qrcode";//  获取版本
    public static final String VERSION_UPDATERECORD = "re-confirm";//  确认课程
    public static final String RE_CANCEL = "re-cancel";//  取消课程
    public static final String CHECK_TASK_QUAESTION_ANSWER = "check-task-question-answer";//  已回答问题
    public static final String RE_USER_REBACK = "re-user-reback";//  用户反馈接口
    public static final String INSTITUTION_LIST = "institution-list";//得到中心列表
    public static final String RE_GET_AREA_LIST = "re-get-area-list";//得到中心选择城市列表
    public static final String RE_GET_ONLINE_CLASS_LIST = "re-get-online-class-list";//获取机构上线课程
    public static final String RE_GET_TEACHER_NAME_LIST = "re-get-teacher-list";//获取上课老师列表
    public static final String RE_GET_USER_ORDER_LIST = "re-get-user-order-list";//  线下课程
    public static final String ROWCLASS_TASK_LIST = "rowclass-task-list";//获取课程详情
    public static final String RE_GET_CANCEL_REASON = "re-get-cancel-reason";//  获取取消原因
    public static final String SAVE_USER_CLASS = "save-user-class";//预约课程任务
    public static final String RE_CHECK_USER_AMOUNT = "re-check-user-amount";//检查用户余额是否足够
    public static final String COMMON_GETAD = "/common/getad";//  获取广告位

    public static final String HAND_URL = "http://s.lechome.com/mobile/icon/add_me.png";//  获取广告位
    public static final String RE_GET_ORDER_DETAIL = "re-get-order-detail";//获取任务详情
    public static final String GET_INSTITUTION_HOME = "institution-home";// 获取机构中⼼心主页
    public static final String GUANZHU_HOME = "guanzhu-home";// 获取关注机构列表
    public static final String CENTER_LIST = "find-ins-list";//机构列表
    public static final String USER_GUANZHU = "user-guanzhu";// 关注取消关注
    public static final String EVALUATION_INS = "evaluation-ins";// 学员点评机构接接口
    public static final String GET_INSTITUTION_COMMENTS = URL + URL_API + "institution-comments";// 中心主页评论列表
    public static final String GET_INSTITUTION_TEACHER = URL + URL_API + "institution-home-teachers";// 中心主页教师列表
    public static final String TEACHER_HOME = URL + URL_API + "teacher-home";//教师主页个人信息
    public static final String TEACHER_HOME_NEWS = URL + URL_API + "teacher-home-news";//教师主页动态
    public static final String TEACHER_HOME_COMMENTS = URL + URL_API + "teacher-home-comments";//教师主页评价
    public static final String TEACHER_NEWS = URL + URL_API + "teacher-news";//教师动态列表
    public static final String TEACHER_SHOW = URL + URL_API + "news-show";//教师动态详情
    public static final String TEACHER_PRICE = URL + URL_API + "save-user-praise";//教师动态点赞
    public static final String TEACHER_COMMENT = URL + URL_API + "save-user-comment";//教师动态评论
    public static final String GET_INSTITUTION_HOME_NEWS = URL + URL_API + "institution-home-news-list";// 中心介绍动态
    public static final String GET_INSTITUTION_CENTER_NEWS = URL + URL_API + "institution-news";// 中心动态
    public static final String INFORMETION_CENTER = "institution-info";  // 非会员 机构信息
    public static final String RE_CHECK_USER_HOUR_AMOUNT = "re-check-user-hour-amount";//验证用户账户
    public static final String GET_CITY_LIST = "get-city-list";// 全部中心获得城市列表
    public static final String STUDENT_EVALUATE = "student-evaluate";// 学员评价接口
    public static final String GET_COMMENT_RECORD_SHOW = "record-show";// 学员评价获取信息接口
    public static final String GET_COMMENT_EVALUATE_INFO = "evaluate-info";// 学员查看评价获取信息接口
    /**
     * false没有登陆，true已登录
     */
    public static boolean islogin = false;
    public static String token = "37ebe220f3e716ebc8ca20621c03a472f05ce858-";
    public static LCLogin userinfo = null;// 用户登录信息
    public static boolean isRef = false;// 判断是否刷新
    public static String reToken = "37ebe220f3e716ebc8ca20621c03a472f05ce858-";// 判断是否是在刷新
    public static int VERIFY_TYPE_REGISTER = 1001;
    public static int VERIFY_TYPE_FINDPASSWD = 1002;
    public static int VERIFY_TYPE_USERNAME = 1003;
    public static boolean ISFRIST = true;

    public static boolean FRIST_NO_LOAD = false;// 关注机构列表


}