package com.dku.etri.database;
/**
 * Copyright(c) 2001 iSavvix Corporation (http://www.isavvix.com/)
 *
 *                        All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * any purpose and without fee is hereby granted, provided that the
 * above copyright notice and this permission notice appear in all
 * copies, and that the name of iSavvix Corporation not be used in
 * advertising or publicity pertaining to this material without the
 * specific, prior written permission of an authorized representative of
 * iSavvix Corporation.
 *
 * ISAVVIX CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES,
 * EXPRESS OR IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR ANY PARTICULAR PURPOSE, AND THE WARRANTY AGAINST
 * INFRINGEMENT OF PATENTS OR OTHER INTELLECTUAL PROPERTY RIGHTS.  THE
 * SOFTWARE IS PROVIDED "AS IS", AND IN NO EVENT SHALL ISAVVIX CORPORATION OR
 * ANY OF ITS AFFILIATES BE LIABLE FOR ANY DAMAGES, INCLUDING ANY
 * LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL DAMAGES RELATING
 * TO THE SOFTWARE.
 *
 */


import java.sql.*;
import java.util.Properties;
import java.util.Vector;


/**
 * 데이터 베이스 연결 클래스
 * 작성자 : 홍민하, 안희정
 * 수정자 : 최건희
 * 마지막 수정 날짜 : 2015. 10. 24
 */
public class DBConnectionMgr {
	private final static String DRIVER = "org.gjt.mm.mysql.Driver";
	private final static String DBURL = "jdbc:mysql://localhost:3306/ETRI";
	private final static String DBUSER = "root";
	private final static String DBPASSWORD = "3469";
	
	//table names
	public static final String INFORMATION_TABLE = "beacon_information";
	public static final String RANGE_TABLE = "beacon_range";
	public static final String PRAMETER_TABLE = "beacon_parameter";
	
	//table beacon_information columns
	public static final String INFORMATION_UUID_COL = "UUID";
	public static final String INFORMATION_MAJOR_COL = "Major";
	public static final String INFORMATION_MINOR_COL = "Minor";
	public static final String INFORMATION_MAPID_COL = "MapID";
	public static final String INFORMATION_X_COL = "PositionX";
	public static final String INFORMATION_Y_COL = "PositionY";
	public static final String INFORMATION_INTERVAL = "beacon_interval";
	public static final String INFORMATION_TXPOWOR = "TxPower";
	
	//table beacon_parameter columns
	public static final String PARAMETER_UUID_COL = "UUID";
	public static final String PARAMETER_MAJOR_COL = "Major";
	public static final String PARAMETER_MINOR_COL = "Minor";
	public static final String PARAMETER_DEVICE_COL = "Device";
	public static final String PARAMETER_D0_COL = "d0";
	public static final String PARAMETER_RSSI0_COL = "RSSI0";
	public static final String PARAMETER_XG_COL = "Xg";
	public static final String PARAMETER_N_COL = "n";
	
	//tablebeacon_range columns
	public static final String RANGE_UUID_COL = "UUID";
	public static final String RANGE_MAJOR_COL = "Major";
	public static final String RANGE_MINOR_COL = "Minor";
	public static final String RANGE_DEVICE_COL = "Device";
	public static final String RANGE_RANGE1_COL = "Range1";
	public static final String RANGE_RANGE2_COL = "Range2";
	public static final String RANGE_RANGE3_COL = "Range3";
	public static final String RANGE_RANGE4_COL = "Range4";
	
	public static final String POSITION_COLS[] = {
													INFORMATION_UUID_COL,
													INFORMATION_MAJOR_COL,
													INFORMATION_MINOR_COL,
													INFORMATION_MAPID_COL,
													INFORMATION_X_COL,
													INFORMATION_Y_COL,
													INFORMATION_INTERVAL,
													INFORMATION_TXPOWOR
												};
	
	public static final String PARAMETER_COLS[] = {
													PARAMETER_UUID_COL,
													PARAMETER_MAJOR_COL,
													PARAMETER_MINOR_COL,
													PARAMETER_DEVICE_COL,
													PARAMETER_D0_COL,
													PARAMETER_RSSI0_COL,
													PARAMETER_XG_COL,
													PARAMETER_N_COL,
												};

	public static final String RANGE_COLS[] = {
													RANGE_UUID_COL,
													RANGE_MAJOR_COL,
													RANGE_MINOR_COL,
													RANGE_DEVICE_COL,
													RANGE_RANGE1_COL,
													RANGE_RANGE2_COL,
													RANGE_RANGE3_COL,
													RANGE_RANGE4_COL,
												};
	
    private Vector connections = new Vector(10);
    
    private boolean _traceOn = false;
    private boolean initialized = false;
    private int _openConnections = 50;
    private static DBConnectionMgr instance = null;

    public DBConnectionMgr() {
    }

    /** Use this method to set the maximum number of open connections before
     unused connections are closed.
     */

