package com.example.wanandroid.javabean;

import java.util.List;

/**
 * Created by zhangchong on 18-3-20.
 */
public class SystemBean {

    private int errorCode;
    private String errorMsg;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
    private List<SystemClass> data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<SystemClass> getData() {
        return data;
    }

    public void setData(List<SystemClass> data) {
        this.data = data;
    }

    public static class SystemClass
    {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int courseId;
        private String name;
        private List<SystemItemBean> children;

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SystemItemBean> getChildren() {
            return children;
        }

        public void setChildren(List<SystemItemBean> children) {
            this.children = children;
        }
    }
}
