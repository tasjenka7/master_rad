package com.example.remotecontrolapp;

public interface AsyncReceiver {

    void onReceive();

    void onFailed(String error);
}
