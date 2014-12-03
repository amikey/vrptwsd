package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.util.IntermediateSolution;
import de.rwth.lofip.library.util.IntermediateSolutionGot;

/**
 * A {@code MetaSolver} is a solver which takes an existing {@link Solution} and
 * tries to improve it.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface MetaSolverInterfaceGot {

	public SolutionGot improve(SolutionGot solution, VrpConfiguration configuration);

	/**
	 * This method is needed to be able to externally cancel a metaheuristic.
	 * How the cancellation is done depends on the implementation.
	 */
	public void setExternallyCancelled(boolean externallyCancelled);

	/**
	 * This method is meant to be called whenever a solution is ready to be
	 * displayed or printed. It does not need to be implemented in a sensible
	 * way, but it allows displaying structures, e.g. a GUI, to get intermediate
	 * results.
	 * 
	 * @param intermediateSolution
	 */
	public void publishSolution(IntermediateSolutionGot intermediateSolution);
}
