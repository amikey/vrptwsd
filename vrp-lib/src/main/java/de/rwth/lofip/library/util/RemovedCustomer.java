package de.rwth.lofip.library.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.solver.metaheuristics.removals.RemovalInterface;

/**
 * A {@code RemovedCustomer} holds additional information about a customer who
 * got removed in a {@link RemovalInterface}. Besides the customer itself it
 * also keeps track of the {@code Tour} and the position the customer got
 * removed from. This is needed to later be able to prevent re-insertion; this
 * logic is used e.g. by TabuSearch.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class RemovedCustomer {

	private Customer customer;
	private long tourId;
	private int position;

	public RemovedCustomer() {
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public long getTourId() {
		return tourId;
	}

	public void setTourId(long tourId) {
		this.tourId = tourId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
