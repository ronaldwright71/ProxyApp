package com.google.gwt.proxyapp.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.proxyapp.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ProxyApp implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
//	private final GwtHostingServiceAsync hostingService = GWT.create(GwtHostingService.class);
	private final GwtPageServiceAsync hp = GWT.create(GwtPageService.class);
	
	final Label msg1 = new Label();
    final Button sendButton = new Button("Send");
	final TextBox nameField = new TextBox();
	final Hidden nameHidden = new Hidden();
	final Label errorLabel = new Label();
    final Label afaIk = new Label();
    final Button hostingButton = new Button("Host");
	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button("Close");
    final Label textToServerLabel = new Label();
	final HTML serverResponseLabel = new HTML();
	FlexTable dfaultFlextable = new FlexTable();

	private String vIp;
		
	public String getvIp() {
		return vIp;
	}

	public void setvIp(String vIp) {
		this.vIp = vIp;
	}

	/**
	 * This is the entry point method.
	 */
    	private static final String[] DEFAULT_CONTAINER_LIST = {
    		"nameFieldContainer",
    		"sendButtonContainer",
    		"errorLabelContainer",
    		"afaIkContainer",
    		"hostingButtonContainer", "msg1container"
    		};
    	
	public void onModuleLoad() {
		String nameGWT = ("GWT User");
		nameField.setText(nameGWT);
		// Add style names to widgets
		sendButton.addStyleName("sendButton");
		sendButton.setWidth("46px");
		hostingButton.setTitle("View information for listed Hosts");
		hostingButton.setWidth("46px");
		msg1.setText("Please enter your host name:");
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("msg1container").add(msg1);
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("afaIkContainer").add(afaIk);
        //set afaIk label text
        clientAfaikIp();
        RootPanel.get("hostingButtonContainer").add(hostingButton);
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();
		
		// Create the popup dialog box
		defaultDlg();
		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText(""); 
				
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true); 
							}
						});
			}
		}
		

		
		class HostingHandler implements ClickHandler {
			/**
			 * Fired when the user clicks on the hostingButton.
			 */
			public void onClick(ClickEvent event) {
				sendHostingToServer();
			}

			public HostingHandlerData data = new HostingHandlerData("One",
				new FlexTable());
		
			private void sendHostingToServer() {
				//Clear the RootPanel
				String textToServer = nameField.getText();
				
				for (String container : DEFAULT_CONTAINER_LIST ){
				//	if(!container.equals("sendButtonContainer"))
						RootPanel.get(container).clear();
				}
				
				final VerticalPanel vPanel = new VerticalPanel();								
				
				hp.doHosting(textToServer, vIp, 
						new AsyncCallback<String []>() {
							@Override
							public void onFailure(Throwable caught) {vPanel.add(new HTML("Oh Snap"));}
							public void onSuccess(String [] result) {
								//body html
								data.setClientHtmlName(result[0]);
								//default client list as string
								String dclientstr = result[1].toString();
								
								List<String> dclients = Arrays.asList((dclientstr).split("\\s*,\\s*"));
								int cnt = 1;
								for (String dclient : dclients){
									data.getFlexTable().setHTML(cnt, 0, dclient);
									cnt++;
								}
								data.getFlexTable().setHTML(0, 1, data.getClientHtmlName());
								
								if (result[2].length() >= 4) {
									@SuppressWarnings("rawtypes")
									CwDataGrid dataGrid = new CwDataGrid(result);
									VerticalPanel dataPanel = new VerticalPanel();
									dataPanel.setCellVerticalAlignment(dataGrid, HasVerticalAlignment.ALIGN_BOTTOM);
									dataPanel.setSize("780px", "405px");
									dataGrid.setSize("775px", "400px");
									dataPanel.add(dataGrid);
									RootPanel.get("hostList").add(dataPanel);
								}else {
									int numRows = data.getFlexTable().getRowCount();
									data.getFlexTable().getFlexCellFormatter().setRowSpan(0, 1, numRows);
									data.getFlexTable().getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
									vPanel.add(data.getFlexTable()); 
									RootPanel.get("hostList").add(vPanel);									
								}					
				}});

/*				hostingService.doHosting(textToServer, vIp,
						new AsyncCallback<String>() {
							@Override
							public void onFailure(Throwable caught) {
								msg1
								.addStyleName("serverResponseLabelError");
								msg1.setText(SERVER_ERROR);
								vPanel.add(msg1);
							}
							@Override
							public void onSuccess(String result) {
								vPanel.add(new HTML(result));
							}					
				}); */ 
			}

		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		HostingHandler hostinghandler = new HostingHandler();
		sendButton.addClickHandler(handler);
		hostingButton.addClickHandler(hostinghandler);
		nameField.addKeyUpHandler(handler);

	}

	private void defaultDlg() {

		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});
	}

	private void clientAfaikIp() {
		greetingService.getClientIpAddress( 
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						afaIk.addStyleName("serverResponseLabelError");
						afaIk.setText("Unable to determin your IP address, your connection may be firewalled");
					}
					public void onSuccess(String result) {
						afaIk.setWordWrap(true);
						afaIk.setWidth("180px");
						afaIk.setText("AFAIK your current IP address is " + result);
						setvIp(result);
					}
				});
	}
	
/*	private void doFlexTable(String dclients[], String clientHtmlName){
		FlexTable flexTable = new FlexTable();
		flexTable.setText(0, 0, "Defaults");
		int cnt = 1;
		for (String dclient : dclients ){
			flexTable.setText(cnt, 0, dclient);
			cnt++;
		}
		flexTable.setHTML(0, 1, clientHtmlName);
		int numRows = flexTable.getRowCount();
		flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
		
		} */
	
}
