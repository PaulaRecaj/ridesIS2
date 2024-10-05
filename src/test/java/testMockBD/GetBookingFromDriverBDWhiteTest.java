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
import testOperations.TestDataAccess;

public class GetBookingFromDriverBDWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 

	@Test
	//Driver with username Ainhoa does NOT exists in DB. Returns null.
	public void test1() {
		try {
			String driverUsername = "Ainhoa";
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
	//Driver with username Ainhoa exists in DB but it has no rides.
	public void test2() {
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
	//El driver con el username Zuri existe en la BD, tiene un Ride pero no está activo.
	public void test3() {
		String driverUsername = "Zuri";
		boolean driverCreated=false;
		List<Ride> rides = null;
		
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
			    driver.getCreatedRides().get(0).setActive(false);
			    rides = driver.getCreatedRides();
			    System.out.println("Rides:"+rides);
			}
			else {
				sut.open();
				rides = sut.getRidesByDriver(driverUsername);
				for(Ride ride: rides) {
					ride.setActive(false);
					//FALTA ACTUALIZAR
				}
				sut.updateDriver(driver);
				sut.close();
			}
			
			testDA.close();
			
			sut.open();
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
			}
	        testDA.close();
	    }
	}
}
