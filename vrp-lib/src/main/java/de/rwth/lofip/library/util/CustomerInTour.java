package de.rwth.lofip.library.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;

/**
 * Wrapper class which keeps the associated information about a customer in the
 * tour. Make sure to update all information when inserting, removing or
 * changing a customer.<br>
 * <br>
 * Extended Version: With a variable "isFixedCustomer", to mark the customer in
 * the tour as fixed (= true), if he should not be changed in the solution.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
*/

public class CustomerInTour implements Cloneable, SolutionElement {

	private Customer customer;
	private Tour tour;			
	private double arrivalTime;
		
	public CustomerInTour(Tour tour) {
		this.tour = tour;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getEarliestLeavingTime() {
		return getEarliestLeavingTimeIfArrivalIsAt(arrivalTime);
	}

	public double getEarliestLeavingTimeIfArrivalIsAt(double arrivalTime) {
		return Math.max(arrivalTime, customer.getTimeWindowOpen())
				+ customer.getServiceTime();
	}

	protected CustomerInTour clone() {
		CustomerInTour cloned = new CustomerInTour(tour);
		cloned.setCustomer(customer);
		cloned.setArrivalTime(arrivalTime);				
		return cloned;
	}

	public void print() {
		System.out.println(customer.getCustomerNo());
	}

}
