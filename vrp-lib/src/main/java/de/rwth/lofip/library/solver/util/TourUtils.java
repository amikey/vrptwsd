package de.rwth.lofip.library.solver.util;

import org.apache.commons.math3.distribution.PoissonDistribution;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
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
public class TourUtils {
	
	static double currentTime;	

	/**
	 * This method calculates the cheapest insertion cost for the customer into
	 * the route. If no insertion can be found because any insertion would
	 * violate the time constraint for the following customers on the tour or
	 * would violate the capacity restriction of the vehicle, the method returns
	 * either null or an object with cost = Double.MAX_VALUE.
	 * 
	 * @param customer
	 * @param tour
	 * 
	 * @return
	 */
	public static CustomerWithCost calculateCost(Customer customer, Tour tour,
			double approximateEquality) {
		CustomerWithCost returnObject = new CustomerWithCost(customer, tour,
				Double.MAX_VALUE);
		// as a special case, the insertion cost for the first position (between
		// the depot and the currently first customer) needs to be calculated
		// upfront
		Customer firstCustomer = tour.getCustomers().get(0);
		double cost = Double.MAX_VALUE;
		int position = 0;
		// now set condition such that either a deterministic setting (demand
		// has to be smaller than capacity) or
		// probabilisitic (demand has to be smaller than twice vehicle capacity
		// with probability approximateEquality) solver is used.
		boolean condition;
		if (approximateEquality == 0)
			// case: deterministic solver
			condition = TourUtils
					.isInsertionPossibleWrtDeterministicDemandAndTW(customer,
							tour, position);
		else
			// case: probabilistic solver
			condition = TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(
					customer, tour, position, approximateEquality);
		if (condition) {
			cost = new Edge(tour.getDepot(), customer).getLength()
					+ new Edge(customer, firstCustomer).getLength()
					- new Edge(tour.getDepot(), firstCustomer).getLength();
			returnObject.setCost(cost);
			returnObject.setPosition(position);
		}

		for (Customer tourCustomer : tour.getCustomers()) {
			// calculate the cost for inserting the new customer after this
			// customer
			position++;
			AbstractPointInSpace nextCustomer = tour.getDepot();
			if (tour.getCustomers().size() > position) {
				nextCustomer = tour.getCustomers().get(position);
			}
			if (approximateEquality == 0)
				// case: deterministic solver
				condition = TourUtils
						.isInsertionPossibleWrtDeterministicDemandAndTW(
								customer, tour, position);
			else
				// case: probabilistic solver
				condition = TourUtils
						.isInsertionPossibleWrtStochasticDemandAndTW(customer,
								tour, position, approximateEquality);
			if (condition) {
				cost = new Edge(tourCustomer, customer).getLength()
						+ new Edge(customer, nextCustomer).getLength()
						- new Edge(tourCustomer, nextCustomer).getLength();
				if (cost < returnObject.getCost()) {
					returnObject.setCost(cost);
					returnObject.setPosition(position);
				}
			}
		}
		return returnObject;
	}

	/**
	 * Helper Method that calculates cheapes insertion cost for customer into
	 * route using the condition (demand on tour < vehicle capacity).
	 * 
	 * @param customer
	 * @param tour
	 * 
	 * @return
	 */
	public static CustomerWithCost calculateCostDeterministicSolver(
			Customer customer, Tour tour) {
		return calculateCost(customer, tour, 0);
	}

	public static CustomerWithCost calculateCostStochasticSolver(
			Customer customer, Tour tour, double approximateEquality) {
		return calculateCost(customer, tour, approximateEquality);
	}

