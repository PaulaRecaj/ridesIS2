package gui;

import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businesslogic.BLFacade;
import businesslogic.BLFacadeImplementation;
import businesslogic.BLFactory;
import businesslogic.ExtendedIterator;
import businesslogic.LocalBLFacadeFactory;
import businesslogic.RemoteBLFacadeFactory;
import configuration.ConfigXML;
import data_access.DataAccess;

public class ApplicationLauncher {

	public static void main(String[] args) {
		
		

		ConfigXML c = ConfigXML.getInstance();
		
		Logger l = Logger.getLogger(ConfigXML.class.getName());

		l.config(c.getLocale());

		Locale.setDefault(new Locale(c.getLocale()));

		l.config("Locale: "+ Locale.getDefault());
		
		/*
		try {
		
			BLFacade appFacadeInterface;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			if (c.isBusinessLogicLocal()) {

				DataAccess da = new DataAccess();
				appFacadeInterface = new BLFacadeImplementation(da);

			}

			else { // If remote

				String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
						+ c.getBusinessLogicName() + "?wsdl";

				URL url = new URL(serviceName);

				// 1st argument refers to wsdl document above
				// 2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businesslogic/", "BLFacadeImplementationService");

				Service service = Service.create(url, qname);

				appFacadeInterface = service.getPort(BLFacade.class);
			}

			MainGUI.setBussinessLogic(appFacadeInterface);
			MainGUI a = new MainGUI();
			a.setVisible(true);

		} catch (Exception e) {
			// a.jLabelSelectOption.setText("Error: "+e.toString());
			// a.jLabelSelectOption.setForeground(Color.RED);

			l.config("Error in ApplicationLauncher: " + e.toString());
			
		}
		// a.pack();
*/
			
		try {
			BLFactory factory;
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			if (c.isBusinessLogicLocal()) {
				
				factory = new LocalBLFacadeFactory();
				
			} else {
				
				factory = new RemoteBLFacadeFactory();
			}
			
			BLFacade appFacadeInterface = factory.getBusinessLogicFactory();
			MainGUI.setBussinessLogic(appFacadeInterface);
			MainGUI a = new MainGUI();
			a.setVisible(true);
			
		} catch (Exception e) {
			l.config("Error in ApplicationLauncher: " + e.toString());
		}
		BLFacade facade = new BLFacadeImplementation();
		ExtendedIterator<String> i = facade.getDepartCitiesIterator(); 
		String s;
		System.out.println("_____________________"); 
		System.out.println("FROM LAST TO FIRST");
		i.goLast(); // Go to last element 
		while (i.hasPrevious()) {
			s = i.previous();
			System.out.println(s); 
		}
		System.out.println(); 
		System.out.println("_____________________");
		System.out.println("FROM FIRST TO LAST"); 
		i.goFirst(); // Go to first element
		while (i.hasNext()) {
			s = i.next();
			System.out.println(s); 
		}
	}

}
