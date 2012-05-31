package com.visma.autosysmonitor.domain;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.ObjectName;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JmxServerInstanceMonitor extends JmxBaseMonitor {

	{
		type = MonitorFactory.JMXSERVER;
	}

	public JmxServerInstanceMonitor(String name, String url, int timeout) {
		this.name = name;
		setUrl(url);
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		parseUrl();
	}

	public JmxServerInstanceMonitor() {
		this.name = "";
		setUrl("");
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public JmxServerInstanceMonitor(MonitorDTO to) {
		this.name = to.getName();
		setUrl(to.getUrl());
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
		parseUrl();
	}

	private void parseUrl() {
		String[] elem = getUrl().split(":");
		host = elem[0];
		port = Integer.parseInt(elem[1]);
		user = elem[2];
		password = elem[3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#update()
	 */
	@Override
	public void update() {
		try {
			initConnection();
			ObjectName[] serverRT = getServerRuntimes();
//			System.out.println("got server runtimes");
			int length = (int) serverRT.length;
			for (int i = 0; i < length; i++) {
				String name = (String) connection.getAttribute(serverRT[i], "Name");
				String state = (String) connection.getAttribute(serverRT[i], "State");
				ObjectName threadpool = (ObjectName) connection.getAttribute(serverRT[i], "ThreadPoolRuntime");
				int hoggingThreads = (Integer) connection.getAttribute(threadpool, "HoggingThreadCount");
				if (hoggingThreads > 0)
					state = "STUCK";
				this.data.put(name, state);
			}
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

}
