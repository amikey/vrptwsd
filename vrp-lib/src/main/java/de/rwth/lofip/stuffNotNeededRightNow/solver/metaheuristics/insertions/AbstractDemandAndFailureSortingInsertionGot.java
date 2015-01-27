package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

import java.util.Comparator;

import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * The {@code Demand and failure sorting insertion} as described by Lei et al.
 * (2011), section 3.3.7.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractDemandAndFailureSortingInsertionGot extends
		AbstractFailureSortingInsertionGot {

	@Override
	public String getName() {
		return "DFSI";
	}

	@Override
	protected Comparator<RemovedCustomer> getCustomerComparator() {
		return new Comparator<RemovedCustomer>() {

			@Override
			public int compare(RemovedCustomer o1, RemovedCustomer o2) {
				if (o1.getCustomer().getDemand() < o2.getCustomer().getDemand()) {
					return -1;
				}
				if (o1.getCustomer().getDemand() > o2.getCustomer().getDemand()) {
					return 1;
				}
				return 0;
			}

		};
	};

}
