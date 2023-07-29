package com.example.remotecontrolapp;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SingletonTest {

    private Singleton singleton;


    @Test
    public void testGetInstance() {
        singleton = Singleton.getInstance();
        assertSame(singleton, Singleton.getInstance());
    }

}
