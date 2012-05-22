package com.visma.autosysmonitor.domain;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SystemDTO {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;

	public SystemDTO(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
	}

	public SystemDTO() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public SystemDTO(HttpGetSystem sys){
		this.name = sys.getName();
		this.url = sys.getUrl();
		this.timeout = sys.getTimeout();
		this.alive = sys.isAlive();
		this.ping = sys.getPing();
		
		
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
