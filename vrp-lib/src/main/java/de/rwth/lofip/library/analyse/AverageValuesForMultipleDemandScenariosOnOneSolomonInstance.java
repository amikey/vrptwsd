package de.rwth.lofip.library.analyse;

import java.util.LinkedList;
import java.util.List;

/**
 * This class calculates average values for multiple runs on different Demand
 * Scenarios that are all run on a single solution for a specific solomon
 * instance. Those values are Additionally,standard deviation, max. value and
 * min. value are calculated for each value.
 * 
 * @author Andreas Braun
 */
public class AverageValuesForMultipleDemandScenariosOnOneSolomonInstance {

	private List<KeyPerformanceIndicators> demandScenarios;

	public AverageValuesForMultipleDemandScenariosOnOneSolomonInstance() {
		demandScenarios = new LinkedList<KeyPerformanceIndicators>();
	}

	/**
	 * @param solution
	 *            - the {@code Solution} to add to the {@code demandScenarios}
	 *            -Set
	 */
	public void addDemandScenario(KeyPerformanceIndicators kpi) {
		demandScenarios.add(kpi);
	}

	public double getMeanValueOfAdditionalDistance() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getAverageOfAdditionalDistances();
		}
		double meanValueOfObjective = sum / demandScenarios.size();

		return meanValueOfObjective;
	}

	/**
	 * This method is used to get the average of the total distances that were
	 * created in one demand scenario run. (as opposed to average of average
	 * distances in the method getMeanValueOfAdditionalDistance())
	 */
	public double getMeanValueOfAdditionalDistanceTotalDistancePerScenario() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getTotalAdditionalDistances();
		}
		double meanValueOfObjective = sum / demandScenarios.size();

		return meanValueOfObjective;
	}

	public double getAverageNumberOfAffectedTours() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getAverageOfAffectedTours();
		}
		double meanValue = sum / demandScenarios.size();

		return meanValue;
	}

	public double getAverageNumberOfProblems() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getNumberOfExistingProblems();
		}
		double meanValue = sum / demandScenarios.size();

		return meanValue;
	}

	public int getTotalNumberOfProblems() {
		int sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getNumberOfExistingProblems();
		}
		return sum;
	}

	public long getMeanValueOfTime() {
		long sum = 0;
		if (getTotalNumberOfProblems() != 0) {
			for (KeyPerformanceIndicators kpi : demandScenarios) {
				sum += kpi.getAverageTimeForCalculation();
			}
			long meanValue = sum / getTotalNumberOfProblems();
			return meanValue;
		}
		return sum;
	}

	public double getNumberOfNewCreatedTours() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getNumbersOfNewCreatedTours();
		}
		double meanValue = sum / demandScenarios.size();

		return meanValue;
	}

	public double getAverageNumberOfNeededVehicles() {
		double sum = 0;
		for (KeyPerformanceIndicators kpi : demandScenarios) {
			sum += kpi.getNumberOfUsedVehiclesOneDemandScenarion();
		}
		double meanValue = sum / demandScenarios.size();

		return meanValue;
	}

	public String getAsString() {
		String s = "Mean Additional Distance: "
				+ getMeanValueOfAdditionalDistance() + "\n";
		s += "avg number affected Tours: " + getAverageNumberOfAffectedTours()
				+ "\n";
		s += "avg number of problems: " + getAverageNumberOfProblems() + "\n";
		s += "total number of problems: " + getTotalNumberOfProblems() + "\n";
		s += "mean value time: " + getMeanValueOfTime() / 1000 / 1000 + "ms\n";
		s += "total number of new created tours: "
				+ getNumberOfNewCreatedTours() + "\n";
		s += "avg number needed vehicles: "
				+ getAverageNumberOfNeededVehicles() + "\n";
		return s;
	}

}
