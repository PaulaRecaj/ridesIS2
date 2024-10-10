package testMockBD;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import domain.User;

public class GauzatuEragiketaMockWhiteTest {/*
											 * 
											 * static DataAccess sut;
											 * 
											 * protected MockedStatic<Persistence> persistenceMock;
											 * 
											 * @Mock protected EntityManagerFactory entityManagerFactory;
											 * 
											 * @Mock protected EntityManager db;
											 * 
											 * @Mock protected EntityTransaction et;
											 * 
											 * @Mock protected TypedQuery<User> tq;
											 * 
											 * @Mock TypedQuery<Double> typedQuery;
											 * 
											 * @Before public void init() { MockitoAnnotations.openMocks(this);
											 * persistenceMock = Mockito.mockStatic(Persistence.class);
											 * persistenceMock.when(() ->
											 * Persistence.createEntityManagerFactory(Mockito.any()))
											 * .thenReturn(entityManagerFactory);
											 * 
											 * Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
											 * Mockito.doReturn(et).when(db).getTransaction(); sut=new DataAccess(db); }
											 * 
											 * @After public void tearDown() { persistenceMock.close(); }
											 * 
											 * 
											 * private String username; private boolean deposit; private double amount;
											 * boolean op;
											 * 
											 * 
											 * @Test //user is null. The test should return false public void test1() {
											 * 
											 * username=null; amount=25.00; deposit=false;
											 * 
											 * try {
											 * 
											 * Mockito.when(db.
											 * createQuery("SELECT u FROM User u WHERE u.username = :username",
											 * User.class)).thenReturn(tq);
											 * Mockito.when(tq.getSingleResult()).thenReturn(null);
											 * 
											 * sut.open();
											 * 
											 * op= sut.gauzatuEragiketa(username, amount, deposit);
											 * 
											 * assertFalse(op);
											 * 
											 * 
											 * }catch(Exception e) {
											 * 
											 * fail();
											 * 
											 * }finally {
											 * 
											 * sut.close();
											 * 
											 * } }
											 * 
											 * 
											 * 
											 * @Test //the user does not exist in the database public void test2() {
											 * 
											 * username="Marta"; amount=80.00; deposit=true;
											 * 
											 * sut.open();
											 * 
											 * try {
											 * 
											 * Mockito.when(db.
											 * createQuery("SELECT u FROM User u WHERE u.username = :username",
											 * User.class)).thenReturn(tq);
											 * Mockito.when(tq.getSingleResult()).thenReturn(null);
											 * 
											 * op= sut.gauzatuEragiketa(username, amount, deposit);
											 * 
											 * assertFalse(op);
											 * 
											 * }catch(Exception e) {
											 * 
											 * fail(); }finally {
											 * 
											 * sut.close();
											 * 
											 * }
											 * 
											 * 
											 * }
											 * 
											 * 
											 * 
											 * @Test //the user does not withdraw all the money from his account leaving
											 * its balance >0 public void test3() {
											 * 
											 * username="Unax"; amount=5.00; deposit=false;
											 * 
											 * User u = new User(username, "7894", "driver"); //u.setMoney(100.00);
											 * 
											 * try {
											 * 
											 * Mockito.when(db.createQuery(Mockito.anyString(),
											 * Mockito.any(Class.class))).thenReturn(typedQuery);
											 * Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00);
											 * 
											 * Mockito.when(db.
											 * createQuery("SELECT u FROM User u WHERE u.username = :username",
											 * User.class)).thenReturn(tq); Mockito.when(tq.setParameter("username",
											 * username)).thenReturn(tq);
											 * Mockito.when(tq.getSingleResult()).thenReturn(u);
											 * 
											 * 
											 * sut.open();
											 * 
											 * double dineroAntes= sut.getActualMoney(username);
											 * 
											 * System.out.println("dinero antes del retiro: "+dineroAntes);
											 * 
											 * op = sut.gauzatuEragiketa(username, amount, deposit);
											 * 
											 * double dineroDespues= sut.getActualMoney(username);
											 * 
											 * System.out.println("dinero despues del retiro: "+dineroDespues);
											 * 
											 * assertTrue(dineroDespues>0);
											 * 
											 * 
											 * }catch(Exception e) {
											 * 
											 * fail(); }finally {
											 * 
											 * 
											 * deposit=true; sut.gauzatuEragiketa(username, amount, deposit);
											 * sut.close();
											 * 
											 * }
											 * 
											 * }
											 * 
											 * 
											 * @Test //the user does withdraws all the money from his account leaving
											 * its balance 0 public void test4() {
											 * 
											 * username="Unax"; amount=100.00; deposit=false;
											 * 
											 * User u = new User(username, "7894", "driver"); //u.setMoney(100.00);
											 * 
											 * try {
											 * 
											 * Mockito.when(db.createQuery(Mockito.anyString(),
											 * Mockito.any(Class.class))).thenReturn(typedQuery);
											 * Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00);
											 * 
											 * Mockito.when(db.
											 * createQuery("SELECT u FROM User u WHERE u.username = :username",
											 * User.class)).thenReturn(tq); Mockito.when(tq.setParameter("username",
											 * username)).thenReturn(tq);
											 * Mockito.when(tq.getSingleResult()).thenReturn(u);
											 * 
											 * sut.open();
											 * 
											 * double dineroAntes= sut.getActualMoney(username); //100
											 * 
											 * System.out.println("dinero antes del retiro: "+dineroAntes);
											 * 
											 * op = sut.gauzatuEragiketa(username, amount, deposit);
											 * 
											 * Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00-amount);
											 * 
											 * double dineroDespues= sut.getActualMoney(username);
											 * 
											 * System.out.println("dinero despues del retiro: "+dineroDespues);
											 * 
											 * assertTrue(dineroDespues==0);
											 * 
											 * }catch(Exception e) {
											 * 
											 * fail(); }finally {
											 * 
											 * deposit=true; sut.gauzatuEragiketa(username, amount, deposit);
											 * sut.close();
											 * 
											 * }
											 * 
											 * }
											 * 
											 * 
											 * @Test //the user deposits money in his account successfully public void
											 * test5() {
											 * 
											 * username="Urtzi"; amount=25.00; deposit=true;
											 * 
											 * User u = new User(username, "7894", "driver"); //u.setMoney(100.00);
											 * 
											 * try {
											 * 
											 * Mockito.when(db.createQuery(Mockito.anyString(),
											 * Mockito.any(Class.class))).thenReturn(typedQuery);
											 * Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00);
											 * 
											 * Mockito.when(db.
											 * createQuery("SELECT u FROM User u WHERE u.username = :username",
											 * User.class)).thenReturn(tq); Mockito.when(tq.setParameter("username",
											 * username)).thenReturn(tq);
											 * Mockito.when(tq.getSingleResult()).thenReturn(u);
											 * 
											 * sut.open();
											 * 
											 * double dineroAntes= sut.getActualMoney(username);;
											 * 
											 * System.out.println("dinero antes del ingreso: "+dineroAntes);
											 * 
											 * op = sut.gauzatuEragiketa(username, amount, deposit);
											 * 
											 * Mockito.when(typedQuery.getSingleResult()).thenReturn(100.00+amount);
											 * 
											 * double dineroDespues= sut.getActualMoney(username);
											 * 
											 * System.out.println("dinero despues del ingreso: "+dineroDespues);
											 * 
											 * assertTrue(dineroDespues== dineroAntes+25.00);
											 * 
											 * 
											 * 
											 * }catch(Exception e) {
											 * 
											 * fail(); }finally {
											 * 
											 * deposit=false; sut.gauzatuEragiketa(username, amount, deposit);
											 * sut.close();
											 * 
											 * }
											 * 
											 * }
											 */

}
