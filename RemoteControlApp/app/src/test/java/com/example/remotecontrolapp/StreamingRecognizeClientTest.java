package com.example.remotecontrolapp;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.remotecontrolapp.controller.CommandsHandler;
import com.example.remotecontrolapp.controller.StreamingRecognizeClient;
import com.google.cloud.speech.v1.SpeechGrpc;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class StreamingRecognizeClientTest {

    private ManagedChannel mockChannel;
    private SpeechGrpc.SpeechStub mockStub;
    private StreamObserver<StreamingRecognizeRequest> mockRequestObserver;
    private StreamingRecognizeClient client;
    private CommandsHandler commandsHandler; // Add this line

    @Before
    public void setUp() {
        mockChannel = mock(ManagedChannel.class);
        mockStub = mock(SpeechGrpc.SpeechStub.class);
        mockRequestObserver = mock(StreamObserver.class);

        // Add assertNotNull check to confirm that mockRequestObserver is not null
        assertNotNull(mockRequestObserver);

        // Initialize commandsHandler as a mock
        commandsHandler = mock(CommandsHandler.class);

        // Set the mock CommandsHandler in Singleton
        Singleton.getInstance().setCommandsHandler(commandsHandler);

        // Mock the stub to return our mock observer when streamingRecognize is called
        when(mockStub.streamingRecognize(any())).thenReturn(mockRequestObserver);

        // Create the client under test, passing the mock objects
        client = new StreamingRecognizeClient(mockChannel, 16000);
    }

    @After
    public void tearDown() {
        Singleton.getInstance().setCommandsHandler(new CommandsHandler());
    }

    @Test
    public void checkSaid_whenSaidPower_shouldCallOnPowerClicked() {
        for (String command : Constants.vpower) {
            client.checkSaid(command);
            verify(commandsHandler).onPowerClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }


    @Test
    public void checkSaid_whenSaidGuide_shouldCallOnGuideClicked() {
        for (String command : Constants.vguide) {
            client.checkSaid(command);
            verify(commandsHandler).onGuideClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }


    @Test
    public void checkSaid_whenSaidMovie_shouldCallOnMovieButtonClicked() {
        for (String command : Constants.vmovie) {
            client.checkSaid(command);
            verify(commandsHandler).onMovieButtonClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidLive_shouldCallOnLiveTvClicked() {
        for (String command : Constants.vlive) {
            client.checkSaid(command);
            verify(commandsHandler).onLiveTvClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidOk_shouldCallOnDpadOkClicked() {
        for (String command : Constants.vok) {
            client.checkSaid(command);
            verify(commandsHandler).onDpadOkClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidBack_shouldCallOnBackClicked() {
        for (String command : Constants.vback) {
            client.checkSaid(command);
            verify(commandsHandler).onBackClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidHome_shouldCallOnHomeClicked() {
        for (String command : Constants.vhome) {
            client.checkSaid(command);
            verify(commandsHandler).onHomeClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidVolumeUp_shouldCallOnVolumeUpClicked() {
        for (String command : Constants.vvolup) {
            client.checkSaid(command);
            verify(commandsHandler).onVolumeUpClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidVolumeDown_shouldCallOnVolumeDownClicked() {
        for (String command : Constants.vvoldown) {
            client.checkSaid(command);
            verify(commandsHandler).onVolumeDownClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidMute_shouldCallOnVolumeMuteClicked() {
        for (String command : Constants.vmute) {
            client.checkSaid(command);
            verify(commandsHandler).onVolumeMuteClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidChannelUp_shouldCallOnChannelUpClicked() {
        for (String command : Constants.vchup) {
            client.checkSaid(command);
            verify(commandsHandler).onChannelUpClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

    @Test
    public void checkSaid_whenSaidChannelDown_shouldCallOnChannelDownClicked() {
        for (String command : Constants.vchdown) {
            client.checkSaid(command);
            verify(commandsHandler).onChannelDownClicked();
            Mockito.reset(commandsHandler); // reset the mock
        }
    }

}
