package com.example.wanandroid.javabean;

import java.util.List;

/**
 * Created by zhangchong on 18-4-3.
 */
public class CollectListBean {

    private int errorCode;
    private String errorMsg;
    private CollectNewsData data;

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

    public CollectNewsData getData() {
        return data;
    }

    public void setData(CollectNewsData data) {
        this.data = data;
    }

    public static class CollectNewsData
    {
        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<CollectNewsBean> datas;

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<CollectNewsBean> getDatas() {
            return datas;
        }

        public void setDatas(List<CollectNewsBean> datas) {
            this.datas = datas;
        }
    }
}
