package de.rwth.lofip.library.util;

import java.util.ArrayList;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;

public class PrintUtils {
	
	public static void printListOfCustomers(ArrayList<Customer> list) {
		String s = "(";
		for (Customer c : list) {
			s += c.getCustomerNo() + " ";	
		}
		s += ")";
		System.out.println(s);		
	}

	public static void printListOfSolutions(ArrayList<SolutionGot> solutions) {
		String s = "";
		for (SolutionGot solution : solutions) {
			s+= "(" +solution.getNumberOfTours() + "; " + solution.getTotalDistanceWithCostFactor() + ") ";
		}
		System.out.println(s);	}

}
