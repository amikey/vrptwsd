package de.rwth.lofip.library.analyse.util;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.analyse.CollectStatistics;

public class Scenario {

	double pc; // plan capacity
	double pom; // penetration
	double sol; // deviation without control station
	double sml; // deviation with control station

	double recoursePenaltyFactor;
	int numberOfDemandScenarios;

	// double totalCostAllSolutions;
	//
	// public double getTotalCostAllSolutions() {
	// return totalCostAllSolutions;
	// }

	CollectStatistics cs = new CollectStatistics();
	CollectStatistics csIst = new CollectStatistics();

	// SolutionWithStatistics
	private List<SolutionWithStatistics> sws = new LinkedList<SolutionWithStatistics>();
	private List<SolutionWithStatistics> consideredSWS = new LinkedList<SolutionWithStatistics>();

	public Scenario() {
	}

	// Getter and Setter

	public double getTotalCostConsideredSolutions() {
		double totalCostAllSolutions = 0;
		for (SolutionWithStatistics s : consideredSWS)
			totalCostAllSolutions += s.getSolution().getTotalDistanceOfAllTours();
		return totalCostAllSolutions;
	}

	public int getTotalNumberOfUsedVehicles() {
		int number = 0;
		for (SolutionWithStatistics s : consideredSWS)
			number += s.getSolution().getTours().size();
		return number;
	}

	public double getPc() {
		return pc;
	}

	public void setPc(double pc) {
		this.pc = pc;
	}

	public double getPom() {
		return pom;
	}

	public void setPom(double pom) {
		this.pom = pom;
	}

	public double getSol() {
		return sol;
	}

	public void setSol(double sol) {
		this.sol = sol;
	}

	public double getSml() {
		return sml;
	}

	public void setSml(double sml) {
		this.sml = sml;
	}

	public double getRecoursePenaltyFactor() {
		return recoursePenaltyFactor;
	}

	public void setRecoursePenaltyFactor(double recoursePenaltyFactor) {
		this.recoursePenaltyFactor = recoursePenaltyFactor;
	}

	public int getNumberOfDemandScenarios() {
		return numberOfDemandScenarios;
	}

	public void setNumberOfDemandScenarios(int numberOfDemandScenarios) {
		this.numberOfDemandScenarios = numberOfDemandScenarios;
	}

	public void addSolutionAndStatistics(Solution solution,
			CollectStatistics cs, CollectStatistics csIst) {
		SolutionWithStatistics sws = new SolutionWithStatistics();
		sws.setSolution(solution);
		sws.setStatistics(cs);
		sws.setStatisticsIst(csIst);
		this.sws.add(sws);
	}

	public List<SolutionWithStatistics> getSolutionWithStatistics() {
		return sws;
	}

	/**
	 * Methods to set scenarios that shall be considered in the report
	 */
	public void setConsideredSolutions(String whichOnes) {
		if (whichOnes == "Alle")
			setConsideredSolutionsAll();
		else if (whichOnes == "clustered")
			setConsideredSolutionsClustered();
		else if (whichOnes == "random")
			setConsideredSolutionsRandom();
		else if (whichOnes == "rc")
			setConsideredSolutionsRC();
	}

	public void setConsideredSolutionsAll() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		consideredSWS = sws;
		setStatistics();
	}

	public void setConsideredSolutionsClustered() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			if (swsTemp.getSolution().getVrpProblem().getDescription()
					.contains("C")
					&& !swsTemp.getSolution().getVrpProblem().getDescription()
							.contains("RC"))
				consideredSWS.add(swsTemp);
		}
		setStatistics();
	}

	public void setConsideredSolutionsRandom() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			if (swsTemp.getSolution().getVrpProblem().getDescription()
					.contains("R")
					&& !swsTemp.getSolution().getVrpProblem().getDescription()
							.contains("RC"))
				consideredSWS.add(swsTemp);
		}
		setStatistics();
	}

	public void setConsideredSolutionsRC() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			if (swsTemp.getSolution().getVrpProblem().getDescription()
					.contains("RC"))
				consideredSWS.add(swsTemp);
		}
		setStatistics();
	}

	public void setConsideredSolutionsTW25() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws)
			if (swsTemp.getSolution().getVrpProblem().getPercentageWithTW() <= 0.25)
				consideredSWS.add(swsTemp);
	}

	public void setConsideredSolutionsTW50() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws)
			if (swsTemp.getSolution().getVrpProblem().getPercentageWithTW() <= 0.50)
				consideredSWS.add(swsTemp);
	}

	public void setConsideredSolutionsTW75() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			System.err.println("getPercentageWithTW: "
					+ swsTemp.getSolution().getVrpProblem()
							.getPercentageWithTW());
			if (swsTemp.getSolution().getVrpProblem().getPercentageWithTW() <= 0.75)
				consideredSWS.add(swsTemp);
		}
	}

	public void setConsideredSolutionsTightlyConstrained() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			System.err.println("getPercentageWithTW: "
					+ swsTemp.getSolution().getVrpProblem()
							.getPercentageWithTW());
			if ((swsTemp.getSolution().getVrpProblem().getPercentageWithTW() >= 0.75)
					&& (swsTemp.getSolution().getVrpProblem()
							.getPercentageWithTightTW() >= 0.50))
				consideredSWS.add(swsTemp);
		}
	}

	public void setConsideredSolutionsLooselyConstrained() {
		consideredSWS = new LinkedList<SolutionWithStatistics>();
		for (SolutionWithStatistics swsTemp : sws) {
			System.err.println("getPercentageWithTW: "
					+ swsTemp.getSolution().getVrpProblem()
							.getPercentageWithTW());
			if ((swsTemp.getSolution().getVrpProblem().getPercentageWithTW() <= 0.50)
					&& (swsTemp.getSolution().getVrpProblem()
							.getPercentageWithTightTW() < 0.25))
				consideredSWS.add(swsTemp);
		}
	}

	private void setStatistics() {
		cs.resetRepairedSolutions();
		for (SolutionWithStatistics swsTemp : consideredSWS)
			cs.addRepairedSolutions(swsTemp.getStatistics()
					.getRepairedSolutions());
		csIst.resetRepairedSolutions();
		for (SolutionWithStatistics swsTemp : consideredSWS)
			csIst.addRepairedSolutions(swsTemp.getStatisticsIst()
					.getRepairedSolutions());
	}

	public List<SolutionWithStatistics> getConsideredSolutions() {
		return consideredSWS;
	}

	// public void setStatisticsAll(){
	// for (SolutionWithStatistics swsTemp : sws)
	// cs.addRepairedSolutions(swsTemp.getStatistics().getRepairedSolutions());
	// }
	//
	// public void setStatisticsIstAll(){
	// for (SolutionWithStatistics swsTemp : sws)
	// csIst.addRepairedSolutions(swsTemp.getStatisticsIst().getRepairedSolutions());
	// }

	public CollectStatistics getCS() {
		return cs;
	}

	public CollectStatistics getCSIST() {
		return csIst;
	}

	// return solutions with XX% Auslastung

	// return solutions with XX% of time windows

	// return geclusterte scenarios

	// return

}
