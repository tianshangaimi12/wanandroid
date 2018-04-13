package com.example.wanandroid.javabean;

import java.util.List;

/**
 * Created by zhangchong on 18-4-13.
 */
public class SearchWordsBean {

    private int errorCode;
    private String errorMsg;
    private List<SearchWordBean> data;

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

    public List<SearchWordBean> getData() {
        return data;
    }

    public void setData(List<SearchWordBean> data) {
        this.data = data;
    }

    public static class SearchWordBean{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
