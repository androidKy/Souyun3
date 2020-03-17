package com.account.manager.model;

import com.google.gson.annotations.SerializedName;
import com.task.cn.jbean.DeviceInfoBean;

/**
 * Description:
 * Created by Quinin on 2020-03-17.
 **/
public class Account {
    /**
     * id : 2
     * user_id : 1
     * account : 16283717282
     * password : hqkwjsisj
     * platform : 1
     * login_info : {"city":"","city_code":"","ip":"","login_date":"2020-03-16 18:36:39"}
     * register_date : 2020-03-16 18:36:39
     * device_id : 1
     * status : 0
     */

    private int id;
    private int user_id;
    private String account;
    private String password;
    private String platform;
    private LoginMsg login_info;
    private String register_date;
    private int device_id;
    private int status;

    @SerializedName("device_info")
    private DeviceInfoBean deviceInfoBean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

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

    public LoginMsg getLogin_info() {
        return login_info;
    }

    public void setLogin_info(LoginMsg login_info) {
        this.login_info = login_info;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DeviceInfoBean getDeviceInfoBean() {
        return deviceInfoBean;
    }

    public void setDeviceInfoBean(DeviceInfoBean deviceInfoBean) {
        this.deviceInfoBean = deviceInfoBean;
    }

    public static class LoginMsg {
        /**
         * city :
         * city_code :
         * ip :
         * login_date : 2020-03-16 18:36:39
         */

        private String city;
        private String city_code;
        private String ip;
        private String login_date;

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

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getLogin_date() {
            return login_date;
        }

        public void setLogin_date(String login_date) {
            this.login_date = login_date;
        }
    }
}
