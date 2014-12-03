package de.rwth.lofip.library.analyse;

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
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.DemandScenarioUtils;
import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.solver.repair.RepairSolution;
import de.rwth.lofip.library.solver.repair.util.RepairedSolution;

public class AverageValuesForMultipleDemandScenariosOnOneSolomonInstance_TEST {

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
	public void AvgMultipleDemandScenarios_TEST() {
		try {
			DemandScenario demandScenario = DemandScenarioUtils
					.createScenarioFromVrpProblem(vrpProblem, 0.2);
			System.out.println(demandScenario.getDemandScenarioAsString());

			KeyPerformanceIndicators keyPerformanceIndicators = new KeyPerformanceIndicators(
					demandScenario);
			// now process demandScenario
			Solution temporarySolution = solution.clone();
			for (Event e : demandScenario.getEvents()) {
				System.out.println("Processing Event for Customer "
						+ e.getCustomerNo() + " at time " + e.getPointInTime());
				List<RepairedSolution> solutionList = new RepairSolution()
						.repair(temporarySolution, e);
				temporarySolution = solutionList.get(0).getNewSolution();
				keyPerformanceIndicators.addRepairedSolution(solutionList
						.get(0));
			}

			DemandScenario demandScenario2 = DemandScenarioUtils
					.createScenarioFromVrpProblem(vrpProblem, 0.2);
			System.out.println(demandScenario2.getDemandScenarioAsString());

			KeyPerformanceIndicators keyPerformanceIndicators2 = new KeyPerformanceIndicators(
					demandScenario2);
			// now process demandScenario
			Solution temporarySolution2 = solution.clone();
			for (Event e : demandScenario2.getEvents()) {
				System.out.println("Processing Event for Customer "
						+ e.getCustomerNo() + " at time " + e.getPointInTime());
				List<RepairedSolution> solutionList = new RepairSolution()
						.repair(temporarySolution2, e);
				temporarySolution2 = solutionList.get(0).getNewSolution();
				keyPerformanceIndicators2.addRepairedSolution(solutionList
						.get(0));
			}

			AverageValuesForMultipleDemandScenariosOnOneSolomonInstance avg = new AverageValuesForMultipleDemandScenariosOnOneSolomonInstance();
			avg.addDemandScenario(keyPerformanceIndicators);
			avg.addDemandScenario(keyPerformanceIndicators2);

			System.out.println(avg.getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
