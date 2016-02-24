package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.math.MathUtils;

/**
 * Static helper methods which calculate stuff related to tours. These are
 * extracted into a separate class and static to make it easier to parallelize
 * the usage.
 */

public class TourUtils {
	
	//CODE_SMELL_TODO: Ist diese statische Variable nicht auch eine total komische Idee?
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
	public static CustomerWithCost calculateCost(Customer customer, Tour tour) {
		CustomerWithCost returnObject = new CustomerWithCost(customer, tour,
				Double.MAX_VALUE);
		// as a special case, the insertion cost for the first position (between
		// the depot and the currently first customer) needs to be calculated
		// upfront
		Customer firstCustomer = tour.getCustomers().get(0);
		double cost = Double.MAX_VALUE;
		int position = 0;	
		boolean isInsertionPossible = TourUtils
				.isInsertionPossibleWrtDemandAndTWinLinearTime(customer,tour, position);	
		if (isInsertionPossible) {
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
			isInsertionPossible = TourUtils
					.isInsertionPossibleWrtDemandAndTWinLinearTime(
							customer, tour, position);			
			if (isInsertionPossible) {
				cost = new Edge(tourCustomer, customer).getLength()
						+ new Edge(customer, nextCustomer).getLength()
						- new Edge(tourCustomer, nextCustomer).getLength();
				if (MathUtils.lessThan(cost, returnObject.getCost())) {
					returnObject.setCost(cost);
					returnObject.setPosition(position);
				}
			}
		}
		return returnObject;
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
	public static boolean isInsertionPossibleWrtDemandAndTWinLinearTime(
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
			if (MathUtils.greaterThan(currentTime, currentCustomer.getTimeWindowClose())) 
				return true;
			else 
				return false;
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

	public static boolean isInsertionPossibleWrtDemand(Customer customer,
			Tour tour) {
		long totalDemand = customer.getDemand();
		for (Customer c : tour.getCustomers()) {
			totalDemand += c.getDemand();
		}
		double tourCapacity = new Double(tour.getVehicle().getCapacity());
		if (tourCapacity < totalDemand) {
			return false;
		}
		return true;
	}

	public static boolean isTourFeasibleWrtDemand(Tour tour) {
		//RUNTIME_TODO: hier auch variante erstellen, die das in O(1) checkt
		//DONE -> überall wo diese Variante verwendet wird auf Variante Check with Refs umstellen
		long totalDemand = 0;
		for (Customer c : tour.getCustomers()) {
			totalDemand += c.getDemand();
		}
		assertEquals(totalDemand, tour.getDemandOnTour(), 0.00001);
		double tourCapacity = new Double(tour.getVehicle().getCapacity());
		if (tourCapacity < totalDemand) {
			return false;
		}
		return true;
	}

	public static boolean isTourFeasibleWrtTW(Tour tour) {
		//RUNTIME_TODO: hier auch variante erstellen und einsetzen, die das in O(1) checkt
		// check whether time window constraints are satisfied
		tour.recalculateTimes();
		boolean feasible = true;
		for (CustomerInTour c : tour.getCustomersInTour()) {			
			if (MathUtils.greaterThan(c.getArrivalTime(), c.getCustomer().getTimeWindowClose()))
				feasible = false;
		}
		return feasible;
	}

	
	
	public static boolean isInsertionOfCustomerFeasibleTestWithRef(Tour tour, Customer customer, int position) {
		return isInsertionOfRefPossible(tour, new ResourceExtensionFunction(customer), position);
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
			return MathUtils.lessThan(ref1.getDemand() + ref2.getDemand(), capacity) || 
					MathUtils.equals(ref1.getDemand() + ref2.getDemand(), capacity);
		}

	public static boolean isInsertionOfRefPossible(Tour tour,ResourceExtensionFunction ref, int position) {
		return isInsertionOfRefPossible(tour, ref, position, position);
	}
	
	public static boolean isInsertionOfRefPossible(Tour tour, ResourceExtensionFunction ref, int positionStartOfSegment, int positionEndOfSegment) {
		ResourceExtensionFunction ref1;
		if (positionStartOfSegment == 0)
			//first position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			ref1 = new ResourceExtensionFunction(tour.getDepot());
		else 
			ref1 = tour.getRefFromBeginningAtPosition(positionStartOfSegment-1);
		ResourceExtensionFunction ref2 = ref;
		ResourceExtensionFunction ref3;
		if (positionEndOfSegment == tour.getCustomerSize())
			//last position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			ref3 = new ResourceExtensionFunction(tour.getDepot());
		else
			ref3 = tour.getRefToEndAtPosition(positionEndOfSegment);
		
		double capacity = tour.getVehicle().getCapacity();
		boolean feasible = isConcatenationOfRefsFeasible(ref1, ref2, ref3, capacity); 
		return feasible;
	}
	
	public static boolean isInsertionOfRefPossibleWrtDemand(Tour tour, ResourceExtensionFunction ref, int positionStartOfSegment, int positionEndOfSegment) {
		ResourceExtensionFunction ref1;
		if (positionStartOfSegment == 0)
			//first position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			ref1 = new ResourceExtensionFunction(tour.getDepot());
		else 
			ref1 = tour.getRefFromBeginningAtPosition(positionStartOfSegment-1);
		ResourceExtensionFunction ref2 = ref;
		ResourceExtensionFunction ref3;
		if (positionEndOfSegment == tour.getCustomerSize())
			//last position has to be treated separately because depot is not part of the array RefsFromBeginningAtPos; stupid design decision
			ref3 = new ResourceExtensionFunction(tour.getDepot());
		else
			ref3 = tour.getRefToEndAtPosition(positionEndOfSegment);
		
		double capacity = tour.getVehicle().getCapacity();
		return isConcatenationOfRefsDemandFeasible(ref1, ref2, ref3, capacity);		
	}
	
	
	private static boolean isConcatenationOfRefsDemandFeasible(
			ResourceExtensionFunction ref1, ResourceExtensionFunction ref2,
			ResourceExtensionFunction ref3, double capacity) {
		if (isConcatenationOfRefsDemandFeasible(ref1,ref2,capacity)) {
			ResourceExtensionFunction ref12 = new ResourceExtensionFunction(ref1);
			ref12.updateWithSubsequentRef(ref2);
			return isConcatenationOfRefsDemandFeasible(ref12,ref3,capacity); 
		} else 
			return false;			
	}


	public static boolean isInsertionOfRefPossibleWrtTWAndDemandInnerTourMove(Tour tour,
			int positionStartOfSegment, int positionEndOfSegment,
			int insertingPos) {		
		if (insertingPos < positionStartOfSegment) {
			//inserting position before removed segment
			ResourceExtensionFunction refUpToInsertingPosIncludingDepot = tour.getRefFromBeginningAtPosition(insertingPos-1);
			ResourceExtensionFunction refForSegmentThatIsSwapped = tour.getRefMatrix().get(positionEndOfSegment-1).get(positionStartOfSegment);
			ResourceExtensionFunction refFromInsertingPosToStartPosSegmentToBeSwapped = tour.getRefMatrix().get(positionStartOfSegment-1).get(insertingPos);
			ResourceExtensionFunction refFromEndOfSegToBeSwappedToEndOfTourIncludingDepot = tour.getRefToEndAtPosition(positionEndOfSegment);
			
			boolean possibleTW1 = isConcatenationOfRefsTWFeasible(refUpToInsertingPosIncludingDepot, refForSegmentThatIsSwapped);
			ResourceExtensionFunction refTemp = new ResourceExtensionFunction(refUpToInsertingPosIncludingDepot);
			refTemp.updateWithSubsequentRef(refForSegmentThatIsSwapped);
			
			boolean possibleTW2 = isConcatenationOfRefsTWFeasible(refTemp, refFromInsertingPosToStartPosSegmentToBeSwapped);
			refTemp.updateWithSubsequentRef(refFromInsertingPosToStartPosSegmentToBeSwapped);
			
			boolean possibleTW3 = isConcatenationOfRefsTWFeasible(refTemp, refFromEndOfSegToBeSwappedToEndOfTourIncludingDepot);
			boolean possibleDemand = isConcatenationOfRefsDemandFeasible(refTemp, refFromEndOfSegToBeSwappedToEndOfTourIncludingDepot, tour.getVehicle().getCapacity());
				
			return possibleTW1 && possibleTW2 && possibleTW3 && possibleDemand;
		} else {
			//inserting position after removed segment
			ResourceExtensionFunction refUpToStartOfSegmentToBeSwappedIncludingDepot = tour.getRefFromBeginningAtPosition(positionStartOfSegment-1);
			ResourceExtensionFunction refFromEndOfSegmentToBeSwappedToInsertingPos = tour.getRefMatrix().get(insertingPos-1).get(positionEndOfSegment);
			ResourceExtensionFunction refForSegmentThatIsSwapped = tour.getRefMatrix().get(positionEndOfSegment-1).get(positionStartOfSegment);
			ResourceExtensionFunction refFromInsertionPosToEndOfTourIncludingDepot = tour.getRefToEndAtPosition(insertingPos);
			
			boolean possible1 = isConcatenationOfRefsTWFeasible(refUpToStartOfSegmentToBeSwappedIncludingDepot, refFromEndOfSegmentToBeSwappedToInsertingPos);
			ResourceExtensionFunction refTemp = new ResourceExtensionFunction(refUpToStartOfSegmentToBeSwappedIncludingDepot);
			refTemp.updateWithSubsequentRef(refFromEndOfSegmentToBeSwappedToInsertingPos);
			
			boolean possible2 = isConcatenationOfRefsTWFeasible(refTemp, refForSegmentThatIsSwapped);
			refTemp.updateWithSubsequentRef(refForSegmentThatIsSwapped);
			
			boolean possible3 = isConcatenationOfRefsTWFeasible(refTemp, refFromInsertionPosToEndOfTourIncludingDepot);
			boolean possibleDemand = isConcatenationOfRefsDemandFeasible(refTemp, refFromInsertionPosToEndOfTourIncludingDepot, tour.getVehicle().getCapacity());	
			
			boolean overallPossible = possible1 && possible2 && possible3 && possibleDemand;
			return overallPossible;
		}		
	}

	public static boolean isTourFeasibleWrtDemandCheckWithRef(Tour tour) {
		double demand = tour.getDemandOnTour();
		double capacity = tour.getVehicle().getCapacity();
		return demand <= capacity;
	}

}