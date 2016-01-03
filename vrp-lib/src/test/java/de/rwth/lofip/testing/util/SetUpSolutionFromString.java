package de.rwth.lofip.testing.util;

import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;

public class SetUpSolutionFromString {

	public static SolutionGot SetUpSolution(String string, VrpProblem problem) {
		boolean inGot = false;
		boolean inTour = false;
		GroupOfTours got = null;
		Tour tour = null;
		SolutionGot solution = new SolutionGot(problem);
		
		string = StringUtils.normalizeSpace(StringUtils.trim(string));
//		System.out.println(string);
//		String[] split = StringUtils.split(string);
		String[] split= string.split(" ");
		for (int i = 0; i < split.length; i++) {
			if (split[i].equals("(")) {
				if (inGot == false) {
					got = new GroupOfTours(solution);
					solution.addGot(got);
					inGot = true;
					inTour = false;
				} else if (inGot == true) {
					tour = new Tour(problem,got);	
					got.addTour(tour);
					inTour = true;
				}
			} else if (split[i].equals(")")) {
				if (inTour == true)
					inTour = false;
				else if (inGot == true)
					inGot = false;
				else
					throw new RuntimeException("Input String not valid (more ')'s than '('s");
			} else if (!split[i].equals("0")) {
				System.out.println(split[i]);
				Customer customer = problem.getCustomerWithCustomerNo(Long.parseLong(split[i]));
				tour.addCustomer(customer);
			}		
		}
		return solution;		
	}
	
}
