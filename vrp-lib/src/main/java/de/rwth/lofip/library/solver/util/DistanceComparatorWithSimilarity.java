package de.rwth.lofip.library.solver.util;

import java.util.Comparator;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.stuffNotNeededRightNow.solver.util.CustomerWithCost;

public class DistanceComparatorWithSimilarity implements Comparator<CustomerWithCost> {
		
	//this indicates the weight of distance as opposed to TWsize when calculating the similarity between two customers
	private static double WEIGHT_DISTANCE = 0.9;
	//this indicates the weight of the similarity value as opposed to insertion cost when comparing two customers
	private static double WEIGHT_SIMVAL_FV = 0.1;
	
	private Set<Customer> assignedCustomers;
	private Set<Customer> unassignedCustomers;
	//this variable saves the maximal cost that occurs when inserting a customer into a tour
	private double maxCostDistance;
	private double maxSimValue = 0;
	private double maxDistance = Double.MIN_VALUE;
	private double maxTWsize = 0;
	
	public DistanceComparatorWithSimilarity(Set<Customer> assignedCustomers, Set<Customer> unassignedCustomers, double maxCost)
	{
		//Konstruktor
		this.assignedCustomers = assignedCustomers;
		this.unassignedCustomers = unassignedCustomers;
		this.maxCostDistance = maxCost;    		
		calculateMaxDistanceAndMaxTWSize();                                   
		this.maxSimValue = calculateMaxSimValue();    		           
	} 
	
	public static void setWeightForDistanceAsOpposedToTW(double factor) {
		WEIGHT_DISTANCE = factor;		
	}
	
	public static void setWeightSimvalAsOpposedToDistanceAndTW(double factor) {
		WEIGHT_SIMVAL_FV = factor;
	}
	
	//the formula for calculating the similarity for an unassigned customer i is given as
	// \frac{1}{assignedCustomers.size()} \sum_{j \in assignedCustomers} s_{ij}
	
	private void calculateMaxDistanceAndMaxTWSize() {
		//diese beiden Werte müssen berechnet werden, damit Distanz und TW normiert werden können
		for (Customer c1 : unassignedCustomers)
	    {
	    	for (Customer c2 : assignedCustomers)
	    	{
	    		double distance = new Edge(c1, c2).getLength();
	            if (distance > maxDistance) {
	                maxDistance = distance;
	            }
	
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
	
	            if (tauIJ > maxTWsize) 
	                maxTWsize = tauIJ;
	            if (tauJI > maxTWsize)
	            	maxTWsize = tauJI;
	    	}
	    }	
	}
	
	private double calculateMaxSimValue() {
		for (Customer c : unassignedCustomers)
	    {
	    	double simVal = calculateSimilarityValue(c);
	    	if (simVal > maxSimValue)
	    		maxSimValue = simVal;
	    }
		return maxSimValue;
	}
	
	//alternatively, another possibility would be to take the minimum of all sim values
	public double calculateSimilarityValue(Customer c)
	{
		//calculate similarity to all assigned customers and take mean value
		double sum = 0;
		for (Customer cust : assignedCustomers)
		{
			//calculateSimilarity returns value between 0 and 1. 0 is better than 1.
			sum += calculateSimilarity(c,cust);
		}
		return sum / assignedCustomers.size();
	}
	
	//calculate s_{ij}
	public double calculateSimilarity(Customer c1, Customer c2)
	{	    
		double distance = new Edge(c1, c2).getLength();
		double distanceTerm = distance / maxDistance;
		
		double TWsize;
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
	
	    if (tauIJ > tauJI) 
	        TWsize = tauIJ;
	    else 
	    	TWsize = tauJI;
	    if (TWsize < 0)
	    	TWsize = 0;
	    
	    double timeWindowTerm = TWsize / maxTWsize;
	    double similarity = WEIGHT_DISTANCE * distanceTerm + (1-WEIGHT_DISTANCE) * timeWindowTerm;
	    return similarity;
	}
	
	
	@Override
	public int compare(CustomerWithCost o1, CustomerWithCost o2) {
		
		double valueCustomer1 = (1-WEIGHT_SIMVAL_FV) * o1.getCost() / maxCostDistance + WEIGHT_SIMVAL_FV * calculateSimilarityValue(o1.getCustomer()) / maxSimValue; 
	
		double valueCustomer2 = (1-WEIGHT_SIMVAL_FV) * o2.getCost() / maxCostDistance + WEIGHT_SIMVAL_FV * calculateSimilarityValue(o2.getCustomer()) / maxSimValue; 
		
	    // the one with the lowest value should be used as initial customer
	    if (valueCustomer1 < valueCustomer2) {
	        return -1;
	    }
	    if (valueCustomer1 > valueCustomer2) {
	        return 1;
	    }
	    return 0;
	}
}        