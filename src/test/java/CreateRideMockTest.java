
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Discount;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class CreateRideMockTest {
	
	static DataAccess sut;
	protected MockedStatic<Persistence> persistenceMock;
	
	@Mock
	protected EntityManagerFactory entityManagerFactory; 
	
	@Mock
	protected EntityManager db;
	
	@Mock
	protected EntityTransaction et;
	
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class); 
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any())).thenReturn(entityManagerFactory); 
		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager(); 
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	} 
	
	@After
	public void tearDown() {
		persistenceMock.close(); 
	}
	
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName)throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println( ">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverName + " date " + date);
		if (driverName == null) return null;
		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustB eLaterThanToday"));
			}
			db.getTransaction().begin();
			Driver driver = db.find(Driver.class, driverName); 
			
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price); 
			// next instruction can be obviated
			db.persist(driver);
			db.getTransaction().commit();
			return ride;
		} catch (NullPointerException e) { 
			db.getTransaction().commit();
			return null; 
		}
	}
	
	@Test
	public void test4() {
		String driverUsername="Driver Test"; 
		String driverPassword="123";
		
		String rideFrom="Donostia"; 
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		Date rideDate=null;;
		try {
			rideDate = sdf.parse("05/10/2026"); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Driver driver = new Driver(driverUsername,driverPassword); 
			driver.addRide(rideFrom, rideTo, rideDate, 2, 10);
			Mockito.when(db.find(Driver.class, driverUsername)).thenReturn(driver); 
			//equivalent to
			//Mockito.doReturn(driver).when (db).find (Driver.class, driverUsername);
			
			//invoke System Under Test (sut)
			sut.open(); 
			sut.createRide(rideFrom,rideTo,rideDate,0,0,driverUsername); 
			sut.close();
			fail();
			
		} catch (RideAlreadyExistException e) { 
			//verify the results
			sut.close();
			assertTrue(true);
		} catch (RideMustBeLaterThanTodayException e) {
			fail(); 
		}
	}
	
	@Test
	public void testCreateDiscount() {
		try {
			//test params
			String code="free10";
			double percent=10;
			boolean active=true;
			Discount d=new Discount(code, percent, active) ;
			//call sut
			sut.createDiscount(d); //test results ???
		} catch (Exception e) {
			fail("If any exception is thrown, it is not working properly");
		}
	}

}
