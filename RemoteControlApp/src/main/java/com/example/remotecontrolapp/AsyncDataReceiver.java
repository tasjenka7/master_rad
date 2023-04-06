package com.example.remotecontrolapp;

public interface AsyncDataReceiver<T> {

    void onReceive(T var);
    void onFailed(T var);

}
