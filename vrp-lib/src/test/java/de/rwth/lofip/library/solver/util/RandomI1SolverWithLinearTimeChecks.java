package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;

public class RandomI1SolverWithLinearTimeChecks extends RandomI1Solver {

	@Override
	protected void insertRemainingCustomersIntoTours() {
		GreedyInsertion gi = new GreedyInsertionWithLinearTimeChecks();
		solution = gi.insertCustomers(solution, remainingCustomers);
	}
}
