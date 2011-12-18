package com.visma.autosysmonitor.domain;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SystemInfo {
	private String name;
	private String url;
	private boolean alive;
	private int ping;
	private int timeout;

	public SystemInfo(String name, String url, int timeout) {
		this.name = name;
		this.url = url;
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
	}

	public SystemInfo() {
		this.name = "";
		this.url = "";
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public void update() {

		long start = System.currentTimeMillis();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(this.url);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			alive=true;
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			httpget.abort();
			alive=false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			httpget.abort();	
			alive=false;
			e.printStackTrace();
		}finally{
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
