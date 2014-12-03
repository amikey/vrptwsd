package de.rwth.lofip.library.solver.util;

import org.apache.commons.math3.distribution.PoissonDistribution;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.CustomerInTour;

/**
 * Static helper methods which calculate stuff related to tours. These are
 * extracted into a separate class and static to make it easier to parallelize
 * the usage.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * @author Olga Bock
 */
public class TourUtilsGot {

	/**
	 * This method calculates the cheapest insertion cost for the customer into
	 * the route. If no insertion can be found because any insertion would
	 * violate the time constraint for the following customers on the tour or
	 * would violate the capacity restriction of the vehicle, the method returns
	 * either null or an object with cost = Double.MAX_VALUE.
	 * 
	 * @param customer
	 * @param tour
	 * @param got 
	 * 
	 * @return
	 */
	public static CustomerWithCost calculateCost(Customer customer, Tour tour,
			GroupOfTours got, double approximateEquality) {
		CustomerWithCost cwc = new CustomerWithCost(customer, tour,
				Double.MAX_VALUE);
		// as a special case, the insertion cost for the first position (between
		// the depot and the currently first customer) needs to be calculated
		// upfront
		Customer firstCustomer = tour.getCustomers().get(0);
		double detCost = Double.MAX_VALUE;
		double recCost = Double.MAX_VALUE;
		double cost = Double.MAX_VALUE;
		int position = 0;
		if (TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(
				customer, tour, position, approximateEquality)) {
			detCost = new Edge(tour.getDepot(), customer).getLength()
					+ new Edge(customer, firstCustomer).getLength()
					- new Edge(tour.getDepot(), firstCustomer).getLength();
			recCost = GotUtils.calculateRecourseGot(got, tour, firstCustomer, position);
			cost = detCost + recCost;
			cwc.setCost(cost);
			cwc.setPosition(position);
		}

		for (Customer tourCustomer : tour.getCustomers()) {
			// calculate the cost for inserting the new customer after this
			// customer
			position++;
			AbstractPointInSpace nextCustomer = tour.getDepot();
			if (tour.getCustomers().size() > position) {
				nextCustomer = tour.getCustomers().get(position);
			}
			if (TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer,
							tour, position, approximateEquality)) {
				detCost = new Edge(tourCustomer, customer).getLength()
						+ new Edge(customer, nextCustomer).getLength()
						- new Edge(tourCustomer, nextCustomer).getLength();
				recCost = GotUtils.calculateRecourseGot(got, tour, tourCustomer, position);
				cost = detCost + recCost;
				if (cost < cwc.getCost()) {
					cwc.setCost(cost);
					cwc.setPosition(position);
				}
			}
		}
		return cwc;
	}

	public static CustomerWithCost calculateCostStochasticSolver(
			Customer customer, Tour tour, GroupOfTours got, double approximateEquality) {
		return calculateCost(customer, tour, got, approximateEquality);
	}
	
}