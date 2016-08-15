package com.dku.etri.export;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dku.etri.vo.BeaconData;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 엑셀 파일로 결과 출력 클래스
 * 작성자 : 홍민하
 * 수정자 : 최건희
 * 마지막 수정일 : 2015. 10. 24
 * 
 */
public class ExportToExcel {
	
	String filePath = "C:/Users/MobileHP/Desktop/ETRI_2015/실험결과/ETRI_experiment25_160217/";
	
	public ExportToExcel(ArrayList<BeaconData> exportClientBeacons, int meter) {
		export(exportClientBeacons, meter);
		
	}

	public void export(	ArrayList<BeaconData> exportClientBeacons, int meter) {
		
		filePath = filePath + "GatherEstimote_" 
				+ exportClientBeacons.get(0).getDevice() + "_" 
				+ exportClientBeacons.get(0).getMajor() + "_"
				+ exportClientBeacons.get(0).getMinor() + "_"
				+ meter + ".xls";
		
		File exportFile = new File(filePath);
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(exportFile);
			
			workbook.createSheet("sheet1", 0);
			WritableSheet sheet = workbook.getSheet(0);
			
			
			Label label = new Label(0,0,"Device");
			sheet.addCell(label);
			label = new Label(1,0,"UUID");
			sheet.addCell(label);
			label = new Label(2,0,"Major");
			sheet.addCell(label);
			label = new Label(3,0,"Minor");
			sheet.addCell(label);
			label = new Label(4,0,"RSSI");
			sheet.addCell(label);
			label = new Label(5,0,"meter");
			sheet.addCell(label);
			
			for(int i=0; i<exportClientBeacons.size(); i++) {			
				label = new Label(0,i+1, exportClientBeacons.get(i).getDevice());
				sheet.addCell(label);
				label = new Label(1,i+1, exportClientBeacons.get(i).getUuid());
				sheet.addCell(label);
				label = new Label(2,i+1, exportClientBeacons.get(i).getMajor());
				sheet.addCell(label);
				label = new Label(3,i+1, exportClientBeacons.get(i).getMinor());
				sheet.addCell(label);
				label = new Label(4,i+1, exportClientBeacons.get(i).getRssi()+"");
				sheet.addCell(label);
				label = new Label(5,i+1, exportClientBeacons.get(i).getMeter()+"");
				sheet.addCell(label);
				
			}
			workbook.write();
			workbook.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
