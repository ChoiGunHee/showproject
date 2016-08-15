package com.dku.etri.serversocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import com.dku.etri.analysis.BeaconAnalyser;
import com.dku.etri.database.DatabaseSearcher;
import com.dku.etri.export.ExportToExcel;
import com.dku.etri.vo.BeaconData;

public class ServerThread extends Thread {
	
	private static final int DATASIZE = 20;
	
	private Socket client;
	private DataGatherServerSocket server;
	
	private BufferedReader reader;
	public BufferedWriter writer;
	private Vector<ServerThread> connetList;

	private ArrayList<BeaconData> currentClientBeacons;
	
	//data save
	private ArrayList<BeaconData> saveBeaconDatas;
	private ArrayList<BeaconData> beaconDatas_2mater = null;
	private ArrayList<BeaconData> beaconDatas_3mater = null;
	
	private BeaconAnalyser analyser;
	private int [] range;
	
	public ServerThread(Vector<ServerThread> connetList, Socket clinet, DataGatherServerSocket server) {
		this.client = clinet;
		this.server = server;
		this.connetList = connetList;
		try {
			reader = new BufferedReader(new InputStreamReader((clinet.getInputStream())));
			writer = new BufferedWriter(new OutputStreamWriter((clinet.getOutputStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			String msg = listen();
			
			if("success".equals(msg)) {
				send("success\n");
				saveAnalysisData(beaconDatas_2mater, beaconDatas_3mater);
			}
			
			if(msg == null) {
				break;
			}
		}
		
		server.deleteClient(client);
	}
	
	public String listen() {
		String msg = "";
		
		try {
			msg = reader.readLine();
			System.out.println("Client msg : " + msg);
			
			if(msg.startsWith("result")) {
				String device = msg.substring(msg.indexOf(" ! ")+3);
				String result = new DatabaseSearcher().getBeaconInfoAll(device);
				send(result);
			} else {
				//비콘 정보 수신 확인
				saveBeaconDatas = new ArrayList<BeaconData>();
				String [] beacons = msg.split(" ! ");
				
				for(int i=0; i<beacons.length; i++) {
					BeaconData beaconData = new BeaconData(beacons[i]);
					saveBeaconDatas.add(beaconData);
					System.out.println(beaconData.toString());
					
					if(saveBeaconDatas.size() == DATASIZE) {
						
						if(beaconData.getMeter() == 2) {
							beaconDatas_2mater = saveBeaconDatas;
							removeFrontData(beaconDatas_2mater);
							new ExportToExcel(beaconDatas_2mater, 2);
						}
						if(beaconData.getMeter() == 3) {
							beaconDatas_3mater = saveBeaconDatas;
							removeFrontData(beaconDatas_3mater);
							new ExportToExcel(beaconDatas_3mater, 3);
						}
						
						
						if(beaconDatas_2mater != null && beaconDatas_3mater != null) {
							return "success";
						}
					}
				}//end of for
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Client null");
		}
		
		
		return msg;
	}//end of listen()
	

	//수정 필요
	private void removeFrontData(ArrayList<BeaconData> beaconDatas) {
		for(int i=0; i<10; i++)
			beaconDatas.remove(0);
	}

	private void saveAnalysisData(ArrayList<BeaconData> beaconDatas_2mater2,
									ArrayList<BeaconData> beaconDatas_3mater2) {
		System.out.println("분석시작");
		analyser = new BeaconAnalyser(beaconDatas_2mater2,beaconDatas_3mater2);
		range = analyser.getRanges();
		System.out.println("분석, 저장 완료 :: " + range[0] + " / " + range[1] + " / " + range[2] + " / " + range[3]);
	}

	public void send(String msg) {
		try {
			this.writer.write(msg);
			
			System.out.println("send : " + msg);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}//end of send
	
	public Socket getSocket() {
		return client;
	}
}
