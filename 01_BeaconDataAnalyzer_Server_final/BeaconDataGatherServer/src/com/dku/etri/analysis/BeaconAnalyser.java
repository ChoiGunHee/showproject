package com.dku.etri.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dku.etri.database.DatabaseSearcher;
import com.dku.etri.vo.BeaconData;
import com.dku.etri.vo.DBInputData;

public class BeaconAnalyser {
	
	private ArrayList<BeaconData> dataList_2meter;
	private ArrayList<BeaconData> dataList_3meter;
	
	//save datas
	int range1;
	int range2;
	int range3;
	int range4;
	public DBInputData inputData_2meter;
	public DBInputData inputData_3meter;
	
	public BeaconAnalyser(ArrayList<BeaconData> list_2meter, ArrayList<BeaconData> list_3meter) {
		dataList_2meter = list_2meter;
		dataList_3meter = list_3meter;
		inputData_2meter = new DBInputData();
		inputData_3meter = new DBInputData();
		
		Collections.sort(dataList_2meter);
		Collections.sort(dataList_3meter);
		System.out.println("정렬 끝");
		
		setRange();
		inputData_2meter = calParamter(list_2meter, inputData_2meter);
		inputData_3meter = calParamter(list_3meter, inputData_3meter);
		
		save();
	}

	
	private DBInputData calParamter(ArrayList<BeaconData> list, DBInputData inputData) {
		
		inputData.setUuid(list.get(0).getUuid());
		inputData.setMajor(list.get(0).getMajor());
		inputData.setMinor(list.get(0).getMinor());
		inputData.setDevice(list.get(0).getDevice());
		
		//d0 연산
		int d0 = list.get(0).getMeter();
		
		//기본연산
		int sumRssi = 0;
		int sumStrDeviation = 0;
		for (BeaconData beaconData : list) {
			sumRssi += beaconData.getRssi();
			sumStrDeviation += beaconData.getRssi() * beaconData.getRssi();
		}
		
		//rssi0 연산
		double rssi0 = ((double)sumRssi)/list.size();
		
		//xg 연산
		double xg = Math.sqrt((((double)sumStrDeviation)/list.size()) - rssi0*rssi0);
		xg = Double.parseDouble(String.format("%.2f", xg));
		
		inputData.setD0(d0);
		inputData.setRssi0(rssi0);
		inputData.setXg(xg);
		System.out.println("UUID : " + inputData.getUuid() + " d0, rssi0, xg 연산 끝");
		System.out.println("d0 : " + d0 + " rssi : " + rssi0 + " xg : " + xg);
		
		//n 연산
		calN(list, inputData);
		
		return inputData;
	}
	
	private void calN(ArrayList<BeaconData> list, DBInputData inputData) {
		
		double [] candidate_N = new double[5];
		int meter = list.get(0).getMeter();
		double exponent = 0;
		
		//System.out.println(meter + " " + inputData.getD0() + " " + inputData.getRssi0() + " " + inputData.getXg() + " ");
		for(int n=2; n<=6; n++) {
			double sum_n_error = 0.0;
			for(int i=0; i<list.size(); i++) {
				exponent = (inputData.getRssi0() - list.get(i).getRssi() - inputData.getXg())/((double)(10*n));
				double test = inputData.getD0() * Math.pow(10, exponent);
				sum_n_error += Math.abs(meter - test);
				//System.out.println( "Rssi : " + list.get(i).getRssi() + " test : " + test + " error : " + (meter - test));
			}
			candidate_N[n-2] = sum_n_error/list.size();
		}
		
		double min = 9999.0;
		int minIndex = 7;
		for(int i=0; i<5; i++) {
			if(min > candidate_N[i]) {
				min = candidate_N[i];
				minIndex = i;
			}
		}
		
		inputData.setN(minIndex + 2);
		for (double d : candidate_N) {
			//System.out.println(d);
		}
		System.out.println("N 연산 끝");
	}
	
	private void setRange() {
		range1 = selectMinValue(dataList_2meter);
		range4 = selectMaxValue(dataList_3meter);
		
		int maxValue_2meter = Math.abs(selectMaxValue(dataList_2meter));
		int minValue_3meter = Math.abs(selectMinValue(dataList_3meter));
		
		if(maxValue_2meter > minValue_3meter) {
			range2 = -minValue_3meter;
			range3 = -maxValue_2meter;
		}
		else {
			range2 = -maxValue_2meter;
			range3 = -minValue_3meter;
		}
	}
	
	public void save() {
		DatabaseSearcher searcher = DatabaseSearcher.getInstance();
		
		searcher.deleteBeaconInfo(inputData_2meter);
		
		searcher.inputBeaconInfo(inputData_2meter);
		
		searcher.inputBeaconRange(inputData_2meter, range1, range2, range3, range4);
		searcher.insertBeaconParmeter(inputData_2meter);
		searcher.insertBeaconParmeter(inputData_3meter);
	}
	
	public int selectMaxValue(ArrayList<BeaconData> list) {
		return list.get(0).getRssi();
	}

	public int selectMinValue(ArrayList<BeaconData> list) {
		return list.get(list.size() - 1).getRssi();
	}
	
	public int selectMaxValue() {
		return dataList_2meter.get(0).getRssi();
	}

	public int selectMinValue() {
		return dataList_2meter.get(dataList_2meter.size() - 1).getRssi();
	}


	public int[] getRanges() {
		int [] result = new int[4];
		result[0] = range1;
		result[1] = range2;
		result[2] = range3;
		result[3] = range4;
		
		return result;
	}
}
