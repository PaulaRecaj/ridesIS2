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

import org.junit.Test;

import data_access.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class GetBookingFromDriverBDBlackTest {
	 
	//sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();


	private Driver driver;
	private Ride ride;
	@SuppressWarnings("unused")
	private Traveler traveler;
	
	@Test
	//Driver's username == null. Returns null. (2)
	public void test1() {
		String driverUsername = null;
		try {
			sut.open();
		    List<Booking> bookings = sut.getBookingFromDriver(driverUsername);
		    assertNull(bookings);
			
		}catch(Exception e) {
			fail();
		}finally {
			sut.close();
		}
		
	}
	
	@Test
	//Driver's username != null, but not in the DB. Returns null. (1,4)
	public void test2() {
		String driverUsername = "Ainhoa";
		try {
			sut.open();
		    List<Booking> bookings = sut.getBookingFromDriver(driverUsername);
		    assertNull(bookings);
			
		}catch(Exception e) {
			fail();
		}finally {
			sut.close();
		}
	}
	
	@Test
	/*Driver's username != null, Driver in the DB, but their rides aren't 
	in the DB. Returns booking empty. (1,3,(6.1 o 6.2)*/
	public void test3() {
		String driverUsername = "Ainhoa";
		String driverPassword = "123";
		boolean driverCreated=false;
		try {
			//create Driver if necessary
			testDA.open();
			if (!testDA.existDriver(driverUsername)) {
				driver = testDA.createDriver(driverUsername,driverPassword);
			    driverCreated=true;
			    driver.setCreatedRides(new ArrayList<>());
			}
			testDA.close();
			
			sut.open();
			List<Booking> bookings = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			//verify the results
			 assertTrue(bookings.isEmpty());
		}catch(Exception e) {
			sut.close();
			fail();
		}finally {
			  //Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (driverCreated)  testDA.removeDriver(driverUsername);
	        testDA.close();
	    }
	}
	
	@Test
	/*Driver's username != null, Driver in the DB, rides !=null,[] BUT no
	 *ride is active (1,3,5,8). Returns booking empty.*/
	public void test4() {
		String driverUsername = "Ainhoa";
		String travelerUsername = "a";
		boolean driverCreated=false;
		
		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try{
			//create Driver if necessary
			testDA.open();
			if (!testDA.existDriver(driverUsername)) {
			    driverCreated=true;
			    driver = testDA.addDriverWithRide(driverUsername, rideFrom, rideTo, rideDate, 0, 0);
			    ride = driver.getCreatedRides().get(0);
			    ride.setActive(false);
			    Traveler traveller1 = new Traveler(travelerUsername,"123");
				Booking booking1 = new Booking(ride, traveller1, 0);
				List<Booking> bookings = new ArrayList<>();
				bookings.add(booking1);
				ride.setBookings(bookings);

			}
			testDA.close();
			
			sut.open();
			if(driverCreated) {
				sut.updateDriver(driver);
				for(Ride ride1: driver.getCreatedRides()) {
					for(Booking book: ride1.getBookings()) {
						sut.updateBooking(book);
					}
				}
			}
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			System.out.println(booking);
			assertTrue(booking.isEmpty());
			
						
		}catch(Exception e) {
			sut.close();
			fail();
		}finally {
			  //Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (driverCreated) {
				testDA.removeDriver(driverUsername);
				testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			}
	        testDA.close();
	    }
	}
	
	@Test
	/*Driver's username != null, Driver in the DB, rides !=null,there's
	 * at least one ride that is active, BUT ride.getBookings() == null.
	 * Returns booking empty. (1,3,5,7,10)*/
	public void test5() {
		String driverUsername = "Ainhoa";
		boolean driverCreated=false;
		
		String rideFrom="Donostia";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try{
			//create Driver if necessary
			testDA.open();
			if (!testDA.existDriver(driverUsername)) {
			    driverCreated=true;
			    driver = testDA.addDriverWithRide(driverUsername, rideFrom, rideTo, rideDate, 0, 0);
			    ride = driver.getCreatedRides().get(0);
				List<Booking> bookings = new ArrayList<>();
				ride.setBookings(bookings);

			}
			testDA.close();
			
			sut.open();
			if(driverCreated) {
				sut.updateDriver(driver);
			}
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			System.out.println(booking);
			assertTrue(booking.isEmpty());
			
						
		}catch(Exception e) {
			sut.close();
			fail();
		}finally {
			  //Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (driverCreated) {
				testDA.removeDriver(driverUsername);
				testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			}
	        testDA.close();
	    }

		
	}
	
	@Test
	/*Driver's username != null, Driver in the DB, rides !=null, there's
	 *at least one ride that is active and ride.getBookings() != null.
	 *Returns booking (not empty). (1,3,5,7,9)*/
	public void test6() {
		String driverUsername = "Ainhoa";
		String travelerUsername = "a";
		boolean driverCreated=false;
		
		String rideFrom="Lasarte";
		String rideTo="Zarautz";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate=null;
		try {
			rideDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try{
			//create Driver if necessary
			testDA.open();
			if (!testDA.existDriver(driverUsername)) {
			    driverCreated=true;
			    driver = testDA.addDriverWithRide(driverUsername, rideFrom, rideTo, rideDate, 0, 0);
			    ride = driver.getCreatedRides().get(0);
			    Traveler traveller1 = new Traveler(travelerUsername,"123");
				Booking booking1 = new Booking(ride, traveller1, 0);
				List<Booking> bookings = new ArrayList<>();
				bookings.add(booking1);
				ride.setBookings(bookings);
			}
			testDA.close();
			
			sut.open();
			if(driverCreated) {
				for(Ride ride1: driver.getCreatedRides()) {
					for(Booking book: ride1.getBookings()) {
						sut.updateBooking(book);
					}
				}
			}
			List<Booking> booking = sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			System.out.println(booking);
			assertFalse(booking.isEmpty());
			
						
		}catch(Exception e) {
			sut.close();
			fail();
		}finally {
			  //Remove the created objects in the database (cascade removing)   
			testDA.open();
			if (driverCreated) {
				testDA.removeDriver(driverUsername);
				testDA.removeRide(driverUsername, rideFrom, rideTo, rideDate);
			}
	        testDA.close();
	    }
		
	}

}
