package testMockBD;

import static org.junit.Assert.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
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
import domain.User;

public class GauzatuEragiketaMockBlackTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;
	@Mock
	protected EntityManager db;
	@Mock
	protected EntityTransaction et;

	@Mock
	protected TypedQuery<User> tq;

	@Mock
	TypedQuery<Double> typedQuery;

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

	private String username;
	private boolean deposit;
	private double amount;
	boolean op;

	@Test
	// username==null, the test should return false
	public void test1() {

		username = null;
		amount = 100.00;
		deposit = true;

		try {

			Mockito.when(db.createQuery("SELECT u from User u WHERE u.username = :username", User.class))
					.thenReturn(tq);
			Mockito.when(tq.getSingleResult()).thenReturn(null);

			sut.open();

			op = sut.gauzatuEragiketa(username, amount, deposit);

			assertFalse(op);

		} catch (Exception e) {

			fail();

		} finally {

			sut.close();

		}

	}

	/**
	 * amount=0 and amount<0 (debería de devolver false, pero como el método
	 * gauzatuEragiketa no comprueba si amount es igual a 0 o menor que 0, devuelve
	 * true y por eso falla )
	 */
	@Test
	public void test2() {

		username = "Unax";
		amount = 0.00;
		double amount2 = -10.00;
		deposit = true;

		User u = new User(username, "123456", "driver");
		// u.setMoney(100.00);

		try {

			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQuery);
			Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00);

			Mockito.when(db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
					.thenReturn(tq);
			Mockito.when(tq.setParameter("username", username)).thenReturn(tq);
			Mockito.when(tq.getSingleResult()).thenReturn(u);

			sut.open();

			op = sut.gauzatuEragiketa(username, amount, deposit);

			assertFalse(op);

			op = sut.gauzatuEragiketa(username, amount2, deposit);

			assertFalse(op);

		} catch (Exception e) {

			fail();

		} finally {

			sut.close();

		}

	}

	// the user does not exist in the database
	@Test
	public void test3() {

		username = "Marta";
		amount = 10.00;
		deposit = false;

		sut.open();

		try {

			Mockito.when(db.createQuery("SELECT u from User WHERE u.username = :username", User.class)).thenReturn(tq);
			Mockito.when(tq.getSingleResult()).thenReturn(null);

			op = sut.gauzatuEragiketa(username, amount, deposit);

			assertFalse(op);

		} catch (Exception e) {

			fail();
		} finally {

			sut.close();

		}

	}

	// the user exists in the database, the method should go perfectly and return
	// true
	@SuppressWarnings("unchecked")
	@Test
	public void test4() {

		username = "Zuri";
		amount = 10.00;
		deposit = true;

		User u = new User(username, "mypass", "driver");
		// u.setMoney(100.00);

		try {

			Mockito.when(db.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQuery);
			Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00);

			Mockito.when(db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class))
					.thenReturn(tq);
			Mockito.when(tq.setParameter("username", username)).thenReturn(tq);
			Mockito.when(tq.getSingleResult()).thenReturn(u);

			sut.open();

			op = sut.gauzatuEragiketa(username, amount, deposit);

			deposit = false;

			op = sut.gauzatuEragiketa(username, amount, deposit);

			assertTrue(op);

		} catch (Exception e) {

			fail();
		} finally {

			sut.close();

		}

	}
}
