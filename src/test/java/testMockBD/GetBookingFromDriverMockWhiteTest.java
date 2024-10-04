package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import configuration.UtilDate;
import data_access.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class GetBookingFromDriverMockWhiteTest {

	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	@Mock
	protected TypedQuery<Driver> query;
	

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	
	Driver driver;
	
	@Test
	//El driver con el username Ainhoa no existe en la BD. Devuelve null.
	public void test1() {
		String driverUsername = "Ainhoa";
		
		try {
			//configure the state through mocks 
	        Mockito.when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username",
					Driver.class)).thenReturn(null);
	        
	      //invoke System Under Test (sut)  
			sut.open();
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertNull(booking);
			System.out.println("Resultado de bookRide: " + booking);
			
		}catch(Exception e) {
			sut.close();
			fail();
		}
	}
	
	@Test
	//El driver con el username Ainhoa existe en la BD pero no tiene ningún ride.
	public void test2() {
		
		driver = null;
		String driverUsername = "Ainhoa";
			
		driver=new Driver(driverUsername,"123");
		Mockito.when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class)).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(driver);
		
		try {	
			//invoke System Under Test (sut)  
			sut.open();
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertTrue(booking.isEmpty());
			System.out.println("Resultado de bookRide: " + booking);
			
		}catch(Exception e) {
			fail();
			sut.close();
		}
		
	}
	
	@Test
	//El driver con el username Zuri existe en la BD, tiene un Ride pero no está activo.
	public void test3() {
		driver = null;
		String driverUsername = "Zuri";
		
		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		driver=new Driver(driverUsername,"123");
		Ride ride1= new Ride(rideFrom, rideTo, rideDate, 0, 0, driver);
		ride1.setActive(false);
		
		List<Ride> rides = new ArrayList<>();
		rides.add(ride1);
		driver.setCreatedRides(rides);
			
		Mockito.when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class)).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(driver);
			
		try {
			sut.open();
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertTrue(booking.isEmpty());
			System.out.println("Resultado de bookRide: " + booking);
			
		}catch(Exception e) {
			fail();
			sut.close();
		}
		
	}
	
	@Test
	//El driver con el username Urtzi existe en la BD, tiene un Ride y está activo. Devuelve el booking con un Ride
	public void test4() {
		driver = null;
		String driverUsername = "Zuri";
		String travelerUsername ="Gaizka";
		
		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		driver=new Driver(driverUsername,"123");
		Ride ride1= new Ride(rideFrom, rideTo, rideDate, 0, 0, driver);
		Traveler traveller1 = new Traveler(travelerUsername,"123");
		Booking booking1 = new Booking(ride1, traveller1, 0);
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		ride1.setBookings(bookings);
		
		List<Ride> rides = new ArrayList<>();
		rides.add(ride1);
		driver.setCreatedRides(rides);
			
		Mockito.when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class)).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(driver);
			
		try {
			sut.open();
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertFalse(booking.isEmpty());
			System.out.println("Resultado de bookRide: " + booking);
			
		}catch(Exception e) {
			fail();
			sut.close();
		}
		
	}

	
	
	
	
	/*
	 * //Obtenemos la lista de reservas asociadas al conductor Urtzi. Expected
	 * value: [ride1, ride2, ride4] public void test3() { String driverUsername =
	 * "Urtzi"; String driverPassword="123";
	 * 
	 * driver = new Driver(driverUsername, driverPassword);
	 * 
	 * Calendar cal = Calendar.getInstance(); cal.set(2024, Calendar.MAY, 30); Date
	 * date2 = UtilDate.trim(cal.getTime());
	 * 
	 * cal.set(2024, Calendar.MAY, 10); Date date3 = UtilDate.trim(cal.getTime());
	 * 
	 * cal.set(2024, Calendar.APRIL, 20); Date date4 = UtilDate.trim(cal.getTime());
	 * 
	 * 
	 * driver.addRide("Donostia", "Madrid", date2, 5, 20); //ride1
	 * driver.addRide("Irun", "Donostia", date2, 5, 2); //ride2
	 * driver.addRide("Madrid", "Donostia", date3, 5, 5); //ride3
	 * driver.addRide("Barcelona", "Madrid", date4, 0, 10); //ride4
	 * 
	 * Ride ride1 = driver.getCreatedRides().get(0); Ride ride2 =
	 * driver.getCreatedRides().get(1); Ride ride3 =
	 * driver.getCreatedRides().get(2); Ride ride4 =
	 * driver.getCreatedRides().get(3);
	 * 
	 * ride3.setActive(false);
	 * 
	 * 
	 * 
	 * }
	 */
}
