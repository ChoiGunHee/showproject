package com.dku.etri.vo;

public class DBInputData {

	private String device;
	private String uuid;
	private String major;
	private String minor;
	private int d0;
	private double rssi0;
	private double xg;
	private int n;
	
	public DBInputData() {
		
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

	public int getD0() {
		return d0;
	}

	public void setD0(int d0) {
		this.d0 = d0;
	}

	public double getRssi0() {
		return rssi0;
	}

	public void setRssi0(double rssi0) {
		this.rssi0 = rssi0;
	}

	public double getXg() {
		return xg;
	}

	public void setXg(double xg) {
		this.xg = xg;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	
}
