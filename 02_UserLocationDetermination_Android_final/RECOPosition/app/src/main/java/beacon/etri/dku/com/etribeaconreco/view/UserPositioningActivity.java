package beacon.etri.dku.com.etribeaconreco.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import beacon.etri.dku.com.etribeaconreco.R;
import beacon.etri.dku.com.etribeaconreco.socket.ClientThread;

public class UserPositioningActivity extends Activity
        implements RECORangingListener, RECOServiceConnectListener {

    private static String uuid = "50ddf411-8cf1-440c-87cd-e368daf9c93e";
    private RECOBeaconManager mRecoManager;
    private ArrayList<RECOBeacon> beacons;
    protected ArrayList<RECOBeaconRegion> mRegions;

    private Socket client;
    String ip = "220.149.230.119";
//    String ip="192.168.0.11";
    int port = 34690;
    public Thread thread;
    public ClientThread clientThread;
    Handler handler;

    private DrawingView drawingView;

    float realHeight = 500;
    float realWidth = 500;

    float viewHeight;
    float viewWidth;

    HashMap<String, Point> myArtifactHashMap;
    HashMap<String, Point> artifactHashMap;
    int range = 130;
    int windowHeight;
    int canvasHeight2;

    String ackData = null;

    List<Point> beaconPositionList = new ArrayList<Point>();
    Point userPosition = new Point(0,0);
    Point userPositionOld = new Point(0,0);
    Point userPositionNew = new Point(0,0);
    Point userPositionReal = new Point(0,0);
    int positionEqualCount = 0;

    boolean isFirstConnect;
    boolean isAckArtifact=false;

    int gap = 100;

    private EventHandler eventHandler = new EventHandler();
    int stepCount;
    int stepCountOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpositioning);

        DisplayMetrics displayMetrics = new DisplayMetrics(); // 스마트폰 화면 크기
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowHeight = displayMetrics.heightPixels;

        StepCounterService.eventHandler = eventHandler;
        startService(new Intent(this, StepCounterService.class));
        StepCounterService.steps=1;
        stepCount=1;
        System.out.println("Step(onCreate) : " + String.valueOf(StepCounterService.steps));

        SharedPreferences mSharedPreferences;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        gap = mSharedPreferences.getInt(GridSettingActivity.DISTANCEGAP, 100);

        isFirstConnect=true;
//        if(isFirstConnect) {
//            Toast.makeText(getApplicationContext(), "FirstConnect", Toast.LENGTH_SHORT).show();
//        }
        socketConnect();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg != null) {
                    super.handleMessage(msg);
                    ackData = (String)msg.getData().get("msg");

                    if(ackData.startsWith("ACK_beaconLoc")) {
                        dbSave();
                    } else if(ackData.startsWith("ACK_contentsLoc")) {
                        artifactClassify();
                    } else if(ackData.startsWith("ACK_userLoc")) {
//                        userPositionReal = calPositionReal();
                        userPositionNew = calPosition();
                    } else if(ackData.startsWith("ACK_contentInfo")) {
                        viewContentInfoActivity();
                    }

                    drawingView.postInvalidate();
                    Log.d("RECEIVE", "receive : " + ackData);
                } else {
                    return;
                }
            }
        };

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        drawingView = new DrawingView(UserPositioningActivity.this);
        linearLayout.addView(drawingView);

        boolean mScanRecoOnly = true;
        boolean mEnableBackgroundTimeout = true;

        mRegions = generateBeaconRegion();
        mRecoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            Toast.makeText(this, event.getX() + ", " + event.getY(), Toast.LENGTH_SHORT).show();

