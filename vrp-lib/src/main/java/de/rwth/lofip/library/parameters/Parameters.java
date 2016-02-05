package de.rwth.lofip.library.parameters;

import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;

public class Parameters {
	
	//Debugging Options
	private static boolean testing = false; //if true slow: processes asserts that need O(n) time for evaluation
	
	//Print Options
	private static boolean publishSolutionValueProgress = false;
	private static boolean publishSolutionAtEndOfTabuSearch = false;
	private static boolean isPublishSolutionAtEndOfAMTSSearch = false;
	
	//GroupOfTours
	private static int MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
	
	//CrossNeighborhood 
	private static int MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 7;
	
	//Tour Elimination
	private static boolean miminizeTours = true;
	private static int MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 3;
	private static int numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour = 1;
	private static int minimumNumberOfIterationsWithoutTourElimination = 10;
	
	//Tour Elimination primäres Ziel?
	// wo werden Touren minimiert:
	// - Akzeptanzkriterium für bestMove in CrossNeighborhood
	// - In Sortieralgorithmus in Adaptive Memory 
	
	//Intensification Phase
	private static int numberOfIntensificationTries = 10;
	
	//AdaptiveMemory
	private static int numberOfDifferentInitialSolutionsInAM = 20;
	private static int maximalNumberOfCallsToAdaptiveMemory = 0; // 0 -> wird nicht verwendet
	private static int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
	private static int maximalNumberOfToursInAdaptiveMemory = 1000;
	
	//TabuSearch
	// + parameter dafür wie lange Moves Tabu sind
	private static int maximalNumberOfIterationsTabuSearch = 0; // 0 -> wird nicht verwendet
	private static int maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
	private static int lengthOfTabuList = 1000000;
	
	//Recourse
	private static int NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
	//+ Parameter für die (Konvex)kombination aus Kosten und #unterschiedlicherRecourseActions
	private static double costFactorForAdditionalTourInRecourse = 2.0;
	private static boolean recourseActionNumberMinimization;
	
	
	//Plankapazität für deterministische Planung
	private static double percentageOfCapacity = 1;

	//Outputdirectory
	private static String directory;
	
	//Simulation
	private final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 100;
	private static double RELATIVE_STANDARD_DEVIATION = 0.15;

	//Seeds
	private static int seedI1 = 100;
	private static int seedGI = 3000;
	private static int seedAM = 3000;

	private static long startingTime;
	private static long endTime;
	private static boolean isRunningTimeSet = false;
	
	//Getter und Setter	
	public static void setAllParametersToDefaultValues(){
		testing = false;
		publishSolutionValueProgress = false;
		publishSolutionAtEndOfTabuSearch = false;
		MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
		MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 7;
		miminizeTours = true;
		MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 7;
		numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour = 1;
		minimumNumberOfIterationsWithoutTourElimination = 10;
		numberOfDifferentInitialSolutionsInAM = 20;
		maximalNumberOfCallsToAdaptiveMemory = 0;
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
		maximalNumberOfToursInAdaptiveMemory = 1000;
		maximalNumberOfIterationsTabuSearch = 0;
		maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
		lengthOfTabuList = 1000000;
		NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
		costFactorForAdditionalTourInRecourse = 2.0;
		recourseActionNumberMinimization = false;
		percentageOfCapacity = 1;
		RELATIVE_STANDARD_DEVIATION = 0.15;
		
		seedI1 = 3000;
		seedGI = 3000;
		seedAM = 3000;
		
		SimulationUtils.resetSeed();
	}
	
