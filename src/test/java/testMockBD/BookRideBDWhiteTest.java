package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

public class BookRideBDWhiteTest {
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
	// Se reserva un ride exitosamente
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
			
			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			System.out.println(sut.getRides(rideFrom, rideTo, rideDate));
			sut.close();

			assertTrue(res);
			System.out.println("Resultado de bookRide: " + res);
			
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
	// Se intenta reservar un ride con un traveler no registrado
	public void test2() {
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
		try {
			testDA.open();
			driver = testDA.addDriverWithRide("TestDriver", rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			ride = driver.getCreatedRides().get(0);
			ride.setnPlaces(ridePlaces);
			testDA.close();
			
			sut.open();
			boolean res = sut.bookRide("Pepe", ride, 3, 1.5);
			sut.close();

			assertFalse(res);
			System.out.println("Resultado de bookRide: " + res);

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
	// Se intenta reservar un ride con asientos insuficientes
	public void test3() {
		travelerName = "Paula";
		travelerPassw = "a";

		rideFrom = "Donostia";
		rideTo = "Agurain";
		ridePlaces = 1; // Ponemos que solo hay 1 asiento disponible
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
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5); // Queremos reservar 3 asientos, pero no hay
			sut.close();

			assertFalse(res);
			System.out.println("Resultado de bookRide: " + res);

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
	// Se intenta reservar un ride sin dinero suficiente
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
		traveler.setMoney(5); // Ponemos dinero insuficiente
		
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
			System.out.println("Resultado de bookRide: " + res);

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
