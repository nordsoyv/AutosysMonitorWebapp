package com.visma.autosysmonitor.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JdbcMonitor implements Monitor {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;
	private String type;

	public JdbcMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.JDBC;
	}

	public JdbcMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.JDBC;
	}

	public JdbcMonitor(MonitorDTO to) {
		this.name = to.getName();
		this.url = to.getUrl();
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
		this.type = MonitorFactory.JDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#toSystemInfoDTO()
	 */
	@Override
	public MonitorDTO toSystemInfoDTO() {
		MonitorDTO sys = new MonitorDTO(this);
		return sys;
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
			Connection conn = DriverManager.getConnection(this.url);
			Statement stmt = conn.createStatement();
			try {
				ResultSet rset = stmt.executeQuery("select 1 from DUAL");
				alive= true;
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
			alive= false;
			e.printStackTrace();
		} catch (SQLException e) {
			alive= false;
			e.printStackTrace();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getUrl()
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#isAlive()
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setAlive(boolean)
	 */
	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getPing()
	 */
	@Override
	public int getPing() {
		return ping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setPing(int)
	 */
	@Override
	public void setPing(int ping) {
		this.ping = ping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getTimeout()
	 */
	@Override
	public int getTimeout() {
		return timeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setTimeout(int)
	 */
	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;		
	}

}
