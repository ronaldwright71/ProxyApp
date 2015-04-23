package com.google.gwt.proxyapp.client;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.proxyapp.client.content.ProxyAppdb;
import com.google.gwt.proxyapp.client.content.ProxyAppdb.HostRecordNx;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

public class CwDataGrid<SafeHtml> extends Composite implements InitializeHandler {

	@SuppressWarnings("rawtypes")
	interface CwDataGridUiBinder extends UiBinder<Widget, CwDataGrid> {
	}
	
	public static String clientPropertiesAts;
	public static String getClientPropertiesAts() {
		return clientPropertiesAts;
	}


	public void setClientPropertiesAts(String clientPropertiesAts) {
		CwDataGrid.clientPropertiesAts = clientPropertiesAts;
	}


	public static interface CwConstants {
		String cwDataGridColumnHost();
	    String cwDataGridColumnIp();
	    String cwDataGridDate();
	    String cwDataGridUser();
	    String cwDataGridImg();
	    String cwDataGridEmpty();
	}
	   /**
	   * ProxyApp Flextable
	   **/
	   @UiField(provided = true)
	   FlexTable ftable;
	   /**
	   /**
	   * ProxyApp Sidebar Flextable
	   **/
	   @UiField(provided = true)
	   FlexTable sftable;
	   /**
	   * The main DataGrid.
	   */
	  @UiField(provided = true)
	  DataGrid<HostRecordNx> dataGrid;

	  /**
	   * The pager used to change the range of data.
	   */
	  @UiField(provided = true)
	  SimplePager pager;

	  //The constants used for the data grid feild names - Headers
	  public static class CreateConstants implements CwConstants {
			CreateConstants (){
			}
						
			public String cwDataGridColumnHost(){
				return "Host Name";}
			
		    public String cwDataGridColumnIp(){
				return "Ip Addr";}
			
			public String cwDataGridDate(){
				return "Date";}
			
			public String cwDataGridUser(){
				return "User Name";}
			
			public String cwDataGridImg(){
				return "Link";}
			
			public String cwDataGridEmpty(){
				return "No Data Found";}
			
		}	  
	  
	  CwConstants constants = new CreateConstants();

   public CwDataGrid(String[] result) {
	
	/**
	 * Called from onModuleLoad program
	 *
	 *Main data string containg host records for database */
	setClientPropertiesAts(result[2]);
		
		String data = (result[2].length()==0) ? 
			"{host=Hackedhost, ipaddress=192.168.0.29, date=Sun Apr 12 18:42:28 PDT 2015, user=bogususer}" :
			result[2];
		
		setClientPropertiesAts(data);
		
	//Import the ProxyApp Flextable
		ftable = new FlexTable();
		sftable = new FlexTable();
	//	private void doFlexTable(String dclients[], String clientHtmlName){

	sftable.setText(0, 0, "Defaults");
	sftable.setStyleName("cw-FlexTable");
	List<String> dclients = Arrays.asList((result[1]).split("\\s*,\\s*"));
	int cnt = 1;
	for (String dclient : dclients ){
		sftable.setText(cnt, 0, dclient);
		cnt++;
	}
	ftable.setHTML(0, 0, result[0]);
	//int numRows = ftable.getRowCount();
	//ftable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
	sftable.getFlexCellFormatter().setStyleName(0, 0, "vp-htmlpanel2");
		
	// Create a DataGrid.
	    /*
	     * Set a key provider that provides a unique key for each record. If key is
	     * used to identify records when fields (such as the name and address)
	     * change.
	     */
	    dataGrid = new DataGrid<HostRecordNx>(ProxyAppdb.HostRecordNx.KEY_PROVIDER);
	    
	    dataGrid.setWidth("100%");
	    /*
	     * Do not refresh the headers every time the data is updated. The footer
	     * depends on the current data, so we do not disable auto refresh on the
	     * footer.
	     */
	    dataGrid.setAutoHeaderRefreshDisabled(true);
	    
		dataGrid.setEmptyTableWidget(new Label(constants.cwDataGridEmpty()));
	    // Attach a column sort handler to the ListDataProvider to sort the list.
	    ListHandler<HostRecordNx> sortHandler =
	        new ListHandler<HostRecordNx>(ProxyAppdb.get().getDataProvider().getList());
	    dataGrid.addColumnSortHandler(sortHandler);
	 // Create a Pager to control the table.
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(dataGrid);
	 // Add a selection model so we can select cells.
	    final SelectionModel<HostRecordNx> selectionModel =
	        new MultiSelectionModel<HostRecordNx>(ProxyAppdb.HostRecordNx.KEY_PROVIDER);
	    dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
	        .<HostRecordNx> createCheckboxManager());

