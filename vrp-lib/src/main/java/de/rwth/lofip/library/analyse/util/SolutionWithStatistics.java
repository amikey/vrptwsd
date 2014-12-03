package de.rwth.lofip.library.analyse.util;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.analyse.CollectStatistics;

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
