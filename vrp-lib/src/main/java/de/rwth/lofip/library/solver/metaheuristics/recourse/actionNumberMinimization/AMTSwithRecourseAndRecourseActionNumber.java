package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.TSWithTourElimination;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.util.PrintUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class AMTSwithRecourseAndRecourseActionNumber extends AdaptiveMemoryTabuSearch {
	
//	@Override
//	protected AdaptiveMemory getAM() {
//		return new AdaptiveMemoryWithRecourseAndRecourseActionNumber();
//	}
	
	public AMTSwithRecourseAndRecourseActionNumber() {
		super();
		Parameters.setRecourseActionNumberMinimization(true);
	}

	protected TabuSearchForElementWithTours getTS() {
		if (Parameters.isTourMinimizationPhase())
			return new TSWithTourElimination();
		else
			return new TSwithRecourseActionNumberMinimization();		
	}

	@Override
	protected boolean isNewSolutionIsNewBestOverallSolution() {
		if (bestOverallSolution == null)
			return true;
		
		if (Parameters.isTourMinimizationPhase()) {
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactor(),bestOverallSolution.getTotalDistanceWithCostFactor())
					&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
						return true;
				} 
		} else {			
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),bestOverallSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
					return true;
			}
		}
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
			return true;
		}
		return false;
	}
	
	@Override
	protected void sortListOfBestSolutionsAccordingToTourNumberAndCost() throws IOException {
		Collections.sort(bestSolutions, new Comparator<SolutionGot>() {
                    @Override
                    public int compare(SolutionGot o1, SolutionGot o2) {
                    	if (o1.getNumberOfTours() < o2.getNumberOfTours())
                    		return -1;
                    	if (o1.getNumberOfTours() > o2.getNumberOfTours())
                    		return 1;
                    	if (o1.getNumberOfTours() == o2.getNumberOfTours()) {
                    		if (MathUtils.lessThan(o1.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),o2.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse())) 
                                return -1;                           
                    		if (MathUtils.greaterThan(o1.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),o2.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse())) 
                                return 1;                            
                    		return 0;
                    	}
                    	throw new RuntimeException("Should not happen");
                    }
                });
		PrintUtils.printListOfSolutions(bestSolutions, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
	}
}
