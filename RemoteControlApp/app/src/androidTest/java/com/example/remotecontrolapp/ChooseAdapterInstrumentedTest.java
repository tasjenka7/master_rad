package com.example.remotecontrolapp;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.remotecontrolapp.stbs.ChooseAdapter;
import com.example.remotecontrolapp.stbs.Stb;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChooseAdapterInstrumentedTest {

    ChooseAdapter chooseAdapter;

    @Before
    public void setUp() {
        ArrayList<Stb> stbList = new ArrayList<>();
        try {
            InetAddress address1 = InetAddress.getByName("192.168.1.1");
            InetAddress address2 = InetAddress.getByName("192.168.1.2");
            stbList.add(new Stb(1, "First STB", "Type", address1, 8080));
            stbList.add(new Stb(2, "Second STB", "Type", address2, 8080));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        chooseAdapter = new ChooseAdapter(context);
        chooseAdapter.refresh(stbList);
    }

    @Test
    public void getItemCount() {
        assertEquals(2, chooseAdapter.getItemCount());
    }
}
