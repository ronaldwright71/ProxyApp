package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtHostingServiceAsync {

	void doHosting(String clname, String vIp, AsyncCallback<String> callback);

}