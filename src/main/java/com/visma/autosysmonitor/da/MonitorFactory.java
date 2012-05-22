package com.visma.autosysmonitor.da;

import com.visma.autosysmonitor.domain.HeaderMonitor;
import com.visma.autosysmonitor.domain.HttpGetMonitor;
import com.visma.autosysmonitor.domain.Monitor;

public class MonitorFactory {

	private static final String HTTPGET = "HTTP-GET";
	private static final String HEADER = "HEADER";
	
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
		return null;
	}

}
