package de.rwth.lofip.library.scenario;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;

public class DemandScenarioUtils_TEST {

	Customer c11 = new Customer();
	Customer c12 = new Customer();
	Customer c13 = new Customer();
	List<Solution> solutionList = new LinkedList<Solution>();
	Solution solution;
	VrpProblem vrpProblem = new VrpProblem();

	@Before
	public void setUp() {
		// set up
		c11.setxCoordinate(15);
		c11.setyCoordinate(80);
		c11.setCustomerNo(11);
		c11.setDemand(10);
		c11.setTimeWindowOpen(278);
		c11.setTimeWindowClose(345);
		c11.setServiceTime(90);

		c12.setCustomerNo(12);
		c12.setxCoordinate(20);
		c12.setyCoordinate(85);
		c12.setDemand(40);
		c12.setTimeWindowOpen(475);
		c12.setTimeWindowClose(528);
		c12.setServiceTime(90);

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
		vrpProblem.addCustomer(c11);
		vrpProblem.addCustomer(c12);
		vrpProblem.addCustomer(c13);
		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		solution = new Solution(vrpProblem);
		solution.addTour(tour);
		// End set up
	}

	@Test
	public void createScenarioFromVrpProblem_TEST() {
		DemandScenario demandScenario = DemandScenarioUtils
				.createScenarioFromVrpProblem(vrpProblem, 0.2);
		System.out.println(demandScenario.getDemandScenarioAsString());
	}

}
