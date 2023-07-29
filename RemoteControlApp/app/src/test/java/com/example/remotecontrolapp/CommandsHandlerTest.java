package com.example.remotecontrolapp;

import static android.view.KeyEvent.*;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import android.os.Build;

import com.example.remotecontrolapp.controller.CommandsHandler;
import com.example.remotecontrolapp.controller.UdpClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

@Config(sdk = Build.VERSION_CODES.P) // Use API level 28
@RunWith(RobolectricTestRunner.class)
public class CommandsHandlerTest {

    @Mock
    private UdpClient mockClient;

    private CommandsHandler handler;

    @Before
    public void setUp() {
        mockClient = mock(UdpClient.class);
        handler = new CommandsHandler();
        handler.setClient(mockClient);
    }

    @Test
    public void testOnPowerClicked() {
        handler.onPowerClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_POWER));
    }

    @Test
    public void testOnNumberClicked() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        int[] keyCodes = {KEYCODE_0, KEYCODE_1, KEYCODE_2, KEYCODE_3, KEYCODE_4, KEYCODE_5, KEYCODE_6, KEYCODE_7, KEYCODE_8, KEYCODE_9};
        for (int i = 0; i <= 9; i++) {
            handler.onNumberClicked(i);
        }
        verify(mockClient, times(10)).send(captor.capture());
        List<String> allValues = captor.getAllValues();
        for (int i = 0; i <= 9; i++) {
            assertEquals(String.valueOf(keyCodes[i]), allValues.get(i));
        }
    }

    @Test
    public void testOnGuideClicked() {
        handler.onGuideClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_GUIDE));
    }

    @Test
    public void testOnMovieButtonClicked() {
        handler.onMovieButtonClicked();
        verify(mockClient).send(String.valueOf(Constants.OPEN_VOD_SCENE));
    }

    @Test
    public void testOnLiveTvClicked() {
        handler.onLiveTvClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_TV));
    }

    @Test
    public void testOnHomeClicked() {
        handler.onHomeClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_HOME));
    }

    @Test
    public void testOnVolumeDownClicked() {
        handler.onVolumeDownClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_VOLUME_DOWN));
    }

    @Test
    public void testOnVolumeMuteClicked() {
        handler.onVolumeMuteClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_VOLUME_MUTE));
    }

    @Test
    public void testOnVolumeUpClicked() {
        handler.onVolumeUpClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_VOLUME_UP));
    }

    @Test
    public void testOnChannelDownClicked() {
        handler.onChannelDownClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_CHANNEL_DOWN));
    }

    @Test
    public void testOnChannelUpClicked() {
        handler.onChannelUpClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_CHANNEL_UP));
    }

    @Test
    public void testOnDpadUpClicked() {
        handler.onDpadUpClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_DPAD_UP));
    }

    @Test
    public void testOnDpadLeftClicked() {
        handler.onDpadLeftClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_DPAD_LEFT));
    }

    @Test
    public void testOnDpadDownClicked() {
        handler.onDpadDownClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_DPAD_DOWN));
    }

    @Test
    public void testOnDpadRightClicked() {
        handler.onDpadRightClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_DPAD_RIGHT));
    }

    @Test
    public void testOnDpadOkClicked() {
        handler.onDpadOkClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_DPAD_CENTER));
    }

    @Test
    public void testOnBackClicked() {
        handler.onBackClicked();
        verify(mockClient).send(String.valueOf(KEYCODE_BACK));
    }

    @Test
    public void testOnDelete() {
        handler.onDelete();
        verify(mockClient).send(String.valueOf(KEYCODE_DEL));
    }


}
