package testMockBD;

import static org.junit.Assert.*;

import org.junit.Test;

import data_access.DataAccess;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDBlackTest {
	/*
	 * //sut:system under test static DataAccess sut=new DataAccess();
	 * 
	 * //additional operations needed to execute the test static TestDataAccess
	 * testDA=new TestDataAccess();
	 * 
	 * private String username; private boolean deposit; private double amount;
	 * boolean op;
	 * 
	 * 
	 * @Test //username==null, the test should return false public void test1() {
	 * 
	 * username=null; amount=100.00; deposit=true;
	 * 
	 * try {
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
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 *//**
		 * amount=0 and amount<0 (debería de devolver false, pero como el método
		 * gauzatuEragiketa no comprueba si amount es igual a 0 o menor que 0, devuelve
		 * true y por eso falla )
		 *//*
			 * @Test public void test2() {
			 * 
			 * username="Urtzi"; amount=0.00; double amount2 = -10.00; deposit= true;
			 * 
			 * 
			 * try {
			 * 
			 * sut.open();
			 * 
			 * op = sut.gauzatuEragiketa(username, amount, deposit);
			 * 
			 * 
			 * assertFalse(op);
			 * 
			 * 
			 * op = sut.gauzatuEragiketa(username, amount2, deposit);
			 * 
			 * assertFalse(op);
			 * 
			 * 
			 * 
			 * }catch(Exception e){
			 * 
			 * fail();
			 * 
			 * }finally{
			 * 
			 * sut.close();
			 * 
			 * }
			 * 
			 * }
			 * 
			 * 
			 * 
			 * 
			 * //the user does not exist in the database
			 * 
			 * @Test public void test3() {
			 * 
			 * username="Marta"; amount=10.00; deposit=false;
			 * 
			 * sut.open();
			 * 
			 * try {
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
			 * //the user exists in the database, the method should go perfectly and return
			 * true
			 * 
			 * @Test public void test4() {
			 * 
			 * username="Zuri"; amount= 10.00; deposit=true;
			 * 
			 * try {
			 * 
			 * sut.open();
			 * 
			 * op = sut.gauzatuEragiketa(username, amount, deposit);
			 * 
			 * assertTrue(op);
			 * 
			 * 
			 * deposit= false;
			 * 
			 * op = sut.gauzatuEragiketa(username, amount, deposit);
			 * 
			 * assertTrue(op);
			 * 
			 * 
			 * 
			 * }catch(Exception e) {
			 * 
			 * fail(); }finally {
			 * 
			 * sut.close();
			 * 
			 * }
			 * 
			 * }
			 */

}
