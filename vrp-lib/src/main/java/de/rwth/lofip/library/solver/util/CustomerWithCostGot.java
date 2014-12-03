package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;

public class CustomerWithCostGot extends CustomerWithCost {
	
	private GroupOfTours got;
	
	public CustomerWithCostGot() {}
	
	public CustomerWithCostGot(Customer customer, double cost) {
		super(customer, cost);
	}
		
	public CustomerWithCostGot(Customer customer, Tour tour, double cost) {
		super(customer, tour, cost);
	}

	public void setGot(GroupOfTours got) {
		this.got = got;  
	}
	
	public GroupOfTours getGot() {
		return got;
	}

}
