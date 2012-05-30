package com.visma.autosysmonitor.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

//import weblogic.health.HealthState;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JmxApplicationMonitor implements Monitor {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;
	private String host;
	private int port;
	private String user;
	private String password;
	private String type;
	private String serverInstance;

	private final ObjectName service;
	private MBeanServerConnection connection;
	private JMXConnector connector;
	private Map<String, Object> appStatusMap;

	public JmxApplicationMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.JMXAPPS;
		appStatusMap = new  HashMap<String, Object>();
		parseUrl();
	}

	public JmxApplicationMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.JMXAPPS;
		appStatusMap = new  HashMap<String, Object>();
	}

	public JmxApplicationMonitor(MonitorDTO to) {
		this.name = to.getName();
		this.url = to.getUrl();
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
		this.type = MonitorFactory.JMXAPPS;
		appStatusMap = new  HashMap<String, Object>();
		parseUrl();
	}

	private void parseUrl() {
		String[] elem = url.split(":");
		host = elem[0];
		port = Integer.parseInt(elem[1]);
		serverInstance = elem[2];
		user = elem[3];
		password = elem[4];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#toSystemInfoDTO()
	 */
	@Override
	public MonitorDTO toMonitorDTO() {
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
			appStatusMap.clear();
			initConnection();
			//printNameAndState();
			getServletData();
			connector.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	{
		try {
			service = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initConnection() throws IOException, MalformedURLException {
		String protocol = "t3";
		String jndiroot = "/jndi/";
		String mserver = "weblogic.management.mbeanservers.domainruntime";
		JMXServiceURL serviceURL = new JMXServiceURL(protocol, host, port, jndiroot + mserver);

		Hashtable h = new Hashtable();
		h.put(Context.SECURITY_PRINCIPAL, user);
		h.put(Context.SECURITY_CREDENTIALS, password);
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
		connector = JMXConnectorFactory.connect(serviceURL, h);
		connection = connector.getMBeanServerConnection();
	}

	public ObjectName[] getServerRuntimes() throws Exception {
		return (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
	}

	public void getServletData() throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		
		for(int i = 0; i< serverRT.length; i++){
			String serverName = (String) connection.getAttribute(serverRT[i], "Name");
			if(serverName.equalsIgnoreCase(serverInstance)){
				//Found serverinstance 
				ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT[i], "ApplicationRuntimes");
				for(int j = 0 ; j < appRT.length; j++){
					String appName = (String) connection.getAttribute(appRT[j], "Name");
					//HealthState health = (HealthState) connection.getAttribute(appRT[j], "HealthState");
					appStatusMap.put(appName,1 );
				}
			}
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

	@Override
	public Map<String, Object> getData() {
		return appStatusMap;
	}

	@Override
	public void setData(Map<String, Object> map) {
		appStatusMap= map;
	}

}
