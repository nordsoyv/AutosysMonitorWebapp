package com.visma.autosysmonitor.da;

import com.visma.autosysmonitor.domain.HttpGetMonitor;
import com.visma.autosysmonitor.domain.Monitor;

public class MonitorFactory {

	public static Monitor createMonitor(String line) {
		String[] elem = line.split(";");
		if (elem[0].startsWith("#")) {
			return null;
		}
		
		return  new HttpGetMonitor(elem[0], elem[1], Integer.parseInt(elem[2]));
	}

}
