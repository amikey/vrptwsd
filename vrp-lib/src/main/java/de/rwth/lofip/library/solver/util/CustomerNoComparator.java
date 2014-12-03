package de.rwth.lofip.library.solver.util;

import java.util.Comparator;

import de.rwth.lofip.library.util.RemovedCustomer;

public class CustomerNoComparator implements Comparator<RemovedCustomer> {

	public int compare(RemovedCustomer c1, RemovedCustomer c2) {
		if (c1.getCustomer().getCustomerNo() < c2.getCustomer().getCustomerNo())
			return -1;
		if (c1.getCustomer().getCustomerNo() == c2.getCustomer()
				.getCustomerNo())
			return 0;
		return 1;
	}
}
