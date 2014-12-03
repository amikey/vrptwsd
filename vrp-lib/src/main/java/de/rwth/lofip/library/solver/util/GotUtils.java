package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;

public class GotUtils {
	
	public static void insertCustomerIntoTourInGot(GroupOfTours gotToBeModified, Tour tour, Customer customer, int position) {
		Tour t = gotToBeModified.identifyTour(tour);
		t.insertCustomerAtPosition(customer, position);		
	}

	public static double calculateRecourseGot(GroupOfTours got, Tour tour,
			Customer customer, int position) {
		GroupOfTours originalGot = got;
    	GroupOfTours gotToBeModified = originalGot.clone();
    	GotUtils.insertCustomerIntoTourInGot(gotToBeModified, tour, customer, position);
    	double recCost = gotToBeModified.getExpectedRecourseCost() - originalGot.getExpectedRecourseCost();
    	return recCost;
	}

}
