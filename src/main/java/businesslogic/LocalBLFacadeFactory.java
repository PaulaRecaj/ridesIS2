package businesslogic;

import data_access.DataAccess;

public class LocalBLFacadeFactory implements BLFactory{

	@Override
	public BLFacade getBusinessLogicFactory() {
        DataAccess da = new DataAccess();
        return new BLFacadeImplementation(da);
	}

}