	    // Initialize the columns.
	    initTableColumns(selectionModel, sortHandler);

	    // Add the CellList to the adapter in the database.
	    ProxyAppdb.get().addDataDisplay(dataGrid);   
	 // Create the UiBinder.
	    
	    CwDataGridUiBinder uiBinder = GWT
				.create(CwDataGridUiBinder.class);
	    initWidget(uiBinder.createAndBindUi(this));
	}
	
    


	private void initTableColumns(final SelectionModel<HostRecordNx> selectionModel,
	
			ListHandler<HostRecordNx> sortHandler) {
			    // Checkbox column. 
				//This table will use a checkbox column for selection.
			    // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
			    // mouse selection.
			    Column<HostRecordNx, Boolean> checkColumn =
			        new Column<HostRecordNx, Boolean>(new CheckboxCell(true, false)) {
			          @Override
			          public Boolean getValue(HostRecordNx object) {
			            // Get the value from the selection model.
			            return selectionModel.isSelected(object);
			          }
			        };
			    dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			    dataGrid.setColumnWidth(checkColumn, 6, Unit.PCT);

			    // Host Column
			    Column<HostRecordNx, String> hostColumn =
			        new Column<HostRecordNx, String>(new EditTextCell()) {
			          @Override
			          public String getValue(HostRecordNx object) {
			            return object.getCwDataGridColumnHost();
			          }
			        };
			    hostColumn.setSortable(true);
			    sortHandler.setComparator(hostColumn, new Comparator<HostRecordNx>() {
			      @Override
			      public int compare(HostRecordNx o1, HostRecordNx o2) {
			        return o1.getCwDataGridColumnHost().compareTo(o2.getCwDataGridColumnHost());
			      }
			    });
			    
			    dataGrid.addColumn(hostColumn, constants.cwDataGridColumnHost());
			    hostColumn.setFieldUpdater(new FieldUpdater<HostRecordNx, String>() {
			      @Override
			      public void update(int index, HostRecordNx object, String value) {
			        // Called when the user changes the value.
			        object.setCwDataGridColumnHost(value);
			        ProxyAppdb.get().refreshDisplays();
			      }
			    });
			    dataGrid.setColumnWidth(hostColumn, 15, Unit.PCT);
			    
			    //Ip Address
			    Column<HostRecordNx, String> ipColumn =
			        new Column<HostRecordNx, String>(new EditTextCell()) {
			          @Override
			          public String getValue(HostRecordNx object) {
			            return object.getCwDataGridColumnIp();
			          }
			        };
			        ipColumn.setSortable(true);
			    sortHandler.setComparator(ipColumn, new Comparator<HostRecordNx>() {
			      @Override
			      public int compare(HostRecordNx o1, HostRecordNx o2) {
			        return o1.getCwDataGridColumnIp().compareTo(o2.getCwDataGridColumnIp());
			      }
			    });
			    
			    dataGrid.addColumn(ipColumn, constants.cwDataGridColumnIp());
			    ipColumn.setFieldUpdater(new FieldUpdater<HostRecordNx, String>() {
			      @Override
			      public void update(int index, HostRecordNx object, String value) {
			        // Called when the user changes the value.
			        object.setCwDataGridColumnIp(value);
			        ProxyAppdb.get().refreshDisplays();
			      }
			    });
			    dataGrid.setColumnWidth(ipColumn, 15, Unit.PCT);
			    
			  //Date
			    Column<HostRecordNx, String> dateColumn =
			        new Column<HostRecordNx, String>(new EditTextCell()) {
			          @Override
			          public String getValue(HostRecordNx object) {
			            return object.getCwDataGridColumnDate();
			          }
			        };
			        dateColumn.setSortable(true);
			    sortHandler.setComparator(dateColumn, new Comparator<HostRecordNx>() {
			      @Override
			      public int compare(HostRecordNx o1, HostRecordNx o2) {
			        return o1.getCwDataGridColumnDate().compareTo(o2.getCwDataGridColumnDate());
			      }
			    });
			    
			    dataGrid.addColumn(dateColumn, constants.cwDataGridDate());
			    dateColumn.setFieldUpdater(new FieldUpdater<HostRecordNx, String>() {
			      @Override
			      public void update(int index, HostRecordNx object, String value) {
			        // Called when the user changes the value.
			        object.setCwDataGridColumnDate(value);
			        ProxyAppdb.get().refreshDisplays();
			      }
			    });
			    dataGrid.setColumnWidth(dateColumn, 35, Unit.PCT);
			    
			  //user name 
			    Column<HostRecordNx, String> userColumn =
			        new Column<HostRecordNx, String>(new EditTextCell()) {
			          @Override
			          public String getValue(HostRecordNx object) {
			            return object.getCwDataGridColumnUser();
			          }
			        };
			        userColumn.setSortable(true);
			    sortHandler.setComparator(userColumn, new Comparator<HostRecordNx>() {
			      @Override
			      public int compare(HostRecordNx o1, HostRecordNx o2) {
			        return o1.getCwDataGridColumnUser().compareTo(o2.getCwDataGridColumnUser());
			      }
			    });
			    
			    dataGrid.addColumn(userColumn, constants.cwDataGridUser());
			    userColumn.setFieldUpdater(new FieldUpdater<HostRecordNx, String>() {
			      @Override
			      public void update(int index, HostRecordNx object, String value) {
			        // Called when the user changes the value.
			        object.setCwDataGridColumnUser(value);
			        ProxyAppdb.get().refreshDisplays();
			      }
			    });
			    dataGrid.setColumnWidth(userColumn, 20, Unit.PCT);

			  //Image Link
			    
/*			    ImageResourceCell myImgCell = new ImageResourceCell() {
			        public Set<String> getConsumedEvents() {
			            HashSet<String> events = new HashSet<String>();
			            events.add("click");
			            return events;
			        }
			    };
			    
		    	
		    	Column<HostRecordNx, ImageResource> imgColumn = new ImgColumn<HostRecordNx, ImageResource>(myImgCell) {};
*/
			    
				@SuppressWarnings("unchecked")
				Column<HostRecordNx, SafeHtml> imgColumn = new Column<HostRecordNx, SafeHtml>((Cell<SafeHtml>) imgCell) 
			    		{
					@Override
					public SafeHtml getValue(HostRecordNx value) {
			            SafeHtmlBuilder sb = new SafeHtmlBuilder();
			            sb.appendHtmlConstant("<div><a href=\"http://" + value.getCwDataGridColumnIp() + ":8080/nxappletTest\">"
			            		+ "<image src=\"/hosting/img/noMachine.gif\" height=\"24px\"></a>");
			            sb.appendHtmlConstant("</div>");
			            return (SafeHtml) sb.toSafeHtml();
					}
			    };
			    dataGrid.addColumn(imgColumn, constants.cwDataGridImg());
				dataGrid.setColumnWidth(imgColumn, 9, Unit.PCT);
				
	}

	final SafeHtmlCell imgCell = new SafeHtmlCell();
	
	@Override
	public void onInitialize(InitializeEvent event) {
		
	}
}
	

