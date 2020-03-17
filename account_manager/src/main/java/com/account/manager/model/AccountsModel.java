package com.account.manager.model;

import java.util.List;

/**
 * Description:
 * Created by Quinin on 2020-03-17.
 **/
public class AccountsModel extends BaseData {

    /**
     * ret : 200
     * data : {"count":2,"lists":[{"id":2,"user_id":1,"account":"16283717282","password":"hqkwjsisj","platform":"1","login_info":{"city":"","city_code":"","ip":"","login_date":"2020-03-16 18:36:39"},"register_date":"2020-03-16 18:36:39","device_id":1,"status":0},{"id":1,"user_id":1,"account":"16283717282","password":"hqkwjsisj","platform":"1","login_info":{"city":"","city_code":"","ip":"","login_date":"2020-03-16 18:35:21"},"register_date":"2020-03-16 18:35:21","device_id":1,"status":0}]}
     * msg :
     */
    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * count : 2
         * lists : [{"id":2,"user_id":1,"account":"16283717282","password":"hqkwjsisj","platform":"1","login_info":{"city":"","city_code":"","ip":"","login_date":"2020-03-16 18:36:39"},"register_date":"2020-03-16 18:36:39","device_id":1,"status":0},{"id":1,"user_id":1,"account":"16283717282","password":"hqkwjsisj","platform":"1","login_info":{"city":"","city_code":"","ip":"","login_date":"2020-03-16 18:35:21"},"register_date":"2020-03-16 18:35:21","device_id":1,"status":0}]
         */
        private int count;
        private List<Account> lists;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Account> getLists() {
            return lists;
        }

        public void setLists(List<Account> lists) {
            this.lists = lists;
        }
    }
}
