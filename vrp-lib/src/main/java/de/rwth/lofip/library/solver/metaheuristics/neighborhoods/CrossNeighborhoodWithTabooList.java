package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.TabuList;

public class CrossNeighborhoodWithTabooList extends CrossNeighborhood {
	
	TabuList tabuList = new TabuList();
		
	public CrossNeighborhoodWithTabooList(ElementWithTours solution) {
		super(solution);
	}

	@Override
	protected boolean isMoveTaboo(AbstractNeighborhoodMove move, int iteration) { 
		return tabuList.isMoveTaboo(move.getCost(), iteration);
	}
	
	public void updateTabuList(int iteration) {
		tabuList.addSolutionToTabuList(bestNonTabooMove.getCost(), iteration);
	}
}
