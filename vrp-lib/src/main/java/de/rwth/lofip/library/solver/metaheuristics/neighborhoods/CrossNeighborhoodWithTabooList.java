package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class CrossNeighborhoodWithTabooList extends CrossNeighborhood {
	
	
	
	public CrossNeighborhoodWithTabooList(SolutionGot solution) {
		super(solution);
	}

	@Override
	protected boolean isMoveNotTaboo(AbstractNeighborhoodMove move) { 
		//TODO implement taboo check;
	}
}
