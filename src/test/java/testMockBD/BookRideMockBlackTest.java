package testMockBD;

import static org.junit.Assert.assertFalse;
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

public class BookRideMockBlackTest {

	static DataAccess sut;
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;

	@Mock
	protected EntityManager db;

	@Mock
	protected EntityTransaction et;

	@Mock
	protected TypedQuery<Traveler> query;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
				.thenReturn(entityManagerFactory);
		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut = new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	@Test
	// Se intenta reservar un ride metiendo parámetros null
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

			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(query);
			Mockito.when(query.setParameter("username", "user1")).thenReturn(query);
			Mockito.when(query.getResultList()).thenReturn(Arrays.asList(traveler));

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

			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(query);
			Mockito.when(query.setParameter("username", "user1")).thenReturn(query);
			Mockito.when(query.getResultList()).thenReturn(Arrays.asList(traveler));

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

			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(query);
			Mockito.when(query.setParameter("username", "user1")).thenReturn(query);
			Mockito.when(query.getResultList()).thenReturn(Arrays.asList(traveler));

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

			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(query);
			Mockito.when(query.setParameter("username", "user1")).thenReturn(query);
			Mockito.when(query.getResultList()).thenReturn(Arrays.asList(traveler));

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

			Mockito.when(db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class))
					.thenReturn(query);
			Mockito.when(query.setParameter("username", "user1")).thenReturn(query);
			Mockito.when(query.getResultList()).thenReturn(Arrays.asList(traveler));

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
