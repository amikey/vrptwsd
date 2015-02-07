package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals;

import java.util.List;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.HeuristicInterface;
import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * An instance of an implementing class of this interface is used in
 * {@code MetaSolverInterface} instances and takes some customers out of a given
 * solution.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface RemovalInterfaceGot extends HeuristicInterface {

	public List<RemovedCustomer> removeCustomers(SolutionGot solution,
			int amountOfRemovedCustomers);

	public String getName();
}