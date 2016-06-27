package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.RecourseCost;

public class CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions extends CrossNeighborhoodWithTabooListAndRecourse {
	
	public CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions(ElementWithTours solution) {
		super(solution);
	}
	
	@Override
	protected void sortMovesWrtToStochasticAspect() {
		Comparator<AbstractNeighborhoodMove> byDeterministicAndStochasticCostAndNumberOfRecourseActions = (e1,e2) -> 
			Double.compare(e1.getDeterministicAndStochasticCostDifferenceWithNumberOfRecourseActions(elementWithTours),
					e2.getDeterministicAndStochasticCostDifferenceWithNumberOfRecourseActions(elementWithTours));		
		Collections.sort(listOfNonTabooMoves, byDeterministicAndStochasticCostAndNumberOfRecourseActions);
	}
	
	@Override
	public AbstractNeighborhoodMove getNeigborhoodMove() {
		AbstractNeighborhoodMove anm = new AbstractNeighborhoodMove(tour1, tour2, 
				positionStartOfSegmentTour1, positionEndOfSegmentTour1, 
				positionStartOfSegmentTour2, positionEndOfSegmentTour2,
				costOfCompleteSolutionThatResultsFromMove, costDifferenceToPreviousSolution);
		anm.setOldRecourseCostOfOldSolution(elementWithTours.getRecourseCost());
		return anm;
	}

}
