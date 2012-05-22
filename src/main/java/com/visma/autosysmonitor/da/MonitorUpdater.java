package com.visma.autosysmonitor.da;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.visma.autosysmonitor.domain.HttpGetMonitor;
import com.visma.autosysmonitor.domain.Monitor;
import com.visma.autosysmonitor.domain.MonitorDTO;

@Repository
public class MonitorUpdater implements ApplicationContextAware {

	private static final String SYSTEM_FILE = "classpath:systems.txt";
	List<Monitor> data = new ArrayList<Monitor>();
	ApplicationContext ctx = null;

	public MonitorUpdater() {

	}

	public List<Monitor> getAll() {
		return java.util.Collections.unmodifiableList(data);
	}

	public void readFromFile() {
		readFromFile(SYSTEM_FILE);
	}

	public void readFromFile(String fileName) {
		Resource r = ctx.getResource(fileName);
		BufferedReader br = null;
		String line;
		try {
			InputStream is = r.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				addSystemInfo(MonitorFactory.createMonitor(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addSystemInfo(Monitor system) {
		if (system != null)
			data.add(system);
	}

	public Monitor updateSystem(MonitorDTO sys) {
		Monitor systemToUpdate = getSystem(sys);
		if (systemToUpdate != null) {
			systemToUpdate.update();
			return systemToUpdate;
		}
		return null;
	}

	public void updateAll() {
		for (Monitor system : data) {
			system.update();
		}
	}

	public Monitor getSystem(MonitorDTO sys) {
		for (Monitor system : data) {
			if (system.getName().equals(sys.getName()))
				return system;
		}
		return null;
	}

	public void clear() {
		data.clear();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

}