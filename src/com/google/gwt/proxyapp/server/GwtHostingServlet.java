package com.google.gwt.proxyapp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.proxyapp.shared.FieldVerifier;
import com.google.gwt.user.client.Window;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings({ "serial", "unused" })
public class GwtHostingServlet extends HttpServlet {
	
		public String getClient() {
		return curClient;
	}

	public void setClient(String client) {
		this.curClient = client;
	}

		public String curClient = "GWT User";
	@Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");

	   // Print a simple HTML page including a <script> tag referencing your GWT module as the response
	   PrintWriter writer = resp.getWriter();

	   doHosting(writer, req, resp);
	   
	  }
	 
	public void doHosting(PrintWriter writer, HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    setClient(req.getParameter("clientName"));
	    String clientName = getClient();
    	if (!FieldVerifier.isValidName(clientName)) {clientName = "FreeWilly"; setClient(clientName);}
		String ip = getClientIpAddress(req);
		String user = req.getParameter("clientName") + "user";
		Date date = new Date();
		
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    Key clientKey = KeyFactory.createKey("Client", clientName);
	    Key dclientKey = KeyFactory.createKey("Dclients", clientName);
	    
	    Query q;
		List<Entity> dclients;
		
		String lddclient = ldclientList(datastore);
	    
//Testing db reset    	deleteClientRecords(datastore);
		
	    q = new Query("Dclients", dclientKey);
	    dclients = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
	    // Run an ancestor query to ensure we see the most up-to-date
	    // view of the Clients belonging to Default Client List.
	    Query query = new Query("Clients", clientKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> clients = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	    if (clients.isEmpty()) {
	    	
	    	if (clients.isEmpty() && !dclients.isEmpty()) {
	    		if (dclients.get(0).getProperty("updVal").equals(true)){
	    			clientName = "<b>Loading New Client to list of NX hosts....</b>";
	    			Entity newclient = new Entity("Clients", clientKey);
	    			newclient.setProperty("user", user);
	    			newclient.setProperty("date", date);
	    			newclient.setProperty("ipaddress", ip);
	    			datastore.put(newclient);
	    			clientName = "</p<p>Client Name: &lt<b>" + curClient + "</b>&gt is now added to the list of valid Clients</p>";
	    		}
	    	} else {
	    	//clientName not found
	    	clientName = "Client List:=(" + lddclient + ") " + ip + " Client Name: &lt<b>" + curClient + 
	    			"</b>&gt is not in the list of valid Clients</p>" +
	    			"<p>Send an email to <a href=\"mailto:ronald.wright71@gmail.com\">ronald.wright71@gmail.com</a> " + 
	    			"for more information and/or to be added to the list";
	    	}
	    } else {
	    	
	    	clientName = "Client name found for: " + curClient + "</p>" +  
	    			"<p>Retrivieng information for Client</p>";
	    	clientName = clientName + "<p>Comparing to previous</p>";	    	
	    	//should update?	    
	    	String clientlist = "";
			for (Entity client : clients) {
				String oldip= client.getProperty("ipaddress").toString();
				if (!oldip.equals(ip)){
	    			clientName = clientName + "<p>Updating ip Address from " + oldip + " to " + ip + "</p>";
	    			client.setProperty("ipaddress", ip);
					datastore.put(client);
	    		} else {
	    	        clientName = clientName + "<p>Records for Client " + curClient + " are as follows</p>";
	        		clientlist = clientlist + "<p>" + client.getProperties().toString() + "</p>";
//Testing  			client.setProperty("ipaddress", "192.168.0.29");
//	    			datastore.put(client);
	    		}
	    	}

    		clientName = clientName + clientlist;
	    }
		
		writer.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">")
			   .append("<link type=\"text/css\" rel=\"stylesheet\" href=\"/ProxyApp.css\">")
		       .append("<script type=\"text/javascript\" src=\"/proxyapp/proxyapp.nocache.js\"></script>")
		       .append("</head><body><table align=\"center\" width=390px><tr><td><div><p><h1>Hello, " + curClient)
		       .append("!</h1></p><p>" + clientName + "</p><p><form method=\"post\" action=\"/\">")
		       .append("<input class=\".sendButton\" type=\"submit\" value=\"Return\"></input></form></p></td></tr>" + 
		    		   "</table></div></body></html>");
	}

	public String ldclientList(DatastoreService datastore) {
		Query q = new Query("Dclients");
	    List<Entity> dclients = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
//	    Check to see if default client list has been loaded 
	    if (dclients.isEmpty()){	   
	    	//No Client List loading defaults
			addDclients(datastore);
	    }
	    String lddclient = "";
	    int cnt = 0;
	    for (Entity dclient : dclients){
	    	if (cnt == 0){lddclient = dclient.getKey().getName(); cnt++;} else
	    	{lddclient = lddclient + ", " + dclient.getKey().getName();}
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
	
	private static final String[] HEADERS_TO_TRY = { 
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };
	
	public static String getClientIpAddress(HttpServletRequest request) {
	    for (String header : HEADERS_TO_TRY) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }	    
	    return request.getRemoteAddr();
	}

}