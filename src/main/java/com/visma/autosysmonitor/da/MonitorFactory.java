package com.visma.autosysmonitor.da;

import com.visma.autosysmonitor.domain.HeaderMonitor;
import com.visma.autosysmonitor.domain.HttpGetMonitor;
import com.visma.autosysmonitor.domain.JdbcMonitor;
import com.visma.autosysmonitor.domain.JmxApplicationMonitor;
import com.visma.autosysmonitor.domain.JmxServerInstanceMonitor;
import com.visma.autosysmonitor.domain.Monitor;

public class MonitorFactory {

	public static final String HTTPGET = "HTTP-GET";
	public static final String HEADER = "HEADER";
	public static final String JDBC = "JDBC";
	public static final String JMX = "JMX";
	public static final String JMXSERVER = "JMXSERVER";
	public static final String JMXAPPS = "JMXAPPS";
	
	
	
	public static Monitor createMonitor(String line) {
		String[] elem = line.split(";");
		if (elem[0].startsWith("#")) {
			return null;
		}
		if (elem[0].equalsIgnoreCase(HTTPGET)){
			return  new HttpGetMonitor(elem[1], elem[2], Integer.parseInt(elem[3]));
		}
		if (elem[0].equalsIgnoreCase(HEADER)){
			return  new HeaderMonitor(elem[1], elem[2], Integer.parseInt(elem[3]));
		}
		if (elem[0].equalsIgnoreCase(JDBC)){
			return new JdbcMonitor(elem[1], elem[2], Integer.parseInt(elem[3]));
		}
		if(elem[0].equalsIgnoreCase(JMXSERVER)){
			return new JmxServerInstanceMonitor(elem[1], elem[2], Integer.parseInt(elem[3]));
		}
		if(elem[0].equalsIgnoreCase(JMXAPPS)){
			return new JmxApplicationMonitor(elem[1], elem[2], Integer.parseInt(elem[3]));
		}
		return null;
	}

}
