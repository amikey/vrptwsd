package de.rwth.lofip.library.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rwth.lofip.library.AbstractPointInSpace;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.DistanceComparatorWithSimilarity;
import de.rwth.lofip.library.solver.util.FVComparatorWithSimilarity;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * A start heuristic, based on the Push Forward Insertion by Solomon (1987).
 * 
 * This version works with groupsOfTours. The first tour is created as in the Lei et al. version.
 * As long as it is possible to add tours to the group of tours this is done using FV
 * or additional distance for inserting customer into tour, resp., and the similarity value to the 
 * customers in the already existing tours  
 * 
 * This heurisic respects time window and capacity constrains 
 * but cannot guarantee the use of a certain number of vehicles.
 * 
 * @author Andreas Braun <braun@dpor.rwth-aachen.de>
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class GroupPushForwardInsertionSolver implements SolverInterfaceGot {

    Set<Customer> unassignedCustomers = new HashSet<Customer>();
	Set<Customer> assignedCustomersInGot = new HashSet<Customer>();
	Set<Customer> assignedCustomersInNextTour = new HashSet<Customer>();
    int iteration = 1;
    SolutionGot solution;
    CustomerWithCost cheapestCustomer;
    private static Logger logger = LogManager
            .getLogger(GroupPushForwardInsertionSolver.class);

    @Override
    public String getDescriptiveName() {
        return "Modified Group-PFI";
    }

    @Override
    public SolutionGot solve(VrpProblem problem) {
        // VRPSDTWInitialSolverConfiguration ssc =
        // (VRPSDTWInitialSolverConfiguration) getConfiguration();

        // we have two sets; the first one contains ALL customers
        Set<Customer> allCustomers = problem.getCustomers();
        // the second one those which are not yet assigned to a route
        unassignedCustomers.addAll(allCustomers);    	
        solution = new SolutionGot(problem);
        Depot depot = problem.getDepot();
        logger.info("starting with a set of {} customers",
                unassignedCustomers.size());
        //NEW GOT
        while (!unassignedCustomers.isEmpty()) {
        	assignedCustomersInGot.clear();
        	
        	createNewTourWithFirstCustomer(new FirstVertexCostFunctionComparator(depot));
           
            //add customers to first tour as usual
            Tour firstTour = solution.getTours().get(0);
            while (!unassignedCustomers.isEmpty()) {
                // now try to fit the remaining customers in an existing tour
                // use the cost function as described in the paper
                cheapestCustomer = new CustomerWithCost(null,
                        null, Double.MAX_VALUE);
                for (Customer customer : unassignedCustomers) {
                	CustomerWithCost calculatedCustomer = TourUtils.calculateCostStochasticSolver(customer, firstTour, 0.99999);                 
                    if (calculatedCustomer != null
                            && calculatedCustomer.getCost() < cheapestCustomer
                                    .getCost()) {
                        cheapestCustomer = calculatedCustomer;
                    }
                }               
                if (isCustomerThatCanBeInsertedWasFound()) {
                    // a possible insertion was found.
                    // insert it and remove the customer from the
                    // unassignedCustomers
                    firstTour.insertCustomerAtPosition(
                            cheapestCustomer.getCustomer(),
                            cheapestCustomer.getPosition(), iteration++,
                            getClass().getSimpleName());
                    unassignedCustomers.remove(cheapestCustomer.getCustomer());
                    assignedCustomersInGot.add(cheapestCustomer.getCustomer());
                    logger.info(
                            "{} customers left for insertion after inserting into route at position {}",
                            unassignedCustomers.size(),
                            cheapestCustomer.getPosition());
                    System.out.println("	Customer " + cheapestCustomer.getCustomer().getId() + " demand: " + cheapestCustomer.getCustomer().getDemand() + " zu erster Tour hinzugefügt");
                    continue;
                } else {
                    // no cheaper customer was found, so continue from the
                    // beginning
                    System.out.println("	kein weiterer Kunde kann der Tour hinzugefügt werden.");
                    break;
                }
            }
            
            assignedCustomersInNextTour.clear();
            //NEW TOUR IN GOT            
            //try to create a new tour in GroupOfTours and fill this tour with customers
            while (!unassignedCustomers.isEmpty() && solution.isNewTourCanBeCreatedInLastGot()) {
            	assignedCustomersInGot.addAll(assignedCustomersInNextTour);            	         
            	
            	//first create new Tour   
            	createNewTourWithFirstCustomer(new FVComparatorWithSimilarity(depot, assignedCustomersInGot, unassignedCustomers));
            	
                Tour tour = solution.getLastTour();
                System.out.println("Neue Tour in GroupOfTours wurde er�ffnet. Anzahl Touren in GOT: " + (solution.getLastGot().getTours().size()) + ", maximale Anzahl: " + solution.getLastGot().getMaximalNumberOfTours());
                
                //add customers to this tour using distance and similarity measure
                while (!unassignedCustomers.isEmpty()) {
                    // now try to fit the remaining customers in an existing tour
                    // use the cost function as described in the paper
                	List<CustomerWithCost> cwcs = new ArrayList<CustomerWithCost>();
                    double maxCost = Double.MIN_VALUE;
                    //here the cheapest customer is selected
                    for (Customer customer : unassignedCustomers) {
                    	CustomerWithCost calculatedCustomer = TourUtils.calculateCostStochasticSolver(customer, tour, 0.99999);
                    	if (calculatedCustomer != null)
                    		cwcs.add(calculatedCustomer);
                        if (calculatedCustomer != null
                                && calculatedCustomer.getCost() > maxCost) 
                            maxCost = calculatedCustomer.getCost();                            
                        }                  
                    //now delete all cwcs whose cost is Double.MAX_VALUE            
                    for (Iterator<CustomerWithCost> cwc = cwcs.iterator(); cwc.hasNext(); )
                        if (cwc.next().getCost() == Double.MAX_VALUE)
                            cwc.remove();

                	if (!cwcs.isEmpty())
                	{
                		//TODO: Ist es richtig in Distance Comparator With Similarity alle assigned customers zu verwenden oder nur die im Got?
                		//wie funktioniert der DistanceComparatorWithSimilarity noch mal?                		
                		DistanceComparatorWithSimilarity dcws = new DistanceComparatorWithSimilarity(assignedCustomersInGot, unassignedCustomers, maxCost);               	
                        Collections.sort(cwcs, dcws);
                        CustomerWithCost cheapestCustomer = cwcs.get(0);

                        // a possible insertion was found.
                        // insert it and remove the customer from the
                        // unassignedCustomers
                        tour.insertCustomerAtPosition(
                                cheapestCustomer.getCustomer(),
                                cheapestCustomer.getPosition(), iteration++,
                                getClass().getSimpleName());
                        unassignedCustomers.remove(cheapestCustomer.getCustomer());
                        assignedCustomersInNextTour.add(cheapestCustomer.getCustomer());
                        logger.info(
                                "{} customers left for insertion after inserting into route at position {}",
                                unassignedCustomers.size(),
                                cheapestCustomer.getPosition());
                        System.out.println("	Customer " + cheapestCustomer.getCustomer().getId() + " zu nächster Tour hinzugef�gt");
                        continue;
                    } else {
                        // no cheaper customer was found, so continue from the beginning
                        break;
                    }
                }
                assignedCustomersInGot.addAll(assignedCustomersInNextTour);
            }
        }
        return solution;
    }
    
	private boolean isCustomerThatCanBeInsertedWasFound() {
    	return cheapestCustomer.getCost() < Double.MAX_VALUE;
	}

	private void createNewTourWithFirstCustomer(Comparator<Customer> firstVertexCostFunctionComparator) {
        List<Customer> customersByInitialCost = new ArrayList<Customer>(unassignedCustomers);
        Collections.sort(customersByInitialCost,firstVertexCostFunctionComparator);
        Customer lowestCostCustomer = customersByInitialCost.get(0);    
        solution.createNewTourWithCustomer(lowestCostCustomer, iteration, getClass().getSimpleName());
        unassignedCustomers.remove(lowestCostCustomer);
        assignedCustomersInGot.add(lowestCostCustomer);
        logger.info("{} customers left for insertion after creating new route", unassignedCustomers.size());
        System.out.println("Erste Tour mit erstem Customer " + lowestCostCustomer.getId() + " eröffnet");		
	}

	/**
     * This method calculates the cheapest insertion cost for the customer into
     * the route. If no insertion can be found because any insertion would
     * violate the time constraint for the following customers on the tour or
     * would violate the capacity restriction of the vehicle, the method returns
     * either null or an object with cost = Double.MAX_VALUE.
     * 
     * @param customer
     * @param tour
     * 
     * @return
     */
    public static CustomerWithCost calculateCost(Customer customer, Tour tour,
            double approximateEquality) {
        CustomerWithCost returnObject = new CustomerWithCost(customer, tour,
                Double.MAX_VALUE);
        // as a special case, the insertion cost for the first position (between
        // the depot and the currently first customer) needs to be calculated
        // upfront
        Customer firstCustomer = tour.getCustomers().get(0);
        double cost = Double.MAX_VALUE;
        int position = 0;
        //now set condition such that either a deterministic setting (demand has to be smaller than capacity) or 
        //probabilisitic (demand has to be smaller than twice vehicle capacity with probability approximateEquality) solver is used.
        boolean condition;
        condition = TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer, tour, position,
                    approximateEquality);
        if (condition) {
            cost = new Edge(tour.getDepot(), customer).getLength()
                    + new Edge(customer, firstCustomer).getLength()
                    - new Edge(tour.getDepot(), firstCustomer).getLength();
            returnObject.setCost(cost);
            returnObject.setPosition(position);
        }

        for (Customer tourCustomer : tour.getCustomers()) {
            // calculate the cost for inserting the new customer after this
            // customer
            position++;
            AbstractPointInSpace nextCustomer = tour.getDepot();
            if (tour.getCustomers().size() > position) {
                nextCustomer = tour.getCustomers().get(position);
            }
            condition = TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer, tour, position,
                        approximateEquality);
            if (condition) {
                cost = new Edge(tourCustomer, customer).getLength()
                        + new Edge(customer, nextCustomer).getLength()
                        - new Edge(tourCustomer, nextCustomer).getLength();
                if (cost < returnObject.getCost()) {
                    returnObject.setCost(cost);
                    returnObject.setPosition(position);
                }
            }
        }
        return returnObject;
    }
    	
    private class FirstVertexCostFunctionComparator implements
            Comparator<Customer> {

        private Depot depot;

        public FirstVertexCostFunctionComparator(Depot depot) {
            this.depot = depot;
        }

        @Override
        public int compare(Customer c1, Customer c2) {
            Edge e1 = new Edge(depot, c1);
            Edge e2 = new Edge(depot, c2);

            // for the explanation of the calculation of these values, see
            // Lei et al. (2011)
            double value1 = ((-0.9) * e1.getLength())
                    + (0.1 * c1.getTimeWindowClose());
            double value2 = ((-0.9) * e2.getLength())
                    + (0.1 * c2.getTimeWindowClose());

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
    
    
    
    
}

