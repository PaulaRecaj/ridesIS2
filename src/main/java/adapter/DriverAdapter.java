package adapter;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

public class DriverAdapter extends AbstractTableModel {

	private Driver driver;
	private String[] columnNames =new String[] {"from", "to", "date", "places", "price" };
	
	public DriverAdapter(Driver driver) {
		
		this.driver=driver;
		
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return driver.getCreatedRides().size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}
	
	
	public String getColumnName(int i) {
	    // Challenge!
		  return columnNames[i];
	  }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		  
		 Ride r = (Ride)driver.getCreatedRides().toArray()[rowIndex];
		 
		 switch(columnIndex) {
		 
		 case 0:
			 return r.getFrom();
		 case 1:
			 return r.getTo();
		 case 2:
			 return r.getDate();
		 case 3:
			 return r.getnPlaces();
		 case 4:
			 return r.getPrice();
			 
		default:
			return null;
		 
		 }
		
	}

}
