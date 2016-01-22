package de.rwth.lofip.library.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	public static void printCustomerWithDemandAndOriginalDemand(GroupOfTours gotTemp) {
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.addAll(gotTemp.getCustomers());
		sortListWrtToCustomerNo(customerList);
		
		for (Customer c : customerList) 
			System.out.println("Customer " + c.getCustomerNo() + " Demand: " + c.getDemand() + " OriginalDemand " + c.getOriginalDemand());
	}

	private static void sortListWrtToCustomerNo(List<Customer> customerList) {
		Comparator<Customer> customerByNoComparator = (e1,e2) -> Double.compare(e1.getCustomerNo(), e2.getCustomerNo());
		Collections.sort(customerList, customerByNoComparator);
	}
}
