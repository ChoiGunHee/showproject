package com.dku.etri.launcher;

import com.dku.etri.serversocket.DataGatherServerSocket;

public class Launcher {
	public static void main(String [] args) {
		DataGatherServerSocket server = new DataGatherServerSocket();
		server.start();
	}
}
