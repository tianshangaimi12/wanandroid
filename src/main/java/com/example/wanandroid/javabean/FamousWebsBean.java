package com.example.wanandroid.javabean;

import java.util.List;

/**
 * Created by zhangchong on 18-3-20.
 */
public class FamousWebsBean {

    private int errorCode;
    private String errorMsg;
    private List<WebBean> data;

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

    public List<WebBean> getData() {
        return data;
    }

    public void setData(List<WebBean> data) {
        this.data = data;
    }

    public static class WebBean
    {
        private String name;
        private String link;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
