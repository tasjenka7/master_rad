package com.example.remotecontrolapp.stbs;

import android.util.Log;

import java.net.InetAddress;

public class Stb {

    String TAG = this.getClass().getSimpleName();


    private int id;
    private String serviceName;
    private String serviceType;
    private InetAddress host;
    private int port;

    public Stb(int id, String serviceName, String serviceType, InetAddress host, int port){

        this.id = id;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.host = host;
        this.port = port;

        Log.d(TAG, "[contructor] stb created");
    }

    public int getId(){
        return this.id;
    }

    public String getServiceName(){
        return this.serviceName;
    }


    public InetAddress getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }



}
