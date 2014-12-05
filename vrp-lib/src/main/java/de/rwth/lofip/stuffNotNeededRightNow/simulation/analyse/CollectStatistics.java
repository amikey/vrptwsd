package de.rwth.lofip.stuffNotNeededRightNow.simulation.analyse;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.stuffNotNeededRightNow.Solution;

//Muss noch für SolutionGot angepasst werden.

/**
* Eine Klasse, um Auswertungen über ALLE Simulationsläufe zu ermöglichen.
* (Arbeitsspeicher könnte aber darunter leiden.)
* 
* @author obock
*/
public class CollectStatistics extends KeyPerformanceIndicators  {
	
	private List<Solution> finalSolutionsForDemandScenarios = new LinkedList<Solution>();

	public CollectStatistics() {
		super();
	}

	public void addFinalSolutionForDemandScenario(Solution solution) {
		finalSolutionsForDemandScenarios.add(solution);	
	}

	public void addFinalSolutionsForDemandScenario(
			List<Solution> finalSolutionsForDemandScenarios) {
		for (Solution solution : finalSolutionsForDemandScenarios)
			this.finalSolutionsForDemandScenarios.add(solution);	
	}
	
	public List<Solution> getFinalSolutionsForDemandScenarios() {
			return finalSolutionsForDemandScenarios;
	}

	public double getAveragePercentageOfCustomersServedTheSameDay() {
		if (finalSolutionsForDemandScenarios.isEmpty())
			throw new RuntimeException("finalSolutionsForDemandScenarios in CollectStatistics ist leer");
		else 
		{
			double averagePercentageOfCustomersServedTheSameDay = 0;
			for (Solution solution : finalSolutionsForDemandScenarios)
			averagePercentageOfCustomersServedTheSameDay += solution.percentageOfCustomersServedTheSameDay();
		    return (double) averagePercentageOfCustomersServedTheSameDay / (double) finalSolutionsForDemandScenarios.size();			
		}
	}

	public double getAveragePercentageOfParcelsCollectedTheSameDay() {
		if (finalSolutionsForDemandScenarios.isEmpty())
			throw new RuntimeException("finalSolutionsForDemandScenarios in CollectStatistics ist leer");
		else 
		{
			double averagePercentageOfParcelsCollectedTheSameDay = 0;
			for (Solution solution : finalSolutionsForDemandScenarios)
				averagePercentageOfParcelsCollectedTheSameDay += solution.percentageOfParcelsCollectedTheSameDay();
			return (double) averagePercentageOfParcelsCollectedTheSameDay / (double) finalSolutionsForDemandScenarios.size();

		}
	}

	public String getAveragePercentageOfCustomersServedTheSameDayAsString() {
		return String.format("%.3f",getAveragePercentageOfCustomersServedTheSameDay());
	}

	public String getAveragePercentageOfParcelsCollectedTheSameDayAsString() {
		return String.format("%.3f",getAveragePercentageOfParcelsCollectedTheSameDay());
	}
	


	// start Set and Add
		
	// TODO: sind eigendlich noch andere Kennzahlen notwendig, als bei der 
	// Klasse KeyPerformanceIndicators?
	// end Set and Add
	
	
}
