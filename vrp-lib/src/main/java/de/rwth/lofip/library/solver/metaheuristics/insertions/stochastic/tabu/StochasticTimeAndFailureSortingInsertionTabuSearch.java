package de.rwth.lofip.library.solver.metaheuristics.insertions.stochastic.tabu;

import java.util.HashSet;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.insertions.TabuInsertionInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.stochastic.StochasticTimeAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.util.TabuTourPosition;

/**
 * An extension to the {@code StochasticTimeAndFailureSortingInsertion} which is
 * needed to respect the Tabu-Search criterion at every insertion step.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class StochasticTimeAndFailureSortingInsertionTabuSearch extends
		StochasticTimeAndFailureSortingInsertion implements
		TabuInsertionInterface {
	private Set<String> tabuPositions = new HashSet<String>();

	public void setTabuPositions(Set<TabuTourPosition> tabuPositions) {
		for (TabuTourPosition ttp : tabuPositions) {
			this.tabuPositions.add(ttp.getTabuValue());
		}
	}

	@Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
		String comparisonValue = customer.getCustomerNo() + ":" + tour.getId();
		if (tabuPositions.contains(comparisonValue)) {
			return false;
		}
		return true;
	}
}
