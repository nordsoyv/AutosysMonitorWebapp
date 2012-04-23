package com.visma.autosysmonitor.da;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.visma.autosysmonitor.domain.SystemInfo;

@Repository
public class SystemInfoUpdater implements ApplicationContextAware{

	List<SystemInfo> data = new ArrayList<SystemInfo>();
	ApplicationContext ctx = null;
	public SystemInfoUpdater() {

	}

	public List<SystemInfo> getAll() {
		return java.util.Collections.unmodifiableList(data);
	}

	public void readFromFile(String fileName) {
		Resource r =ctx.getResource("classpath:systems.txt");
		BufferedReader br = null;
		String line;
		try {
			InputStream is = r.getInputStream();
	        br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				String[] elem = line.split(";");
				if(elem[0].startsWith("#") ){
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

	public void updateAll() {
		for (SystemInfo system : data) {
			system.update();
		}
	}
	
	public void clear(){
		data.clear();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}
	

}