	public static void setAllParametersToMinimalTestingValues() {
		testing = false;
		publishSolutionValueProgress = false;
		publishSolutionAtEndOfTabuSearch = false;
		MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
		MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 4;
		miminizeTours = true;
		MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 7;
		numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour = 1;
		minimumNumberOfIterationsWithoutTourElimination = 10;
		numberOfDifferentInitialSolutionsInAM = 1;
		maximalNumberOfCallsToAdaptiveMemory = 0;
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 0;
		maximalNumberOfToursInAdaptiveMemory = 200;
		maximalNumberOfIterationsTabuSearch = 0;
		maximalNumberOfIterationsWithoutImprovementTabuSearch = 0;
		lengthOfTabuList = 1000000;
		NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
		costFactorForAdditionalTourInRecourse = 2.0;
		recourseActionNumberMinimization = false;
		percentageOfCapacity = 1;
		RELATIVE_STANDARD_DEVIATION = 0.15;
		
		seedI1 = 3000;
		seedGI = 3000;
		seedAM = 3000;
		
		SimulationUtils.resetSeed();
	}

	
	public static void setAllParametersToNewBestValuesAfterParameterTesting(){
		testing = false;
		publishSolutionValueProgress = false;
		publishSolutionAtEndOfTabuSearch = false;
		MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
		MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 4;
		miminizeTours = true;
		MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 7;
		numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour = 1;
		minimumNumberOfIterationsWithoutTourElimination = 10;
		numberOfDifferentInitialSolutionsInAM = 100;
		maximalNumberOfCallsToAdaptiveMemory = 0;
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
		maximalNumberOfToursInAdaptiveMemory = 200;
		maximalNumberOfIterationsTabuSearch = 0;
		maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
		lengthOfTabuList = 1000000;
		NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
		costFactorForAdditionalTourInRecourse = 2.0;
		recourseActionNumberMinimization = false;
		percentageOfCapacity = 1;
		RELATIVE_STANDARD_DEVIATION = 0.15;
		
		seedI1 = 3000;
		seedGI = 3000;
		seedAM = 3000;
		
		SimulationUtils.resetSeed();
	}
	
	public static int getNumberOfDemandScenarioRuns() {	
		return NUMBER_OF_DEMAND_SCENARIO_RUNS;
	}

	public static double getFluctuationOfDemandInPercentage() { 
		return RELATIVE_STANDARD_DEVIATION;
	}

	public static int getMaximalNumberOfToursInGots() {
		return MAXIMAL_NUMBER_OF_TOURS_IN_GOTS;
	}

	public static int getMaximalLengthOfSegmentInMove() {
		return MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED;
	}

	public static void setNumberOfToursInGot(int numberOfToursInGot) {
		MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = numberOfToursInGot;		
	}

	public static int getNumberOfMovesThatStochasticCostIsCalculatedFor() {
		return NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR;
	}

	public static int getMaximalNumberOfToursInAdaptiveMemory() {
		return maximalNumberOfToursInAdaptiveMemory;
	}

	public static int getNumberOfDifferentInitialSolutionsInAM() {
		return numberOfDifferentInitialSolutionsInAM;
	}

	public static int getMaximalNumberOfIterationsTabuSearch() {
		return maximalNumberOfIterationsTabuSearch;
	}

	public static int getMaximalNumberOfIterationsWithoutImprovementTabuSearch() {
		return maximalNumberOfIterationsWithoutImprovementTabuSearch;
	}

	public static int getMaximalNumberOfCallsToAdaptiveMemory() {
		return maximalNumberOfCallsToAdaptiveMemory;
	}

	public static int getMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory() {
		return maximalNumberOfCallsWithoutImprovementToAdaptiveMemory;
	}

	public static boolean publishSolutionValueProgress() {
		return publishSolutionValueProgress;
	}

	public static boolean publishSolutionAtEndOfTabuSearch() {
		return publishSolutionAtEndOfTabuSearch;
	}

	public static boolean shallTourNumberBeMinimized() {
		return miminizeTours;
	}

	public static boolean isTestingMode() {
		return testing;
	}

	public static void setOutputDirectory(String string) {
		directory = string;
	}

	public static String getOutputDirectory() {
		if (directory == null)
			throw new RuntimeException("Outputdirectory muss gesetzt werden in RunAdaptiveMemorySolver");
		return directory;
	}

	public static void setPublishSolutionAtEndOfTabuSearch(boolean b) {
		publishSolutionAtEndOfTabuSearch = b;
	}

	public static double getPercentageOfCapacity() {
		return percentageOfCapacity;
	}

