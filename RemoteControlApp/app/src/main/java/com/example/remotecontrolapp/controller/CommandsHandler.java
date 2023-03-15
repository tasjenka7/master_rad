package com.example.remotecontrolapp.controller;

import static android.view.KeyEvent.KEYCODE_0;
import static android.view.KeyEvent.KEYCODE_1;
import static android.view.KeyEvent.KEYCODE_2;
import static android.view.KeyEvent.KEYCODE_3;
import static android.view.KeyEvent.KEYCODE_4;
import static android.view.KeyEvent.KEYCODE_5;
import static android.view.KeyEvent.KEYCODE_6;
import static android.view.KeyEvent.KEYCODE_7;
import static android.view.KeyEvent.KEYCODE_8;
import static android.view.KeyEvent.KEYCODE_9;
import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_CHANNEL_DOWN;
import static android.view.KeyEvent.KEYCODE_CHANNEL_UP;
import static android.view.KeyEvent.KEYCODE_DEL;
import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;
import static android.view.KeyEvent.KEYCODE_GUIDE;
import static android.view.KeyEvent.KEYCODE_HOME;
import static android.view.KeyEvent.KEYCODE_POWER;
import static android.view.KeyEvent.KEYCODE_TV;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_MUTE;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.example.remotecontrolapp.Constants;

public class CommandsHandler {

    String TAG = this.getClass().getSimpleName();
    private NsdServiceInfo service;
    private UdpClient client;
    private static int pairingCode;

    /* ---------------Getters and Setters ---------------------- */

    public NsdServiceInfo getService() {
        return service;
    }

    public void setService(NsdServiceInfo service) {
        this.service = service;
    }

    public UdpClient getClient() {
        return client;
    }

    public void setClient(UdpClient client) {
        this.client = client;
    }

    public int getRandomNumber() {
        pairingCode = (int) (Math.random() * 9000) + 1000;
        return pairingCode;
    }

    public int getPairingCode() {
        return pairingCode;
    }


    /* ----------------- Commands -------------------- */

    public void disconnect(){
        getClient().disconnect();
    }

    public void onPowerClicked(){
        Log.d(TAG, "[onPowerClicked]: " + Constants.COMMAND_POWER);
        getClient().send(String.valueOf(KEYCODE_POWER));
    }

    public void onNumberClicked(int number){
        switch (number) {
            case Constants.ONE: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_ONE);
                getClient().send(String.valueOf(KEYCODE_1));
                break;
            }
            case Constants.TWO: {
                Log.d(TAG, "[onNumberClicked] " + number + " " +Constants.COMMAND_TWO);
                getClient().send(String.valueOf(KEYCODE_2));
                break;
            }
            case Constants.THREE: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_THREE);
                getClient().send(String.valueOf(KEYCODE_3));
                break;
            }
            case Constants.FOUR: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_FOUR);
                getClient().send(String.valueOf(KEYCODE_4));
                break;
            }
            case Constants.FIVE: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_FIVE);
                getClient().send(String.valueOf(KEYCODE_5));
                break;
            }
            case Constants.SIX: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_SIX);
                getClient().send(String.valueOf(KEYCODE_6));
                break;
            }
            case Constants.SEVEN: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_SEVEN);
                getClient().send(String.valueOf(KEYCODE_7));
                break;
            }
            case Constants.EIGHT: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_EIGHT);
                getClient().send(String.valueOf(KEYCODE_8));
                break;
            }
            case Constants.NINE: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_NINE);
                getClient().send(String.valueOf(KEYCODE_9));
                break;
            }
            case Constants.ZERO: {
                Log.d(TAG, "[onNumberClicked] " + number + " " + Constants.COMMAND_ZERO);
                getClient().send(String.valueOf(KEYCODE_0));
                break;
            }
        }
    }

    public void onGuideClicked() {
        Log.d(TAG, "[onGuideClicked] " + Constants.COMMAND_GUIDE);
        getClient().send(String.valueOf(KEYCODE_GUIDE));
    }

    public void onMovieButtonClicked(){
        Log.d(TAG, "[onMovieButtonClicked] " + Constants.COMMAND_MOVIE);
        getClient().send(String.valueOf(Constants.OPEN_VOD_SCENE));
    }

    public void onLiveTvClicked(){
        Log.d(TAG, "[onLiveTvClicked] " + Constants.COMMAND_LIVETV);
        getClient().send(String.valueOf(KEYCODE_TV));
    }


    public void onHomeClicked() {
        Log.d(TAG, "[onHomeClicked] " + Constants.COMMAND_HOME);
        getClient().send(String.valueOf(KEYCODE_HOME));
    }

    public void onVolumeDownClicked(){
        Log.d(TAG, "[onVolumeDownClicked] " + Constants.COMMAND_VOLUME_DOWN);
        getClient().send(String.valueOf(KEYCODE_VOLUME_DOWN));
    }


    public void onVolumeMuteClicked(){
        Log.d(TAG, "[onVolumeMuteClicked] " + Constants.COMMAND_VOLUME_MUTE);
        getClient().send(String.valueOf(KEYCODE_VOLUME_MUTE));
    }


    public void onVolumeUpClicked(){
        Log.d(TAG, "[onVolumeUpClicked] " + Constants.COMMAND_VOLUME_UP);
        getClient().send(String.valueOf(KEYCODE_VOLUME_UP));
    }


    public void onChannelDownClicked(){
        Log.d(TAG, "[onChannelDownClicked] " + Constants.COMMAND_CHANNEL_DOWN);
        getClient().send(String.valueOf(KEYCODE_CHANNEL_DOWN));
    }



    public void onChannelUpClicked() {
        Log.d(TAG, "[onChannelUpClicked] " + Constants.COMMAND_CHANNEL_UP);
        getClient().send(String.valueOf(KEYCODE_CHANNEL_UP));
    }


    public void onDpadUpClicked() {
        Log.d(TAG, "[onDpadUpClicked] " + Constants.COMMAND_UP);
        getClient().send(String.valueOf(KEYCODE_DPAD_UP));
    }


    public void onDpadLeftClicked() {
        Log.d(TAG, "[onDpadLeftClicked] " + Constants.COMMAND_LEFT);
        getClient().send(String.valueOf(KEYCODE_DPAD_LEFT));
    }


    public void onDpadDownClicked() {
        Log.d(TAG, "[onDpadDownClicked] " + Constants.COMMAND_DOWN);
        getClient().send(String.valueOf(KEYCODE_DPAD_DOWN));
    }

        public void onDpadRightClicked() {
        Log.d(TAG, "[onDpadRightClicked] " + Constants.COMMAND_RIGHT);
        getClient().send(String.valueOf(KEYCODE_DPAD_RIGHT));
    }

    public void onDpadOkClicked() {
        Log.d(TAG, "[onDpadOkClicked] " + Constants.COMMAND_OK);
        getClient().send(String.valueOf(KEYCODE_DPAD_CENTER));
    }

    public void onBackClicked() {
        Log.d(TAG, "[onBackClicked] " + Constants.COMMAND_BACK);
        getClient().send(String.valueOf(KEYCODE_BACK));
    }

    public void onDelete() {
        Log.d(TAG, "[onDelete] " + KEYCODE_DEL);
        getClient().send(String.valueOf(KEYCODE_DEL));
    }


    public void onKeyboardSubmitClicked(String s) {
        Log.d(TAG, "[onKeyboardSubmitClicked] ");
        getClient().send(s);
    }

    public void onServerCommandReceived(String msg) {
    }



}
