package com.example.remotecontrolapp;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.remotecontrolapp.controller.UdpClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpClientTest {

    private UdpClient udpClient;
    private DatagramSocket mockSocket;
    private AsyncReceiver mockReceiver;

    @Before
    public void setup() throws SocketException {
        udpClient = new UdpClient();
        mockSocket = mock(DatagramSocket.class);
        mockReceiver = mock(AsyncReceiver.class);
        udpClient.mSocket = mockSocket;

    }

    @Test
    public void disconnect_shouldCloseSocket() {
        udpClient.disconnect();

        verify(mockSocket).close();
    }

    @Test
    public void connect_shouldConnectSocketAndCallAsyncReceiverOnReceive() throws IOException, InterruptedException {
        String address = "127.0.0.1";
        int port = 1234;
        InetAddress mockAddress = InetAddress.getByName(address);

        when(mockSocket.isConnected()).thenReturn(false);
        doNothing().when(mockSocket).connect(mockAddress, port);
        when(mockSocket.isConnected()).thenReturn(true);

        udpClient.connect(mockAddress, port, mockReceiver);
        Thread.sleep(5000);
        verify(mockReceiver).onReceive();
    }

    @Test
    public void send_shouldSendDataToSocket() throws IOException {
        String message = "Test message";
        byte[] messageBytes = message.getBytes();
        String address = "127.0.0.1";
        int port = 1234;
        InetAddress mockAddress = InetAddress.getByName(address);

        doNothing().when(mockSocket).send(any(DatagramPacket.class));

        udpClient.mSocket = mockSocket; // Assign the mockSocket to udpClient.mSocket
        when(mockSocket.isConnected()).thenReturn(true);
        udpClient.send(message);

        ArgumentCaptor<DatagramPacket> packetCaptor = ArgumentCaptor.forClass(DatagramPacket.class);
        verify(mockSocket).send(packetCaptor.capture());

        DatagramPacket sentPacket = packetCaptor.getValue();
        String sentMessage = new String(sentPacket.getData(), sentPacket.getOffset(), sentPacket.getLength());

        assertEquals(message, sentMessage);
    }

}

