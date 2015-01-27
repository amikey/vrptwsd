package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu;

import java.util.HashSet;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.util.TabuTourPosition;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.TabuInsertionInterface;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.StochasticGreedyInsertion;

/**
 * An enhanced version of the {@link StochasticGreedyInsertion}, added with
 * aspects of the Tabu Search heuristic.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class StochasticGreedyInsertionTabuSearch extends
		StochasticGreedyInsertion implements TabuInsertionInterface {

	private Set<String> tabuPositions = new HashSet<String>();

	@Override
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
