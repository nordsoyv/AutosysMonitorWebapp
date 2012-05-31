package com.visma.autosysmonitor.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JdbcMonitor extends BaseMonitor {
	{
		type = MonitorFactory.JDBC;
	}

	public JdbcMonitor(String name, String url, int timeout) {
		this.name = name;
		setUrl(url);
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
	}

	public JdbcMonitor() {
		this.name = "";
		setUrl("");
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public JdbcMonitor(MonitorDTO to) {
		this.name = to.getName();
		setUrl(to.getUrl());
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#update()
	 */
	@Override
	public void update() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection conn = DriverManager.getConnection(getUrl());
			Statement stmt = conn.createStatement();
			try {
				ResultSet rset = stmt.executeQuery("select 1 from DUAL");
				alive = true;
				try {
				} finally {
					try {
						rset.close();
					} catch (Exception ignore) {
					}
				}

			} finally {
				stmt.close();
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			alive = false;
			e.printStackTrace();
		} catch (SQLException e) {
			alive = false;
			e.printStackTrace();
		}

	}

}
