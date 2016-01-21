package de.rwth.lofip.library.util;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;

public class GotUtils {
	
	public static boolean isCustomersInGotsHaveTheSameDemands(GroupOfTours got1, GroupOfTours got2) {
		boolean same = true;
		for (Customer cGot1 : got1.getCustomers()) {
			Customer cGot2 = got2.getCustomerWithNo((int) cGot1.getCustomerNo());
			if (cGot1.getDemand() != cGot2.getDemand())
				same = false;
		}
		return same;
	}

}
