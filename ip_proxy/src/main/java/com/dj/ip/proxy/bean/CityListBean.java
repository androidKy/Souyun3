package com.dj.ip.proxy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by Quinin on 2019-07-19.
 **/
public class CityListBean implements Serializable {

    /**
     * code : 0
     * msg :
     * data : {"count":24,"cityList":[{"name":"安徽","data":[{"value":15613,"name":"宿州市","cityid":"341300"},{"value":8486,"name":"阜阳市","cityid":"341200"}]},{"name":"广东","data":[{"value":17495,"name":"佛山市","cityid":"440600"},{"value":7426,"name":"汕头市","cityid":"440500"}]}]}
     */

    private int code;
    private String msg;
    private CityData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CityData getData() {
        return data;
    }

    public void setData(CityData data) {
        this.data = data;
    }

    public static class CityData {
        /**
         * count : 24
         * cityList : [{"name":"安徽","data":[{"value":15613,"name":"宿州市","cityid":"341300"},{"value":8486,"name":"阜阳市","cityid":"341200"}]},{"name":"广东","data":[{"value":17495,"name":"佛山市","cityid":"440600"},{"value":7426,"name":"汕头市","cityid":"440500"}]}]
         */

        private int count;
        private List<ProvinceList> cityList;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ProvinceList> getCityList() {
            return cityList;
        }

        public void setCityList(List<ProvinceList> provinceList) {
            this.cityList = provinceList;
        }

        public static class ProvinceList {
            /**
             * name : 安徽
             * data : [{"value":15613,"name":"宿州市","cityid":"341300"},{"value":8486,"name":"阜阳市","cityid":"341200"}]
             */

            private String name;
            private List<CityList> data;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CityList> getData() {
                return data;
            }

            public void setData(List<CityList> data) {
                this.data = data;
            }

            public static class CityList {
                /**
                 * value : 15613
                 * name : 宿州市
                 * cityid : 341300
                 */

                private int value;
                private String name;
                private String cityid;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCityid() {
                    return cityid;
                }

                public void setCityid(String cityid) {
                    this.cityid = cityid;
                }
            }
        }
    }
}
