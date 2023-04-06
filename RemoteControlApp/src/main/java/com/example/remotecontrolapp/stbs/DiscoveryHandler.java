package com.example.remotecontrolapp.stbs;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.example.remotecontrolapp.AsyncDataReceiver;
import com.example.remotecontrolapp.Singleton;

import java.util.ArrayList;
import java.util.Objects;

public class DiscoveryHandler {

    private final String TAG = this.getClass().getSimpleName();

    private NsdDiscover nsdDiscover;
    private ArrayList<Stb> stbList = new ArrayList<>();

    public void getStbList(final AsyncDataReceiver receiver){

        stopDiscovery();

        nsdDiscover = new NsdDiscover(Singleton.getContext()){
            @Override
            public void onDiscover(NsdServiceInfo service) {
                Log.d(TAG, "[onDiscover] service discovered: " + service.getServiceName());
                boolean alreadyFound = false;
                Stb stb = new Stb(0, service.getServiceName(), service.getServiceType(), service.getHost(), service.getPort());

                if(stbList.size() == 0){
                    stbList.add(stb);
                    receiver.onReceive(stbList);
                }else{
                    for(int i = 0; i < stbList.size(); i++){
                        if(Objects.requireNonNull(stbList.get(i).getHost().getHostAddress()).equalsIgnoreCase(stb.getHost().getHostAddress())){
                            alreadyFound = true;
                            break;
                        }
                    }
                    if(!alreadyFound){
                        stbList.add(stb);
                    }
                    receiver.onReceive(stbList);
                }
                Log.d(TAG, "[onDiscover] choose box: " + service.getServiceName() + " on port " + service.getPort());
            }
        };
        startDiscovery();
    }

    public void startDiscovery(){
        Log.d(TAG, "[startDiscovery]");
        nsdDiscover.startDiscovery();
    }

    public void stopDiscovery(){

        if(nsdDiscover != null){
            Log.d(TAG, "[stopDiscovery]");
            nsdDiscover.stopDiscovery();
        }
    }
}
