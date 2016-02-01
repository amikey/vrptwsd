package de.rwth.lofip.library.util;

import java.util.ArrayList;

import de.rwth.lofip.library.Customer;

public class PrintUtils {
	
	public static void printListOfCustomers(ArrayList<Customer> list) {
		String s = "(";
		for (Customer c : list) {
			s += c.getCustomerNo() + " ";	
		}
		s += ")";
		System.out.println(s);		
	}

}
