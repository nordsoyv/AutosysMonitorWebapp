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

import com.visma.autosysmonitor.domain.SystemInfo;
import com.visma.autosysmonitor.domain.SystemInfoDTO;

@Repository
public class SystemInfoUpdater implements ApplicationContextAware {

	private static final String SYSTEM_FILE = "classpath:systems.txt";
	List<SystemInfo> data = new ArrayList<SystemInfo>();
	ApplicationContext ctx = null;

	public SystemInfoUpdater() {

	}

	public List<SystemInfo> getAll() {
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
				String[] elem = line.split(";");
				if (elem[0].startsWith("#")) {
					continue;
				}
				SystemInfo sys = new SystemInfo(elem[0], elem[1], Integer.parseInt(elem[2]));
				addSystemInfo(sys);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addSystemInfo(SystemInfo system) {
		data.add(system);
	}

	public SystemInfo updateSystem(SystemInfoDTO sys) {
		SystemInfo systemToUpdate = getSystem(sys);
		if (systemToUpdate != null) {
			systemToUpdate.update();
			return systemToUpdate;
		}
		return null;
	}

	public void updateAll() {
		for (SystemInfo system : data) {
			system.update();
		}
	}

	public SystemInfo getSystem(SystemInfoDTO sys) {
		for (SystemInfo system : data) {
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