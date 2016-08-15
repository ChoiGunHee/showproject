package com.etri.recodataapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends Activity
        implements RECORangingListener, RECOServiceConnectListener {

    EditText ipEditText;
    EditText portEditText;
    EditText uuidEditText;
    EditText majorEditText;
    EditText minorEditText;

    Button connectButton;
    Button resetButton;
    Button meter2Button;
    Button meter3Button;
    Button resultViewButton;

    TextView countTextView;

    private static String uuid = "50ddf411-8cf1-440c-87cd-e368daf9c93e";
    private RECOBeaconManager mRecoManager;
    private ArrayList<RECOBeacon> beacons;
    protected ArrayList<RECOBeaconRegion> mRegions;

    private Socket client;
    String ip = "220.149.230.119";
    int port = 34696;
    public Thread thread;
    public ClientThread clientThread;
    Handler handler;

    private String msg = "";
    String meter="1";
    int count;
    String majorString = "";
    String minorString = "";

    boolean isSen = true;
    String resultData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEditText = (EditText) findViewById(R.id.ipEditText);
        portEditText = (EditText) findViewById(R.id.portEditText);
        uuidEditText = (EditText) findViewById(R.id.uuidEditText);
        majorEditText = (EditText) findViewById(R.id.majorEditText);
        minorEditText = (EditText) findViewById(R.id.minorEditText);

        connectButton = (Button) findViewById(R.id.connectButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        meter2Button = (Button) findViewById(R.id.meter2Button);
        meter3Button = (Button) findViewById(R.id.meter3Button);
        resultViewButton = (Button) findViewById(R.id.resultViewButton);

        countTextView = (TextView) findViewById(R.id.countTextView);

        majorEditText.setText("18498");
        minorEditText.setText("16706");

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg != null) {
                    super.handleMessage(msg);
                    resultData = (String)msg.getData().get("msg");

                    if(resultData.equals("success")) {
                        Log.d("success", resultData);
//                        countTextView.setText(resultData);
                    } else {
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("result", resultData);
                        startActivity(intent);
//                    Toast.makeText(getApplicationContext(), resultData, Toast.LENGTH_SHORT).show();
                        Log.d("RECEIVE", resultData);
                    }
                }
                else {
//                    Toast.makeText(getApplicationContext(), "wow", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        boolean mScanRecoOnly = true;
        boolean mEnableBackgroundTimeout = true;

        mRegions = generateBeaconRegion();
        mRecoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
    }

    public void showResult(View view) {
        if(clientThread != null) {
            clientThread.send("result ! android");
        }
    }

    public void collectStart(View view) {
        count = 0;
        if (meter2Button.isEnabled()) {
//            countTextView.setText("2m start");
            meter = "2";
            majorString = majorEditText.getText().toString();
            minorString = minorEditText.getText().toString();
        }
        if (meter3Button.isEnabled() && !meter2Button.isEnabled()) {
//            countTextView.setText("3m start");
            meter = "3";
            onServiceConnect();
            majorString = majorEditText.getText().toString();
            minorString = minorEditText.getText().toString();
        }
    }

    public void send(View view) {
        ip = ipEditText.getText().toString();
        port = Integer.parseInt(portEditText.getText().toString());
        socketConnect();

//        countTextView.setText("Connect");
        connectButton.setEnabled(false);
        resetButton.setEnabled(true);
        meter2Button.setEnabled(true);
        meter3Button.setEnabled(true);
        resultViewButton.setEnabled(true);
    }

    public void close(View view) {
//        this.stop(mRegions);
        socketClose();

        meter = "1";
        count = 0;

        resetButton.setEnabled(false);
        connectButton.setEnabled(true);
        meter2Button.setEnabled(false);
        meter3Button.setEnabled(false);
//        countTextView.setText("RESET");
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();

        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(uuid, "RECO Sample Region");

        regions.add(recoRegion);

        return regions;
    }

    //여기서 비콘 정보 가져온다.
    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoBeaconRegion) {
        beacons = new ArrayList<>(recoBeacons);
        if(meter.equals("2") || meter.equals("3")) {
            if (!beacons.isEmpty()) {
                for (int i = 0; i < recoBeacons.size(); i++) {
                    if (majorString.equals(beacons.get(i).getMajor() + "") && minorString.equals(beacons.get(i).getMinor() + "")) {
                        if (Math.abs(beacons.get(i).getRssi()) > 0) {
//                msg += Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + " / ";
                            msg += "android / ";
//                            msg += Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + " / ";
                            msg += beacons.get(i).getProximityUuid() + " / ";
                            msg += beacons.get(i).getMajor() + " / ";
                            msg += beacons.get(i).getMinor() + " / ";
                            msg += beacons.get(i).getRssi() + " / ";
                            msg += meter + " ! ";
                            count++;
                        }
                        if (meter.equals("2")) {
                            countTextView.setText("데이터 수집중입니다. 잠시만 기다려주세요");
                            Log.d("Log Select", "count : " + count + " distance : " + meter + " major : " + beacons.get(i).getMajor() + " minor : " + beacons.get(i).getMinor() + " rssi : " + beacons.get(i).getRssi());

                            if (clientThread != null) {
                                if (count == 20) {
                                    //   this.stop(mRegions);
                                    meter2Button.setEnabled(false);
                                    meter3Button.setEnabled(true);
                                    clientThread.send(msg);
                                    count = 0;
                                    countTextView.setText("2m 완료");
                                    msg = "";
                                    meter = "1";
                                }
                            }
                        } else if (meter.equals("3")) {
                            countTextView.setText("데이터 수집중입니다. 잠시만 기다려주세요");
                            Log.d("Log Select", "count : " + count + " distance : " + meter + " major : " + beacons.get(i).getMajor() + " minor : " + beacons.get(i).getMinor() + " rssi : " + beacons.get(i).getRssi());

                            if (clientThread != null) {
                                if (count == 20) {
//                                    this.stop(mRegions);
                                    meter3Button.setEnabled(false);
                                    clientThread.send(msg);
                                    count = 0;
                                    countTextView.setText("3m 완료");
                                    msg = "";
                                    meter = "1";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onServiceConnect() {
//        Toast.makeText(this, "onServiceConnect()", Toast.LENGTH_SHORT).show();
        boolean DISCONTINUOUS_SCAN = false;
        mRecoManager.setDiscontinuousScan(DISCONTINUOUS_SCAN);
        for(RECOBeaconRegion region : mRegions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        return;
    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        return;
    }

    private void unbind() {
        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.i("RECOMonitoringActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECOMonitoringActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    public void socketConnect() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);
                    clientThread.start();

//                        Toast.makeText(MainActivity.this, "socketConnect", Toast.LENGTH_SHORT).show();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void socketClose() {
        try {
            client.close();
            clientThread = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
        onServiceConnect();
    }

    @Override
    protected void onPause() {
//        this.stop(mRegions);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.stop(mRegions);
        this.unbind();
        if(clientThread!=null)
            socketClose();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
