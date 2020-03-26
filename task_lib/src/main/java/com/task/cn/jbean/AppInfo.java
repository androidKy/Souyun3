package com.task.cn.jbean;

import android.graphics.drawable.Drawable;

/**
 * Description:
 * Created by Quinin on 2020-03-16.
 **/
public class AppInfo {
    private String appName;
    private String pkgName;
    private String versionName;
    private String versionCode;
    private Drawable iconDrawable;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }
}