	/**
	 * Check whether demand on tour does not exceed twice the vehicle capacity
	 * with probability @param approximateEquality.
	 * 
	 * Check, if an insertion at the given position into the given tour does
	 * violate the time window of the customer to be inserted or the time
	 * constraint for any of the following customers.
	 * 
	 * @param customer
	 * @param tour
	 * @param position
	 * @param approximateEquality
	 * @return boolean
	 */
	public static boolean isInsertionPossibleWrtStochasticDemandAndTW(
			Customer customer, Tour tour, int position,
			double approximateEquality) {

		// calculate the probability that the demand of all customers of the
		// tour - including the new one - is less than twice the vehicle
		// capacity
		long totalDemand = customer.getDemand();
		for (Customer c : tour.getCustomers()) {
			totalDemand += c.getDemand();
		}

		double cumulativeProbability = new PoissonDistribution(totalDemand)
				.cumulativeProbability(new Double(tour.getVehicle()
						.getCapacity()).intValue() * 2);
		if (cumulativeProbability < approximateEquality) {
			return false;
		}

		// now we can be sure that the customer won't lead to a too large
		// demand.

		// It is now needed to check if the insertion at the given position will
		// violate any time windows of the following customers.
		currentTime = 0;
		if (position > 0 && position <= tour.getCustomers().size()) {
			currentTime = tour.getCustomerAtPosition(position - 1)
					.getEarliestLeavingTime();
		}
		// now the distance from the previous customer to the to-be-inserted
		// customer
		if (position == 0) {
			currentTime += new Edge(tour.getDepot(), customer).getLength();
		}

		if (position > 0 && position <= tour.getCustomers().size()) {
			currentTime = currentTime
					+ new Edge(tour.getCustomerAtPosition(position - 1)
							.getCustomer(), customer).getLength();
		}
		// If the vehicle arrives AFTER the time window has closed, there is no
		// need to check any further.
		if (currentTime > customer.getTimeWindowClose()) {
			return false;
		}

		currentTime += Math.max(currentTime, customer.getTimeWindowOpen())
				+ customer.getServiceTime();

		// The currentTime now holds the earliest time the vehicle can leave
		// from the newly inserted customer.
		// Now check for the customers which come after the one to be inserted
		// if their time windows are still obeyed.

		if (position < tour.getCustomers().size()) {
			for (int i = position; i < tour.getCustomers().size(); i++) {
				Customer previousCustomer = customer;
				Customer currentCustomer = tour.getCustomers().get(i);
				currentTime = currentTime
						+ new Edge(previousCustomer, currentCustomer)
								.getLength();
				if (currentTime > currentCustomer.getTimeWindowClose()) {
					return false;
				}
				currentTime = tour.getCustomerAtPosition(i)
						.getEarliestLeavingTimeIfArrivalIsAt(currentTime);
				previousCustomer = currentCustomer;
			}

		}

		return true;
	}
	
	/**
	 * Check, if an insertion at the given position into the given tour does
	 * violate the time window of the customer to be inserted or the time
	 * constraint for any of the following customers. And check as well, if the
	 * deterministic demand of all customers in the tour - including the new one
	 * - is less than the vehicle capacity.
	 * 
	 * @param customerToBeInserted
	 * @param tour
	 * @param position
	 * @return boolean
	 */
	public static boolean isInsertionPossibleWrtDeterministicDemandAndTW(
			Customer customerToBeInserted, Tour tour, int position) {
		if (position > tour.getCustomers().size())
			throw new RuntimeException("isInsertionPossible wurde mit einem Index aufgerufen, der nicht in der Tour liegt.");
		
		if (demandIsGreaterThanCapacity(customerToBeInserted, tour))
			return false;

		if (areTimeWindowsViolatedAtCustomerToBeInserted(customerToBeInserted, tour, position))
			return false;
		
		if (areTimeWindowsViolatedAfterCustomerToBeInserted(customerToBeInserted, tour, position))
			return false;
		
		return true;
	}
	
		private static boolean demandIsGreaterThanCapacity(Customer customer, Tour tour) {
			long totalDemand = customer.getDemand();
			totalDemand += tour.getDemandOnTour();			
			double tourCapacity = new Double(tour.getVehicle().getCapacity());
			if (tourCapacity < totalDemand) 
				return true;
			else 
				return false;
		}
		
		private static boolean areTimeWindowsViolatedAtCustomerToBeInserted(Customer customerToBeInserted, Tour tour, int position) {
			AbstractPointInSpace previousPoint;
			if (position == 0) {
				currentTime = 0;
				previousPoint = tour.getDepot();			
			} else {
				currentTime = tour.getCustomerAtPosition(position - 1).getEarliestLeavingTime();
				previousPoint = tour.getCustomerAtPosition(position - 1).getCustomer();
			}					
			if (areTimeWindowsViolated(previousPoint, customerToBeInserted))
				return true;		
			// Set the currentTime so that it holds the earliest time the vehicle can leave from the newly inserted customer.		
			currentTime = Math.max(currentTime, customerToBeInserted.getTimeWindowOpen()) + customerToBeInserted.getServiceTime();
			return false;
		}

		private static boolean areTimeWindowsViolatedAfterCustomerToBeInserted(Customer customerToBeInserted, Tour tour, int position) {
			// Now check for the customers which come after the one to be inserted
			// if their time windows are still obeyed.
			Customer previousCustomer = customerToBeInserted;
			Customer currentCustomer;
			if (position < tour.getCustomers().size()) {
				for (int i = position; i < tour.getCustomers().size(); i++) {				
					currentCustomer = tour.getCustomers().get(i);
					if (areTimeWindowsViolated(previousCustomer, currentCustomer))
						return true;
					currentTime = tour.getCustomerAtPosition(i).getEarliestLeavingTimeIfArrivalIsAt(currentTime);
					previousCustomer = currentCustomer;
				}

			}
			return false;
		}

		private static boolean areTimeWindowsViolated(AbstractPointInSpace previousPoint, Customer currentCustomer) {
			currentTime += new Edge(previousPoint, currentCustomer).getLength();
			if (currentTime > currentCustomer.getTimeWindowClose()) 
				return true;
			else 
				return false;
		}

