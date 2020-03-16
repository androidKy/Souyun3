package com.account.manager.model;

/**
 * Description:
 * Created by Quinin on 2020-03-16.
 **/
public class CommitAccountBean extends BaseData {

    /**
     * account : 1784389748
     * password : abc123456
     * platform : 1
     * device_id : 1
     * login_info : {"ip":"192.89.194.3","city":"广州市","city_code":"55500","login_date":"2020-3-2 17:53"}
     * register_date : 2020-3-3 17:53
     */

    private String account;
    private String password;
    private String platform;
    private String device_id;
    private LoginInfoBean login_info;
    private String register_date;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public LoginInfoBean getLogin_info() {
        return login_info;
    }

    public void setLogin_info(LoginInfoBean login_info) {
        this.login_info = login_info;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public static class LoginInfoBean {
        /**
         * ip : 192.89.194.3
         * city : 广州市
         * city_code : 55500
         * login_date : 2020-3-2 17:53
         */

        private String ip;
        private String city;
        private String city_code;
        private String login_date;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getLogin_date() {
            return login_date;
        }

        public void setLogin_date(String login_date) {
            this.login_date = login_date;
        }
    }
}
