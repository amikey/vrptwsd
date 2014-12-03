package de.rwth.lofip.libray.misc;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.CustomerInTour;

/**
 * Test class which contains some tests to find out how certain things work
 * 
 * @author Andreas Braun
 */
public class Misc_TEST {

	// if you clone a solution, test whether tour ids equal each other
	// also test which number of customers in tour is returned
	@Test
	public void testCloneAndEqualsForSolutions() {
		// set up
		Customer c11 = new Customer();
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(11);
		c11.setDemand(10);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		Customer c12 = new Customer();
		c12.setCustomerNo(12);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(40);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

		Customer c13 = new Customer();
		c13.setCustomerNo(13);
		c13.setxCoordinate(25);
		c13.setyCoordinate(85);
		c13.setDemand(20);
		c13.setTimeWindowOpen(625);
		c13.setTimeWindowClose(721);
		c13.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new Tour(depot, vehicle);
		tour.addCustomer(c11);
		tour.addCustomer(c12);
		tour.addCustomer(c13);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c11);
		vrpProblem.addCustomer(c12);
		vrpProblem.addCustomer(c13);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		Solution solution = new Solution(vrpProblem);
		solution.addTour(tour);
		Solution solution2 = solution.clone();
		// End set up

		assertEquals(solution.getTours().get(0).getId(), solution2.getTours()
				.get(0).getId());
		// Note: Ids are the same :-)

		System.out.println("Customers in Tour laut tour.getCustomers().size() "
				+ tour.getCustomers().size());
		// Note: tour.getCustomers().size() is 3.
		for (CustomerInTour c : tour.getCustomersInTour())
			System.out.println("Position Customer "
					+ c.getCustomer().getCustomerNo() + ": " + c.getPosition());
		// Note:
		// Position Customer 11: 0
		// Position Customer 12: 1
		// Position Customer 13: 2
	}

}