    public static DBConnectionMgr getInstance() {
        if (instance == null) {
            synchronized (DBConnectionMgr.class) {
                if (instance == null) {
                    instance = new DBConnectionMgr();
                }
            }
        }

        return instance;
    }

    public void setOpenConnectionCount(int count) {
        _openConnections = count;
    }


    public void setEnableTrace(boolean enable) {
        _traceOn = enable;
    }


    /** Returns a Vector of java.sql.Connection objects */
    public Vector getConnectionList() {
        return connections;
    }


    /** Opens specified "count" of connections and adds them to the existing pool */
    public synchronized void setInitOpenConnections(int count)
            throws SQLException {
        Connection c = null;
        ConnectionObject co = null;

        for (int i = 0; i < count; i++) {
            c = createConnection();
            co = new ConnectionObject(c, false);

            connections.addElement(co);
            trace("ConnectionPoolManager: Adding new DB connection to pool (" + connections.size() + ")");
        }
    }


    /** Returns a count of open connections */
    public int getConnectionCount() {
        return connections.size();
    }


    /** Returns an unused existing or new connection.  */
    public synchronized Connection getConnection()
            throws Exception {
        if (!initialized) {
            Class c = Class.forName(DRIVER);
            DriverManager.registerDriver((Driver) c.newInstance());

            initialized = true;
        }


        Connection c = null;
        ConnectionObject co = null;
        boolean badConnection = false;


        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);

            // If connection is not in use, test to ensure it's still valid!
            if (!co.inUse) {
                try {
                    badConnection = co.connection.isClosed();
                    if (!badConnection)
                        badConnection = (co.connection.getWarnings() != null);
                } catch (Exception e) {
                    badConnection = true;
                    e.printStackTrace();
                }

                // Connection is bad, remove from pool
                if (badConnection) {
                    connections.removeElementAt(i);
                    trace("ConnectionPoolManager: Remove disconnected DB connection #" + i);
                    continue;
                }

                c = co.connection;
                co.inUse = true;

                trace("ConnectionPoolManager: Using existing DB connection #" + (i + 1));
                break;
            }
        }

        if (c == null) {
            c = createConnection();
            co = new ConnectionObject(c, true);
            connections.addElement(co);

            trace("ConnectionPoolManager: Creating new DB connection #" + connections.size());
        }

        return c;
    }


    /** Marks a flag in the ConnectionObject to indicate this connection is no longer in use */
    public synchronized void freeConnection(Connection c) {
        if (c == null)
            return;

        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if (c == co.connection) {
                co.inUse = false;
                break;
            }
        }

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if ((i + 1) > _openConnections && !co.inUse)
                removeConnection(co.connection);
        }
    }

    public void freeConnection(Connection c, PreparedStatement p, ResultSet r) {
        try {
            if (r != null) r.close();
            if (p != null) p.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection c, Statement s, ResultSet r) {
        try {
            if (r != null) r.close();
            if (s != null) s.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection c, PreparedStatement p) {
        try {
            if (p != null) p.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void freeConnection(Connection c, Statement s) {
        try {
            if (s != null) s.close();
            freeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void freeConnection( ResultSet r) {
        try {
            if (r != null) r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Marks a flag in the ConnectionObject to indicate this connection is no longer in use */
    public synchronized void removeConnection(Connection c) {
        if (c == null)
            return;

        ConnectionObject co = null;
        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if (c == co.connection) {
                try {
                    c.close();
                    connections.removeElementAt(i);
                    trace("Removed " + c.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    private Connection createConnection()
            throws SQLException {
        Connection con = null;

        try {
        	/*
            if (DBUSER == null)
                DBUSER = "";
            if (DBPASSWORD == null)
                DBPASSWORD = "";
			*/
            Properties props = new Properties();
            props.put("user", DBUSER);
            props.put("password", DBPASSWORD);

            con = DriverManager.getConnection(DBURL, props);
        } catch (Throwable t) {
            throw new SQLException(t.getMessage());
        }

        return con;
    }


    /** Closes all connections and clears out the connection pool */
    public void releaseFreeConnections() {
        trace("ConnectionPoolManager.releaseFreeConnections()");

        Connection c = null;
        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            if (!co.inUse)
                removeConnection(co.connection);
        }
    }


    /** Closes all connections and clears out the connection pool */
    public void finalize() {
        trace("ConnectionPoolManager.finalize()");

        Connection c = null;
        ConnectionObject co = null;

        for (int i = 0; i < connections.size(); i++) {
            co = (ConnectionObject) connections.elementAt(i);
            try {
                co.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            co = null;
        }

        connections.removeAllElements();
    }


    private void trace(String s) {
        if (_traceOn)
            System.err.println(s);
    }

}


class ConnectionObject {
    public java.sql.Connection connection = null;
    public boolean inUse = false;

    public ConnectionObject(Connection c, boolean useFlag) {
        connection = c;
        inUse = useFlag;
    }
}
