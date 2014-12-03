package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.solver.metaheuristics.insertions.poisson.PoissonDemandAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.poisson.PoissonFeasibilityOrientedInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.poisson.PoissonGreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.poisson.PoissonTimeAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.metaheuristics.removals.ExpectedWorstRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.FeasibilityOrientedRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.library.solver.metaheuristics.removals.SimilarityRemoval;

public class PoissonLeiEtAlHeuristic extends LeiEtAlHeuristic {

	public PoissonLeiEtAlHeuristic() {

		resetHeuristics();

		addRemovalHeuristic(ExpectedWorstRemoval.class, "EWR");
		addRemovalHeuristic(FeasibilityOrientedRemoval.class, "FOR");
		addRemovalHeuristic(SimilarityRemoval.class, "SR");
		addRemovalHeuristic(RandomRemoval.class, "RR");

		addInsertionHeuristic(PoissonGreedyInsertion.class, "GI");
		addInsertionHeuristic(PoissonFeasibilityOrientedInsertion.class, "FOI");
		addInsertionHeuristic(PoissonDemandAndFailureSortingInsertion.class,
				"DFSI");
		addInsertionHeuristic(PoissonTimeAndFailureSortingInsertion.class,
				"TFSI");
	}

}
