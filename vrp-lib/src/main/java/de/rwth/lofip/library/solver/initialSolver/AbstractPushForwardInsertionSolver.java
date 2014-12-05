package de.rwth.lofip.library.solver.initialSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * A start heuristic, based on the Push Forward Insertion by Solomon (1987).
 * This is a slightly modified version by Lei et al. (2011).
 * 
 * This heurisic respects time window and capacity constrains but cannot
 * guarantee the use of a certain number of vehicles.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractPushForwardInsertionSolver extends AbstractSolver {

	private static Logger logger = LogManager
			.getLogger(AbstractPushForwardInsertionSolver.class);

	@Override
	public String getDescriptiveName() {
		return "Modified PFI";
	}

	@Override
	public Solution solve(VrpProblem problem) {
		// VRPSDTWInitialSolverConfiguration ssc =
		// (VRPSDTWInitialSolverConfiguration) getConfiguration();

		// we have two sets
		// the first one contains ALL customers
		Set<Customer> allCustomers = problem.getCustomers();
		// the second one those which are not yet assigned to a route
		Set<Customer> unassignedCustomers = new HashSet<Customer>(allCustomers);
		Solution solution = new Solution(problem);

		// int vehicleCount = problem.getVehicles().size();
		double vehicleCapacity = problem.getVehicles().iterator().next()
				.getCapacity();

		Depot depot = problem.getDepot();

		logger.info("starting with a set of {} customers",
				unassignedCustomers.size());
		int vehicleId = 0;
		int i = 1;
		while (!unassignedCustomers.isEmpty()) {
			List<Customer> customersByInitialCost = new ArrayList<Customer>(
					unassignedCustomers);

			Collections.sort(customersByInitialCost,
					new FirstVertexCostFunctionComparator(depot));
			// now, the customer with the lowest initial cost is at the first
			// position of the list. Take that customer and create a new tour
			Customer lowestCostCustomer = customersByInitialCost.get(0);
			Tour tour = createTourHook(depot, vehicleId, vehicleCapacity);
			vehicleId++;
			tour.addCustomer(lowestCostCustomer, i++, getClass()
					.getSimpleName(), false);
			unassignedCustomers.remove(lowestCostCustomer);
			solution.addTour(tour);
			logger.info(
					"{} customers left for insertion after creating new route",
					unassignedCustomers.size());

			while (!unassignedCustomers.isEmpty()) {
				// now try to fit the remaining customers in an existing tour
				// use the cost function as described in the paper
				CustomerWithCost cheapestCustomer = new CustomerWithCost(null,
						null, Double.MAX_VALUE);
				for (Customer customer : unassignedCustomers) {
					CustomerWithCost calculatedCustomer = calculateCustomerHook(
							customer, tour, 0.99999);
					if (calculatedCustomer != null
							&& calculatedCustomer.getCost() < cheapestCustomer
									.getCost()) {
						cheapestCustomer = calculatedCustomer;
					}
				}
				if (cheapestCustomer.getCost() < Double.MAX_VALUE) {
					// a possible insertion was found.
					// insert it and remove the customer from the
					// unassignedCustomers
					cheapestCustomer.getTour().insertCustomerAtPosition(
							cheapestCustomer.getCustomer(),
							cheapestCustomer.getPosition(), i++,
							getClass().getSimpleName());
					unassignedCustomers.remove(cheapestCustomer.getCustomer());
					logger.info(
							"{} customers left for insertion after inserting into route at position {}",
							unassignedCustomers.size(),
							cheapestCustomer.getPosition());
					continue;
				} else {
					// no cheaper customer was found, so continue from the
					// beginning
					break;
				}
			}

		}
		return solution;
	}

	protected abstract CustomerWithCost calculateCustomerHook(
			Customer customer, Tour tour, double approximateEquality);

	protected abstract Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity);

	private class FirstVertexCostFunctionComparator implements
			Comparator<Customer> {

		private Depot depot;

		public FirstVertexCostFunctionComparator(Depot depot) {
			this.depot = depot;
		}

		@Override
		public int compare(Customer c1, Customer c2) {
			Edge e1 = new Edge(depot, c1);
			Edge e2 = new Edge(depot, c2);

			// for the explanation of the calculation of these values, see
			// Lei et al. (2011)
			double value1 = ((-0.9) * e1.getLength())
					+ (0.1 * c1.getTimeWindowClose());
			double value2 = ((-0.9) * e2.getLength())
					+ (0.1 * c2.getTimeWindowClose());

			// the one with the lowest value should be used as initial customer
			if (value1 < value2) {
				return -1;
			}
			if (value1 > value2) {
				return 1;
			}
			return 0;
		}

	}
}
