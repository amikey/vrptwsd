package de.rwth.lofip.library.analyse;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Solution;

/**
 * This class calculates average values for multiple solutions to a single
 * Solomon Instance. Those values are total cost, deterministic cost, recourse
 * cost and time needed. Additionally,standard deviation, max. value and min.
 * value are calculated for each value.
 * 
 * @author Andreas Braun
 */
public class AverageValuesForMultipleSolutionsOnOneSolomonInstance {

	private List<Solution> solutions;

	public AverageValuesForMultipleSolutionsOnOneSolomonInstance() {
		solutions = new LinkedList<Solution>();
	}

	/**
	 * @param solution
	 *            - the {@code Solution} to add to the {@code solutions}-Set
	 */
	public void addSolution(Solution solution) {
		if (solution == null) {
			solutions = new LinkedList<Solution>();
		}
		solutions.add(solution);
	}

	public double getMeanValueOfObjective() throws Exception {
		double sum = 0;
		for (Solution solution : solutions) {
			sum += solution
					.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
		}
		double meanValueOfObjective = sum / solutions.size();

		return meanValueOfObjective;
	}

	public double getMinimalValueOfObjective() throws Exception {
		double minimalValue = Double.MAX_VALUE;
		for (Solution solution : solutions) {
			if (solution
					.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() < minimalValue)
				minimalValue = solution
						.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
		}
		return minimalValue;
	}

	public double getMaximalValueOfObjective() throws Exception {
		double maximalValue = 0;
		for (Solution solution : solutions) {
			if (solution
					.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() > maximalValue)
				maximalValue = solution
						.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
		}
		return maximalValue;
	}

	public double getStandardDeviationOfObjective() throws Exception {
		double denominator = 0;
		for (Solution solution : solutions) {
			denominator += Math
					.pow(solution
							.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost()
							- getMeanValueOfObjective(), 2);
		}
		double variance = denominator / solutions.size();
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	public double getMeanValueOfDetObjective() {
		double sum = 0;
		for (Solution solution : solutions) {
			sum += solution.getTotalDistanceOfAllTours();
		}
		double meanValueOfObjective = sum / solutions.size();

		return meanValueOfObjective;
	}

	public double getMinimalValueOfDetObjective() {
		double minimalValue = Double.MAX_VALUE;
		for (Solution solution : solutions) {
			if (solution.getTotalDistanceOfAllTours() < minimalValue)
				minimalValue = solution.getTotalDistanceOfAllTours();
		}
		return minimalValue;
	}

	public double getMaximalValueOfDetObjective() {
		double maximalValue = 0;
		for (Solution solution : solutions) {
			if (solution.getTotalDistanceOfAllTours() > maximalValue)
				maximalValue = solution.getTotalDistanceOfAllTours();
		}
		return maximalValue;
	}

	public double getStandardDeviationOfDetObjective() {
		double denominator = 0;
		for (Solution solution : solutions) {
			denominator += Math.pow(solution.getTotalDistanceOfAllTours()
					- getMeanValueOfDetObjective(), 2);
		}
		double variance = denominator / solutions.size();
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	public double getMeanValueOfStochObjective() throws Exception {
		double sum = 0;
		for (Solution solution : solutions) {
			sum += solution.getExpectedRecourseCost();
		}
		double meanValueOfObjective = sum / solutions.size();

		return meanValueOfObjective;
	}

	public double getMinimalValueOfStochObjective() throws Exception {
		double minimalValue = Double.MAX_VALUE;
		for (Solution solution : solutions) {
			if (solution.getExpectedRecourseCost() < minimalValue)
				minimalValue = solution.getExpectedRecourseCost();
		}
		return minimalValue;
	}

	public double getMaximalValueOfStochObjective() throws Exception {
		double maximalValue = 0;
		for (Solution solution : solutions) {
			if (solution.getExpectedRecourseCost() > maximalValue)
				maximalValue = solution.getExpectedRecourseCost();
		}
		return maximalValue;
	}

	public double getStandardDeviationOfStochObjective() throws Exception {
		double denominator = 0;
		for (Solution solution : solutions) {
			denominator += Math.pow(solution.getExpectedRecourseCost()
					- getMeanValueOfStochObjective(), 2);
		}
		double variance = denominator / solutions.size();
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	public long getMeanValueOfTime() {
		long sum = 0;
		for (Solution solution : solutions) {
			sum += solution.getTimeNeeded();
		}
		long meanValueOfObjective = sum / solutions.size();

		return meanValueOfObjective;
	}

	public long getMinimalValueOfTime() {
		long minimalValue = Long.MAX_VALUE;
		for (Solution solution : solutions) {
			if (solution.getTimeNeeded() < minimalValue)
				minimalValue = solution.getTimeNeeded();
		}
		return minimalValue;
	}

	public long getMaximalValueOfTime() throws Exception {
		long maximalValue = 0;
		for (Solution solution : solutions) {
			if (solution
					.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() > maximalValue)
				maximalValue = solution.getTimeNeeded();
		}
		return maximalValue;
	}

	public long getStandardDeviationOfTime() {
		long denominator = 0;
		for (Solution solution : solutions) {
			denominator += Math.pow(solution.getTimeNeeded()
					- getMeanValueOfTime(), 2);
		}
		long variance = denominator / solutions.size();
		long standardDeviation = (long) Math.sqrt(variance);
		return standardDeviation;
	}

}
