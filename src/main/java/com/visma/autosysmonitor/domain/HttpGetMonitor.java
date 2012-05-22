package com.visma.autosysmonitor.domain;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpGetMonitor {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;

	public HttpGetMonitor(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
	}

	public HttpGetMonitor() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public HttpGetMonitor(SystemDTO to) {
		this.name = to.getName();
		this.url = to.getUrl();
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();
	}

	public SystemDTO toSystemInfoDTO() {
		SystemDTO sys = new SystemDTO(this);
		return sys;
	}

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
			e.printStackTrace();
		} catch (IOException e) {
			alive = false;
			e.printStackTrace();
		} catch (Exception e) {
			alive = false;
			e.printStackTrace();
		} finally {
			httpget.abort();
			ping = (int) (System.currentTimeMillis() - start);
		}

		return;
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

}