	public static void setPercentageOfCapacity(double percentageOfCapacity) {
		Parameters.percentageOfCapacity = percentageOfCapacity;
	}

	public static void setNumberOfNonImprovingIterationsInTS(int i) {
		maximalNumberOfIterationsWithoutImprovementTabuSearch = i;
	}

	public static void setNumberOfIterationsInTS(
			int maximalNumberOfIterationsTabuSearch2) {
		maximalNumberOfIterationsTabuSearch = maximalNumberOfIterationsTabuSearch2;
		
	}

	public static void setRelativeStandardDeviationTo(double d) {
		RELATIVE_STANDARD_DEVIATION = d;
	}
	
	public static void setSeeds(int i) {
		seedI1 = i;
		seedGI = i;
		seedAM = i;
	}

	public static int getSeedI1() {
		return seedI1;
	}

	public static int getSeedGI() {
		return seedGI;
	}

	public static int getSeedAM() {
		return seedAM;
	}

	public static void setFluctuationOfDemandTo(double d) {
		RELATIVE_STANDARD_DEVIATION = d;
	}

	public static void setTestingMode(boolean b) {
		testing = true;
	}

	public static void setMaximalNumberOfToursInAdaptiveMemory(int i) {
		maximalNumberOfToursInAdaptiveMemory = i;
	}

	public static double getCostFactorForAdditionalToursInRecourse() {
		return costFactorForAdditionalTourInRecourse;
	}

	public static boolean isRecourseActionNumberMinimization() {
		return recourseActionNumberMinimization;
	}
	
	public static void setRecourseActionNumberMinimization(boolean b) {
		recourseActionNumberMinimization = b;
	}

	public static void setMaximalNumberOfCustomersConsideredInSegment(int i) {
		 MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = i;
	}

	public static void setRunningTimeInHours(int i) {
		startingTime = System.currentTimeMillis();
		endTime = startingTime + (i * 60 * 60 * 1000);
		isRunningTimeSet = true;
	}

	public static boolean isRunningTimeReached() {
		if (!isRunningTimeSet)
			return false;
		else 
			return ( System.currentTimeMillis() >= endTime);
	}

	public static void setPublishSolutionValueProgress(boolean b) {
		publishSolutionValueProgress = b;
	}

	public static void setNumberOfInitialSolutions(int i) {
		numberOfDifferentInitialSolutionsInAM = i;
	}

	public static void setNumberOfNonImprovingAMCalls(int i) {
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = i;
	}

	public static int getNumberOfInsertionTries() {
		return numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour;
	}
	
	public static void setNumberOfInsertionTries(int i) {
		numberOfTimesThatGreedyInsertionIsTriedWithDeletedTour = i;
	}

	public static int getMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood() {
		return MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD;
	}
	
	public static void setMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood(int i) {
		MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = i;
		
	}

	public static int getMinimumNumberOfIterationsWithoutTourElemination() {
		return minimumNumberOfIterationsWithoutTourElimination;
	}

	public static void setMinimumNumberOfIterationsWithoutTourElemination(int i) {
		minimumNumberOfIterationsWithoutTourElimination = i;
	}
	
	public static boolean publishSolutionAtEndOfAMTSSearch() {
		return isPublishSolutionAtEndOfAMTSSearch;
	}

	public static void setPublishSolutionAtEndOfAMTSSearch(boolean b) {
		isPublishSolutionAtEndOfAMTSSearch = b;		
	}

	public static int getLengthOfTabuList() {
		return lengthOfTabuList;
	}

	public static void resetSeedsAndRandomElements() {
		GreedyInsertion.resetRandomElementWithSeed(1);
		RandomI1Solver.resetRandomElementWithSeed(1);
		AdaptiveMemory.resetRandomElementWithSeed(1);
	}

	public static int getNumberOfIntensificationTries() {
		return numberOfIntensificationTries;
	}

	public static void setNumberOfIntensificationTries(int numberOfIntensificationTries) {
		Parameters.numberOfIntensificationTries = numberOfIntensificationTries;
	}

	
	

}
