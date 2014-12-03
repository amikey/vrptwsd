package de.rwth.lofip.library.solver.util;

/**
 * This class is basically an object which holds the information about a tabu
 * criterion. It only has an iteration to be able to remove it after some
 * specified time and a String, holding a representation of some tabued
 * criterion. The exact representation will be implementation-specific.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class TabuTourPosition {

	private int iteration;
	private String tabuValue;

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public String getTabuValue() {
		return tabuValue;
	}

	public void setTabuValue(String tabuValue) {
		this.tabuValue = tabuValue;
	}

}
