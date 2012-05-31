package com.visma.autosysmonitor.domain;

import com.visma.autosysmonitor.da.MonitorFactory;

public class HeaderMonitor extends BaseMonitor  {
	
	{
		type = MonitorFactory.HEADER;
	}
	
	public HeaderMonitor(String name, String url, int timeout) {
		this.name = name;
		setUrl(url);
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
	}

	public HeaderMonitor() {
		this.name = "";
		setUrl("");
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public HeaderMonitor(MonitorDTO to) {
		this.name = to.getName();
		setUrl(to.getUrl());
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
	}


	/* (non-Javadoc)
	 * @see com.visma.autosysmonitor.domain.Monitor#update()
	 */
	@Override
	public void update() {
		return;
	}


}
