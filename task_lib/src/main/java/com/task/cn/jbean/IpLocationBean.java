package com.task.cn.jbean;

/**
 * Description:
 * Created by Quinin on 2020-03-26.
 **/
public class IpLocationBean {

    /**
     * status : OK
     * result : {"location":{"lng":110.365554,"lat":21.276723},"precise":0,"confidence":20,"level":"城市"}
     */

    private String status;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * location : {"lng":110.365554,"lat":21.276723}
         * precise : 0
         * confidence : 20
         * level : 城市
         */

        private Location location;
        private int precise;
        private int confidence;
        private String level;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public int getPrecise() {
            return precise;
        }

        public void setPrecise(int precise) {
            this.precise = precise;
        }

        public int getConfidence() {
            return confidence;
        }

        public void setConfidence(int confidence) {
            this.confidence = confidence;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public static class Location {
            /**
             * lng : 110.365554
             * lat : 21.276723
             */

            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }
    }
}
