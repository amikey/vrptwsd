package de.rwth.lofip.library.solver.interfaces;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;

/**
 * This interface needs to be implemented by all classes which represent a
 * solver for a VrpProblem. When the program is started, it tries to find all
 * implementing classes in the classpath and offers them as possible solvers.
 * The respective configuration is found by filename convention.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface SolverInterfaceGot {
	/**
	 * 
	 * Method to actually solve a given {@code VrpProblem}. The returned
	 * {@code Solution} must contain the given problem as a reference and
	 * deliver a solution with all tours.
	 * 
	 * @param problem
	 * @return
	 * @throws Exception
	 */
	public SolutionGot solve(VrpProblem problem);

	/**
	 * A text which will appear on the button in the user interface.
	 * 
	 * @return
	 */
	public String getDescriptiveName();

}
