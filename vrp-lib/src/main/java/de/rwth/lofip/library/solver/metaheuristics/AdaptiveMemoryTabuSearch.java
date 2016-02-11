package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.util.PrintUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearch {
	
	private int numberOfDifferentInitialSolutions = Parameters.getNumberOfDifferentInitialSolutionsInAM();
	private int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = Parameters.getMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory();
	private int lengthOfList = Parameters.getNumberOfSolutionsThatIsProcessedWithCostMinimizationPhase();
	
	private AdaptiveMemory adaptiveMemory = getAM();
	
	protected SolutionGot solution = null;
	protected SolutionGot bestOverallSolution;
	protected ArrayList<SolutionGot> bestSolutions;
	@SuppressWarnings("unused")
	private int callsToAdaptiveMemory = 0;
	private int numberOfAMCallsWithoutImprovement = 0;
	@SuppressWarnings("unused")
	private int numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
	private long endTime;
	private long timeNeeded;
	private long startTime;
	private int iteration;
	
	protected AdaptiveMemory getAM() {
		return new AdaptiveMemory();
	}
	
	public SolutionGot solve(VrpProblem vrpProblem) throws IOException {
		
		startTime = System.currentTimeMillis();
		bestSolutions = new ArrayList<SolutionGot>();
		
		System.out.println("STARTE INITIALISIERUNG AM");
		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfTabuSearch(vrpProblem);
		initialiseAdaptiveMemoryWithInitialSolutions(vrpProblem);
		
		publishSolution(bestOverallSolution);
		
		iteration = 0;		
		while (!isStoppingCriterionMet()) {
			System.out.println("AM CALL Nr. " + iteration);
			
			solution = constructInitialSolutionFromAdaptiveMemory();
			
			publishSolution(solution);
			
			TabuSearchForElementWithTours tabuSearch = getTS(); 
			solution = (SolutionGot) tabuSearch.improve(solution);
			
			publishSolution(solution);
			
			storeNewToursInAdaptiveMemory(solution);
			
			if (isBestOverallSolutionFoundAgain())
				increaseNumberOfTimesBestOverallSolutionHasBeenFound();
			
			if (isNewSolutionIsNewBestOverallSolution()) {
				setBestOverallSolutionToNewSolution();
				addNewSolutionToBestSolutions();
				numberOfAMCallsWithoutImprovement = 0;
				numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
			} else
				numberOfAMCallsWithoutImprovement++;
			
			publishSolution(bestOverallSolution);
			
			iteration++;
		}
		if (bestOverallSolution == null)
			throw new RuntimeException("bestOverallSolution ist Null. Fehler!");
		
		publishSolution(bestOverallSolution);
		improveBestSolutionsWithCostMinimizationPhase();
		publishSolutionAtEndOfAMTSSearch();
		return bestOverallSolution;
	}

		private void publishSolution(SolutionGot solution2) {
			System.out.println(iteration + " " + solution2.getAsTupel());
			System.out.println(solution2.getTotalDistanceWithCostFactor() + "; " + solution2.getNumberOfTours());
		}

		protected TabuSearchForElementWithTours getTS() {
			return new TabuSearchForElementWithTours();		 
		}

		private void initialiseAdaptiveMemoryWithInitialSolutions(VrpProblem vrpProblem) throws IOException {
			for (int i = 0; i < numberOfDifferentInitialSolutions; i++) {
				System.out.println("Initialising AM " + i);
				RandomI1Solver initialSolver = new RandomI1Solver();
				solution = initialSolver.solve(vrpProblem);
				//TODO: Ausgabe entfernen
				System.out.println(solution.getAsTupel());
				System.out.println(solution.getTotalDistanceWithCostFactor() + "; " + solution.getNumberOfTours());
				TabuSearchForElementWithTours tabuSearch = getTS();
				solution = (SolutionGot) tabuSearch.improve(solution);
				adaptiveMemory.addTours(solution);
				if (isNewSolutionIsNewBestOverallSolution()) {
					setBestOverallSolutionToNewSolution();
					addNewSolutionToBestSolutions();
				}
			}
		}
		
		private boolean isStoppingCriterionMet() {
			if (Parameters.isRunningTimeReached())
				return true;
			if (numberOfAMCallsWithoutImprovement > maximalNumberOfCallsWithoutImprovementToAdaptiveMemory)
				return true;
//			if (callsToAdaptiveMemory >= maximalNumberOfCallsToAdaptiveMemory)
//				return true;
//			if (numberOfTimesSameBestOverallSolutionHasBeenFound >= 3)
//				return true;
			return false;
		}
		
		private SolutionGot constructInitialSolutionFromAdaptiveMemory() throws IOException {
			callsToAdaptiveMemory++;
			return adaptiveMemory.constructSolutionFromTours();
		}
		
		private void storeNewToursInAdaptiveMemory(SolutionGot solution) {
			adaptiveMemory.addTours(solution);
		}
		
		private boolean isBestOverallSolutionFoundAgain() {
			return solution.equals(bestOverallSolution);
		}
		
		private void increaseNumberOfTimesBestOverallSolutionHasBeenFound() {
			numberOfTimesSameBestOverallSolutionHasBeenFound++;
		}
	
		protected boolean isNewSolutionIsNewBestOverallSolution() {
//			assertEquals(false, solution.equals(bestOverallSolution));
			if (bestOverallSolution == null)
				return true;
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactor(),bestOverallSolution.getTotalDistanceWithCostFactor())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
					return true;
			}
			if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
				return true;
			}
			return false;
		}
		
		private void setBestOverallSolutionToNewSolution() throws IOException {
			bestOverallSolution = solution.clone();	
			IOUtils.write("Dies ist die neue beste Lösung in AM \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
		}
		
		private void addNewSolutionToBestSolutions() {
			bestSolutions.add(bestOverallSolution);
		}
	
		protected void improveBestSolutionsWithCostMinimizationPhase() throws IOException {
			System.out.println("Starte Cost MinimizationPhase");
			sortListOfBestSolutionsAccordingToTourNumberAndCost();
			CutListAtSomePoint();
			processSolutionsWithCostMinimzationTSAndStoreBestOverallSolution();
			publishSolution(bestOverallSolution);
		}

			protected void sortListOfBestSolutionsAccordingToTourNumberAndCost() throws IOException {
	//			Comparator<SolutionGot> byDeterministicCost = (e1,e2) -> Double.compare(e1.getTotalDistanceWithCostFactor(),e2.getTotalDistanceWithCostFactor());		
	//			Collections.sort(bestSolutions, byDeterministicCost);	
			Collections.sort(bestSolutions,
	                new Comparator<SolutionGot>() {
	                    @Override
	                    public int compare(SolutionGot o1, SolutionGot o2) {
	                    	if (o1.getNumberOfTours() < o2.getNumberOfTours())
	                    		return -1;
	                    	if (o1.getNumberOfTours() > o2.getNumberOfTours())
	                    		return 1;
	                    	if (o1.getNumberOfTours() == o2.getNumberOfTours()) {
	                    		if (MathUtils.lessThan(o1.getTotalDistanceWithCostFactor(),o2.getTotalDistanceWithCostFactor())) {
	                                return -1;
	                            }
	                    		if (MathUtils.greaterThan(o1.getTotalDistanceWithCostFactor(),o2.getTotalDistanceWithCostFactor())) {
	                                return 1;
	                            }
	                    		return 0;
	                    	}
	                    	throw new RuntimeException("Should not happen");
	                    }
	                });
			PrintUtils.printListOfSolutions(bestSolutions, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
		}

		private void CutListAtSomePoint() throws IOException {
			if (bestSolutions.size() >= lengthOfList) {
				bestSolutions.subList(0, lengthOfList);
			}
			PrintUtils.printListOfSolutions(bestSolutions, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
		}

		private void processSolutionsWithCostMinimzationTSAndStoreBestOverallSolution() throws IOException {
			Parameters.setTourMinimizationPhase(false);
			int iteration = 0;
			for (SolutionGot sol : bestSolutions) {
				iteration++;
				IOUtils.write("Verbessere Lösung " + iteration + " von " +  bestSolutions.size() +": " + "(" +solution.getNumberOfTours() + ", " + solution.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
				TabuSearchForElementWithTours ts = getTS();
				solution = (SolutionGot) ts.improve(sol);
				IOUtils.write("Verbesserte Lösung: " + "(" +solution.getNumberOfTours() + ", " + solution.getTotalDistanceWithCostFactor() + ") \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
				if (isNewSolutionIsNewBestOverallSolution())
					setBestOverallSolutionToNewSolution();
			}
			Parameters.setTourMinimizationPhase(true);
		}

		private void publishSolutionAtEndOfAMTSSearch() throws IOException {
			endTime = System.currentTimeMillis();
			timeNeeded = endTime - startTime;
			ReadAndWriteUtils.publishSolutionAtEndOfAMTSSearch(bestOverallSolution, timeNeeded);
		}

	public static void setNewRandomWithSeeds(int seedAM, int seedGI, int seedI1) {
		AdaptiveMemory.resetRandomElementWithSeed(seedAM);
		GreedyInsertion.resetRandomElementWithSeed(seedGI);
		RandomI1Solver.resetRandomElementWithSeed(seedI1);
	}

}
