package com.example.remotecontrolapp.controller;

import android.util.Log;

import com.example.remotecontrolapp.Constants;
import com.example.remotecontrolapp.Singleton;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechGrpc;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.auth.ClientAuthInterceptor;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.okhttp.OkHttpChannelProvider;
import io.grpc.stub.StreamObserver;

public class StreamingRecognizeClient implements StreamObserver<StreamingRecognizeResponse> {

    private final String TAG = this.getClass().getSimpleName();

    private final int mSamplingRate;
    private final ManagedChannel mChannel;
    private final SpeechGrpc.SpeechStub mSpeechClient;
    private boolean mIsInitialized = false;
    StreamObserver<StreamingRecognizeRequest> requestObserver;
    private static final List<String> OAUTH2_SCOPES =
            List.of("https://www.googleapis.com/auth/cloud-platform");



    public StreamingRecognizeClient(ManagedChannel channel, int samplingRate) {

        this.mSamplingRate = samplingRate;
        this.mChannel = channel;

        mSpeechClient = SpeechGrpc.newStub(channel);
    }


    private void initializeRecognition() {

        ArrayList<String> languageList = new ArrayList<>();
        languageList.add("sr-RS");
        languageList.add("hr-HR");

        requestObserver = mSpeechClient.streamingRecognize(this);

        RecognitionConfig config =
                RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setLanguageCode("en-US")
                        .addAllAlternativeLanguageCodes(languageList)
                        .setSampleRateHertz(mSamplingRate)
                        .setModel("command_and_search")
                        .build();

        StreamingRecognitionConfig streamingConfig =
                StreamingRecognitionConfig.newBuilder()
                        .setConfig(config)
                        .setInterimResults(true)
                        .setSingleUtterance(true)
                        .build();

        StreamingRecognizeRequest initial =
                StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build();
        requestObserver.onNext(initial);
    }

    @Override
    public void onNext(StreamingRecognizeResponse response) {

        Log.i(TAG, "[onNext]: Received response: " +
                response.toString());

        String text = null;
        final StreamingRecognitionResult result = response.getResults(0);

        if(result.getAlternativesCount() > 0){
            final SpeechRecognitionAlternative alternative = result.getAlternatives(0);
            text = alternative.getTranscript();
            Log.d(TAG, "[onNext]: transcript returned: " + text);
        }

        if( text != null){
            Log.i(TAG,"[onNext]: call checkSaid()");
            checkSaid(text);
        }
    }

    @Override
    public void onError(Throwable error) {

        Status status = Status.fromThrowable(error);
        Log.w(TAG, "[onError]: recognize failed: {0}: " + status);
        Log.e(TAG, "Error to Recognize.", error);
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "[onCompleted] recognize completed.");
    }

    public void recognizeBytes(byte[] audioBytes, int size) {

        if (!mIsInitialized) {
            Log.d(TAG, "[recognizeBytes]: call initializeRecognition");
            initializeRecognition();
            mIsInitialized = true;
        }

        try {
            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setAudioContent(ByteString.copyFrom(audioBytes, 0, size))
                            .build();
            requestObserver.onNext(request);
        } catch (RuntimeException e) {
            Log.e(TAG, "[recognizeBytes]: Error stopping.", e);
            requestObserver.onError(e);
            throw e;
        }

    }

    public void finish() {
        Log.i(TAG, "[finish]: call onCompleted");
        requestObserver.onCompleted();
        mIsInitialized = false;
    }

    @SuppressWarnings("deprecation")
    public static ManagedChannel createChannel(String host, int port, InputStream credentials)
            throws IOException {

        GoogleCredentials creds = GoogleCredentials.fromStream(credentials);
        creds = creds.createScoped(OAUTH2_SCOPES);
        OkHttpChannelProvider provider = new OkHttpChannelProvider();
        OkHttpChannelBuilder builder = provider.builderForAddress(host, port);
        ManagedChannel channel =  builder.intercept(new ClientAuthInterceptor(creds, Executors
                        .newSingleThreadExecutor
                                ()))
                .build();
        credentials.close();
        return channel;
    }

    public void shutdown() throws InterruptedException {
        mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    public void checkSaid(String recordedData){

        String checkedTag = "SRC [checkSaid]";

        if(recordedData!= null){
            if(Constants.vpower.contains(recordedData)){
                Singleton.getInstance().getCommandsHandler().onPowerClicked();
                Log.d(checkedTag, "Power said");
            } else if(Constants.vguide.contains(recordedData)){
                Singleton.getInstance().getCommandsHandler().onGuideClicked();
                Log.d(checkedTag, "Guide said");
            }else if(Constants.vmovie.contains(recordedData)){
                Singleton.getInstance().getCommandsHandler().onMovieButtonClicked();
                Log.d(checkedTag, "Movie said");
            }else if(Constants.vlive.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onLiveTvClicked();
                Log.d(checkedTag, "live said");
            }else if(Constants.vok.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onDpadOkClicked();
                Log.d(checkedTag, "Ok said");
            }else if(Constants.vback.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onBackClicked();
                Log.d(checkedTag, "Back said");
            }else if(Constants.vhome.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onHomeClicked();
                Log.d(checkedTag, "Home said");
            }else if(Constants.vvolup.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onVolumeUpClicked();
                Log.d(checkedTag, "Vol up said");
            }else if(Constants.vvoldown.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onVolumeDownClicked();
                Log.d(checkedTag, "Vol down said");
            }else if(Constants.vmute.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onVolumeMuteClicked();
                Log.d(checkedTag, "Mute said");
            }else if(Constants.vchup.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onChannelUpClicked();
                Log.d(checkedTag, "Channel up said");
            }else if(Constants.vchdown.contains(recordedData)) {
                Singleton.getInstance().getCommandsHandler().onChannelDownClicked();
                Log.d(checkedTag, "Channel down said");
            }else{
                Log.d(checkedTag, "not recognizable command");

            }
        }else{
            Log.d(checkedTag, "Nothing said");

        }

    }

}
