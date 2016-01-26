package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions extends CrossNeighborhoodWithTabooListAndRecourse {
	
	public CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions(ElementWithTours solution) {
		super(solution);
	}
	
	@Override
	protected void sortMovesWrtToStochasticAspect() {
		Comparator<AbstractNeighborhoodMove> byDeterministicAndStochasticCostAndNumberOfRecourseActions = (e1,e2) -> 
			Double.compare(e1.getDeterministicAndStochasticCostDifferenceWithNumberOfRecourseActions(),
					e2.getDeterministicAndStochasticCostDifferenceWithNumberOfRecourseActions());		
		Collections.sort(listOfNonTabooMoves, byDeterministicAndStochasticCostAndNumberOfRecourseActions);
	}

}
