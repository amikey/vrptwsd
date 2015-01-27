package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * Just remove a random selection of customers from the solution.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class RandomRemoval implements RemovalInterface {

	private static int seed = 0;
	Random r;

	public RandomRemoval() {
		seed++;
		r = new Random(seed);
	}

	public static void setSeed(int seed) {
		RandomRemoval.seed = seed;
	}

	@Override
	public String getName() {
		return "RR";
	}

	@Override
	public List<RemovedCustomer> removeCustomers(Solution solution,
			int amountOfCustomersToBeRemoved) {

		List<RemovedCustomer> returnSet = new ArrayList<RemovedCustomer>(
				amountOfCustomersToBeRemoved);

		// make sure that the system does not try to remove more customers than
		// there actually are
		int totalCustomers = solution.getVrpProblem().getCustomers().size();
		int actualAmountOfCustomersToRemove = Math.min(
				amountOfCustomersToBeRemoved, totalCustomers);

		while (returnSet.size() < actualAmountOfCustomersToRemove) {
			int i = r.nextInt(solution.getCustomersInTours().size());			
			CustomerInTour cit = solution.getCustomersInTours().get(i);
			RemovedCustomer rc = new RemovedCustomer();
			rc.setCustomer(cit.getTour().removeCustomerAtPosition(
					cit.getPosition()));
			rc.setTourId(cit.getTour().getId());
			rc.setPosition(cit.getPosition());
			returnSet.add(rc);
			solution.removeEmptyTours();
		}
		return returnSet;
	}
}
