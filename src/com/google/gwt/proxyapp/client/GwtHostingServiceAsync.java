package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtHostingServiceAsync {

	void doHosting(String name, AsyncCallback<String> callback);

}