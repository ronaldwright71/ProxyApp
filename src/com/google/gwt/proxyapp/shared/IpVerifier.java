package com.google.gwt.proxyapp.shared;

import javax.servlet.http.HttpServletRequest;

public class IpVerifier {
	private String curClient;

	public IpVerifier() {

	}

	public String getCurClient() {
		return curClient;
	}

	public void setCurClient(HttpServletRequest client) {
		String ip = "192.168.0.1";
		
	    for (String header : HEADERS_TO_TRY) {
	        ip = client.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	        	this.curClient = ip;
	        	return;
	        }
	    }
		this.curClient = client.getRemoteAddr();
	}
	
	private static final String[] HEADERS_TO_TRY = { 
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };
	
}