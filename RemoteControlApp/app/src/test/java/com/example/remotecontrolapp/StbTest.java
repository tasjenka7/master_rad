package com.example.remotecontrolapp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.remotecontrolapp.stbs.Stb;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class StbTest {

    private Stb stb;

    @Before
    public void setUp() {
        int id = 1;
        String serviceName = "STB 1";
        String serviceType = "myvtremote";
        InetAddress host = mock(InetAddress.class);
        int port = 1234;

        stb = new Stb(id, serviceName, serviceType, host, port);
    }

    @Test
    public void testGetId() {
        assertEquals(1, stb.getId());
    }

    @Test
    public void testGetServiceName() {
        assertEquals("STB 1", stb.getServiceName());
    }

    @Test
    public void testGetHost() {
        InetAddress expectedHost = mock(InetAddress.class);
        when(expectedHost.getHostAddress()).thenReturn("127.0.0.1"); // Example host address

        Stb stb = new Stb(1, "STB 1", "myvtremote", expectedHost, 1234);

        assertEquals(expectedHost.getHostAddress(), stb.getHost().getHostAddress());
    }


    @Test
    public void testGetPort() {
        assertEquals(1234, stb.getPort());
    }
}
