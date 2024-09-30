package testOperations;
import org.mockito.Mockito;

import businesslogic.BLFacade;
import gui.MainGUI;

public class RidesTest {
	
	static BLFacade appFacadeInterface = Mockito.mock(BLFacade.class);
	
	public static void main(String args[]) {
		Mockito.doReturn(true).when(appFacadeInterface).isRegistered("a","a");
		Mockito.doReturn("Driver").when(appFacadeInterface).getMotaByUsername(Mockito.anyString());
		
		MainGUI a = new MainGUI(); 
		MainGUI.setBussinessLogic(appFacadeInterface); 
		a.setVisible(true);
	}
}
