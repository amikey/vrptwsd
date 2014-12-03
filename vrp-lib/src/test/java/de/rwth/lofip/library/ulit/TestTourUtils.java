package de.rwth.lofip.library.ulit;
import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.TourUtils;

public class TestTourUtils {

	@Test
	public void testInsertionPossible() {

		Customer c6 = new Customer(25, 30);
		c6.setDemand(3);
		c6.setTimeWindowOpen(89);
		c6.setTimeWindowClose(119);
		c6.setServiceTime(10);

		Customer c2 = new Customer(35, 17);
		c2.setDemand(7);
		c2.setTimeWindowOpen(40);
		c2.setTimeWindowClose(70);
		c2.setServiceTime(10);

		Depot depot = new Depot();
		depot.setxCoordinate(35);
		depot.setyCoordinate(35);

		Tour tour = new Tour(depot, new Vehicle(1, 50));
		tour.addCustomer(c2);

		boolean insertionPossible = TourUtils
				.isInsertionPossibleWrtStochasticDemandAndTW(c6, tour, 0,
						0.99999);
		// should fail because the time window of c2 is no longer valid
		assert (insertionPossible == false);

		tour = new Tour(depot, new Vehicle(1, 50));
		tour.addCustomer(c6);
		insertionPossible = TourUtils
				.isInsertionPossibleWrtStochasticDemandAndTW(c2, tour, 1,
						0.99999);
		assert (insertionPossible == false);
	}
}
