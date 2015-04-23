/**
 * 
 */
package com.google.gwt.proxyapp.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.proxyapp.client.content.ProxyAppdb.HostRecordNx;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;

/**
 * @author fantomfang1
 * @param <C>
 * @param <T>
 *
 */
public class ImgColumn<T, C> extends Column<T, C> {
	
	private final Cell<C> cell; 
	
	public ImgColumn(Cell<C> cell) {
		super(cell);
		this.cell = cell;
	}
	/**
	* The {@link FieldUpdater} used for updating values in the column.
	*/
	private FieldUpdater<T, C> fieldUpdater;
	


	/* (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.Column#getValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public C getValue(T object) {
		return (C) resources.getImageResource();
	}
	
	  /**
	   * Handle a browser event that took place within the column.
	   */
	public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event, C C) {
	  final int index = context.getIndex();
	  ValueUpdater<C> valueUpdater = (fieldUpdater == null) ? null : new ValueUpdater<C>() {
	    @Override
	    public void update(C value) {
	      fieldUpdater.update(index, object, value);
	    }
	  };
	  
	  cell.onBrowserEvent(context, elem, C, event, valueUpdater);
	  
	  String eventType = event.getType();
		 
		if ("click".equals(eventType)) {
				if (object.getClass().getName()=="ProxyAppdb"){
					Window.open("http://" + ((HostRecordNx) object).getCwDataGridColumnIp() + 
			    				":8080/nxappletTest", "NoMachine", null);
				}
			    }
			    // Special case the ENTER key for a unified user experience.
		if ("click".equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
			    	onEnterKeyDown(context, elem, object, event, valueUpdater);   }
		
	}	
	
	protected void onEnterKeyDown(Context context, Element elem,
			Object value, NativeEvent event,
			ValueUpdater<C> valueUpdater) {
		
	}
	
    interface Resources extends ClientBundle {
    	  @Source("noMachine.gif")
    	  ImageResource getImageResource();
    	}	
      Resources resources = GWT.create(Resources.class);
}
