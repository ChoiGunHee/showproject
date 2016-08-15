package com.dku.etri.database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dku.etri.vo.DBInputData;
import com.mysql.jdbc.Statement;

/**
 * 데이터베이스에서 데이터 검색자 클래스
 * 작성자 : 최건희
 * 수정자 : 최건희
 * 마지막 수정일 : 2015. 10. 25
 */
public class DatabaseSearcher {
	
	private static DatabaseSearcher instance;
	
	private DBConnectionMgr pool;
	
	//DB SQL
	private String positionGetSql = "select * from " + DBConnectionMgr.INFORMATION_TABLE;
	private String rangeGetSql = "select range1, range2, range3, range4 from "
									+ DBConnectionMgr.RANGE_TABLE + " "
									+ "where " + "uuid = ? "
									+ "and major = ? "
									+ "and minor = ? ";
	private String parameterGetSql = "select * from " 
									+ DBConnectionMgr.PRAMETER_TABLE
									+ " where " + " uuid = ? "
									+ "and major = ? "
									+ "and minor = ? "
									+ "and device = ? "
									+ "and d0 = ? ";
	
	private String insertBeaconInfoSql = "insert into "
											+ DBConnectionMgr.INFORMATION_TABLE
											+ " values ( ?, ?, ?, null, null, null, null, null)";
	
	private String insertBeaconParameter = "insert into "
											+ DBConnectionMgr.PRAMETER_TABLE
											+ " values ( ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private String insertBeaconRangeSql = "insert into "
											+ DBConnectionMgr.RANGE_TABLE
											+ " values ( ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private String getBeaconInfoSql = "select * from "
											+ DBConnectionMgr.INFORMATION_TABLE;
	
	private String getBeaconParameterSql = "select * from " 
											+ DBConnectionMgr.PRAMETER_TABLE
											+ " where UUID=? and major=? and minor=?";
	
	private String getBeaconRangeSql = "select * from " 
											+ DBConnectionMgr.RANGE_TABLE
											+ " where Device=?";
	
	private String deleteBeaconInfoSql = "delete from "
											+ DBConnectionMgr.INFORMATION_TABLE
											+ " where "
											+ "uuid = ? "
											+ "and major = ? "
											+ "and minor = ? ";
	
	private String deleteBeaconRangeSql = "delete from "
											+ DBConnectionMgr.RANGE_TABLE
											+ " where "
											+ "uuid = ? "
											+ "and major = ? "
											+ "and minor = ? "
											+ "and device = ? ";
	
	private String deleteBeaconParameter = "delete from "
											+ DBConnectionMgr.PRAMETER_TABLE
											+ " where "
											+ "uuid = ? "
											+ "and major = ? "
											+ "and minor = ? "
											+ "and device = ? ";
	public boolean deleteBeaconInfo(DBInputData data) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(deleteBeaconParameter);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			pstmt.setString(4, data.getDevice());
			pstmt.executeUpdate();
			
			pstmt = con.prepareStatement(deleteBeaconRangeSql);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			pstmt.setString(4, data.getDevice());
			pstmt.executeUpdate();
			
			pstmt = con.prepareStatement(deleteBeaconInfoSql);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public boolean inputBeaconRange(DBInputData data, int range1, int range2, int range3, int range4) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(insertBeaconRangeSql);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			pstmt.setString(4, data.getDevice());
			pstmt.setInt(5, range1);
			pstmt.setInt(6, range2);
			pstmt.setInt(7, range3);
			pstmt.setInt(8, range4);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public boolean inputBeaconInfo(DBInputData data) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(insertBeaconInfoSql);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean insertBeaconParmeter(DBInputData data) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(insertBeaconParameter);
			pstmt.setString(1, data.getUuid());
			pstmt.setString(2, data.getMajor());
			pstmt.setString(3, data.getMinor());
			pstmt.setString(4, data.getDevice());
			pstmt.setInt(5, data.getD0());
			pstmt.setDouble(6, data.getRssi0());
			pstmt.setDouble(7, data.getXg());
			pstmt.setLong(8, data.getN());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public String getBeaconInfoAll(String device) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String result = "";
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(getBeaconRangeSql);
			pstmt.setString(1, device);
			ResultSet rs = pstmt.executeQuery();
			
			String beaconUUID = "";
			String beaconIdentity = "";
			String beaconRange = "";
			String beaconParam[] = {"", ""};
			
			while(rs.next()) {
				beaconUUID = rs.getString("UUID");
				beaconIdentity = rs.getString("Major") + "/" + rs.getString("Minor");
				beaconRange = rs.getString("Range1") + "/" + rs.getString("Range2") + "/" + rs.getString("Range3") + "/" + rs.getString("Range4");
				beaconParam = getBeaconParameter(rs.getString("UUID"), rs.getString("Major"), rs.getString("Minor"));
				
				System.out.println("getBeaconInfo result : " + result + beaconUUID + "?" + beaconIdentity + "?" + beaconRange + "?" + beaconParam[0] + "?" + beaconParam[1] + "!");
				result = result + beaconUUID + "?" + beaconIdentity + "?" + beaconRange + "?" + beaconParam[0] + "?" + beaconParam[1] + "!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result+"\n";
	}
	
	public String[] getBeaconParameter(String uuid, String major, String minor) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String[] readParam = {"", ""};
		
		try {
			con = pool.getConnection();
			pstmt = con.prepareStatement(getBeaconParameterSql);
			pstmt.setString(1, uuid);
			pstmt.setString(2, major);
			pstmt.setString(3, minor);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String read = rs.getString("d0") + "/" + rs.getString("RSSI0") + "/" + rs.getString("Xg") + "/" + rs.getString("n");
				if(("2").equals(rs.getString("d0")))
						readParam[0] = read;
//				else if(("3").equals(rs.getString("d0")))
				else
					readParam[1] = read;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return readParam;
	}
	
	public DatabaseSearcher() {
		pool = DBConnectionMgr.getInstance();
	}
	
	//인스턴스 싱글턴 패턴
	public static DatabaseSearcher getInstance() {
		if(instance == null) {
			instance = new DatabaseSearcher();
			return instance;
		}
		return instance;	
	}
	

}
