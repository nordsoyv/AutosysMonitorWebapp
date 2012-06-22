package com.visma.autosysmonitor.domain;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.visma.autosysmonitor.controller.HomeController;
import com.visma.autosysmonitor.da.MonitorFactory;

public class HttpPostMonitor extends BaseMonitor {

	{
		type = MonitorFactory.HTTPPOST;
	}

	private static final Logger logger = LoggerFactory.getLogger(HttpPostMonitor.class);
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
		this.reqFile = "monitor/" + reqFile;
		this.responseFile = "monitor/" + responseFile;

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
	
	private Map<String,Object> readResponseFile(){
		try {
			ObjectMapper mapper = new ObjectMapper();

			@SuppressWarnings("unchecked")
			Map<String,Object> responseDate =  mapper.readValue(ctx.getResource("classpath:" + responseFile).getInputStream(), Map.class);
			return responseDate;
		} catch (JsonParseException e) {
			logger.debug("Error reading json file : " + responseFile);
		} catch (JsonMappingException e) {
			logger.debug("Error reading json file : " + responseFile);
		} catch (IOException e) {
			logger.debug("Error reading file : " + responseFile);
		}
		return new HashMap<String, Object>();
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

			boolean isEqual = true;
			Map<String,Object>  responseExpected = readResponseFile();
			for (String key : responseExpected.keySet() ) {
				Node target = findNode(responseDoc, key);
				String content = target.getTextContent();
				if (!content.equals(responseExpected.get(key))) {
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

	private Node findNode(Node doc, String name) {
		if (doc.getNodeName().equals(name))
			return doc;
		NodeList childNodes = doc.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node res = findNode(childNodes.item(i), name);
			if (res != null)
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
			long numbytes = responseEntity.getContentLength();
			InputStream content = responseEntity.getContent();
			byte[] responseBytes = new byte[(int) numbytes];
			int readBytes = content.read(responseBytes);
			if(readBytes < numbytes){
				while(readBytes< numbytes){
					readBytes += content.read( responseBytes, readBytes, (int) (numbytes-readBytes));
				}
			}
		

			alive = checkResponse(new ByteArrayInputStream(responseBytes));
			
			/* for debugging of xml response
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(responseBytes)));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			*/
			
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
