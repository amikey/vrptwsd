package de.rwth.lofip.library.solver.metaheuristics.util;

import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.util.RecourseCost;

public class TourMatchingWrtRecourseCost extends TourMatchingWithNumberOfRecourseActions {
	
	@Override
	protected void sortListOfRecourseCostsAccordingToConvexCombinationOfRecourseCostAndNumberOfRecourseActions() {
		//this actually only compares recourse cost and does not consider number of recourse actions
		Comparator<RecourseCost> byRecourseCost = (e1,e2) -> Double.compare(e1.getRecourseCost(),e2.getRecourseCost());		
		Collections.sort(listOfRecourseCosts, byRecourseCost);						
	}
	@Override
	protected SolutionGot returnEitherNewOrOldSolutionDependingOnWhichHasLessCost() {
		if (newSolution.getTotalDistanceWithCostFactorAndRecourse() <= 
				oldSolution.getTotalDistanceWithCostFactorAndRecourse())	
			return newSolution;
		else
			return oldSolution;		
	}

}
