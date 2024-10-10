package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import data_access.DataAccess;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDBlackTest {
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();

	private String travelerName;
	private String travelerPassw;
	
	private Ride ride;
	private String rideFrom;
	private String rideTo;
	private int ridePlaces;
	private float ridePrice;
	private SimpleDateFormat sdf; 
	private Date rideDate;
	
	private Driver driver;
	

	@Test
	// //Se intenta reservar un ride metiendo parámetros null
	public void test1() {
		travelerName = "Paula";
		travelerPassw = "a";
		
		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 5;
		ridePrice = 10;

		sdf = new SimpleDateFormat("dd/MM/yyyy");
		rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		traveler.setMoney(100);
		
		try {
			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, null, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (NullPointerException e) {
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo parámetros null dentro del ride
	public void test2() {
		travelerName = "Paula";
		travelerPassw = "a";
		
		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 5;
		ridePrice = 10;

		sdf = new SimpleDateFormat("dd/MM/yyyy");
		rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		traveler.setMoney(100);
		
		try {
			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, null, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRide(driver.getUsername(), rideFrom, rideTo, rideDate);
			testDA.removeDriver(driver.getUsername());
			testDA.close();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo asientos que queremos en negativo o en 0
	public void test3() {
		travelerName = "Paula";
		travelerPassw = "a";
		
		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 5;
		ridePrice = 10;

		sdf = new SimpleDateFormat("dd/MM/yyyy");
		rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		traveler.setMoney(100);
		
		try {
			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, -3, 1.5); // Asientos en negativo
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRide(driver.getUsername(), rideFrom, rideTo, rideDate);
			testDA.removeDriver(driver.getUsername());
			testDA.close();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo un descuento negativo
	public void test4() {
		travelerName = "Paula";
		travelerPassw = "a";
		
		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 5;
		ridePrice = 10;

		sdf = new SimpleDateFormat("dd/MM/yyyy");
		rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		traveler.setMoney(100);
		
		try {
			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();

			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, -5.5); // Descuento negativo
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRide(driver.getUsername(), rideFrom, rideTo, rideDate);
			testDA.removeDriver(driver.getUsername());
			testDA.close();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo el precio del viaje en negativo o en 0
	public void test5() {
		travelerName = "Paula";
		travelerPassw = "a";
		
		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 5;
		ridePrice = 0; // Precio a 0 (no es lógico)

		sdf = new SimpleDateFormat("dd/MM/yyyy");
		rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		traveler.setMoney(100);
		
		try {
			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();

			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRide(driver.getUsername(), rideFrom, rideTo, rideDate);
			testDA.removeDriver(driver.getUsername());
			testDA.close();
		}
	}
}
