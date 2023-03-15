package com.example.remotecontrolapp.stbs;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;


public class NsdDiscover {

    private final String TAG = this.getClass().getSimpleName();

    private final String NSD_SERVICE_TYPE = "_myvtremote._udp.";

    Context mContext;

    NsdManager mNsdManager;
    NsdManager.DiscoveryListener mDiscoveryListener;

    public NsdDiscover(Context context){

        this.mContext = context;
        this.mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        Log.d(TAG, "[constructor] -> before initializing discovery listener");
        initializeDiscoveryListener();
        Log.d(TAG, "'[constructor]' -> between initializations");
        initializeResolveListener();
        Log.d(TAG, "[constructor] -> after initializing resolve listener");
    }


    private NsdManager.DiscoveryListener initializeDiscoveryListener() {

        NsdManager.DiscoveryListener listener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "[onDiscoveryStarted] Service discovery started, regType " + regType);

                WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                String ipAddress = wifiInfo.toString();

                Log.d(TAG, "[onDiscoveryStarted] ipAdress: " + ipAddress);

            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "[onServiceFound] Service discovery success: " + service);

                String type = service.getServiceType();

                if(!type.equals(NSD_SERVICE_TYPE)) {
                    Log.d(TAG, "[onServiceFound] No matching type, found: " + type);
                }if (service.getServiceType().equals(NSD_SERVICE_TYPE) && !service.getServiceName().equalsIgnoreCase(Settings.Global.getString(mContext.getContentResolver(), Settings.Global.DEVICE_NAME))) {
                    mNsdManager.resolveService(service, initializeResolveListener());
                    Log.d(TAG, "[onServiceFound] Service found: " + service.getServiceName());
                }

            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e(TAG, "[onServiceLost]: service" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "[onDiscoveryStopped] serviceType " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "[onStopDiscoveryFailed] Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "[onStopDiscoveryFailed] Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
        Log.d(TAG, "[initializeDiscoveryListener] initialized");
        return listener;
    }

    private NsdManager.ResolveListener initializeResolveListener(){

        NsdManager.ResolveListener listener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int errorCode) {
                Log.e(TAG, "[onResolveFailed] Error code: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                Log.d(TAG, "[onServiceResolved] nsdServiceInfo: " + nsdServiceInfo);

                onDiscover(nsdServiceInfo);
            }
        };
        return listener;
    }

    public void tearDown(){
        Log.d(TAG, "[tearDown]");
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public void startDiscovery(){
        Log.d(TAG, "[startDiscovery]");
        stopDiscovery();
        mDiscoveryListener = initializeDiscoveryListener();
        mNsdManager.discoverServices(NSD_SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery(){
        if(mDiscoveryListener != null){
            try{
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
                Log.d(TAG, "[stopDiscovery] try");
            }finally {
                Log.d(TAG, "[stopDiscovery] finally");
            }
            mDiscoveryListener = null;
        }
    }

    public void onDiscover(NsdServiceInfo service){}

}
