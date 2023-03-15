package com.example.remotecontrolapp.controller;

import android.util.Log;

import com.example.remotecontrolapp.AsyncReceiver;
import com.example.remotecontrolapp.Singleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClient {

    String TAG = this.getClass().getSimpleName();

    private Thread mThread;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;
    private static final int kTHREAD_SLEEP_TIME = 2000;

    private DatagramSocket mSocket;


    public void disconnect(){

        if(mSocket != null){
            mSocket.close();
        }

    }

    public void connect(final InetAddress address, final int port, final AsyncReceiver asyncDataReceiver){
        disconnect();

        new Thread(() -> {

            try {
                mSocket = new DatagramSocket(0);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            mSocket.connect(address, port);

            if (mSocket.isConnected()) {
                Log.d(TAG, "[connect] Socket IS CONNECTED " + mSocket.isConnected() + " for (address, port) " + address + port);
                asyncDataReceiver.onReceive();
            } else {
                asyncDataReceiver.onFailed("Socket not connected");
            }
        }).start();

    }

    public void send(final String message) {

        Log.d(TAG, "[send] message " + message);

        new Thread(() -> {
            byte[] buf = message.getBytes();
            DatagramPacket p = new DatagramPacket(buf, buf.length);

            try {
                if (mSocket.isConnected()) {
                    mSocket.send(p);
                    Log.d(TAG, "[send] sending data to stb - mSocket is connected");
                } else {
                    Log.d(TAG, "[send] sending data to stb - mSOCKET not connected");
                }

            } catch (IOException e) {
                Log.e(TAG, "[send] " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void startListening() {

        Log.d(TAG, "[startListening] entered");
        stopListening();

        mThread = new Thread(() -> {

            Log.d(TAG, "[startListening] Thread created");

            while (!Thread.currentThread().isInterrupted()) {

                byte[] buf = new byte[MAX_UDP_DATAGRAM_LEN];
                final DatagramPacket pack = new DatagramPacket(buf, buf.length);

                if (mSocket != null) {
                    if (mSocket.isConnected()) {
                        try {
                            mSocket.receive(pack);
                            String msg = new String(pack.getData(), pack.getOffset(), pack.getLength());
                            msg = msg.trim();
                            Log.d(TAG, "[startListening] Packet from server Received: " + msg);
                            Singleton.getInstance().getCommandsHandler().onServerCommandReceived(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                try {
                    Thread.sleep(kTHREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "[startListening] Remote app listener thread stopped");
        });
        mThread.start();
    }
    public void stopListening() {

        if (mThread != null) {
            mThread.interrupt();
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
