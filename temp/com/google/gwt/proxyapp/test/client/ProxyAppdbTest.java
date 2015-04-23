package com.google.gwt.proxyapp.test.client;

import com.google.gwt.proxyapp.test.client.CwDataGridTest.CwConstants;

public class ProxyAppdbTest extends ProxyAppTest {

	interface AppDbConstants extends CwConstants {
	    
    }
	
	public static class CreateConstants implements AppDbConstants {
		CreateConstants (){	
		}					
		public String cwDataGridColumnHost(){
			return "Host Name";}
		
	    public String cwDataGridColumnIp(){
			return "Ip Addr";}
		
		public String cwDate(){
			return "Date";}
		
		public String cwUser(){
			return "User Name";}
		
		public String cwImg(){
			return "Link";}
		
		public String cwDataGridEmpty(){
			return "No Data Found";}
	}
	
	public void load(){
		AppDbConstants constants = new CreateConstants();
		System.out.println(constants.cwDataGridColumnHost().toString());
		System.out.println(constants.cwDataGridColumnIp());
		System.out.println(constants.cwDataGridEmpty());
		System.out.println(constants.cwDate());
		System.out.println(constants.cwImg());
		System.out.println(constants.cwUser());
	}
}
