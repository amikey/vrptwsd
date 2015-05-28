package de.rwth.lofip.library.solver.util;

import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.math.MathUtils;

public class SimilarityUtils {
	
	private static double maxDistanceOfAll = Double.MIN_VALUE;
	
	public static void setMaxDistance(Customer customer, List<CustomerInTour> customers) {
		for (CustomerInTour c : customers)
		{
			double distance = new Edge(c, customer).getLength();
			if (MathUtils.greaterThan(distance, maxDistanceOfAll)) 
				maxDistanceOfAll = distance;
		}
	}
	
	public static void setMaxDistance(double maxDistanceOfAllTemp) {
		maxDistanceOfAll = maxDistanceOfAllTemp;	
	}

	//this is the similarity value calculation from lei et al paper
	public static double calculateSimilarity(Customer c1, Customer c2)
	{	    
		if (maxDistanceOfAll == Double.MIN_VALUE)
			throw new RuntimeException("maxDistanceOfAll ist nicht gesetzt. " +
					"Es wurde vergessen irgendwo beim Programmstart den Aufruf SimilarityUtils.calcualteMaxDistnance(vrpProblem) zu setzen.");
		
		double distance = new Edge(c1, c2).getLength();		
		double firstDivisor = distance / maxDistanceOfAll;

	    Customer i = c1;
	    Customer j = c2;
	    double tauIJ = (Math.min(j.getTimeWindowClose(),
	                            i.getTimeWindowClose() + i.getServiceTime()
	                                    + distance) - Math.max(
	                            j.getTimeWindowOpen(), i.getTimeWindowOpen()
	                                    + distance + i.getServiceTime()));
	
	    i = c2;
	    j = c1;
	    double tauJI = (Math.min(j.getTimeWindowClose(),
	                            i.getTimeWindowClose() + i.getServiceTime()
	                                    + distance) - Math.max(
	                            j.getTimeWindowOpen(), i.getTimeWindowOpen()
	                                    + distance + i.getServiceTime()));

	    double secondDivisor = 1 / (tauIJ + tauJI);
	    
	    double term = 1 / (firstDivisor + secondDivisor);
	    return term;	    	  
	}



}
