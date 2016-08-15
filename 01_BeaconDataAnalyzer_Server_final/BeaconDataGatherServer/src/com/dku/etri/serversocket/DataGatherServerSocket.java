package com.dku.etri.serversocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class DataGatherServerSocket {
	
private final static int SERVERPORT = 34696;
	
	
	private ServerSocket server;
	private Vector<ServerThread> connetList;
	
	public DataGatherServerSocket() {
	 
		connetList = new Vector<ServerThread>();	
	
	}// end of constructor
	
	public void deleteClient(Socket client) {
		for(int i=0; i<connetList.size(); i++) {
			if(client.getInetAddress().getHostAddress().equals(connetList.get(i).getSocket().getInetAddress().getHostAddress())) {
				connetList.remove(i);
			}
		}
	}
	
	public void start() {
		
		try {
			server = new ServerSocket(SERVERPORT);
			System.out.println("서버 시작!");
			
			while (true) {
				Socket clinet = server.accept();
				InetAddress inetAddress = clinet.getInetAddress();
				String clinetIP = inetAddress.getHostAddress();
				System.out.println("클라이언트 연결 : " + clinetIP);
				
				ServerThread serverThread = new ServerThread(connetList, clinet, this);
				serverThread.start();
				connetList.add(serverThread);
				System.out.println("현재 접속자 수 : " + connetList.size());
			}//end of while
			
		} catch (IOException e) {
			e.printStackTrace();
		}//end of try~catch
		
	}//end of start()
}
