package beacon.etri.dku.com.etribeaconreco.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import beacon.etri.dku.com.etribeaconreco.R;
import beacon.etri.dku.com.etribeaconreco.socket.ClientThread;

public class UserFaveUpdateActivity extends Activity
    implements AdapterView.OnItemClickListener {
    private Socket client;
    String ip = "220.149.230.119";
//    String ip="192.168.0.11";
    int port = 34690;
    String uuid = "94F65C27-C801-43C8-A207-54FBA781911A";
    public Thread thread;
    public ClientThread clientThread;
    Handler handler;

    String myArtifactData = null;

    UserFaveListAdapter adapter;

    List<UserFaveVo> userFaveVoList = new ArrayList<UserFaveVo>();
    ListView myArtifactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfaveupdate);
        myArtifactListView = (ListView)findViewById(R.id.myArtifactListView);
        myArtifactListView.setOnItemClickListener(this);
        socketConnect();


        handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg != null) {
                    super.handleMessage(msg);
                    Bundle bundle = msg.getData();
                    myArtifactData = (String)msg.getData().get("msg");
                    if(myArtifactData.startsWith("ACK_userFave")) {
                        myArtifactListView();
                    }
                    Log.d("RECEIVE", "receive : " + myArtifactData);
                } else {
                    return;
                }
            }
        };

    }

    public void socketConnect() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    client = new Socket(ip, port);
                    clientThread = new ClientThread(client, handler);
                    clientThread.start();

                    String msg="REQ_userFave ? ";
                    msg += Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    clientThread.send(msg);
                    Log.d("Send Message : ", msg);
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

    public void myArtifactListView() {
        String[] myArtifacts =  myArtifactData.split(" \\? ")[1].split(" ! ");

        UserFaveVo data = null;
        for(int i=0; i<myArtifacts.length; i++) {
            String[] splitData = myArtifacts[i].split(" / ");
            if(splitData[1].equals("true")) {
                data = new UserFaveVo(splitData[0], true);
            } else {
                data = new UserFaveVo(splitData[0], false);
            }
            userFaveVoList.add(data);
        }

        adapter = new UserFaveListAdapter(this, R.layout.userfave, userFaveVoList);
        myArtifactListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void saveMyArtifact(View view) {
        if(clientThread!=null) {
            String msg = "REQ_userFaveUpdate ? ";
            msg += Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)+" ! ";
            for(int i=0; i< userFaveVoList.size(); i++) {
                msg+= userFaveVoList.get(i).getIsSelect()+" ! ";
            }
            clientThread.send(msg);
            Log.d("send Message : ", msg);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        socketClose();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("userfave : ", "Test");
        userFaveVoList.get(i).setIsSelect(!userFaveVoList.get(i).getIsSelect());
        adapter.notifyDataSetChanged();
    }
}