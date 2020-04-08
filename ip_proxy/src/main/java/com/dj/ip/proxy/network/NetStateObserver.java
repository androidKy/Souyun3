package com.dj.ip.proxy.network;

public interface NetStateObserver {
    void onDisconnected();

    void onConnected(NetworkType networkType);
}
