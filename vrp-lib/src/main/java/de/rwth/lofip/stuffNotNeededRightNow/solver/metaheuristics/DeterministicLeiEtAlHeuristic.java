package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics;

import de.rwth.lofip.library.solver.metaheuristics.insertions.deterministic.DeterministicDemandAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.deterministic.DeterministicFeasibilityOrientedInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.deterministic.DeterministicGreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.deterministic.DeterministicTimeAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.metaheuristics.removals.ExpectedWorstRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.FeasibilityOrientedRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.SimilarityRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * An implementation of the meta heuristic proposed by Lei et al. (2011) without
 * any stochastic components.
 * 
 * @author Andreas Braun <braun@dpor.rwth-aachen.de>
 * 
 */
public class DeterministicLeiEtAlHeuristic extends LeiEtAlHeuristic {
    
    @Override
	protected void setUpInsertionAndRemovalHeuristics() {
		resetHeuristics();

		addRemovalHeuristic(ExpectedWorstRemoval.class, "EWR");
		addRemovalHeuristic(FeasibilityOrientedRemoval.class, "FOR");
		addRemovalHeuristic(SimilarityRemoval.class, "SR");
		addRemovalHeuristic(RandomRemoval.class, "RR");

        addInsertionHeuristic(DeterministicGreedyInsertion.class, "GI");
        addInsertionHeuristic(DeterministicFeasibilityOrientedInsertion.class, "FOI");
        addInsertionHeuristic(DeterministicDemandAndFailureSortingInsertion.class, "DFSI");
        addInsertionHeuristic(DeterministicTimeAndFailureSortingInsertion.class, "TFSI");	
	}
    
    @Override
   	protected boolean isCreatedSolutionResultsInNewFeasibleGlobalBestSolution(Solution solution, Solution bestSolution) {
   		double bestCost = bestSolution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
   		if (solution.getVehicleCount() <= bestSolution.getVehicleCount()
   				&& solution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() < bestCost)
   			return true;
   		else 
   			return false;
   	}
}
