package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import data_access.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class GetBookingFromDriverMockBlackTest {

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
	//Driver's username = null. Returns null. (2)
	public void test1(){
		String driverUsername = null;
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
	//Driver's username != null, but not in the DB. Returns null. (1,4)
	public void test2() {
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
	/*Driver's username != null, Driver in the DB, but their rides aren't 
	in the DB. Returns null. (1,3,(6.1 o 6.2)*/
	
	public void test3() {
		driver = null;
		String driverUsername = "Ainhoa";
			
		driver=new Driver(driverUsername,"123");
		Mockito.when(db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class)).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(driver);
		driver.setCreatedRides(new ArrayList<>());
		
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
	/*Driver's username != null, Driver in the DB, rides !=null,[] BUT no
	 *ride is active (1,3,5,8). Returns null.*/
	public void test4() {
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
	/*Driver's username != null, Driver in the DB, rides !=null,there's
	 * at least one ride that is active, BUT ride.getBookings() == null.
	 * Returns null. (1,3,5,7,10)*/
	public void test5() {
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
		//bookings.add(booking1);
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
			
			assertTrue(booking.isEmpty());
			System.out.println("Resultado de bookRide: " + booking);
			
		}catch(Exception e) {
			fail();
			sut.close();
		}
	}
	@Test
	/*Driver's username != null, Driver in the DB, rides !=null, there's
	 *at least one ride that is active and ride.getBookings() != null.
	 *Returns bookings. (1,3,5,7,10)*/
	public void test6() {
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
	
}
