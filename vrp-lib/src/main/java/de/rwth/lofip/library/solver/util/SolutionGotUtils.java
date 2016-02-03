package de.rwth.lofip.library.solver.util;

import java.util.Iterator;
import java.util.Map.Entry;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;

public class SolutionGotUtils {
	
	public static boolean isSolutionFeasibleWrtDemand(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemand(tour))
				return false;
		}
		return true;
	}
	
	public static boolean isSolutionFeasibleWrtTW(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtTW(tour))
				return false;
		}
		return true;
	}
	
	public static String createStringForCustomersServedByNumberOfVehicles(ElementWithTours bestOverallSolution) {
		String s = "";
		Iterator<Entry<Integer, Integer>> it = ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfCustomersServedByNumberOfDifferentTours().entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pair = it.next();
	        //the following line is totally not necessary and just for better readiness 
	        int currentlyExaminedNumberOfVehicles = pair.getKey();
	        int numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles = pair.getValue();
	        s = s + numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles + ";";
	    }
	    return s;
	}
}
