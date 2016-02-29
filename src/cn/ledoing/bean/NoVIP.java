package cn.ledoing.bean;

/**
 * Created by wupeitao on 15/11/27.
 */
public class NoVIP extends BaseBean {
    private Result data;

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

    public class Result{
       private String ins_id;
        private String ins_addr;
        private String ins_name;
        private String business_hours;
        private String traffic_info;
        private String contact_info;

        public String getIns_id() {
            return ins_id;
        }

        public void setIns_id(String ins_id) {
            this.ins_id = ins_id;
        }

        public String getIns_addr() {
            return ins_addr;
        }

        public void setIns_addr(String ins_addr) {
            this.ins_addr = ins_addr;
        }

        public String getIns_name() {
            return ins_name;
        }

        public void setIns_name(String ins_name) {
            this.ins_name = ins_name;
        }

        public String getBusiness_hours() {
            return business_hours;
        }

        public void setBusiness_hours(String business_hours) {
            this.business_hours = business_hours;
        }

        public String getTraffic_info() {
            return traffic_info;
        }

        public void setTraffic_info(String traffic_info) {
            this.traffic_info = traffic_info;
        }

        public String getContact_info() {
            return contact_info;
        }

        public void setContact_info(String contact_info) {
            this.contact_info = contact_info;
        }
    }
}
