package beacon.etri.dku.com.etribeaconreco.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by GunHee on 2015-09-10.
 */
public class ClientThread extends Thread {
    BufferedReader bufferR;
    BufferedWriter bufferW;
    Socket client;
    Handler handler;

    public ClientThread(Socket client, Handler handler) {
        this.handler = handler;
        try {
            this.client = client;
            //연결된 소켓으로부터 대화를 나눌 스트림을 얻음
            bufferR = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bufferW = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //send
    public void send(String data){
        System.out.println("전송");
        try {
            System.out.println("data:"+data);
            bufferW.write(data+"\n");
            bufferW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //listen
    public String listen(){
        String msg=null;
        try {
            while(true){
                msg=bufferR.readLine();
                Message m = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("msg", msg);
                m.setData(bundle);
                handler.sendMessage(m);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void run() {
        super.run();
        listen();
    }

}



