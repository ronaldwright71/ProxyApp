package com.google.gwt.proxyapp.client.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.proxyapp.client.CwDataGrid;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

public class ProxyAppdb {
    
	public static class HostRecordNx implements Comparable<HostRecordNx>{
		
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
		
			public void setCwDataGridColumnImg(String string) {
				this.cwDataGridColumnImg = string;
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
	

	private static ProxyAppdb instance;
	public static ProxyAppdb get() {
		if (instance == null) {
		      instance = new ProxyAppdb();
		    }
		    return instance;
	}
	
    /**
    * The provider that holds the list of contacts in the database.
    */
    private ListDataProvider<HostRecordNx> dataProvider = new ListDataProvider<HostRecordNx>();
	  
	
    public void refreshDisplays() {
	    dataProvider.refresh();
    }
    
	/**
     * Construct a new contact database.
     */
	private ProxyAppdb() {
		//Host Record Map String - List Host record as csv list key=value rows seperated by "} {" */
				List<String> hrms;
				//Host Record Map List Host record as list key=value  
				HashMap<String, String> hrm;
				// Host Record Set - List of maps  
				HashSet<HashMap<String, String>> hrs = new HashSet<HashMap<String, String>>();
			//	if (!data[2].isEmpty()){
					//Chop up data[2] on '} {'
					String [] dt2 = CwDataGrid.clientPropertiesAts.split("[}][,][ ][{]");
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
					 * 
					 * 
					 * **/
					List<HostRecordNx> hostRecords = dataProvider.getList();
					//Load the list of host records 
					 	
					Iterator<HashMap<String, String>> it = hrs.iterator();
					while (it.hasNext()){
						HashMap<String, String> hostMap = it.next();
						HostRecordNx hostRecord = new HostRecordNx();
						hostRecord.setCwDataGridColumnHost(hostMap.get("host"));
						hostRecord.setCwDataGridColumnIp(hostMap.get("ipaddress"));
						hostRecord.setCwDataGridColumnDate(hostMap.get("date"));
						hostRecord.setCwDataGridColumnUser(hostMap.get("user"));
						hostRecord.setCwDataGridColumnImg("<a href=\"http://" + hostMap.get("ipaddress") +":8080/nxappletTest\"><image src=\"/hosting/img/noMachine.gif\"></a>");
						hostRecords.add(hostRecord);
					}
		
	}
	/**
	 *@param display a {@Link HasData}.  In from line 122 CwDataGrid
	*/
	public void addDataDisplay(HasData<HostRecordNx> display) {
		dataProvider.addDataDisplay(display);
	}

	public ListDataProvider<HostRecordNx> getDataProvider() {
		return dataProvider;
	}


}

