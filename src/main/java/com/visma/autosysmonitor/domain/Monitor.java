package com.visma.autosysmonitor.domain;

import java.util.Map;

public interface Monitor {

	public abstract MonitorDTO toMonitorDTO();

	public abstract void update();

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getUrl();

	public abstract void setUrl(String url);

	public abstract boolean isAlive();

	public abstract void setAlive(boolean alive);

	public abstract int getPing();

	public abstract void setPing(int ping);

	public abstract int getTimeout();

	public abstract void setTimeout(int timeout);
	
	public abstract String getType();
	
	public abstract void setType(String type);

	public abstract Map<String, Object> getData();
	
	public abstract void setData(Map<String, Object> map);
	
}