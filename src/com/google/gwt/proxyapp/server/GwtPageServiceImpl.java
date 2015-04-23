package com.google.gwt.proxyapp.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.proxyapp.client.GwtPageService;
import com.google.gwt.proxyapp.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.view.client.ProvidesKey;


@SuppressWarnings("serial")
public class GwtPageServiceImpl extends RemoteServiceServlet implements
GwtPageService {

public static class HostRecordNx implements Comparable<HostRecordNx> {
	public HostRecordNx(){
		
	}
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public final static ProvidesKey<HostRecordNx> KEY_PROVIDER = new ProvidesKey<HostRecordNx>() {
      @Override
      public Object getKey(HostRecordNx item) {
        return item == null ? null : item.getId();
       }
     };

	@Override
	public int compareTo(HostRecordNx o) {
		return (o == null || o.cwDataGridColumnHost == null) ? -1 : -o.cwDataGridColumnHost.compareTo(cwDataGridColumnHost);
	}
	
	@Override
    public boolean equals(Object o) {
      if (o instanceof HostRecordNx) {
        return id == ((HostRecordNx) o).id;
      }
      return false;
    }

	private static int nextId=0;
    private final int id = nextId;
	private String cwDataGridColumnHost;
	private String cwDataGridColumnIp;
	private String cwDataGridColumnDate;
	private String cwDataGridColumnUser;
	private String cwDataGridColumnImg;
	private String cwDataGridEmpty;

	/**
     * @return the unique ID
     */
    public int getId() {
      return this.id;
    }
    
	public String getCwDataGridColumnHost() {
		return cwDataGridColumnHost;
	}

	public void setCwDataGridColumnHost(String cwDataGridColumnHost) {
		this.cwDataGridColumnHost = cwDataGridColumnHost;
	}
	public String getCwDataGridColumnIp() {
		return cwDataGridColumnIp;
	}

	public void setCwDataGridColumnIp(String cwDataGridColumnIp) {
		this.cwDataGridColumnIp = cwDataGridColumnIp;
	}

	public String getCwDataGridColumnDate() {
		return cwDataGridColumnDate;
	}

	public void setCwDataGridColumnDate(String cwDataGridColumnDate) {
		this.cwDataGridColumnDate = cwDataGridColumnDate;
	}

	public String getCwDataGridColumnUser() {
		return cwDataGridColumnUser;
	}

	public void setCwDataGridColumnUser(String cwDataGridColumnUser) {
		this.cwDataGridColumnUser = cwDataGridColumnUser;
	}

	public String getCwDataGridColumnImg() {
		return cwDataGridColumnImg;
	}

	public void setCwDataGridColumnImg(String cwDataGridColumnImg) {
		this.cwDataGridColumnImg = cwDataGridColumnImg;
	}

	public String getCwDataGridEmpty() {
		return cwDataGridEmpty;
	}

	public void setCwDataGridEmpty(String cwDataGridEmpty) {
		this.cwDataGridEmpty = cwDataGridEmpty;
	}

	public static int getNextId() {
		return nextId;
	}

	public static void setNextId(int nextId) {
		HostRecordNx.nextId = nextId;
	}
}

private String clientName;
private String curClient;
private String [] dfaultClients;
private String [] clientProperties;
private static final String[] DEFAULT_CLIENT_LIST = {
	"roadrunner",
	"CarlyJoe",
	"Bubba",
	"Crystal",
	"Opie" };

 
    public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public void setCurClient(String curClientName){
		this.curClient = curClientName;
	}
	public String getCurClient() {
		return curClient;
	}
	public String[] getClientProperties() {
		return clientProperties;
	}

	public void setClientProperties(String[] clientProperties) {
		this.clientProperties = clientProperties;
	}
	
	public String [] doHosting(String clname, String vIp) throws IllegalArgumentException {
    	if (!FieldVerifier.isValidName(clname)) {
    		setClientName("FreeWilly"); setCurClient(getClientName());
    	} else {
    		setClientName(clname); setCurClient(clname);
    	}

		String ip = vIp;
		
		String user = getCurClient() + "user";
		Date date = new Date();
		
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    
	    Key clientKey = KeyFactory.createKey("Client", clientName);
	    Key dclientKey = KeyFactory.createKey("Dclients", clientName);
	    
		ldclientList(datastore);
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
	    			setClientName(clientName);
	    			setClientProperties(new String [0]);
	    		} else {
	    			clientName = "<p>No records have been added for this Host " + curClient + ", </p><p>" + 
	    					"Contact <a href=\"ronaldWright71@gmail.com\">ronald.wright71@gmail.com</a>  to setup this host</p>";
	    			setClientName(clientName);
	    			setClientProperties(new String [0]);
	    		}
	    	} else {
	    	//clientName not found
	    		clientName = "<p>Client Name: &lt&lt<b>" + curClient + 
	    			"</b>&gt " + ip + "&gt is not in the list of valid Clients</p>" +
	    			"<p>Send an email to <a href=\"mailto:ronald.wright71@gmail.com\">ronald.wright71@gmail.com</a> " + 
	    			"for more information and/or to be added to the list";
	    		setClientProperties(new String [0]);
	    	}
	    } else {
	    	
	    	clientName = "<div class=\"vp-htmlpanel2\"><p>Client name found for: " + curClient + "...." +  
	    			"Retrivieng information for Client...<br>" + 
	    			"Records for Client " + curClient + " are as follows:</p></div>";
					//create a data grid for resulting values
					
	    	int i = 0;
	    	String [] clientProperties = new String [clients.size()]; 
			for (Entity client : clients) {
				String tmpClProp = client.getProperties().toString();
				clientProperties[i] = "{host=" + curClient + ", " + tmpClProp.substring(1,tmpClProp.length());
				i++;
	    	}
/*			i = clientProperties.length +1;
			String[] clprop = new String[i];
			clprop[i -1] = "{host=Hackedhost, ipaddress=192.168.0.29, date=Sun Apr 12 18:42:28 PDT 2015, user=bogususer}";
			i=0;
			for (String cp : clientProperties){
				clprop[i] = cp;
			} 
			setClientProperties(clprop); //(clientProperties) added host for testing multiple hosts; */
			setClientProperties(clientProperties);
	    } 
		String dfaultClientsAts = Arrays.toString(getDfaultClients()); 
		String clientPropertiesAts = Arrays.toString(getClientProperties());	    
	    String [] data = new String [4];
	  //Html for Flextable body  
	    data [0] = clientName;
	  //List of defaultHosts
	    data [1] = dfaultClientsAts.substring(1, dfaultClientsAts.length() -1);
	  //clientProperties; Data table for Client usage & Host Data 
		data [2] = (clientPropertiesAts.length() >= 4 ) ? clientPropertiesAts.substring(2, clientPropertiesAts.length() -2) : "";	
