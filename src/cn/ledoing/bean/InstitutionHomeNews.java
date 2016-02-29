package cn.ledoing.bean;

import java.util.List;

/**
 * Created by lc-php1 on 2015/11/25.
 */
public class InstitutionHomeNews extends BaseBean {

    private Datas data;

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public class Datas{
        private String total_count;

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        private List<DatasList> list;

        public List<DatasList> getList() {
            return list;
        }

        public void setList(List<DatasList> list) {
            this.list = list;
        }

        public class DatasList{
            private String news_id;
            private String news_pic;

            public String getNews_pic() {
                return news_pic;
            }

            public void setNews_pic(String news_pic) {
                this.news_pic = news_pic;
            }

            public String getNews_id() {
                return news_id;
            }

            public void setNews_id(String news_id) {
                this.news_id = news_id;
            }
        }
    }
}
