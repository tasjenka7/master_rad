package com.example.remotecontrolapp.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.example.remotecontrolapp.Constants;
import com.example.remotecontrolapp.R;
import com.example.remotecontrolapp.Singleton;
import com.example.remotecontrolapp.stbs.ChooseConnection;

import java.io.InputStream;

import io.grpc.ManagedChannel;

public class RemoteView extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private Vibrator vibrator;
    private EditText etInput;
    private final String searchPattern = "SEARCH:";

    private static final String HOSTNAME = "speech.googleapis.com";
    private static final int PORT = 443;
    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    public AudioRecord mAudioRecord = null;
    private boolean mIsRecording = false;
    private StreamingRecognizeClient mStreamingClient;
    private int mBufferSize;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_control_scene);

        initialize();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etInput = findViewById(R.id.et_input_filed);

        etInput.setOnKeyListener((v, keyCode, event) -> {
            Log.d(TAG, "[onKeyListener] keyCode: " + keyCode + " KeyEvent: " + event.getAction());

            String input = String.valueOf(etInput.getText());
            if (!input.isEmpty()) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //Sending text from input field to box
                    Singleton.getInstance().getCommandsHandler().onKeyboardSubmitClicked(searchPattern + input);
                    etInput.getText().clear();
                    return true;
                }
            } else {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Singleton.getInstance().getCommandsHandler().onDelete();
                }
                return false;
            }
            return false;
        });

        vibrator = (Vibrator) getSystemService(RemoteView.VIBRATOR_SERVICE);

        mBufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;

        if (PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
            ImageButton mic = findViewById(R.id.micBtn);
            ImageButton micOrig = findViewById(R.id.micBtnOriginal);
            mic.setEnabled(false);
            micOrig.setEnabled(false);
            mic.setImageResource(R.drawable.ic_baseline_mic_off_32);
            micOrig.setImageResource(R.drawable.ic_baseline_mic_off_32);
        }

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                mBufferSize);

    }

    @Override
    protected void onDestroy() {

        if (mStreamingClient != null) {
            try {
                mStreamingClient.shutdown();
            } catch (InterruptedException e) {
                Log.e(TAG, "[onDestroy]: Error " + e);
            }
        }
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.powerBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onPowerClicked();
                break;
            case R.id.guideBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onGuideClicked();
                break;
            case R.id.movieBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onMovieButtonClicked();
                break;
            case R.id.liveTvBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onLiveTvClicked();
                break;
            case R.id.okBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onDpadOkClicked();
                break;
            case R.id.rightBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onDpadRightClicked();
                break;
            case R.id.leftBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onDpadLeftClicked();
                break;
            case R.id.upBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onDpadUpClicked();
                break;
            case R.id.downBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onDpadDownClicked();
                break;
            case R.id.backBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onBackClicked();
                break;
            case R.id.micBtn:
                onMicClicked();
                break;
            case R.id.micBtnOriginal:
                onMicOriginalClicked();
                break;
            case R.id.homeBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onHomeClicked();
                break;
            case R.id.volPlusBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onVolumeUpClicked();
                break;
            case R.id.volMinusBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onVolumeDownClicked();
                break;
            case R.id.muteBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onVolumeMuteClicked();
                break;
            case R.id.chMinusBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onChannelDownClicked();
                break;
            case R.id.chPlusBtn:
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onChannelUpClicked();
                break;
            case R.id.button1: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.ONE);
                break;
            }
            case R.id.button2: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.TWO);
                break;
            }
            case R.id.button3: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.THREE);
                break;
            }
            case R.id.button4: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.FOUR);
                break;
            }
            case R.id.button5: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.FIVE);
                break;
            }
            case R.id.button6: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.SIX);
                break;
            }
            case R.id.button7: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.SEVEN);
                break;
            }
            case R.id.button8: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.EIGHT);
                break;
            }
            case R.id.button9: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.NINE);
                break;
            }
            case R.id.button0: {
                vibrator.vibrate(200);
                Singleton.getInstance().getCommandsHandler().onNumberClicked(Constants.ZERO);
                break;
            }
            case R.id.disconnectBtn: {
                Singleton.getInstance().getCommandsHandler().disconnect();
                Intent intent = new Intent(RemoteView.this, ChooseConnection.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (etInput != null) {
            etInput.getText().clear();
        }
        super.onBackPressed();

    }

    // Initializing Google Cloud credentials
    private void initialize() {

        new Thread(() -> {

            try {
                InputStream credentials = getAssets().open("credentials.json");
                ManagedChannel channel = StreamingRecognizeClient.createChannel(
                        HOSTNAME, PORT, credentials);
                mStreamingClient = new StreamingRecognizeClient(channel, RECORDER_SAMPLERATE);
            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
        }).start();
    }


    public void onMicClicked() {

        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mAudioRecord.stop();
                mStreamingClient.finish();
                mIsRecording = false;
                findViewById(R.id.micBtn).setEnabled(true);
                setToastEnd();
            }
        };

        if(mIsRecording){
            timer.cancel();
        }

        if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            timer.start();
            startRecording();
            findViewById(R.id.micBtn).setEnabled(false);
            Toast.makeText(this, "Snimanje u toku.", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "[onMicClicked]: AudioRecord mot initialized.");
        }

    }

    private void startRecording() {

        mAudioRecord.startRecording();
        mIsRecording = true;

        Log.d(TAG, "[startRecording]: before Thread");

        Thread mRecordingThread = new Thread(() -> {
            Log.d(TAG, "[startRecording]: in Thread");
            readData();
        }, "AudioRecorder Thread");

        mRecordingThread.start();

    }

    private void readData() {

        byte[] sData = new byte[mBufferSize];
        String read = null;

        while (mIsRecording) {
            int bytesRead = mAudioRecord.read(sData, 0, mBufferSize);
            if (bytesRead > 0) {
                try {
                    mStreamingClient.recognizeBytes(sData, bytesRead);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "[readData]: Error while reading bytes: " + bytesRead);
            }
        }
    }

    private void setToastEnd(){
        Toast.makeText(this, "Snimanje gotovo.", Toast.LENGTH_SHORT).show();
    }

    public ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                String recordedData = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                if (recordedData != null) {
                    recordedData = recordedData.toLowerCase();
                    if (Constants.vpower.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onPowerClicked();
                        Log.d("STT", "Power said");
                    } else if (Constants.vguide.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onGuideClicked();
                        Log.d("STT", "Guide said");
                    } else if (Constants.vmovie.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onMovieButtonClicked();
                        Log.d("STT", "Movie said");
                    } else if (Constants.vlive.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onLiveTvClicked();
                        Log.d("STT", "live said");
                    } else if (Constants.vok.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onDpadOkClicked();
                        Log.d("STT", "Ok said");
                    } else if (Constants.vback.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onBackClicked();
                        Log.d("STT", "Back said");
                    } else if (Constants.vhome.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onHomeClicked();
                        Log.d("STT", "Home said");
                    } else if (Constants.vvolup.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onVolumeUpClicked();
                        Log.d("STT", "Vol up said");
                    } else if (Constants.vvoldown.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onVolumeDownClicked();
                        Log.d("STT", "Vol down said");
                    } else if (Constants.vmute.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onVolumeMuteClicked();
                        Log.d("STT", "Mute said");
                    } else if (Constants.vchup.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onChannelUpClicked();
                        Log.d("STT", "Channel up said");
                    } else if (Constants.vchdown.contains(recordedData)) {
                        Singleton.getInstance().getCommandsHandler().onChannelDownClicked();
                        Log.d("STT", "Channel down said");
                    } else {
                        Log.d("STT", "Not recognized or not supported command");
                    }
                } else {
                    Log.d("STT", "Nothing said");
                }

            }
        }
    });

    public void onMicOriginalClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say command. Numbers not supported.");

        if (intent.resolveActivity(Singleton.getContext().getPackageManager()) != null) {
            startForResult.launch(intent);
        } else {
            Toast.makeText(Singleton.getContext(), "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }
}
