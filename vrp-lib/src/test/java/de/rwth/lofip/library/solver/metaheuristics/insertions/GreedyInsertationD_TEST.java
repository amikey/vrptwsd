package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

public class GreedyInsertationD_TEST {

	@Test
	public void test() {
		// set up test instances
		Customer c19 = new Customer();
		c19.setxCoordinate(15);
		c19.setyCoordinate(80);
		c19.setCustomerNo(19);
		c19.setDemand(10);
		c19.setTimeWindowOpen(278);
		c19.setTimeWindowClose(345);
		c19.setServiceTime(90);

		Customer c16 = new Customer();
		c16.setCustomerNo(16);
		c16.setxCoordinate(20);
		c16.setyCoordinate(85);
		c16.setDemand(40);
		c16.setTimeWindowOpen(475);
		c16.setTimeWindowClose(528);
		c16.setServiceTime(90);

		Customer c12 = new Customer();
		c12.setCustomerNo(12);
		c12.setxCoordinate(25);
		c12.setyCoordinate(85);
		c12.setDemand(20);
		c12.setTimeWindowOpen(625);
		c12.setTimeWindowClose(721);
		c12.setServiceTime(90);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(50);

		Vehicle vehicle = new Vehicle(1, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle);

		Tour tour = new Tour(depot, vehicle);
		tour.addCustomer(c19);
		tour.addCustomer(c16);
		tour.addCustomer(c12);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c19);
		vrpProblem.addCustomer(c16);
		vrpProblem.addCustomer(c12);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		Solution solution = new Solution(vrpProblem);
		solution.addTour(tour);
		// End set up

		// assertTrue(false);
		// GreedyInsertationD greedyInsertD = new GreedyInsertationD();
		// greedyInsertD.insertCustomers(solution, customers, iteration,
		// configuration)

		// assertTrue(false);
	}
}
