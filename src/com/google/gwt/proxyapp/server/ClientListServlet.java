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

@SuppressWarnings("serial")
public class ClientListServlet extends HttpServlet{
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
	    String clientName = req.getParameter("clientName");
	    String val = "";
	    
	    Key clientKey = KeyFactory.createKey("Client", clientName);

		// view of the Clients belonging to Default Client List.
	    Query query = new Query("Clients", clientKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> clients = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	    if (!clients.isEmpty()) {
	    	for (Entity client : clients) {
				val = client.getParent().getName() + "=" + client.getProperty("ipaddress").toString();
	    	}
	    }
	    
	    PrintWriter writer = resp.getWriter();
	    writer.append(val);	   
	    
	  }
}
