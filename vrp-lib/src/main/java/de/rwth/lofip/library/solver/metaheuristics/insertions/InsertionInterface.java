package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.Collection;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.HeuristicInterface;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * An instance of an implementing class of this interface is used in
 * {@code MetaSolverInterface} instances and takes an existing solution and a
 * Set of {@link Customer Customers} and adds those customers.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public interface InsertionInterface extends HeuristicInterface {

	public Solution insertCustomers(Solution solution,
			Collection<RemovedCustomer> customers, final int iteration,
			VrpConfiguration configuration);

	/**
	 * This method is needed as a hook to pre-decide whether an insertion of the
	 * given {@code customer} into the {@code tour} at the {@code position} is
	 * possible.
	 * 
	 * @param customer
	 * @param tour
	 * @param position
	 * @return true, if nothing prevents the insertion
	 */
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position);

	public String getName();
}
