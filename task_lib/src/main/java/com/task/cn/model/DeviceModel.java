package com.task.cn.model;

import com.google.gson.annotations.SerializedName;
import com.task.cn.jbean.DeviceInfoBean;

/**
 * Description:
 * Created by Quinin on 2020-03-16.
 **/
public class DeviceModel {

    /**
     * ret : 200
     * data : {"id":1}
     * msg :
     */

    private int ret;
    private DeviceData data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DeviceData getData() {
        return data;
    }

    public void setData(DeviceData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DeviceData {
        /**
         * id : 1
         */

        private long id;

        @SerializedName("device_info")
        private DeviceInfoBean deviceInfoBean;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public DeviceInfoBean getDeviceInfoBean() {
            return deviceInfoBean;
        }

        public void setDeviceInfoBean(DeviceInfoBean deviceInfoBean) {
            this.deviceInfoBean = deviceInfoBean;
        }
    }
}
