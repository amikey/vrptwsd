package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
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
public abstract class AbstractFeasibilityOrientedInsertionGot implements
		InsertionInterfaceGot {
	
	SolutionGot improvedSolution;
	int iteration;

	@Override
	public String getName() {
		return "FOI";
	}

	@Override
	public SolutionGot insertCustomers(SolutionGot solution,
			Collection<RemovedCustomer> customers, int iteration,
			VrpConfiguration configuration) {

		this.iteration = iteration;
		improvedSolution = solution.clone();

		int currentTourCount = solution.getTours().size();
		int wantedTourCount = solution.getVrpProblem().getVehicleCount();
		int insertionCustomerCount = customers.size();

		if (wantedTourCount > currentTourCount) {
			int neededNewTours = wantedTourCount - currentTourCount;

			if (neededNewTours >= insertionCustomerCount) {
				// create a new tour for every customer who should be inserted

				// first sort customers according to customer number (for
				// pseudo-randomness)
				List<RemovedCustomer> customersList = new ArrayList<RemovedCustomer>(
						customers);
				Collections.sort(customersList, new CustomerNoComparator());

				// for (RemovedCustomer c : customers) {
				for (int i = 0; i < customersList.size(); i++) {
					Customer c = customersList.get(i).getCustomer();
					improvedSolution.createNewTourWithCustomer(c, iteration, getClass().getSimpleName());						
//					
//					Tour t = createTourHook(depot,
//							new Random().nextInt(Integer.MAX_VALUE), capacity);
//
//					t.addCustomer(c.getCustomer(), iteration, getClass()
//							.getSimpleName());
//										
//					improvedSolution.addTour(t);
				}

			} else {
				// sort the vertices in decreasing order of their expected demand
				List<RemovedCustomer> customerList = new ArrayList<RemovedCustomer>(
						customers);
				Collections.sort(customerList, new CustomerComparator());
				// now, take the first customers and insert them into new tours
				for (int i = 0; i < neededNewTours; i++) {
					Customer c = customerList.get(i).getCustomer();
					improvedSolution.createNewTourWithCustomer(c, iteration, getClass().getSimpleName());
//					
//					Tour t = createTourHook(depot,
//							new Random().nextInt(Integer.MAX_VALUE), capacity);
//					t.addCustomer(customerList.get(i).getCustomer(), iteration,
//							getClass().getSimpleName());
//					improvedSolution.addTour(t);
				}
				// there are still remaining customers in the customerList.
				// Insert them like Greedy Insertion
				AbstractGreedyInsertionGot gi = getGreedyInsertion();
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

			// move into separate method so it can be overridden.
			AbstractGreedyInsertionGot gi = getGreedyInsertion();
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

	protected abstract AbstractGreedyInsertionGot getGreedyInsertion();

}
