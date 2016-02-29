package cn.ledoing.bean;

/**
 * Created by wpt on 2015/8/18.
 */
public class BeanTask {
    private String id;
    private String taskname;
    private String addlecbean;
    private String tasktype;

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getAddlecbean() {
        return addlecbean;
    }

    public void setAddlecbean(String addlecbean) {
        this.addlecbean = addlecbean;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public BeanTask(String id, String taskname, String addlecbean, String tasktype) {
        this.id = id;
        this.taskname = taskname;
        this.addlecbean = addlecbean;
        this.tasktype = tasktype;
    }

    public BeanTask() {
    }
}
