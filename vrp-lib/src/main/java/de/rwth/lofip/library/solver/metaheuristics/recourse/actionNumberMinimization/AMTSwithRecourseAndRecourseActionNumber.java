package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.TSWithTourElimination;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.recourse.AdaptiveMemoryTabuSearchWithRecourse;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatchingWithNumberOfRecourseActions;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.PrintUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class AMTSwithRecourseAndRecourseActionNumber extends AdaptiveMemoryTabuSearchWithRecourse {
	
//	@Override
//	protected AdaptiveMemory getAM() {
//		return new AdaptiveMemoryWithRecourseAndRecourseActionNumber();
//	}
	
	private boolean targetSolutionsFound;
	
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
			if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
				return true;
			}
		} else {			
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),bestOverallSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse()))
//				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
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
	
	
	//----------------------------------------------
	
	public SolutionGot solveWithNewAlgo(VrpProblem problem) throws IOException {
		startTime = System.currentTimeMillis();
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfTabuSearch(problem);
		Parameters.setTourMinimizationPhase(false);
		List<SolutionGot> solutions = new ArrayList<SolutionGot>();
		int targetVehicleNumber = problem.getNumberOfVehiclesWith80PercentWorkload();
		thereHasBeenImprovementInLastDecreasedVehicleNumber = true;
		
		solutions = generateInitialSolutions(problem, solutions);
		solutions = improveSolutions(solutions, targetVehicleNumber);
		
		while (thereHasBeenImprovementInLastDecreasedVehicleNumber == true) {
			targetVehicleNumber--;
			solutions = findSolutionsWithTargetVehicleNumber(solutions, targetVehicleNumber);
			if (targetSolutionsFound)
				solutions = improveSolutions(solutions, targetVehicleNumber);
			else
				thereHasBeenImprovementInLastDecreasedVehicleNumber = false;
		}
		publishSolutionAtEndOfAMTSSearch();
		return bestOverallSolution;
	}

	private List<SolutionGot> generateInitialSolutions(VrpProblem problem, List<SolutionGot> solutions) throws IOException {
		int numberOfRunsPerVehicleNumber = 10;
		int targetVehicleNumber = problem.getNumberOfVehiclesWith80PercentWorkload();
		for (int i = 1; i <=numberOfRunsPerVehicleNumber; i++) {
			SolutionGot solution1 = SolutionGotUtils.createSolutionWithSingleTours(problem, targetVehicleNumber);
			if (solution1.getNumberOfTours() > targetVehicleNumber)
				solution1 = SolutionGotUtils.findSolutionWithTargetNumber(solution1, targetVehicleNumber);
			solutions.add(solution1);
		}
		return solutions;
	}
	
	private List<SolutionGot> findSolutionsWithTargetVehicleNumber(List<SolutionGot> solutions, int targetVehicleNumber) throws IOException {
		targetSolutionsFound = true;
		List<SolutionGot> solutions2 = new ArrayList<SolutionGot>();
		for (SolutionGot sol : solutions) {
			if (sol.getNumberOfTours() > targetVehicleNumber) {
				sol = SolutionGotUtils.findSolutionWithLowerOrEqualThanTargetNumber(sol, targetVehicleNumber);
				if (sol.getNumberOfTours() > targetVehicleNumber)
					targetSolutionsFound = false;
			}
			solutions2.add(sol);
		}
		return solutions2;
	}
	
	private List<SolutionGot> improveSolutions(List<SolutionGot> solutions, int targetVehicleNumber) throws IOException {
		System.out.println("Start improve Solutions");		
		int iteration = 0;
		thereHasBeenImprovementInLastDecreasedVehicleNumber = false;
		List<SolutionGot> solutions2 = new ArrayList<SolutionGot>();
		for (SolutionGot sol : solutions) {
			iteration++;
			if (Parameters.isPublishSolutionAtEndOfTabuSearch())
				IOUtils.write("TargetVehicleNumber: " + targetVehicleNumber + "; Verbessere Lösung " + iteration + " von XX: " + "(" + sol.getNumberOfTours() + ", " + sol.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(sol));
			TabuSearchForElementWithTours ts = getTS();
			solution = (SolutionGot) ts.improve(sol);
			solutions2.add(solution);
			if (Parameters.isPublishSolutionAtEndOfTabuSearch())
				IOUtils.write("Verbesserte Lösung: " + "(" + solution.getNumberOfTours() + ", " + solution.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(solution));
			if (isNewSolutionIsNewBestOverallSolution())
				setBestOverallSolutionToNewSolution();
		}
		return solutions2;
	}
	
	//----------------------------------------------
	
//	@Override
//	protected TourMatchingWithNumberOfRecourseActions getTourMatching() {
//		return new TourMatchingWithNumberOfRecourseActions();
//	}
	
	
}
