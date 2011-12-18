package com.visma.autosysmonitor.da;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.visma.autosysmonitor.domain.SystemInfo;

@Repository
public class SystemInfoUpdater {

	List<SystemInfo> data = new ArrayList<SystemInfo>();

	
	public SystemInfoUpdater(){
		data.add(new SystemInfo("VG", "http://vg.no", 500));
		data.add(new SystemInfo("Dagbladet", "http://db.no", 500));
		data.add(new SystemInfo("Aftenposten", "http://aftenposten.no", 500));
		data.add(new SystemInfo("Nettavisen", "http://nettavisen.no", 500));
	}
	
	public List<SystemInfo> getAll() {
		return java.util.Collections.unmodifiableList(data);
	}
	
	public void addSystemInfo(SystemInfo system ){
		data.add(system);
	}
	
	public void updateAll(){
		for (SystemInfo system : data) {
			system.update();
		}
		
	}

}