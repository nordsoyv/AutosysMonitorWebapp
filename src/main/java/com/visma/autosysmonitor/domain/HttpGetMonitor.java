package com.visma.autosysmonitor.domain;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.visma.autosysmonitor.da.MonitorFactory;

public class HttpGetMonitor implements Monitor {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;
	private String type;

	public HttpGetMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.HTTPGET;
	}

	public HttpGetMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
		this.type = MonitorFactory.HTTPGET;
	}

	public HttpGetMonitor(MonitorDTO to) {
		this.name = to.getName();
		this.url = to.getUrl();
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
		this.type = MonitorFactory.HTTPGET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#toSystemInfoDTO()
	 */
	@Override
	public MonitorDTO toMonitorDTO() {
		MonitorDTO sys = new MonitorDTO(this);
		return sys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#update()
	 */
	@Override
	public void update() {

		long start = System.currentTimeMillis();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, this.timeout);
		HttpConnectionParams.setSoTimeout(params, this.timeout);
		HttpGet httpget = new HttpGet(this.url);
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			alive = true;

		} catch (ClientProtocolException e) {
			alive = false;
			// e.printStackTrace();
		} catch (IOException e) {
			alive = false;
			// e.printStackTrace();
		} catch (Exception e) {
			alive = false;
			e.printStackTrace();
		} finally {
			httpget.abort();
			ping = (int) (System.currentTimeMillis() - start);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getUrl()
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#isAlive()
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setAlive(boolean)
	 */
	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getPing()
	 */
	@Override
	public int getPing() {
		return ping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setPing(int)
	 */
	@Override
	public void setPing(int ping) {
		this.ping = ping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#getTimeout()
	 */
	@Override
	public int getTimeout() {
		return timeout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.visma.autosysmonitor.domain.Monitor#setTimeout(int)
	 */
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

}
