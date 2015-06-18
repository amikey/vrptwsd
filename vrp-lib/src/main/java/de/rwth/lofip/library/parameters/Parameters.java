package de.rwth.lofip.library.parameters;

public class Parameters {
	
	//Algorithm
	private static int MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 2;
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
	
	//Simulation
	private final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 100;
	private final static double FLUCTUATION_OF_DEMAND_IN_PERCENTAGE = 0.45;
	
	
	//Getter und Setter	
	public static int getNumberOfDemandScenarioRuns() {	
		return NUMBER_OF_DEMAND_SCENARIO_RUNS;
	}

	public static double getFluctuationOfDemandInPercentage() { 
		return FLUCTUATION_OF_DEMAND_IN_PERCENTAGE;
	}

	public static int getMaximalNumberOfToursInGots() {
		return MAXIMAL_NUMBER_OF_TOURS_IN_GOTS;
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

}
