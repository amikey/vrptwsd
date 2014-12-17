package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.insertions.InsertionInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.NeighborhoodInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.TabuInsertionInterface;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterface;
import de.rwth.lofip.library.solver.metaheuristics.removals.ExpectedWorstRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.FeasibilityOrientedRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.SimilarityRemoval;
import de.rwth.lofip.library.solver.util.TabuTourPosition;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu.StochasticDemandAndFailureSortingInsertionTabuSearch;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu.StochasticFeasibilityOrientedInsertionTabuSearch;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu.StochasticGreedyInsertionTabuSearch;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic.tabu.StochasticTimeAndFailureSortingInsertionTabuSearch;

/**
 * This is the heuristic from Lei et al., enhanced with the aspects of
 * Tabu-Search.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class TabuSearchHeuristic extends LeiEtAlHeuristic implements
		MetaSolverInterface {

	private Set<TabuTourPosition> tabuSet = new HashSet<TabuTourPosition>();

	private int customerCount = 0;

	public TabuSearchHeuristic() {

		resetHeuristics();

		addRemovalHeuristic(ExpectedWorstRemoval.class, "EWR");
		addRemovalHeuristic(FeasibilityOrientedRemoval.class, "FOR");
		addRemovalHeuristic(SimilarityRemoval.class, "SR");
		addRemovalHeuristic(RandomRemoval.class, "RR");

		addInsertionHeuristic(StochasticGreedyInsertionTabuSearch.class, "GI");
		addInsertionHeuristic(
				StochasticFeasibilityOrientedInsertionTabuSearch.class, "FOI");
		addInsertionHeuristic(
				StochasticDemandAndFailureSortingInsertionTabuSearch.class,
				"DFSI");
		addInsertionHeuristic(
				StochasticTimeAndFailureSortingInsertionTabuSearch.class,
				"TFSI");
	}

	@Override
	public Solution improve(Solution solution, VrpConfiguration configuration) throws IOException {
		this.customerCount = solution.getVrpProblem().getCustomers().size();
		return super.improve(solution, configuration);
	}

    @Override
    protected void addRemovedCustomersToTabuListHook(List<RemovedCustomer> removedCustomers,
            int iteration) {
        for (RemovedCustomer rc : removedCustomers) {
            TabuTourPosition ttp = new TabuTourPosition();
            ttp.setIteration(iteration);
            ttp.setTabuValue(rc.getCustomer().getCustomerNo() + ":"
                    + rc.getTourId());
            tabuSet.add(ttp);
        }
    }

    @Override
    protected void setTabuPositionsInInsertionHeuristicHook(InsertionInterface insertion) {
        ((TabuInsertionInterface) insertion).setTabuPositions(tabuSet);
    }
  
    @Override
    protected void deleteExpiredEntriesFromTabuListHook(int iteration) {
        // clear too old entries from the tabu set
        Set<TabuTourPosition> newSet = new HashSet<TabuTourPosition>();
        for (TabuTourPosition ttp : tabuSet) {
            if (iteration - ttp.getIteration() <= customerCount) {
                newSet.add(ttp);
            }
        }
        tabuSet = newSet;
    }
    
    @Override
    protected void AddRemovedCustomersToTabuListHook(InsertionInterface insertion) {
    	try {
    		NeighborhoodInterface NI = (NeighborhoodInterface) insertion;
        	tabuSet = NI.getTabuPositions();	        	
    	} catch (Exception e) {
    		//doesn't matter, no customers were removed in insertion
    		//(i.e. insertion is no neighborhood)
    		//-> nothing needs to be done
    	}
    	
    }
}
