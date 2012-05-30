package com.visma.autosysmonitor.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import com.visma.autosysmonitor.da.MonitorFactory;

public class JmxMonitor extends JmxBaseMonitor {

	{
		type = MonitorFactory.JMX;
	}

	
	public JmxMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.JMX;
		parseUrl();
	}

	public JmxMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public JmxMonitor(MonitorDTO to) {
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
			printNameAndState();
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



	protected void getServletData() throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		int length =  serverRT.length;
		for (int i = 0; i < length; i++) {
			ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT[i], "ApplicationRuntimes");
			int appLength =  appRT.length;
			for (int x = 0; x < appLength; x++) {
				System.out.println("Application name: " + (String) connection.getAttribute(appRT[x], "Name"));
				ObjectName[] compRT = (ObjectName[]) connection.getAttribute(appRT[x], "ComponentRuntimes");
				int compLength = compRT.length;
				for (int y = 0; y < compLength; y++) {
					System.out.println("  Component name: " + (String) connection.getAttribute(compRT[y], "Name"));
					String componentType = (String) connection.getAttribute(compRT[y], "Type");
					System.out.println(componentType.toString());
					if (componentType.toString().equals("WebAppComponentRuntime")) {
						ObjectName[] servletRTs = (ObjectName[]) connection.getAttribute(compRT[y], "Servlets");
						int servletLength =  servletRTs.length;
						for (int z = 0; z < servletLength; z++) {
							System.out.println("    Servlet name: " + (String) connection.getAttribute(servletRTs[z], "Name"));
							System.out.println("       Servlet context path: " + (String) connection.getAttribute(servletRTs[z], "ContextPath"));
							System.out.println("       Invocation Total Count : "
									+ (Object) connection.getAttribute(servletRTs[z], "InvocationTotalCount"));
						}
					}
				}
			}
		}
	}

	protected void printNameAndState() throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		System.out.println("got server runtimes");
		int length = (int) serverRT.length;
		for (int i = 0; i < length; i++) {
			String name = (String) connection.getAttribute(serverRT[i], "Name");
			String state = (String) connection.getAttribute(serverRT[i], "State");
			System.out.println("Server name: " + name + ".   Server state: " + state);
		}
	}


}
