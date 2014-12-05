package de.rwth.lofip.library.util;

import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * This class is meant to serve as a "meta class" for solutions, providing more
 * information than just the actual solution. It can be used in the
 * {@code publish()} method of meta heuristics. The calling system (GUI, CLI,
 * ...) can then decide what to do with that information.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class IntermediateSolution {
	private Solution solution;
	private String removalHeuristic;
	private String insertionHeuristic;
	private boolean accepted;
	private boolean bestSolution;
	private long timeForRemoval;
	private long timeForInsertion;
	private long totalTime;
	private boolean improvedSolution;
	private int customersChanged;
	private String weights;

	public int getCustomersChanged() {
		return customersChanged;
	}

	public void setCustomersChanged(int customersChanged) {
		this.customersChanged = customersChanged;
	}

	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public String getRemovalHeuristic() {
		return removalHeuristic;
	}

	public void setRemovalHeuristic(String removalHeuristic) {
		this.removalHeuristic = removalHeuristic;
	}

	public String getInsertionHeuristic() {
		return insertionHeuristic;
	}

	public void setInsertionHeuristic(String insertionHeuristic) {
		this.insertionHeuristic = insertionHeuristic;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(boolean bestSolution) {
		this.bestSolution = bestSolution;
	}

	public long getTimeForRemoval() {
		return timeForRemoval;
	}

	public void setTimeForRemoval(long timeForRemoval) {
		this.timeForRemoval = timeForRemoval;
	}

	public long getTimeForInsertion() {
		return timeForInsertion;
	}

	public void setTimeForInsertion(long timeForInsertion) {
		this.timeForInsertion = timeForInsertion;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public boolean isImprovedSolution() {
		return improvedSolution;
	}

	public void setImprovedSolution(boolean improvedSolution) {
		this.improvedSolution = improvedSolution;
	}

	public void setWeights(String insertionHeuristicWeights,
			String removalHeuristicWeights) {
		weights = insertionHeuristicWeights + removalHeuristicWeights;
	}

	public String getWeightHeuristics() {
		return weights;
	}

}
