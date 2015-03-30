package com.google.gwt.proxyapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.proxyapp.client.GwtHostingService;

@SuppressWarnings("serial")
public class GwtHostingServiceImpl extends RemoteServiceServlet implements

	GwtHostingService {

	@Override
	public String doHosting(String name) throws IllegalArgumentException {
		return null;
	}

}
