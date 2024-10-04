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

	@Test
	// Se reserva un ride exitosamente
	public void test1() {
		String travelerName = "Paula";
		String travelerPassw = "a";

		String driverName = "DriverTest1";
		String driverPassw = "a";

		String rideFrom = "Donostia";
		String rideTo = "Agurain";
		int ridePlaces = 5;
		float ridePrice = 10;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Traveler traveler = new Traveler(travelerName, travelerPassw);
		Driver driver = new Driver(driverName, driverPassw);
		Ride ride = new Ride(rideFrom, rideTo, rideDate, ridePlaces, ridePrice, driver);

		driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
		traveler.setMoney(100);
		try {

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertTrue(res);
			System.out.println("Resultado de bookRide: " + res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride con un traveler no registrado
	public void test2() {
		String driverName = "DriverTest1";
		String driverPassw = "a";

		String rideFrom = "Donostia";
		String rideTo = "Agurain";
		int ridePlaces = 5;
		float ridePrice = 10;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Driver driver = new Driver(driverName, driverPassw);
			Ride ride = new Ride(rideFrom, rideTo, rideDate, ridePlaces, ridePrice, driver);
			driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);

			sut.open();
			boolean res = sut.bookRide("Pepe", ride, 3, 1.5);
			sut.close();

			assertFalse(res);
			System.out.println("Resultado de bookRide: " + res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride con asientos insuficientes
	public void test3() {
		String travelerName = "Paula";
		String travelerPassw = "a";

		String driverName = "DriverTest1";
		String driverPassw = "a";

		String rideFrom = "Donostia";
		String rideTo = "Agurain";
		int ridePlaces = 1; // Ponemos que solo hay 1 asiento disponible
		float ridePrice = 10;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Traveler traveler = new Traveler(travelerName, travelerPassw);
			Driver driver = new Driver(driverName, driverPassw);
			Ride ride = new Ride(rideFrom, rideTo, rideDate, ridePlaces, ridePrice, driver);
			driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			traveler.setMoney(100);

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();

			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5); // Queremos reservar 3 asientos, pero no hay
			sut.close();

			assertFalse(res);
			System.out.println("Resultado de bookRide: " + res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride sin dinero suficiente
	public void test4() {
		String travelerName = "Paula";
		String travelerPassw = "a";

		String driverName = "DriverTest1";
		String driverPassw = "a";

		String rideFrom = "Donostia";
		String rideTo = "Agurain";
		int ridePlaces = 5;
		float ridePrice = 10;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date rideDate = null;
		try {
			rideDate = sdf.parse("22/12/2024");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Traveler traveler = new Traveler(travelerName, travelerPassw);
			Driver driver = new Driver(driverName, driverPassw);
			Ride ride = new Ride(rideFrom, rideTo, rideDate, ridePlaces, ridePrice, driver);
			driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			traveler.setMoney(5); // Ponemos dinero insuficiente

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();

			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertFalse(res);
			System.out.println("Resultado de bookRide: " + res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
}
