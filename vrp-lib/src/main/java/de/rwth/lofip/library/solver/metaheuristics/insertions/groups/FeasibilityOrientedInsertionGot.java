package de.rwth.lofip.library.solver.metaheuristics.insertions.groups;

import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractFeasibilityOrientedInsertionGot;

public class FeasibilityOrientedInsertionGot extends AbstractFeasibilityOrientedInsertionGot {

	@Override
	protected GreedyInsertionGot getGreedyInsertion() {
        return new GreedyInsertionGot();
    }
}
