package de.rwth.lofip.library.analyse;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.solver.repair.util.RepairedSolution;

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

	/**
	 * @return the demandScenario
	 */
	public DemandScenario getDemandScenario() {
		return demandScenario;
	}

	/**
	 * @return the repairedSolutions
	 */
	public List<RepairedSolution> getRepairedSolutions() {
		return repairedSolutions;
	}

	/**
	 * @param repairedSolutions
	 *            the repairedSolutions to set
	 */
	public void setRepairedSolutions(List<RepairedSolution> repairedSolutions) {
		this.repairedSolutions = repairedSolutions;
	}

	/**
	 * @param repairedSolutions
	 *            the repairedSolutions to add
	 */
	public void addRepairedSolutions(
			List<RepairedSolution> additionalRepairedSolutions) {
		if (repairedSolutions == null) {
			repairedSolutions = new LinkedList<RepairedSolution>();
		}
		for (RepairedSolution rs : additionalRepairedSolutions) {
			repairedSolutions.add(rs);
		}

	}

	/**
	 * @param repairedSolution
	 *            - the {@code RepairedSolution} to add to the
	 *            {@code repairedSolutions}-Set
	 */
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