//            Point touchPoint = new Point((int) (event.getX() / (realWidth / viewWidth)), (int) (event.getY() / (realHeight / viewHeight)));

            Point touchPoint = new Point((int) (event.getX()), (int) (event.getY()));


            Set  myKey = myArtifactHashMap.keySet();
            for(Iterator iterator = myKey.iterator(); iterator.hasNext();) {
                String keyName = (String)iterator.next();
                Point value = myArtifactHashMap.get(keyName);
                isTouchArtifact(keyName, value, touchPoint);
            }

            myKey = artifactHashMap.keySet();
            for(Iterator iterator = myKey.iterator(); iterator.hasNext();) {
                String keyName = (String)iterator.next();
                Point value = artifactHashMap.get(keyName);
                isTouchArtifact(keyName, value, touchPoint);
            }
        }

        return true;
    }

    public void isTouchArtifact(String keyName, Point value, Point touchPoint) {

        float xf = (value.x / (realWidth / viewWidth)) - 40;
        float yf = (value.y / (realHeight / viewHeight)) - 40;

        Log.d("is in the range : ", touchPoint.toString()+" / "+xf+", "+yf);

        float rangeUp = yf - range;
        float rangeLeft = xf - range;
        float rangeDown = yf + range;
        float rangeRight = xf + range;

        int touchPointY = touchPoint.y-(windowHeight-canvasHeight2);

        Log.d("range +, -", rangeUp+", "+rangeLeft+", "+rangeDown+", "+rangeRight);
        if( rangeLeft < touchPoint.x && touchPoint.x < rangeRight
                && rangeUp < touchPointY && touchPointY < rangeDown) {
            Log.d("is in the range : ", "Yes");
            if(clientThread != null) {
                clientThread.send("REQ_contentInfo ? "+keyName);
            }
        }
    }

    private class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            stepCount = Integer.parseInt(String.valueOf(msg.what));
            System.out.println("Step(EventHandler) : " + String.valueOf(msg.what));
        }
    }

    private void viewContentInfoActivity() {
        String[] artifactInfo = ackData.split(" \\? ")[1].split(" / ");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(artifactInfo[1])
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialgo = dialogBuilder.create();
        dialgo.setTitle(artifactInfo[0]);
        dialgo.show();
        Log.d("artifactInfo : ", artifactInfo[0]+" "+artifactInfo[1]);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void dbRequest() {
        String msg="REQ_beaconLoc ? android";
        if(clientThread != null) {
            clientThread.send(msg);
        }
        msg="REQ_contentsLoc ? "+ Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if(clientThread != null) {
            clientThread.send(msg);
        }
    }

    private void dbSave() {
        String[] beaconXYList = ackData.split(" \\? ")[1].split(" ! ");

        for(int i=0; i<beaconXYList.length; i++) {
            String[] beaconXY = beaconXYList[i].split(",");
            Point point = new Point(Integer.parseInt(beaconXY[0]), Integer.parseInt(beaconXY[1]));
            beaconPositionList.add(point);
        }
    }

    private void artifactClassify() {
        String[] artifactsPosition = ackData.split(" \\? ")[1].split(" ! ");

        myArtifactHashMap = new HashMap<String, Point>();
        artifactHashMap = new HashMap<String, Point>();

        for(int i=0; i<artifactsPosition.length; i++) {
            String[] artifactPosition = artifactsPosition[i].split(" / ");
            Point artifactPoint = new Point(Integer.parseInt(artifactPosition[2].split(" , ")[0]), Integer.parseInt(artifactPosition[2].split(" , ")[1]));
            if(artifactPosition[1].equals("true")) {
                Log.d("isTrue : ", artifactPosition[0]+" "+artifactPosition[1]);
                myArtifactHashMap.put(artifactPosition[0], artifactPoint);
            } else {
                Log.d("isFalse : ", artifactPosition[0]+" "+artifactPosition[1]);
                artifactHashMap.put(artifactPosition[0], artifactPoint);
            }
        }

        isAckArtifact=true;
    }

//    private Point calPositionReal() {
//        String[] userPositionXY = (ackData.split(" \\? "))[1].split(",");
//        int userX = (int)Double.parseDouble(userPositionXY[0]);
//        int userY = (int)Double.parseDouble(userPositionXY[1]);
//
//        return new Point(userX, userY);
//    }

    private Point calPosition() {
        String[] userPositionXY = (ackData.split(" \\? "))[1].split(",");
        Double userX = Double.parseDouble(userPositionXY[0]);
        Double userY = Double.parseDouble(userPositionXY[1]);

        int userXX = ((userX.intValue()/gap)*2+1)*(gap/2);
        int userYY = ((userY.intValue()/gap)*2+1)*(gap/2);

        return new Point(userXX, userYY);
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

        if(!beacons.isEmpty()) {
            String msg = "";
            for (int i = 0; i < recoBeacons.size(); i++) {
//                msg += Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + " / ";
                msg += "android / ";
                msg += beacons.get(i).getProximityUuid() + " / ";
                msg += beacons.get(i).getMajor() + " / ";
                msg += beacons.get(i).getMinor() + " / ";
                msg += beacons.get(i).getRssi() + " / ";
                msg += "null / ";
                msg += "null / ";
                msg += "null ! ";
            }

//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            if (clientThread != null) {
//                        if(!msg.endsWith("DI ! ")) {
                clientThread.send(msg);
                Log.d("send message : ", msg);
                stepCountOld = stepCount;
                StepCounterService.steps = 0;
                stepCount = 0;
//                        }
            }

            drawingView.postInvalidate();
        }
    }

    @Override
    public void onServiceConnect() {
        dbRequest();

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

        StepCounterService.eventHandler = eventHandler;
        System.out.println("Step(onResume) : " + String.valueOf(StepCounterService.steps));

        if (isFirstConnect) {
                dbRequest();
                isFirstConnect = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stop(mRegions);
        stopService(new Intent(this, StepCounterService.class));
        this.unbind();
        if(clientThread!=null)
            socketClose();
    }

    public class DrawingView extends View {
        Bitmap bitmapMap;

        Bitmap bitmapUser;
        Bitmap bitmapUser2;

        Bitmap bitmapMyArtifact;
        Bitmap bitmapArtifact;

        public DrawingView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvasHeight2 = canvas.getHeight();

            drawMap(canvas);

            for(int i=0; i<beaconPositionList.size(); i++) {
                Point beaconPosition = beaconPositionList.get(i);
                drawBeacon(canvas, beaconPosition.x, beaconPosition.y);
            }

            if(isAckArtifact) {
                drawMyArtifact(canvas);
                drawArtifact(canvas);
            }

            String text="";
            if(stepCountOld==0 && positionEqualCount<3) {
                if(userPositionOld.equals(userPositionNew)) {
                    positionEqualCount++;
                } else {
                    positionEqualCount=0;
                    userPositionOld = userPositionNew;
                }
                text = "userPosition : "+userPosition+" , "+"userPosititionNew : "+userPositionNew+" , "+positionEqualCount;
            }
            if(stepCountOld>=1 || positionEqualCount==2) {
                userPositionOld = userPositionNew;
                userPosition = userPositionOld;
                if (stepCountOld >= 1) {
                    text = "step : " + stepCountOld;
                } else {
                    text = "userPosition : "+userPosition+" , "+positionEqualCount;
                }
                positionEqualCount=0;
            }

            drawUser(canvas);

            Log.d("isMoving? : ", text);
        }

        public void drawMap(Canvas canvas) {
            bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.demomapyellow);

            int canvasHeight = canvas.getHeight();
            int canvasWidth = canvas.getWidth();

            float bitmapWidth = bitmapMap.getWidth();
            float bitmapHeight = bitmapMap.getHeight();

            float forCalWidth = bitmapWidth * canvasWidth / bitmapWidth;
            float forCalHeight = bitmapHeight * canvasWidth / bitmapWidth;

            if(forCalHeight<=canvasHeight) {
                viewWidth = forCalWidth;
                viewHeight = forCalHeight;
            } else {
                viewWidth = bitmapWidth * canvasHeight / bitmapHeight;
                viewHeight = canvasHeight;
            }
            bitmapMap = Bitmap.createScaledBitmap(bitmapMap, (int) viewWidth, (int) viewHeight, true);

            canvas.drawBitmap(bitmapMap, 0, 0, null);
        }

        public void drawBeacon(Canvas canvas, int x, int y) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(30);

            canvas.drawCircle((x / (realWidth / viewWidth)), (y / (realHeight / viewHeight)), 10, paint);
            Log.d("beacon Location : ", x+", "+y);
        }

        public void drawMyArtifact(Canvas canvas) {
            bitmapMyArtifact = BitmapFactory.decodeResource(getResources(), R.drawable.myartifact);
            bitmapMyArtifact = Bitmap.createScaledBitmap(bitmapMyArtifact, 80, 80, true);

            if(!myArtifactHashMap.isEmpty()) {
                Set  myKey = myArtifactHashMap.keySet();
                for(Iterator iterator = myKey.iterator(); iterator.hasNext();) {
                    String keyName = (String)iterator.next();
                    Point value = myArtifactHashMap.get(keyName);

                    float myArtifactPositionX = value.x;
                    float myArtifactPositionY = value.y;

                    float xf = (myArtifactPositionX / (realWidth / viewWidth)) - (bitmapMyArtifact.getWidth() / 2);
                    float yf = (myArtifactPositionY / (realHeight / viewHeight)) - (bitmapMyArtifact.getHeight() / 2);

                    canvas.drawBitmap(bitmapMyArtifact, xf, yf, null);
                    Log.d("myArtifact Location : ", myArtifactPositionX + ", " + myArtifactPositionY);
                }
            }
        }

        public void drawArtifact(Canvas canvas) {
            bitmapArtifact = BitmapFactory.decodeResource(getResources(), R.drawable.artifact);
            bitmapArtifact = Bitmap.createScaledBitmap(bitmapArtifact, 80, 80, true);

            if(!artifactHashMap.isEmpty()) {
                Set  myKey = artifactHashMap.keySet();
                for(Iterator iterator = myKey.iterator(); iterator.hasNext();) {
                    String keyName = (String)iterator.next();
                    Point value = artifactHashMap.get(keyName);

                    float artifactPositionX = value.x;
                    float artifactPositionY = value.y;

                    float xf = (artifactPositionX / (realWidth / viewWidth)) - (bitmapArtifact.getWidth() / 2);
                    float yf = (artifactPositionY / (realHeight / viewHeight)) - (bitmapArtifact.getHeight() / 2);

                    canvas.drawBitmap(bitmapArtifact, xf, yf, null);
                }
            }
        }

        public void drawUser(Canvas canvas) { // 격자사용
            bitmapUser = BitmapFactory.decodeResource(getResources(), R.drawable.people);

            float userX = userPosition.x;
            float userY = userPosition.y;

            bitmapUser = Bitmap.createScaledBitmap(bitmapUser, 150, 150, true);

            float xf = (userX / (realWidth / viewWidth)) - (bitmapUser.getWidth()/2);
            float yf = (userY / (realHeight / viewHeight)) - (bitmapUser.getHeight()/2);

            canvas.drawBitmap(bitmapUser, xf, yf, null);
        }

//        public void drawUserReal(Canvas canvas) { // 격자 사용 안함
//            bitmapUser2 = BitmapFactory.decodeResource(getResources(), R.drawable.people2);
//
//            float userX = userPositionReal.x;
//            float userY = userPositionReal.y;
//
//            bitmapUser2 = Bitmap.createScaledBitmap(bitmapUser2, 150, 150, true);
//
//            float xf = (userX / (realWidth / viewWidth)) - (bitmapUser2.getWidth()/2);
//            float yf = (userY / (realHeight / viewHeight)) - (bitmapUser2.getHeight()/2);
//
//            canvas.drawBitmap(bitmapUser2, xf, yf, null);

//            Paint paint = new Paint();
//            paint.setColor(Color.YELLOW);
//            paint.setAntiAlias(true);
//            paint.setStrokeWidth(30);

//            canvas.drawPoint(userX / (realWidth / viewWidth), userY / (realHeight / viewHeight), paint);
//
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(40);
//            canvas.drawText(userX+" , "+userY, userX/(realWidth/viewWidth), userY/(realHeight/viewHeight)+30, paint);
//        }
    }
}
