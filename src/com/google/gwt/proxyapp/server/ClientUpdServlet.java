package com.google.gwt.proxyapp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.proxyapp.shared.IpVerifier;

@SuppressWarnings("serial")
public class ClientUpdServlet extends HttpServlet {

	public IpVerifier data = new IpVerifier();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		data.setCurClient(req);
		
		String ip = data.getCurClient();
		//ip = "192.168.0.29";
	    String clientName = req.getParameter("clientName");
	    
	    String val = "";
	    Key clientKey = KeyFactory.createKey("Client", clientName);
	    
		//Update Clients belonging to Default Client List.
	    Query query = new Query("Clients", clientKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> clients = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	    if (!clients.isEmpty()) {
	    	for (Entity client : clients) {
				String oldip= client.getProperty("ipaddress").toString();
				if (!oldip.equals(ip)){
	    			val = "<p>Updating <b>" + clientName + "</b> ip Address from " + oldip + " to " + ip + "</p>";
	    			client.setProperty("ipaddress", ip);
					datastore.put(client);
	    		} else {
	    			val = "<p>" + clientName + "=" +  client.getProperty("ipaddress").toString() + "</p>";
	    		}
			}
	    }
	    PrintWriter writer = resp.getWriter();
	    writer.append(val);
	}
}
