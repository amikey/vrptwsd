package de.rwth.lofip.library.solver.metaheuristics.recourse;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.TSWithTourElimination;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatchingWithNumberOfRecourseActions;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatchingWrtRecourseCost;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.PrintUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearchWithRecourse extends AdaptiveMemoryTabuSearch {

	private boolean thereHasBeenNoImprovementInTheLastIncreasedVehicleNumber;

	protected TabuSearchForElementWithTours getTS() {
		if (Parameters.isTourMinimizationPhase())
			return new TSWithTourElimination();
		else
			return new TabuSearchWithRecourse();		 
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
			if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
				return true;
			}
		} else {			
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndRecourse(),bestOverallSolution.getTotalDistanceWithCostFactorAndRecourse())) {
//				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
					return true;
			}
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
                    		if (MathUtils.lessThan(o1.getTotalDistanceWithCostFactorAndRecourse(),o2.getTotalDistanceWithCostFactorAndRecourse())) 
                                return -1;                          
                    		if (MathUtils.greaterThan(o1.getTotalDistanceWithCostFactorAndRecourse(),o2.getTotalDistanceWithCostFactorAndRecourse())) 
                                return 1;                           
                    		return 0;
                    	}
                    	throw new RuntimeException("Should not happen");
                    }
                });
		PrintUtils.printListOfSolutions(bestSolutions, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
	}
	
	@Override
	protected void processSolutionsWithCostMinimzationTSAndStoreBestOverallSolution() throws IOException {
		boolean tourMinimizationPhaseFlag = Parameters.isTourMinimizationPhase();
		Parameters.setTourMinimizationPhase(false);		
		int minimalVehicleNumber = bestSolutions.get(0).getVehicleCount();
		int targetVehicleNumber = minimalVehicleNumber-1;
		thereHasBeenNoImprovementInTheLastIncreasedVehicleNumber = false;
		while (thereHasBeenNoImprovementInTheLastIncreasedVehicleNumber == false) {
			thereHasBeenNoImprovementInTheLastIncreasedVehicleNumber = true;
			targetVehicleNumber++;
			int iteration = 0;
			for (SolutionGot sol : bestSolutions) {
				iteration++;
				if (targetVehicleNumber > minimalVehicleNumber)
					//only create new solution if target vehicle number is greater than that minimal vehicle number
					sol = SolutionGotUtils.createSolutionWithVehicleGoalNumber(solution, targetVehicleNumber);
				if (Parameters.isPublishSolutionAtEndOfTabuSearch())
					IOUtils.write("TargetVehicleNumber: " + targetVehicleNumber + "; Verbessere Lösung " + iteration + " von " +  bestSolutions.size() +": " + "(" + sol.getNumberOfTours() + ", " + sol.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
				TabuSearchForElementWithTours ts = getTS();
				solution = (SolutionGot) ts.improve(sol);
				if (Parameters.isPublishSolutionAtEndOfTabuSearch())
					IOUtils.write("Verbesserte Lösung: " + "(" +solution.getNumberOfTours() + ", " + solution.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
				if (isNewSolutionIsNewBestOverallSolution()) {
					postProcessSolutionWithTourMachting();
					if (Parameters.isPublishSolutionAtEndOfTabuSearch())
						IOUtils.write("Lösung mit Tourmatching verbessert: " + "(" +solution.getNumberOfTours() + ", " + solution.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
					setBestOverallSolutionToNewSolution();
				}
			}
		}
		Parameters.setTourMinimizationPhase(tourMinimizationPhaseFlag);
	}
	
	private void postProcessSolutionWithTourMachting() throws IOException {
		solution = getTourMatching().matchToursToGots(solution);		
	}

	@Override
	protected void setBestOverallSolutionToNewSolution() throws IOException {
		bestOverallSolution = solution.clone();
		thereHasBeenNoImprovementInTheLastIncreasedVehicleNumber = false;
		if (Parameters.isPublishSolutionAtEndOfTabuSearch())				
			IOUtils.write("Dies ist die neue beste Lösung in AM \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
	}
	
	protected TourMatchingWithNumberOfRecourseActions getTourMatching() {
		return new TourMatchingWrtRecourseCost();
	}
}
