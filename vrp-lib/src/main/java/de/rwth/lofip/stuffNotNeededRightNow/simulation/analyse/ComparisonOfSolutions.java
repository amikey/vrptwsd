package de.rwth.lofip.stuffNotNeededRightNow.simulation.analyse;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util.RepairedSolution;
import de.rwth.lofip.stuffNotNeededRightNow.simulation.scenario.DemandScenario;

/**
 * Abstrakte Klasse, um Vergleiche von L�sungen zu erm�glichen.
 * 
 * @author obock
 * 
 */
public abstract class ComparisonOfSolutions {

	private List<RepairedSolution> repairedSolutions;
	private DemandScenario demandScenario;

	// TODO: eigentlich brauchen wir das demandScenario f�r keine der
	// Auswertungen oder?
	public ComparisonOfSolutions(DemandScenario demandScenario) {
		this.demandScenario = demandScenario;
		repairedSolutions = new LinkedList<RepairedSolution>();
	}

	public ComparisonOfSolutions() {
		demandScenario = null;
		repairedSolutions = new LinkedList<RepairedSolution>();
	}

	public DemandScenario getDemandScenario() {
		return demandScenario;
	}

	public List<RepairedSolution> getRepairedSolutions() {
		return repairedSolutions;
	}


	public void setRepairedSolutions(List<RepairedSolution> repairedSolutions) {
		this.repairedSolutions = repairedSolutions;
	}

	public void addRepairedSolutions(
			List<RepairedSolution> additionalRepairedSolutions) {
		if (repairedSolutions == null) {
			repairedSolutions = new LinkedList<RepairedSolution>();
		}
		for (RepairedSolution rs : additionalRepairedSolutions) {
			repairedSolutions.add(rs);
		}

	}

	public void addRepairedSolution(RepairedSolution repairedSolution) {
		if (repairedSolutions == null) {
			repairedSolutions = new LinkedList<RepairedSolution>();
		}
		repairedSolutions.add(repairedSolution);
	}

	public void resetRepairedSolutions() {
		repairedSolutions.clear();
	}

}
