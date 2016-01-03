package de.rwth.lofip.library.parameters;

import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;

public class Parameters {
	
	//Debugging Options
	private static boolean debugging = false; //if true slow: processes asserts that need O(n) time for evaluation
	
	//Print Options
	private static boolean publishSolutionValueProgress = false;
	private static boolean publishSolutionAtEndOfTabuSearch = false;
	
	//GroupOfTours
	private static int MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
	
	//CrossNeighborhood 
	private static int MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 7;
	
	//Tour Elimination
	private static boolean miminizeTours = true;
	private static int MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 7;
	
	//Tour Elimination primäres Ziel?
	// wo werden Touren minimiert:
	// - Akzeptanzkriterium für bestMove in CrossNeighborhood
	// - In Sortieralgorithmus in Adaptive Memory 
	
	//AdaptiveMemory
	private static int numberOfDifferentInitialSolutions = 20;
	private static int maximalNumberOfCallsToAdaptiveMemory = 0; // 0 -> wird nicht verwendet
	private static int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
	private static int maximalNumberOfToursInAdaptiveMemory = 1000;
	
	//TabuSearch
	// + parameter dafür wie lange Moves Tabu sind
	private static int maximalNumberOfIterationsTabuSearch = 0; // 0 -> wird nicht verwendet
	private static int maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
	
	//Recourse
	private static int NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
	//+ Parameter für die (Konvex)kombination aus Kosten und #unterschiedlicherRecourseActions
	
	//Plankapazität für deterministische Planung
	private static double percentageOfCapacity = 1;

	//Outputdirectory
	private static String directory;
	
	//Simulation
	private final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 100;
	private final static double RELATIVE_STANDARD_DEVIATION = 0.15;
	
	
	//Getter und Setter	
	public static void setAllParametersToDefaultValues(){
		debugging = false;
		publishSolutionValueProgress = false;
		publishSolutionAtEndOfTabuSearch = false;
		MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
		MAXIMAL_NUMBER_OF_CUSTOMERS_IN_SEGMENT_THAT_CAN_BE_MOVED = 7;
		miminizeTours = true;
		MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD = 7;
		numberOfDifferentInitialSolutions = 20;
		maximalNumberOfCallsToAdaptiveMemory = 0;
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
		maximalNumberOfToursInAdaptiveMemory = 1000;
		maximalNumberOfIterationsTabuSearch = 0;
		maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
		NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
		percentageOfCapacity = 1;
		
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

	public static int getMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood() {
		return MAXIMAL_NUMBER_OF_CUSTOMERS_THAT_TOUR_CAN_CONTAIN_TO_BE_CONSIDERED_FOR_DELETION_IN_TOUR_ELIMINATION_NEIGHBORHOOD;
	}

	public static int getMaximalNumberOfToursInAdaptiveMemory() {
		return maximalNumberOfToursInAdaptiveMemory;
	}

	public static int getNumberOfDifferentInitialSolutionsInAM() {
		return numberOfDifferentInitialSolutions;
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

	public static boolean isDebuggingMode() {
		return debugging;
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

	public static void setNumberOfImprovingIterationsInTS(int i) {
		maximalNumberOfIterationsWithoutImprovementTabuSearch = i;
	}

	public static void setNumberOfIterationsInTS(
			int maximalNumberOfIterationsTabuSearch2) {
		maximalNumberOfIterationsTabuSearch = maximalNumberOfIterationsTabuSearch2;
		
	}
	

}
