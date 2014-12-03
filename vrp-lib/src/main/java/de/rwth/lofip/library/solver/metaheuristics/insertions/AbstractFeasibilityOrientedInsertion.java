package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.util.CustomerNoComparator;
import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * <p>
 * An implementation of the Feasibility Oriented Insertion as described by Lei
 * et al. (2011) in section 3.3.6.
 * </p>
 * <p>
 * The idea behind this insertion heuristic is to get towards an m-feasible
 * solution, i.e. that the amount of tours in the solution gets closer to the
 * expected amount.
 * </p>
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractFeasibilityOrientedInsertion implements
		InsertionInterface {

	@Override
	public String getName() {
		return "FOI";
	}

	@Override
	public Solution insertCustomers(Solution solution,
			Collection<RemovedCustomer> customers, int iteration,
			VrpConfiguration configuration) {

		Solution improvedSolution = solution.clone();

		int currentTourCount = solution.getTours().size();
		int wantedTourCount = solution.getVrpProblem().getVehicleCount();
		int insertionCustomerCount = customers.size();

		int customerCountBeforeInsertion = solution.getCustomersInTours()
				.size();
		int customerCountInProblem = solution.getVrpProblem().getCustomers()
				.size();
		if (customerCountBeforeInsertion + insertionCustomerCount != customerCountInProblem) {
			System.err
					.println("Something's wrong: the customer count is wrong.");
		}

		if (wantedTourCount > currentTourCount) {
			int neededNewTours = wantedTourCount - currentTourCount;
			VrpProblem problem = solution.getVrpProblem();
			Depot depot = problem.getDepot();
			double capacity = problem.getVehicles().iterator().next()
					.getCapacity();

			if (neededNewTours >= insertionCustomerCount) {
				// create a new tour for every customer who should be inserted

				// first sort customers according to customer number (for
				// pseudo-randomness)
				List<RemovedCustomer> customersList = new ArrayList<RemovedCustomer>(
						customers);
				Collections.sort(customersList, new CustomerNoComparator());

				// for (RemovedCustomer c : customers) {
				for (int i = 0; i < customersList.size(); i++) {
					RemovedCustomer c = customersList.get(i);
					System.err.println("FOI: inserting customer"
							+ c.getCustomer().getCustomerNo());
					Tour t = createTourHook(depot,
							new Random().nextInt(Integer.MAX_VALUE), capacity);

					t.addCustomer(c.getCustomer(), iteration, getClass()
							.getSimpleName());
					improvedSolution.addTour(t);
				}

			} else {
				// sort the vertices in decreasing order of their expected
				// demand
				List<RemovedCustomer> customerList = new ArrayList<RemovedCustomer>(
						customers);
				Collections.sort(customerList, new CustomerComparator());
				// now, take the first customers and insert them into new tours
				for (int i = 0; i < neededNewTours; i++) {
					Tour t = createTourHook(depot,
							new Random().nextInt(Integer.MAX_VALUE), capacity);
					t.addCustomer(customerList.get(i).getCustomer(), iteration,
							getClass().getSimpleName());
					improvedSolution.addTour(t);
				}
				// there are still remaining customers in the customerList.
				// Insert them like Greedy Insertion
				AbstractGreedyInsertion gi = getGreedyInsertion();
				improvedSolution = gi.insertCustomers(
						improvedSolution,
						customerList.subList(neededNewTours,
								customerList.size()), iteration, configuration);

			}

		} else {
			// act like Greedy Insertion
			List<RemovedCustomer> sortedCustomers = new ArrayList<RemovedCustomer>(
					customers);
			Collections.sort(sortedCustomers, new CustomerComparator());
			if (sortedCustomers.size() + customerCountBeforeInsertion != customerCountInProblem) {
				System.err.println("Wrong!");
			}
			// move into separate method so it can be overridden.
			AbstractGreedyInsertion gi = getGreedyInsertion();
			improvedSolution = gi.insertCustomers(improvedSolution,
					sortedCustomers, iteration, configuration);
		}

		return improvedSolution;
	}

	private class CustomerComparator implements Comparator<RemovedCustomer> {
		@Override
		public int compare(RemovedCustomer o1, RemovedCustomer o2) {
			if (o1.getCustomer().getDemand() > o2.getCustomer().getDemand()) {
				return -1;
			}
			if (o1.getCustomer().getDemand() < o2.getCustomer().getDemand()) {
				return 1;
			}
			return 0;
		}
	}

	@Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
		return true;
	}

	protected abstract Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity);

	protected abstract AbstractGreedyInsertion getGreedyInsertion();

}
