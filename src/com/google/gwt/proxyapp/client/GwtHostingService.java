package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface GwtHostingService  extends RemoteService {
	String doHosting(String name) throws IllegalArgumentException;
	
}