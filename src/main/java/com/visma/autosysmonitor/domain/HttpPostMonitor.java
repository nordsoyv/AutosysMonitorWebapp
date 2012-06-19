package com.visma.autosysmonitor.domain;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.visma.autosysmonitor.da.MonitorFactory;

public class HttpPostMonitor extends BaseMonitor {

	{
		type = MonitorFactory.HTTPPOST;
	}

	private ApplicationContext ctx;
	private String reqFile;
	private String responseFile;

	public HttpPostMonitor(String name, String url, String reqFile, String responseFile, int timeout, ApplicationContext ctx) {
		this.name = name;
		setUrl(url);
		this.timeout = timeout;
		this.alive = false;
		this.ping = 0;
		this.ctx = ctx;
		this.reqFile = reqFile;
		this.responseFile = responseFile;

	}

	public HttpPostMonitor() {
		this.name = "";
		setUrl("");
		this.timeout = 0;
		this.alive = false;
		this.ping = 0;
	}

	public HttpPostMonitor(MonitorDTO to) {
		this.name = to.getName();
		setUrl(to.getUrl());
		this.timeout = to.getTimeout();
		this.alive = to.isAlive();
		this.ping = to.getPing();

	}

	private InputStream getResponseFileInputStream() {
		try {
			return ctx.getResource("classpath:" + responseFile).getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private byte[] readBytesFromFile(String filename) {
		Resource r = ctx.getResource(filename);
		InputStream is = null;
		try {
			is = r.getInputStream();
			int length = is.available();
			byte[] bytes = new byte[length];
			is.read(bytes);
			return bytes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	private byte[] readRequestFromFile() {
		return readBytesFromFile("classpath:" + reqFile);
	}

	
	private boolean checkResponse(InputStream responseStream) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			dbf.setNamespaceAware(true);
			dbf.setCoalescing(true);
			dbf.setIgnoringElementContentWhitespace(true);
			dbf.setIgnoringComments(true);
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			Document responseDoc = db.parse(responseStream);
			responseDoc.normalizeDocument();
			BufferedReader br =  new BufferedReader(new InputStreamReader(getResponseFileInputStream()));

			String line;
			boolean isEqual = true;
			while ((line = br.readLine()) != null) {
				String[] elem = line.split(":");
				String key = elem[0];
				String value = elem[1];
				Node target =  findNode(responseDoc, key);
				String content = target.getTextContent();
				if(!content.equals(value)){
					isEqual = false;
				}
				
			}

			return isEqual;
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private Node findNode(Node doc, String name){
		if(doc.getNodeName().equals(name))
			return doc;
		NodeList childNodes = doc.getChildNodes();
		for(int i = 0 ; i< childNodes.getLength(); i++){
			Node res = findNode(childNodes.item(i), name);
			if(res != null)
				return res;
		}
		return null;
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
		HttpPost httpPost = new HttpPost(getUrl());

		byte[] soapReq = readRequestFromFile();
		HttpEntity entity = new ByteArrayEntity(soapReq);
		httpPost.setEntity(entity);
		HttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			byte[] responseBytes = new byte[responseEntity.getContent().available()];
			responseEntity.getContent().read(responseBytes);
			
			alive =checkResponse(new ByteArrayInputStream(responseBytes));
		} catch (ClientProtocolException e) {
			alive = false;
			// e.printStackTrace();
		} catch (IOException e) {
			alive = false;
			// e.printStackTrace();
		} catch (Exception e) {
			alive = false;
			// e.printStackTrace();
		} finally {
			httpPost.abort();
			ping = (int) (System.currentTimeMillis() - start);
		}

		return;
	}

}
