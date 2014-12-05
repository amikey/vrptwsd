package de.rwth.lofip.stuffNotNeededRightNow.simulation.analyse.util;

import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.analyse.CollectStatistics;

public class SolutionWithStatistics {

	private Solution solution;
	private CollectStatistics statistics;
	private CollectStatistics statisticsIst;

	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public CollectStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(CollectStatistics statistics) {
		this.statistics = statistics;
	}

	public CollectStatistics getStatisticsIst() {
		return statisticsIst;
	}

	public void setStatisticsIst(CollectStatistics statisticsIst) {
		this.statisticsIst = statisticsIst;
	}

}
