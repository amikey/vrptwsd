package de.rwth.lofip.library.solver.metaheuristics.interfaces;

import java.io.IOException;

import de.rwth.lofip.library.interfaces.ElementWithTours;

/**
 * A {@code MetaSolver} is a solver which takes an existing {@link Solution} and
 * tries to improve it.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface MetaSolverInterfaceGot {

	public ElementWithTours improve(ElementWithTours solution) throws IOException;
	

//	/**
//	 * This method is needed to be able to externally cancel a metaheuristic.
//	 * How the cancellation is done depends on the implementation.
//	 */
//	public void setExternallyCancelled(boolean externallyCancelled);
//
//	/**
//	 * This method is meant to be called whenever a solution is ready to be
//	 * displayed or printed. It does not need to be implemented in a sensible
//	 * way, but it allows displaying structures, e.g. a GUI, to get intermediate
//	 * results.
//	 * 
//	 * @param intermediateSolution
//	 */
//	public void publishSolution(IntermediateSolutionGot intermediateSolution);
}
