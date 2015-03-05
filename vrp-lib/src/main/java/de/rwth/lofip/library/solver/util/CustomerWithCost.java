package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;

/**
 * A helper construct which holds the information of how much it costs to insert
 * the customer into the tour at the given position. Needed in some heuristics
 * to determine the cheapest insertion point.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class CustomerWithCost {
	private Customer customer;
	private double cost;
	private Tour tour;
	private int position;
	
	public CustomerWithCost(){}

	public CustomerWithCost(Customer customer, Tour tour, double cost) {
		this.customer = customer;
		this.tour = tour;
		this.cost = cost;
	}

	public CustomerWithCost(Customer customer, double cost) {
		this.customer = customer;
		this.cost = cost;
	}

	public Customer getCustomer() {
		return customer;
	}

	public double getCost() {
		return cost;
	}

	public Tour getTour() {
		return tour;
	}

	public int getPosition() {
		return position;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