/*		List<String> x = (data[1].isEmpty()) ? null : Arrays.asList((data [1]).split("\\s*,\\s*"));
		//List of host Records
		List<String> y = (data[2].isEmpty()) ? null : Arrays.asList((data [2]).split("\\s*,\\s*"));
		//Host Record Map String - List Host record as list key=value  */
		List<String> hrms;
		//Host Record Map List Host record as list key=value  
		HashMap<String, String> hrm;
		// Host Record Set - List of maps  
		HashSet<HashMap<String, String>> hrs = new HashSet<HashMap<String, String>>();
		if (!data[2].isEmpty()){
			//Chop up data[2] on '} {'
			String [] dt2 = data[2].split("[}][,][ ][{]");
			for (String dt2x : dt2){
				hrms =  Arrays.asList((dt2x).split("\\s*,\\s*"));
				hrm = new HashMap<String, String>();
				//Host Record Map
				for (String w : hrms){
					String [] hrk = w.split("=");
					hrm.put(hrk[0], hrk[1]);
				}
				hrs.add(hrm);
			}
			/**
			 * Tests 
			 *Load the list of host records into test db
			 **/ 
			List<HostRecordNx> hostRecords = new ArrayList<HostRecordNx>(); 
						  	
			Iterator<HashMap<String, String>> it = hrs.iterator();
			while (it.hasNext()){
				HashMap<String, String> hostMap = it.next();
				HostRecordNx hostRecord = new HostRecordNx();
				hostRecord.setCwDataGridColumnHost(curClient);
				hostRecord.setCwDataGridColumnIp(hostMap.get("ipaddress"));
				hostRecord.setCwDataGridColumnDate(hostMap.get("date"));
				hostRecord.setCwDataGridColumnUser(hostMap.get("user"));
				hostRecord.setCwDataGridColumnImg("<a href=\"http://" + hostMap.get("ipaddress") +":8080/nxappletTest\"><image src=\"/hosting/img/noMachine.gif\"></a>");				
				hostRecords.add(hostRecord); 
			} 
		}
			
		return data; 
	}
//	private ListDataProvider<HostRecordNx> dataProvider = new ListDataProvider<HostRecordNx>();
	
	private String ldclientList(DatastoreService datastore) {
		Query q = new Query("Dclients");
	    List<Entity> dclients = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
	    //Check to see if default client list has been loaded 
	    if (dclients.isEmpty()){	   
	    	//No Client List loading defaults (initialize database)
			addDclients(datastore);
	    }
	    
	    String lddclient = "";
	    
	    int cnt = 0;
	    
	    String [] dfaultClientsList = new String [dclients.size()];
	    
	    for (Entity dclient : dclients){
	    	if (cnt == 0){lddclient = dclient.getKey().getName();} else
	    	{lddclient = lddclient + ", " + dclient.getKey().getName();}
	    	dfaultClientsList[cnt] = dclient.getKey().getName();
	    	cnt++;
	    	//Normally commented, for testing delete all Kinds of type Dclients 
	    	//datastore.delete(dclient.getKey());	    	
	    }
	    setDfaultClients(dfaultClientsList);
	    
		return lddclient;
	}
	@Override
	public String[] getDfaultClients() {
		return dfaultClients;
	}

	public void setDfaultClients(String[] dfaultClients) {
		this.dfaultClients = dfaultClients;
	}

	public void deleteClientRecords(DatastoreService datastore) {
		//delete all client records
    	Query delclientRecs = new Query ("Clients");
    	PreparedQuery delList = datastore.prepare(delclientRecs);
		for (Entity delclientRec : delList.asIterable()){
    		datastore.delete(delclientRec.getKey());	
    	}
	}
	
	public static void addDclients(DatastoreService datastore){
		for (String dclient : DEFAULT_CLIENT_LIST ){
			Entity newclient = new Entity("Dclients", dclient);
			newclient.setProperty("updVal", false);
			datastore.put(newclient);
		}
	}

}
