package cn.ledoing.bean;

import java.util.List;

/**
 * Created by wupeitao on 15/11/24.
 */
public class TracherHomeNews extends BaseBean {

    public Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<ListData> list;
        private int total_count;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public class ListData {
            private String news_id;
            private String news_pic;

            public String getNews_id() {
                return news_id;
            }

            public void setNews_id(String news_id) {
                this.news_id = news_id;
            }

            public String getNews_pic() {
                return news_pic;
            }

            public void setNews_pic(String news_pic) {
                this.news_pic = news_pic;
            }
        }
    }
}
