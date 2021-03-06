package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("hostlist")
public interface GwtHostingService  extends RemoteService {

	String doHosting(String clname, String vIp) throws IllegalArgumentException;
}