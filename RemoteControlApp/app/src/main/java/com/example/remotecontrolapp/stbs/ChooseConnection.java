package com.example.remotecontrolapp.stbs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotecontrolapp.AsyncDataReceiver;
import com.example.remotecontrolapp.AsyncReceiver;
import com.example.remotecontrolapp.Constants;
import com.example.remotecontrolapp.R;
import com.example.remotecontrolapp.Singleton;
import com.example.remotecontrolapp.controller.RemoteView;
import com.example.remotecontrolapp.controller.UdpClient;

import java.net.InetAddress;
import java.util.ArrayList;

public class ChooseConnection extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private final int APP_CONNECTED_CODE = 420;
    private final int REMOVE_PAIRING_SCENE_CODE = 333;
    public static final int PAIR_COMMAND = 666;

    private RecyclerView stbs; // list of stbs
    private ProgressDialog progressDialog;
    private RelativeLayout enterCodeContainer; //enter code container
    private EditText enterCode; //enter text view
    private ArrayList<Stb> items;

    private boolean isFound = false;

    private boolean isEnterShown = false; //is enter code view shown
    private Vibrator vibrator;
    private String selectedHostAddress; // server selected from list
    private boolean shouldLoadDevices = false;
    private ChooseAdapter cAdapter;
    private boolean firstTimePermissionCheck = false;

    //Constructor
    public ChooseConnection() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stb);

        stbs = findViewById(R.id.rv_boxes_list);
        enterCodeContainer = findViewById(R.id.rl_pairing_container);
        enterCode = findViewById(R.id.et_pairing_code);

        //send code btn
        Button btnSendCode = findViewById(R.id.btn_send_code);
        btnSendCode.setOnClickListener(view -> {
            if (enterCode != null) {
                Log.d(TAG, "[onCreate] Entered code " + enterCode.getText().toString());
                String codeReceived = enterCode.getText().toString();

                if (!codeReceived.isEmpty() && codeReceived.equalsIgnoreCase(String.valueOf(Singleton.getInstance().getCommandsHandler().getPairingCode()))) {

                    Singleton.getInstance().getCommandsHandler().getClient().send(String.valueOf(APP_CONNECTED_CODE));
                    if (selectedHostAddress != null && !selectedHostAddress.equals("")) {
                        saveServer(selectedHostAddress);
                    }
                    openRemoteView();
                } else {
                    Toast.makeText(ChooseConnection.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressDialog = new ProgressDialog(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        stbs.setLayoutManager(linearLayoutManager);
        cAdapter = new ChooseAdapter(this);
        cAdapter.registerClickListener(position -> {
            vibrator.vibrate(100);
            progressDialog.setMessage("Connecting to... " + items.get(position).getServiceName());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            new ConnectToStb().execute(position);
        });

        vibrator = (Vibrator) getSystemService(RemoteView.VIBRATOR_SERVICE);
        stbs.setAdapter(cAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                showMessageOkCancel((dialogInterface, i) -> requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 123));
                firstTimePermissionCheck = true;

            }

        }
        if(firstTimePermissionCheck){
            new CountDownTimer(10000, 1000) {
                public void onTick(long millisUntilFinished) {}

                public void onFinish() {
                    loadDevice();
                    firstTimePermissionCheck = false;
                }
            }.start();
        }else{
            loadDevice();
        }

    }

    private void showMessageOkCancel(DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(this)
                .setMessage(Constants.permissionMsg)
                .setPositiveButton("OK", okListener)
                .create()
                .show();

    }

    public void saveServer(String selectedHostAddress) {

        SharedPreferences preferences = Singleton.getContext().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        Log.d(TAG, "[saveServer] getSharedPreferences: " + preferences);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.kSAVED_SERVER, selectedHostAddress);
        editor.apply();

    }

    public boolean isServerInSharedPrefs(String serverHostAddress) {

        SharedPreferences preferences = Singleton.getContext().getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        Log.d(TAG, "[isServerInSharedPrefs] getSharedPreferences: " + preferences);
        String savedHostAddress = preferences.getString(Constants.kSAVED_SERVER, null);
        return savedHostAddress != null && !savedHostAddress.isEmpty() && savedHostAddress.equalsIgnoreCase(serverHostAddress);

    }

    private void enterCode() {
        runOnUiThread(() -> {
            Log.d(TAG, "[enterCode] run on UI Thread");

            stbs.setVisibility(View.GONE);
            enterCodeContainer.setVisibility(View.VISIBLE);
            isEnterShown = true;
        });
    }


    public void loadDevice() {

        progressDialog.setMessage(Constants.lookingForDevices);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        items = new ArrayList<>();

        Log.d(TAG, "[loadDevice] before getting StbList");
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                try {
                    Singleton.getInstance().getDiscoveryHandler().getStbList(new AsyncDataReceiver() {
                        @Override
                        public void onReceive(Object var) {
                            items = (ArrayList<Stb>) var;
                            progressDialog.dismiss();
                            cancel(); //Countdown cancel
                            isFound = true;

                            Log.d(TAG, "[loadDevice] onReceive");
                            runOnUiThread(() -> {
                                stbs.setVisibility(View.VISIBLE);
                                cAdapter.refresh(items);
                            });
                        }

                        @Override
                        public void onFailed(Object var) {
                            Log.d(TAG, "[loadDevice] onFailed");
                            cancel();
                            onFinish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                if (!isFound) {
                    progressDialog.dismiss();
                    createDialog();
                }
            }
        }.start();

    }


    private void openRemoteView() {
        Intent intent = new Intent(ChooseConnection.this, RemoteView.class);
        startActivity(intent);
        enterCodeContainer.setVisibility(View.GONE);
        finish();
    }


    protected void onResume() {
        super.onResume();
        if (shouldLoadDevices) {
            loadDevice();
            shouldLoadDevices = false;
        }

    }


    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constants.noDevices);
        builder.setCancelable(true);

        builder.setPositiveButton(Constants.searchAgain,
                (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    loadDevice();

                });
        builder.setNegativeButton(Constants.closeApp,
                (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    finish();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @SuppressLint("StaticFieldLeak")
    private class ConnectToStb extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            int position = (int) objects[0];
            final UdpClient client = new UdpClient();
            final InetAddress inetAddress = items.get(position).getHost();
            client.connect(inetAddress, items.get(position).getPort(), new AsyncReceiver() {
                @Override
                public void onReceive() {
                    Singleton.getInstance().getCommandsHandler().setClient(client);
                    if (!isServerInSharedPrefs(inetAddress.getHostAddress())) {

                        Singleton.getInstance().getCommandsHandler().getClient()
                                .send(PAIR_COMMAND + "@" + Singleton.getInstance().getCommandsHandler().getRandomNumber());
                        selectedHostAddress = inetAddress.getHostName();
                        progressDialog.dismiss();
                        enterCode();
                    } else {
                        Singleton.getInstance().getCommandsHandler().getClient().send(String.valueOf(APP_CONNECTED_CODE));
                        progressDialog.dismiss();
                        openRemoteView();
                    }
                    client.startListening();
                }

                @Override
                public void onFailed(String error) {

                }
            });
            return null;
        }
    }

    public void onBackPressed() {
        if (isEnterShown) {
            Singleton.getInstance().getCommandsHandler().getClient().send(String.valueOf(REMOVE_PAIRING_SCENE_CODE));

            enterCodeContainer.setVisibility(View.GONE);
            stbs.setVisibility(View.VISIBLE);
            loadDevice();
            isEnterShown = false;
        } else {
            super.onBackPressed();
        }

    }

    public void onPause() {
        if (progressDialog != null && progressDialog.isShowing()) {
            shouldLoadDevices = true;
        }
        Singleton.getInstance().getDiscoveryHandler().stopDiscovery();
        super.onPause();
    }

    public void onDestroy() {
        Singleton.getInstance().getDiscoveryHandler().stopDiscovery();
        if (isEnterShown) {
            Singleton.getInstance().getCommandsHandler().getClient().send(String.valueOf(REMOVE_PAIRING_SCENE_CODE));
        }
        super.onDestroy();
    }

}
