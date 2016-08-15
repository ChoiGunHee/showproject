package com.dku.etri.vo;

public class BeaconData implements Comparable<BeaconData> {
	
	private String device;
	private String uuid;
	private String major;
	private String minor;
	private int rssi;
	private int meter;
	
	public BeaconData(String data) {
		String [] datas = data.split(" / ");
		
		device = datas[0];
		uuid = datas[1];
		major = datas[2];
		minor = datas[3];
		rssi = Integer.parseInt(datas[4]);
		meter = Integer.parseInt(datas[5]);
	}
	

	@Override
	public String toString() {
		return "BeaconData [device=" + device + ", uuid=" + uuid + ", major="
				+ major + ", minor=" + minor + ", rssi=" + rssi + ", meter="
				+ meter + "]";
	}


	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public int getMeter() {
		return meter;
	}


	public void setMeter(int meter) {
		this.meter = meter;
	}


	@Override
	public int compareTo(BeaconData o) {
		if (this.getRssi() > o.getRssi())
			return 1;
		else if (this.getRssi() < o.getRssi())
			return -1;
		
		return 0;
	}


	
	
}
