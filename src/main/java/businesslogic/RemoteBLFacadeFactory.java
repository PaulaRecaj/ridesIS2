package businesslogic;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;

public class RemoteBLFacadeFactory implements BLFactory{

	@Override
	public BLFacade getBusinessLogicFactory() {
		try {
            ConfigXML c = ConfigXML.getInstance();
			String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
					+ c.getBusinessLogicName() + "?wsdl";

			URL url = new URL(serviceName);

			// 1st argument refers to wsdl document above
			// 2nd argument is service name, refer to wsdl document above
			QName qname = new QName("http://businesslogic/", "BLFacadeImplementationService");

			Service service = Service.create(url, qname);
            return service.getPort(BLFacade.class);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

}
