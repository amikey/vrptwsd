package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.groups;

import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractFeasibilityOrientedInsertionGot;

public class FeasibilityOrientedInsertionGot extends AbstractFeasibilityOrientedInsertionGot {

	@Override
	protected GreedyInsertionGot getGreedyInsertion() {
        return new GreedyInsertionGot();
    }
}
