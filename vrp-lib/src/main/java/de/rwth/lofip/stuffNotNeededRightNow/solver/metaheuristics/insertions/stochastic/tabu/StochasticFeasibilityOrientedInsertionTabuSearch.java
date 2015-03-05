package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu;

import java.util.HashSet;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.TabuInsertionInterface;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.StochasticFeasibilityOrientedInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.StochasticGreedyInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.util.TabuTourPosition;

/**
 * Enhanced version of the {@code AbstractFeasibilityOrientedInsertion} which
 * respects the tabu list at every insertion.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class StochasticFeasibilityOrientedInsertionTabuSearch extends
		StochasticFeasibilityOrientedInsertion implements
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

	@Override
	protected StochasticGreedyInsertion getGreedyInsertion() {
		return new StochasticGreedyInsertionTabuSearch();
	}
}