	/**
	 * Check, if the deterministic demand of all customers in the tour -
	 * including the new one - is less than the vehicle capacity.
	 * 
	 * @param customer
	 * @param tour
	 * @return boolean
	 * 
	 * @author Andreas Braun
	 */
	public static boolean isInsertionPossibleWrtDemand(Customer customer,
			Tour tour) {

		// check whether the demand of all customers of the tour
		// - including the new one - is less than the vehicle capacity

		long totalDemand = customer.getDemand();
		for (Customer c : tour.getCustomers()) {
			totalDemand += c.getDemand();
		}

		double tourCapacity = new Double(tour.getVehicle().getCapacity());
		if (tourCapacity < totalDemand) {
			return false;
		}
		// now we can be sure that the customer won't lead to a too large
		// demand.
		return true;
	}

	public static boolean isTourFeasibleWrtDemand(Tour tour) {

		// check whether the demand of all customers of the tour
		// is less than the vehicle capacity

		long totalDemand = 0;
		for (Customer c : tour.getCustomers()) {
			totalDemand += c.getDemand();
		}

		double tourCapacity = new Double(tour.getVehicle().getCapacity());
		if (tourCapacity < totalDemand) {
			return false;
		}
		// now we can be sure that the tour won't lead to a too large
		// demand.
		return true;
	}

	public static boolean isTourFeasibleWrtTW(Tour tour) {
		// check whether time window constraints are satisfied
		tour.recalculateTimes();
		boolean feasible = true;
		for (CustomerInTour c : tour.getCustomersInTour()) {			
			if (c.getArrivalTime() > c.getCustomer().getTimeWindowClose())
				feasible = false;
		}
		return feasible;
	}

	public static boolean isConcatenationOfRefsTWFeasible(ResourceExtensionFunction ref1, ResourceExtensionFunction ref2) {		
		double duration = new Edge(ref1.getLastCustomer(), ref2.getFirstCustomer()).getLength();
		return ref1.getEarliestDepartureTime() + duration <= ref2.getLatestArrivalTime(); 
	}

	protected static boolean isConcatenationOfRefsFeasible(
			ResourceExtensionFunction ref1, ResourceExtensionFunction ref2,
			ResourceExtensionFunction ref3, double capacity) {
		if (isConcatenationOfRefsTWFeasible(ref1, ref2) && isConcatenationOfRefsDemandFeasible(ref1,ref2,capacity)) {
			ResourceExtensionFunction ref12 = new ResourceExtensionFunction(ref1);
			ref12.updateWithSubsequentRef(ref2);
			return isConcatenationOfRefsTWFeasible(ref12,ref3) && isConcatenationOfRefsDemandFeasible(ref12,ref3,capacity); 
		} else 
			return false;			
	}

	private static boolean isConcatenationOfRefsDemandFeasible(
			ResourceExtensionFunction ref1, ResourceExtensionFunction ref2,
			double capacity) {
		return ref1.getDemand() + ref2.getDemand() <= capacity;
	}

	public static boolean isInsertionOfRefPossible(Tour tour,ResourceExtensionFunction ref, int position) {
		return isInsertionOfRefPossible(tour, ref, position, position);
		
	}

	public static boolean isInsertionOfRefPossible(Tour tour, ResourceExtensionFunction ref, int positionStartOfSegment, int positionEndOfSegment) {
		ResourceExtensionFunction ref1;
		if (positionStartOfSegment == 0)
			//first position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			ref1 = new ResourceExtensionFunction();
//			ref1 = new ResourceExtensionFunction(tour.getDepot());
		else 
			ref1 = tour.getRefFromBeginningAtPosition(positionStartOfSegment-1);
		ResourceExtensionFunction ref2 = ref;
		ResourceExtensionFunction ref3;
		if (positionEndOfSegment == tour.getCustomerSize())
			//last position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			//TODO: create new Ref with depot
			ref3 = new ResourceExtensionFunction();
			//ref3 = new ResourceExtensionFunction(tour.getDepot());
		else
			ref3 = tour.getRefToEndAtPosition(positionEndOfSegment);
		
		double capacity = tour.getVehicle().getCapacity();
		return isConcatenationOfRefsFeasible(ref1, ref2, ref3, capacity);		
	}

	public static boolean isInsertionPossibleWrtTW(Customer customerToBeInserted, Tour tour,int position) {
		if (position > tour.getCustomers().size())
			throw new RuntimeException("isInsertionPossible wurde mit einem Index aufgerufen, der nicht in der Tour liegt.");				

		if (areTimeWindowsViolatedAtCustomerToBeInserted(customerToBeInserted, tour, position))
			return false;
		
		if (areTimeWindowsViolatedAfterCustomerToBeInserted(customerToBeInserted, tour, position))
			return false;
		
		return true;
	}
}