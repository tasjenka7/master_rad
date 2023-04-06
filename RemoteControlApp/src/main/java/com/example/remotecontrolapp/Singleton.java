package com.example.remotecontrolapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.remotecontrolapp.controller.CommandsHandler;
import com.example.remotecontrolapp.stbs.DiscoveryHandler;

public class Singleton extends Application {

    private static final String TAG = "Singleton";

    @SuppressLint("StaticFieldLeak")
    private static Context context;     //Activity context
    @SuppressLint("StaticFieldLeak")
    private static Singleton instance;  //Singleton instance

    private DiscoveryHandler discoveryHandler; //BackendHandler
    private CommandsHandler commandsHandler; //RemoteHandler

    public Singleton(){//Constructor

    }

    public static Context getContext(){
        return context;
    }

    public static Singleton getInstance(){

        if(instance == null){
            instance = new Singleton();
            Log.d(TAG, "[getInstance] instance == null, new Singleton()");
        }
        return instance;
    }


    public DiscoveryHandler getDiscoveryHandler(){
        return discoveryHandler;
    }

    public CommandsHandler getCommandsHandler(){
        return commandsHandler;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = this;
        discoveryHandler = new DiscoveryHandler();
        commandsHandler = new CommandsHandler();

        Log.d(TAG, "[onCreate]");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
