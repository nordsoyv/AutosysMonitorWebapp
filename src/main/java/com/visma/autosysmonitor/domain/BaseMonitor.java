package com.visma.autosysmonitor.domain;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMonitor implements Monitor {

	protected String name;
	protected String url;
	protected boolean alive;
	protected int ping;
	protected int timeout;
	protected String type;
	protected Map<String, Object> data;

	{
		data = new HashMap<String, Object>();
	}

	public BaseMonitor() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public int getPing() {
		return ping;
	}

	@Override
	public void setPing(int ping) {
		this.ping = ping;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public MonitorDTO toMonitorDTO() {
		MonitorDTO sys = new MonitorDTO(this);
		return sys;
	}

	@Override
	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public void setData(Map<String, Object> map) {
		data = map;

	}

}