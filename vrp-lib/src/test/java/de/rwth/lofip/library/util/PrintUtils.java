package de.rwth.lofip.library.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

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
			s+= "(" +solution.getNumberOfTours() + ": " + String.format("%.3f",solution.getTotalDistanceWithCostFactor()) + ") ";
		}
		System.out.println(s);	
	}

	public static void printListOfSolutions(List<SolutionGot> bestSolutions, FileOutputStream outputStream) throws IOException {
		String s = "Beste Lösungen am Ende von AMTS: ";
		for (SolutionGot solution : bestSolutions) {
			s+= "(" +solution.getNumberOfTours() + ": " + String.format("%.3f",solution.getTotalDistanceWithCostFactor()) + ") ";
		}
		s += "\n";
		IOUtils.write(s, outputStream);
	}

}
