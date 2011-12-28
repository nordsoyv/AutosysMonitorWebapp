package com.visma.autosysmonitor.da;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.visma.autosysmonitor.domain.SystemInfo;

@Repository
public class SystemInfoUpdater {

	List<SystemInfo> data = new ArrayList<SystemInfo>();

	public SystemInfoUpdater() {
		data.add(new SystemInfo("VG", "http://vg.no", 500));
		// data.add(new SystemInfo("Dagbladet", "http://db.no", 500));
		// data.add(new SystemInfo("Aftenposten", "http://aftenposten.no",
		// 500));
		// data.add(new SystemInfo("Nettavisen", "http://nettavisen.no", 500));
		data.add(new SystemInfo("tull", "http://nasdfasdfn.no", 200));
		data.add(new SystemInfo("localhost", "http://localhost:8080/AutosysMonitor/", 200));
	}

	public List<SystemInfo> getAll() {
		return java.util.Collections.unmodifiableList(data);
	}

	public void readFromFile(String fileName) {
		String line;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
			while ((line = in.readLine()) != null) {
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
				in.close();
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
	

}