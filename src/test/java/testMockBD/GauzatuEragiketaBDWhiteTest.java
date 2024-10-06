package testMockBD;

import static org.junit.Assert.*;

import org.junit.Test;

import data_access.DataAccess;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDWhiteTest {
	
	
	//sut:system under test
		 static DataAccess sut=new DataAccess();
		 
	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();
	
	
	 private String username;
	 private boolean deposit;
	 private double amount;
	 boolean op;
	 
	 
	 @Test
	 //user is null. The test should return false
	 public void test1() {
		 
		 username=null;
		 amount=25.00;
		 deposit=false;
		 
		 try {
			 
			 sut.open();
			 
			 op= sut.gauzatuEragiketa(username, amount, deposit);
			 
			 assertFalse(op);
			 

		 }catch(Exception e) {
			 
			 fail();
			 
		 }finally {
			 
			 sut.close();
			 
		 }
	 }
	 
	 
	 	
		 @Test
		//the user does not exist in the database
		 public void test2() {
			 
			 username="Marta";
			 amount=80.00;
			 deposit=true;
			 
			 sut.open();
			 
			 try {
				 
				 op= sut.gauzatuEragiketa(username, amount, deposit);
				 
				 assertFalse(op);
				 
			 }catch(Exception e) {
				 
				 fail();
			 }finally {
				 
				 sut.close();
				 
			 }
			 
			 
		 }
		 
		 
		 
		 @Test
		 //the user does not withdraw all the money from his account leaving its balance >0
		 public void test3() {
			 
			 username="Unax";
			 amount=5.00;
			 deposit=false;
			 
			 try {
				 
				 sut.open();
				 
				 
				 double dineroAntes= sut.getActualMoney(username);
				 
				 System.out.println("dinero antes del retiro: "+dineroAntes);
				 
				 op = sut.gauzatuEragiketa(username, amount, deposit);
				 
				 double dineroDespues= sut.getActualMoney(username);
				 
				 System.out.println("dinero despues del retiro: "+dineroDespues);
				 
				 assertTrue(dineroDespues>0);
				 
				 
			 }catch(Exception e) {
				 
				 fail();
			 }finally {
				 
				 deposit=true;
				 sut.gauzatuEragiketa(username, amount, deposit);
				 sut.close();
				 
			 }
			 
		 }
		 
		 
		 @Test
		 //the user does withdraws all the money from his account leaving its balance 0
		 public void test4() {
			 
			 username="Unax";
			 amount=100.00;
			 deposit=false;
			 
			 try {
				 
				 sut.open();
				 
				 double dineroAntes= sut.getActualMoney(username);
				 
				 System.out.println("dinero antes del retiro: "+dineroAntes);
				 
				 op = sut.gauzatuEragiketa(username, amount, deposit);
				 
				 
				 double dineroDespues= sut.getActualMoney(username);
				 
				 System.out.println("dinero despues del retiro: "+dineroDespues);
				 
				 assertTrue(dineroDespues==0);
				 
			 }catch(Exception e) {
				 
				 fail();
			 }finally {
				 
				 deposit=true;
				 sut.gauzatuEragiketa(username, amount, deposit);
				 sut.close();
				 
			 }
			 
		 }
		 
		 
		 @Test
		 //the user deposits money in his account successfully
		 public void test5() {
			 
			 username="Urtzi";
			 amount=25.00;
			 deposit=true;
			 
			 try {
				 
				 sut.open();
				 
				 double dineroAntes= sut.getActualMoney(username);
				 
				 System.out.println("dinero antes del ingreso: "+dineroAntes);
				 
				 op = sut.gauzatuEragiketa(username, amount, deposit);
				 
				 double dineroDespues= sut.getActualMoney(username);
				 
				 System.out.println("dinero despues del ingreso: "+dineroDespues);
				 
				 assertTrue(dineroDespues== dineroAntes+25.00);
				 
				 
				 
			 }catch(Exception e) {
				 
				 fail();
			 }finally {
				 
				 deposit=false;
				 sut.gauzatuEragiketa(username, amount, deposit);
				 sut.close();
				 
			 }
			 
		 }
		 
	

}
