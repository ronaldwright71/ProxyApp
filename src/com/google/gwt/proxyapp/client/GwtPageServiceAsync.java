package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtPageServiceAsync {
	void doHosting(String clname, String vIp, AsyncCallback<String[]> callback);

	void getDfaultClients(AsyncCallback<String[]> asyncCallback);
}
