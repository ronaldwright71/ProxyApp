package com.google.gwt.proxyapp.server;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.proxyapp.client.GwtHostingService;
import com.google.gwt.proxyapp.shared.FieldVerifier;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class GwtHostingServiceImpl extends RemoteServiceServlet implements
	GwtHostingService {

	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
		this.curClient = clientName;
	}
	private String clientName;
	private String curClient;
	
	public String getCurClient() {
		return curClient;
	}
	
	public String doHosting(String clname, String vIp) throws IllegalArgumentException {
    	if (!FieldVerifier.isValidName(clname)) {
    		setClientName("FreeWilly"); 
    	} else {
    		setClientName(clname);
    	}

		String ip = vIp;
		
		String user = getCurClient() + "user";
		Date date = new Date();
		
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    Key clientKey = KeyFactory.createKey("Client", clientName);
	    Key dclientKey = KeyFactory.createKey("Dclients", clientName);
	    
		String lddclient = ldclientList(datastore);
	    //Testing db reset    	deleteClientRecords(datastore);		

		Query q = new Query("Dclients", dclientKey);
	    List<Entity> dclients = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
	    if(!dclients.isEmpty()) {
	    	if( dclients.get(0).getKey().getName().equals("roadrunner")) 
	    		dclients.get(0).setProperty("updVal", true);
	    	}
	    // Run an ancestor query to ensure we see the most up-to-date
	    // view of the Clients belonging to Default Client List.
	    Query query = new Query("Clients", clientKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> clients = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	    if (clients.isEmpty()) {
	    	if (!dclients.isEmpty()) {
	    		if (dclients.get(0).getProperty("updVal").equals(true)){
	    			clientName = "<b>Loading New Client to list of NX hosts....</b>";
	    			Entity newclient = new Entity("Clients", clientKey);
	    			newclient.setProperty("user", user);
	    			newclient.setProperty("date", date);
	    			newclient.setProperty("ipaddress", ip);
	    			datastore.put(newclient);
	    			clientName = "<p>Client Name: &lt<b>" + curClient + "</b>&gt is now added to the list of valid Clients</p>";
	    		} else {
	    			clientName = "<p>No records have been added for this Host " + curClient + ", </p><p>" + 
	    					"Contact <a href=\"ronaldWright71@gmail.com\">ronald.wright71@gmail.com</a>  to setup this host</p>";
	    		}
	    	} else {
	    	//clientName not found
	    	clientName = "<p>Client List:=(" + lddclient + ") </p><p>Client Name: &lt&lt<b>" + curClient + 
	    			"</b>&gt " + ip + "&gt is not in the list of valid Clients</p>" +
	    			"<p>Send an email to <a href=\"mailto:ronald.wright71@gmail.com\">ronald.wright71@gmail.com</a> " + 
	    			"for more information and/or to be added to the list";
	    	}
	    } else {
	    	
	    	clientName = "<p>Client name found for: " + curClient + "</p>" +  
	    			"<p>Retrivieng information for Client</p>";	    
			for (Entity client : clients) {
				clientName = clientName + "<p>Records for Client " + curClient + " are as follows</p>" +
				"<p>" + client.getProperties().toString() + "</p><p><a href=\"http://" + client.getProperty("ipaddress") + ":8080/nxappletTest\"><image src=\"/hosting/img/noMachine.gif\"></a>";
	    	}
	    } 
		return clientName; 
	}
	
	private FlexTable dfaultFlextable = new FlexTable();
	
	public String ldclientList(DatastoreService datastore) {
		Query q = new Query("Dclients");
	    List<Entity> dclients = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
	    //Check to see if default client list has been loaded 
	    if (dclients.isEmpty()){	   
	    	//No Client List loading defaults
			addDclients(datastore);
	    }
	    
	    String lddclient = "";
	    
	    int cnt = 0;
	    setDfaultFlextable(dfaultFlextable,cnt, 0,"Defaults");
	    cnt++;
	    
	    for (Entity dclient : dclients){
	    	if (cnt == 1){lddclient = dclient.getKey().getName();} else
	    	{lddclient = lddclient + ", " + dclient.getKey().getName();}
	    
	    	setDfaultFlextable(dfaultFlextable,cnt, 0, dclient.getKey().getName());
	    	cnt++;
	    	//Normally commented, for testing delete all Kinds of type Dclients 
	    	//datastore.delete(dclient.getKey());	    	
	    }
	    
		return lddclient;
	}

	public void deleteClientRecords(DatastoreService datastore) {
		//delete all client records
    	Query delclientRecs = new Query ("Clients");
    	PreparedQuery delList = datastore.prepare(delclientRecs);
		for (Entity delclientRec : delList.asIterable()){
    		datastore.delete(delclientRec.getKey());	
    	}
	}
	
	private static final String[] DEFAULT_CLIENT_LIST = {
		"roadrunner",
		"CarlyJoe",
		"Bubba",
		"Crystal",
		"Opie" };
	
	public static void addDclients(DatastoreService datastore){
		for (String dclient : DEFAULT_CLIENT_LIST ){
			Entity newclient = new Entity("Dclients", dclient);
			newclient.setProperty("updVal", false);
			datastore.put(newclient);
		}
	}

	public FlexTable getDfaultFlextable() {
		return dfaultFlextable;
	}

	public void setDfaultFlextable(FlexTable dfaultFlextable, int r, int c, String dclientStr) {
		dfaultFlextable.setText(r, c, dclientStr);
		this.dfaultFlextable = dfaultFlextable;
	}

}
