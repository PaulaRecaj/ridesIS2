package adapter;

import businesslogic.*;
import domain.Driver;

public class Main {

	public static void main(String[] args)	{
	//		the	BL	is	local
		//boolean isLocal =	true;
		//BLFacade blFacade = new BLFactory().getBusinessLogicFactory(isLocal);
		BLFacade blf = new BLFacadeImplementation();
		LocalBLFacadeFactory localblf = new LocalBLFacadeFactory();
		
		blf.initializeBD();
		
		Driver	d= blf.getDriver("Urtzi");		
		DriverTable	dt=new DriverTable(d);
		dt.setVisible(true);
	}
	
}
