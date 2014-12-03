package de.rwth.lofip.library.solver.util;

import java.util.Comparator;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;

//this class is saved in a separate file so it can be properly tested
/**
 * This class is used to calculate a value FV' \in [0...1] to select a first vertex 
 * for creating a new tour in the PFI solver when there are already tours in a group
 * according to the following formula:
 * 
 * FV'(i) = (1-\alpha) * \frac{FV(i)}{FV_{max}} + \alpha * s(i)
 * 
 * with 
 * 
 * s(i) = \frac{1}{assignedCustomers.size()} \sum_{j \in assignedCustomers} s_{ij}
 * 
 * @author Andreas Braun <braun@dpor.rwth-aachen.de>
 * 
 */
public class FVComparatorWithSimilarity implements Comparator<Customer> {

	//TODO: Save this in some central instance
	private double WEIGHT_DISTANCE = 0.3;
	private double WEIGHT_SIMVAL_FV = 0.3; 

    private Depot depot;
    private Set<Customer> assignedCustomers;
    private double maxFVValue = 0;
    private double maxSimValue = 0;
    private double maxDistance = Double.MIN_VALUE;
    private double maxTWsize = 0;

    public FVComparatorWithSimilarity(Depot depot, Set<Customer> assignedCustomers, Set<Customer> unassignedCustomers, double DistanceWeight, double WeightSimvalFV) {
    	this(depot, assignedCustomers, unassignedCustomers);
    	this.WEIGHT_DISTANCE = DistanceWeight;
    	this.WEIGHT_SIMVAL_FV = WeightSimvalFV;
    }
    
    public FVComparatorWithSimilarity(Depot depot, Set<Customer> assignedCustomers, Set<Customer> unassignedCustomers) {
        this.depot = depot;
        this.assignedCustomers = assignedCustomers;
        
        //calculate maximal similarity value
        //this needs to be done so that 
        //to do so first calculate the maximal distance of all unassigned customers to all assigned customers
        //and also calculate the maximum time window size of all unassigned customers with all assigned customers
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
        
        //now actually calculate the maximal similarity value
        for (Customer c : unassignedCustomers)
        {
        	double simVal = calculateSimilarityValue(c);
        	if (simVal > maxSimValue)
        		maxSimValue = simVal;
        }
        
        //calculate max FV value
        for (Customer c : unassignedCustomers)
        {
	        double FVvalue = calculateFVvalue(c);
	        if (maxFVValue < FVvalue)
	        	maxFVValue = FVvalue;
	    }
    }
    
    //the formula for calculating the similarity for an unassigned customer i is given as
    // \frac{1}{assignedCustomers.size()} \sum_{j \in assignedCustomers} s_{ij}
    
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
    	double firstTerm = distance / maxDistance;
    	
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
        
        double secondTerm = TWsize / maxTWsize;
        double sim = WEIGHT_DISTANCE * firstTerm + (1-WEIGHT_DISTANCE) * secondTerm;
        return sim;
    }
    
    public double calculateFVvalue(Customer customer)
    {
    	return ((-0.9) * new Edge(depot, customer).getLength())
                + (0.1 * customer.getTimeWindowClose());
    }

    @Override
    public int compare(Customer c1, Customer c2) {

        // for the explanation of the calculation of these values, see
        // Lei et al. (2011)
        double value1 = (1-WEIGHT_SIMVAL_FV) * calculateFVvalue(c1) / maxFVValue + WEIGHT_SIMVAL_FV * calculateSimilarityValue(c1) / maxSimValue; 
//        System.out.println("cust " + c1.getCustomerNo());
//        System.out.println("FV Value " + calculateFVvalue(c1));
//        System.out.println("max FV Value " + maxFVValue);
//        System.out.println("sim value " + calculateSimilarityValue(c1));
//        System.out.println("overall value " + value1);

        double value2 = (1-WEIGHT_SIMVAL_FV) * calculateFVvalue(c2) / maxFVValue + WEIGHT_SIMVAL_FV * calculateSimilarityValue(c2) / maxSimValue; 
//        System.out.println("cust " + c2.getCustomerNo());
//        System.out.println("FV Value " + calculateFVvalue(c2));
//        System.out.println("max FV Value " + maxFVValue);
//        System.out.println("sim value " + calculateSimilarityValue(c2));
//        System.out.println("overall value " + value2);
        
        // the one with the lowest value should be used as initial customer
        if (value1 < value2) {
            return -1;
        }
        if (value1 > value2) {
            return 1;
        }
        return 0;
    }

}
