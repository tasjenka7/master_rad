//package com.example.remotecontrolapp;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.openMocks;
//import static org.powermock.api.mockito.PowerMockito.doAnswer;
//
//import android.content.Context;
//import android.net.nsd.NsdManager;
//import android.net.nsd.NsdServiceInfo;
//import android.widget.TextView;
//
//import com.example.remotecontrolapp.stbs.ChooseAdapter;
//import com.example.remotecontrolapp.stbs.DiscoveryHandler;
//import com.example.remotecontrolapp.stbs.NsdDiscover;
//import com.example.remotecontrolapp.stbs.Stb;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.net.InetAddress;
//import java.util.ArrayList;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DiscoveryHandlerTest {
//
//    @Spy
//    private DiscoveryHandler discoveryHandler;
//
//    @Mock
//    private AsyncDataReceiver<ArrayList<Stb>> receiver;
//
//    @Mock
//    private NsdServiceInfo serviceInfo;
//
//    @Mock
//    private NsdDiscover nsdDiscover;
//
//    @Mock
//    private InetAddress inetAddress;
//
//    @Before
//    public void setUp() {
//        openMocks(this);
//
//        // Mock Context
//        Context context = Mockito.mock(Context.class);
//
//        // Mock NsdManager
//        NsdManager nsdManager = Mockito.mock(NsdManager.class);
//
//        // Define the behavior for the getSystemService call on the context
//        when(context.getSystemService(Context.NSD_SERVICE)).thenReturn(nsdManager);
//
//        when(serviceInfo.getHost()).thenReturn(inetAddress);
//        when(serviceInfo.getServiceName()).thenReturn("TestServiceName");
//        when(serviceInfo.getServiceType()).thenReturn("TestServiceType");
//        when(serviceInfo.getPort()).thenReturn(1234);
//
//        doAnswer(invocation -> {
//            NsdServiceInfo service = invocation.getArgument(0);
//            ArrayList<Stb> stbList = new ArrayList<>();
//            stbList.add(new Stb(0, service.getServiceName(), service.getServiceType(), service.getHost(), service.getPort()));
//            receiver.onReceive(stbList);
//            return null;
//        }).when(nsdDiscover).onDiscover(any(NsdServiceInfo.class));
//    }
//
//
//    @Test
//    public void refresh_UpdatesData() {
//        // Arrange
//        Context context = Mockito.mock(Context.class);
//        ChooseAdapter adapter = new ChooseAdapter(context);
//        ArrayList<Stb> testStbs = new ArrayList<>();
//        testStbs.add(new Stb(0, "Test Service", "Test Type", null, 0));
//
//        // Act
//        adapter.refresh(testStbs);
//
//        // Assert
//        assertEquals(testStbs.size(), adapter.getItemCount());
//        assertEquals(testStbs.get(0).getServiceName(), ((TextView) adapter.onCreateViewHolder(null, 0).getNameView()).getText().toString());
//    }
//
//    @Test
//    public void getStbList_shouldReceiveStbList() {
//        discoveryHandler.getStbList(receiver);
//
//        // After starting discovery, simulate a service discovery
//        nsdDiscover.onDiscover(serviceInfo);
//
//        // Verify if the onReceive method of the AsyncDataReceiver is called
//        verify(receiver, times(1)).onReceive(any(ArrayList.class));
//    }
//
//    @Test
//    public void stopDiscovery_shouldStopDiscovery() {
//        discoveryHandler.stopDiscovery();
//        verify(nsdDiscover, times(1)).stopDiscovery();
//    }
//
//    @Test
//    public void startDiscovery_shouldStartDiscovery() {
//        discoveryHandler.startDiscovery();
//        verify(nsdDiscover, times(1)).startDiscovery();
//    }
//}
