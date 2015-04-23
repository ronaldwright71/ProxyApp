package com.google.gwt.proxyapp.client;

import com.google.gwt.user.client.ui.FlexTable;

public class HostingHandlerData {
	private String clientHtmlName;
	private FlexTable flexTable;

	public HostingHandlerData(String clientHtmlName, FlexTable flexTable) {
		this.clientHtmlName = clientHtmlName;
		flexTable.setStyleName("cw-FlexTable");
		flexTable.setHTML(0, 0, "Defaults");
		flexTable.getFlexCellFormatter().setStyleName(0, 0, "vp-htmlpanel2");
		this.flexTable = flexTable;
	}

	public String getClientHtmlName() {
		return clientHtmlName;
	}

	public void setClientHtmlName(String clientHtmlName) {
		this.clientHtmlName = clientHtmlName;
	}

	public FlexTable getFlexTable() {
		return flexTable;
	}

	public void setFlexTable(FlexTable flexTable) {
		this.flexTable = flexTable;
	}

	public void setFlexTable(String[] result) {
		
	}
	
}