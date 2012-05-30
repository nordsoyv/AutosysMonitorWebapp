package com.visma.autosysmonitor.domain;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.ObjectName;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JmxApplicationMonitor extends JmxBaseMonitor {
	private String serverInstance;

	{
		type = MonitorFactory.JMXAPPS;
	}

	public JmxApplicationMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		parseUrl();
	}

	public JmxApplicationMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public JmxApplicationMonitor(MonitorDTO to) {
		this.name = to.getName();
		this.url = to.getUrl();
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
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
	 * @see com.visma.autosysmonitor.domain.Monitor#update()
	 */
	@Override
	public void update() {
		try {
			data.clear();
			initConnection();
			// printNameAndState();
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

	private void getServletData() throws Exception {
		ObjectName[] serverRT = getServerRuntimes();

		for (int i = 0; i < serverRT.length; i++) {
			String serverName = (String) connection.getAttribute(serverRT[i], "Name");
			if (serverName.equalsIgnoreCase(serverInstance)) {
				// Found serverinstance
				ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT[i], "ApplicationRuntimes");
				for (int j = 0; j < appRT.length; j++) {
					String appName = (String) connection.getAttribute(appRT[j], "Name");
					// HealthState health = (HealthState)
					// connection.getAttribute(appRT[j], "HealthState");
					data.put(appName, 1);
				}
			}
		}
	}

}
