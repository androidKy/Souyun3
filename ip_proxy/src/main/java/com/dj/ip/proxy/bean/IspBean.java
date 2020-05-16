package com.dj.ip.proxy.bean;

/**
 * description:
 * author:kyXiao
 * date:2020/5/16
 */
public class IspBean {
    private String ispName;
    private int ispFlag;    //随机0， 电信1，联通2，移动3， 广电4
    private int status = 0;    //使用状态，1表示正在使用，0表示未使用

    public IspBean(String ispName, int ispFlag) {
        this.ispName = ispName;
        this.ispFlag = ispFlag;
    }

    public IspBean(String ispName, int ispFlag, int status) {
        this.ispName = ispName;
        this.ispFlag = ispFlag;
        this.status = status;
    }

    public String getIspName() {
        return ispName;
    }

    public void setIspName(String ispName) {
        this.ispName = ispName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIspFlag() {
        return ispFlag;
    }

    public void setIspFlag(int ispFlag) {
        this.ispFlag = ispFlag;
    }
}
