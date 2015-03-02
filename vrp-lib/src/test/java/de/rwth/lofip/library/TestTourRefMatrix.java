package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestTourRefMatrix {

private Tour tour;
	
		@Test 
	public void testInsertCustomersAtPostion() {
		whenGivenOneEmptyTour();
		thenRefMatrixShouldBeEmpty();
		whenInsertingCustomer1();
		thenRefMatrixShouldContainCustomer1();
		whenInsertingCustomer2();
		thenRefMatrixShouldContainCustomers1And2();
		whenInsertingCustomer3();
		thenRefMatrixShouldContainCustomers1And2And3();
		whenDeletingCustomer2();
		thenRefMatrixShouldContainCustomer1And3();
	}	

	private void whenGivenOneEmptyTour() {
		tour = SetUpUtils.getEmptyTour();		
	}
	
	private void thenRefMatrixShouldBeEmpty() {
		assertEquals(true, tour.getRefMatrix().isEmpty());
	}
	
	private void whenInsertingCustomer1() {
		List<Customer> customers = new LinkedList<Customer>();
		customers.add(SetUpUtils.getC1());		
		tour.insertCustomersAtPosition(customers, 0);
	}
	
	private void thenRefMatrixShouldContainCustomer1() {
		assertEquals(true,tour.getRefMatrix().get(0).get(0).equals(new ResourceExtensionFunction(SetUpUtils.getC1())));		 
	}
	
	private void whenInsertingCustomer2() {
		tour.addCustomer(SetUpUtils.getC2());
	}
	
	private void thenRefMatrixShouldContainCustomers1And2() {		
		assertEquals(true,tour.getRefMatrix().get(0).get(0).equals(new ResourceExtensionFunction(SetUpUtils.getC1())));
		List<Customer> customers = new LinkedList<Customer>();
		customers.add(SetUpUtils.getC1());		
		customers.add(SetUpUtils.getC2());		
		assertEquals(true,tour.getRefMatrix().get(1).get(0).equals(new ResourceExtensionFunction(customers)));
		assertEquals(true,tour.getRefMatrix().get(1).get(1).equals(new ResourceExtensionFunction(SetUpUtils.getC2())));		
	}
	
	private void whenInsertingCustomer3() {	
		tour.addCustomer(SetUpUtils.getC3());
	}
	
	private void thenRefMatrixShouldContainCustomers1And2And3() {			
		assertEquals(true,tour.getRefMatrix().get(0).get(0).equals(new ResourceExtensionFunction(SetUpUtils.getC1())));
		List<Customer> customers = new LinkedList<Customer>();
		customers.add(SetUpUtils.getC1());		
		customers.add(SetUpUtils.getC2());		
		assertEquals(true,tour.getRefMatrix().get(1).get(0).equals(new ResourceExtensionFunction(customers)));
		assertEquals(true,tour.getRefMatrix().get(1).get(1).equals(new ResourceExtensionFunction(SetUpUtils.getC2())));
		
		customers.add(SetUpUtils.getC3());
		assertEquals(true, tour.getRefMatrix().get(2).get(0).equals(new ResourceExtensionFunction(customers)));
		
		customers.remove(0);
		assertEquals(true, tour.getRefMatrix().get(2).get(1).equals(new ResourceExtensionFunction(customers)));
		assertEquals(true, tour.getRefMatrix().get(2).get(2).equals(new ResourceExtensionFunction(SetUpUtils.getC3())));
	}
	
	private void whenDeletingCustomer2() {
		tour.removeCustomerAtPosition(1);	
	}
	
	private void thenRefMatrixShouldContainCustomer1And3() {
		assertEquals(true,tour.getRefMatrix().get(0).get(0).equals(new ResourceExtensionFunction(SetUpUtils.getC1())));
		List<Customer> customers = new LinkedList<Customer>();
		customers.add(SetUpUtils.getC1());		
		customers.add(SetUpUtils.getC3());
		assertEquals(true,tour.getRefMatrix().get(1).get(0).equals(new ResourceExtensionFunction(customers)));
		assertEquals(true,tour.getRefMatrix().get(1).get(1).equals(new ResourceExtensionFunction(SetUpUtils.getC3())));
	}
}
