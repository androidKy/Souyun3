package com.account.manager.ui.login.data.model;

import com.account.manager.model.BaseData;

/**
 * Description:
 * Created by Quinin on 2020-03-13.
 **/
public class LoginData extends BaseData {


    /**
     * ret : 200
     * data : {"user_id":1,"token":"8f7759bd461009cf5b727e0a9df7c4fb","login_time":"2020-03-13 14:28:20"}
     * msg :
     */

    private int ret;
    private Login data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public static class Login {
        /**
         * user_id : 1
         * token : 8f7759bd461009cf5b727e0a9df7c4fb
         * login_time : 2020-03-13 14:28:20
         */

        private int user_id;
        private String token;
        private String login_time;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }
    }
}
