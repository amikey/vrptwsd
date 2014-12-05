package de.rwth.lofip.library.scenario;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.DemandScenario;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.Event;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.PenetrationOfTheMarketScenario;

public class POMS_Test {

	@Test
	public void testCreatNewCustomer() throws IOException {
		Customer c3 = new Customer();
		Customer c4 = new Customer();
		Customer c5 = new Customer();

		Customer c6 = new Customer();
		Customer c7 = new Customer();

		Solution solution;
		// set up
		c3.setxCoordinate(30);
		c3.setyCoordinate(40);
		c3.setCustomerNo(1);
		c3.setDemand(10);
		c3.setTimeWindowOpen(15);
		c3.setTimeWindowClose(45);
		c3.setServiceTime(10);

		c4.setCustomerNo(2);
		c4.setxCoordinate(38);
		c4.setyCoordinate(45);
		c4.setDemand(20);
		c4.setTimeWindowOpen(100);
		c4.setTimeWindowClose(140);
		c4.setServiceTime(15);

		c5.setCustomerNo(3);
		c5.setxCoordinate(42);
		c5.setyCoordinate(40);
		c5.setDemand(30);
		c5.setTimeWindowOpen(325);
		c5.setTimeWindowClose(500);
		c5.setServiceTime(90);

		c6.setxCoordinate(50);
		c6.setyCoordinate(40);
		c6.setCustomerNo(4);
		c6.setDemand(15);
		c6.setTimeWindowOpen(90);
		c6.setTimeWindowClose(125);
		c6.setServiceTime(30);

		c7.setCustomerNo(5);
		c7.setxCoordinate(50);
		c7.setyCoordinate(30);
		c7.setDemand(30);
		c7.setTimeWindowOpen(10);
		c7.setTimeWindowClose(40);
		c7.setServiceTime(15);

		Depot depot = new Depot();
		depot.setxCoordinate(40);
		depot.setyCoordinate(10);

		Vehicle vehicle1 = new Vehicle(1, 75);
		Vehicle vehicle2 = new Vehicle(2, 75);
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicles.add(vehicle1);
		vehicles.add(vehicle2);

		Tour tour1 = new Tour(depot, vehicle1);
		tour1.addCustomer(c3);
		tour1.addCustomer(c4);
		tour1.addCustomer(c5);

		Tour tour2 = new Tour(depot, vehicle1);
		tour2.addCustomer(c7);
		tour2.addCustomer(c6);

		// create vprProblem
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.addCustomer(c3);
		vrpProblem.addCustomer(c4);
		vrpProblem.addCustomer(c5);
		vrpProblem.addCustomer(c6);
		vrpProblem.addCustomer(c7);

		vrpProblem.addDepot(depot);
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setMaxTime(10000);

		// create solution
		solution = new Solution(vrpProblem);
		solution.addTour(tour1);
		solution.addTour(tour2);

		double percentageOfCustomersWithControlStation = 0.4;

		DemandScenario ds = PenetrationOfTheMarketScenario
				.createScenarioForPenetrationOfTheMarked(vrpProblem, solution,
						percentageOfCustomersWithControlStation, 0.1, 0.2);

		System.out.println("Scenario Events:");
		System.out.println("  No  D  T");
		System.out.println("____________");
		for (Event e : ds.getEvents()) {
			System.out.println("   " + e.getCustomerNo() + "  " + e.getDemand()
					+ "  " + e.getPointInTime());
		}

	}

}
