package com.google.gwt.proxyapp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.proxyapp.shared.FieldVerifier;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class GwtHostingServlet extends HttpServlet {
	
		public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

		public String client = "GWT User";
	@Override
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");

	   // Print a simple HTML page including a <script> tag referencing your GWT module as the response
	   PrintWriter writer = resp.getWriter();

	   doHosting(writer, req, resp);
	   
	  }
	 
	public void doHosting(PrintWriter writer, HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    String clientName = req.getParameter("clientName");
	    setClient(req.getParameter("clientName"));
    	if (!FieldVerifier.isValidName(clientName)) clientName = "FreeWilly";
		String addClient = req.getParameter("addClient"); 
		String ip = getClientIpAddress(req);
		String user = req.getParameter("clientName") + "user";
		Date date = new Date();
		
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key clientKey = KeyFactory.createKey("Client", clientName);
	    // Run an ancestor query to ensure we see the most up-to-date
	    // view of the Greetings belonging to the selected Guestbook.
		
	    Query query = new Query("Clients", clientKey).addSort("date", Query.SortDirection.DESCENDING);
	    List<Entity> clients = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

	    if (clients.isEmpty()) {
	    	if (FieldVerifier.isValidName(addClient) && addClient.matches("true")) {
	    		clientName = "<b>Loading New Client to list of NX hosts....</b>";

	    		Entity newclient = new Entity("Clients", clientKey);
	            newclient.setProperty("user", user);
	            newclient.setProperty("date", date);
	            newclient.setProperty("ipaddress", ip);
	            datastore.put(newclient);
	            clientName = "</p<p>Client Name: &lt<b>" + client + "</b>&gt is now added to the list of valid Clients</p>";
	    	} else {
	    	//put clientName
	    	clientName = "add client := " + addClient +" Client Name: &lt<b>" + clientName + "</b>&gt is not in the list of valid Clients</p>" +
	    	"<p>Send an email to <a href=\"mailto:ronald.wright71@gmail.com\">ronald.wright71@gmail.com</a> for more information and/or to be added to the list";
	    	}
	    } else {
	    	//return results
	    	clientName = "Client name found for: " + clientName;
	    }
		
		writer.append("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">")
			   .append("<link type=\"text/css\" rel=\"stylesheet\" href=\"/ProxyApp.css\">")
		       .append("<script type=\"text/javascript\" src=\"/proxyapp/proxyapp.nocache.js\"></script>")
		       .append("</head><body><table align=\"center\" width=390px><tr><td><div><p><h1>Hello, " + client)
		       .append("!</h1></p><p>" + ip + " " + clientName + "</p><p><form method=\"post\" action=\"/\">")
		       .append("<input class=\".sendButton\" type=\"submit\" value=\"Return\"></input></form></p></td></tr></table></div></body></html>");
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