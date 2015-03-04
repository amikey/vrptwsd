package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

import java.util.Comparator;

import de.rwth.lofip.stuffNotNeededRightNow.solver.util.CustomerNoComparator;
import de.rwth.lofip.stuffNotNeededRightNow.util.RemovedCustomer;

/**
 * The {@code Time and failure sorting insertion} as described by Lei et al.
 * (2011), section 3.3.8.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractTimeAndFailureSortingInsertion extends
		AbstractFailureSortingInsertion {

	@Override
	public String getName() {
		return "TFSI";
	}

	@Override
	protected Comparator<RemovedCustomer> getCustomerComparator() {
		return new Comparator<RemovedCustomer>() {

			@Override
			public int compare(RemovedCustomer c1, RemovedCustomer c2) {
				double timeWindow1 = c1.getCustomer().getTimeWindowClose()
						- c1.getCustomer().getTimeWindowOpen();
				double timeWindow2 = c2.getCustomer().getTimeWindowClose()
						- c2.getCustomer().getTimeWindowOpen();
				if (timeWindow1 < timeWindow2) {
					return -1;
				} else if (timeWindow1 > timeWindow2) {
					return 1;
				} else {
					// compare customers according to customer number
					CustomerNoComparator cnc = new CustomerNoComparator();
					return cnc.compare(c1, c2);
				}
			}

		};
	}
}
