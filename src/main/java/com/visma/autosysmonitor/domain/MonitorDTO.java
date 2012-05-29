package com.visma.autosysmonitor.domain;

import java.util.HashMap;
import java.util.Map;


public class MonitorDTO {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;
	private String type;
	private Map<String, Object> data;

	public MonitorDTO(String type,String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.type= type;
		this.setData(new HashMap<String, Object>());
	}

	public MonitorDTO() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
		type= "";
		this.setData(new HashMap<String, Object>());
	}

	public MonitorDTO(Monitor sys){
		this.name = sys.getName();
		this.url = sys.getUrl();
		this.timeout = sys.getTimeout();
		this.alive = sys.isAlive();
		this.ping = sys.getPing();
		this.type = sys.getType();
		this.setData(sys.getData());
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
