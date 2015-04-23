package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtpage")
public interface GwtPageService extends RemoteService {
	
	String[] doHosting(String clname, String vIp) throws IllegalArgumentException;

	String[] getDfaultClients()throws IllegalArgumentException;
}

