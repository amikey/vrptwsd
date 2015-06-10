package de.rwth.lofip.library.parameters;

public class Parameters {
	
	//Algorithm
	private static int MAXIMAL_NUMBER_OF_TOURS_IN_GOTS = 1;
	
	//Recourse
	private static int NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR = 10;
	
	//Simulation
	private final static int NUMBER_OF_DEMAND_SCENARIO_RUNS = 100;
	private final static double FLUCTUATION_OF_DEMAND_IN_PERCENTAGE = 0.8;
	
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

	public static int getNumberOfMovesThatRecourseCostAreCalculatedFor() {
		return NUMBER_OF_MOVES_THAT_RECOURSE_COST_ARE_CALCULATED_FOR;
	}

}
