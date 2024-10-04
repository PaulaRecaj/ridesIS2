package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class BookRideBDBlackTest {
	// sut:system under test
	static DataAccess sut = new DataAccess();

	// additional operations needed to execute the test
	static TestDataAccess testDA = new TestDataAccess();

	@Test
	// //Se intenta reservar un ride metiendo parámetros null
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
		try {
			Traveler traveler = new Traveler(travelerName, travelerPassw);
			Driver driver = new Driver(driverName, driverPassw);
			driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			traveler.setMoney(100);

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, null, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo parámetros null dentro del ride
	public void test2() {
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
			Ride ride = new Ride(rideFrom, null, rideDate, ridePlaces, ridePrice, driver); // Ponemos null en el destino
			driver.addRide(rideFrom, rideTo, rideDate, ridePlaces, ridePrice);
			traveler.setMoney(100);

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo asientos que queremos en negativo o en 0
	public void test3() {
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
			traveler.setMoney(100);

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();
			
			sut.open();
			boolean res = sut.bookRide(travelerName, ride, -3, 1.5); // Asientos en negativo
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo un descuento negativo
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
			traveler.setMoney(100);

			sut.open();
			sut.updateTraveler(traveler); // Actualizamos el dinero
			sut.close();

			sut.open();
			boolean res = sut.bookRide(travelerName, ride, 3, -5.5); // Descuento negativo
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}

	@Test
	// Se intenta reservar un ride metiendo el precio del viaje en negativo o en 0
	public void test5() {
		String travelerName = "Paula";
		String travelerPassw = "a";

		String driverName = "DriverTest1";
		String driverPassw = "a";

		String rideFrom = "Donostia";
		String rideTo = "Agurain";
		int ridePlaces = 5;
		float ridePrice = 0; // Precio a 0 (no es lógico)

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
			boolean res = sut.bookRide(travelerName, ride, 3, 1.5);
			sut.close();

			assertFalse(res);

		} catch (Exception e) {
			sut.close();
			fail();
		}
	}
}
