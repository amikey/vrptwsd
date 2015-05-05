package de.rwth.lofip.library.solver.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * Implement the Greedy Insertion as described by Lei et al. in section 3.3.5 of
 * their paper.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class GreedyInsertion {
	
	private static int seed = 0;
	private Random random = new Random(seed); 
	
	public GreedyInsertion() {
		seed++;
	}

    public SolutionGot insertCustomers(SolutionGot solution,
            List<Customer> customers) {
    	    	    
    	Collections.shuffle(customers, random);
    	
        final Depot depot = solution.getVrpProblem().getDepot();

        // for all customers, find their N (=10) best insertion points wrt the
        // deterministic part of the cost (distance)
        for (final Customer customerToBeInserted : customers) {

        	//create list of insertion points (each insertion point is a customer. 
        	//is the respective insertion point before or after this customer?)
            List<CustomerWithCost> insertionPoints = new ArrayList<CustomerWithCost>();

            // get number of Processors available and create corresponding number of possible concurrent threads
            int threads = Runtime.getRuntime().availableProcessors(); 
            ExecutorService service = Executors.newFixedThreadPool(threads);
            // ??
            List<Future<List<CustomerWithCost>>> costFutures = new ArrayList<Future<List<CustomerWithCost>>>();

            // determine the deterministic costs of inserting a customer at a
            // position in a tour
            for (final Tour tour : solution.getTours()) {
                final int tourCustomerCount = tour.getCustomers().size();

                Callable<List<CustomerWithCost>> cwcCallable = new Callable<List<CustomerWithCost>>() {

                    @Override
                    public List<CustomerWithCost> call() throws Exception {
                        List<CustomerWithCost> retList = new ArrayList<CustomerWithCost>();

                        for (int i = 0; i <= tourCustomerCount; i++) {
                            if (TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(
                                                    customerToBeInserted,
                                                    tour, i)) {
                                // calculate the cost of insertion
                                double cost = 0;
                                if (i == 0) {
                                    assert (cost == 0);
                                    cost += new Edge(depot,
                                            customerToBeInserted)
                                            .getLength();
                                    cost += new Edge(
                                            customerToBeInserted,
                                            tour.getCustomerAtPosition(0))
                                            .getLength();
                                    cost -= new Edge(depot,
                                            tour.getCustomerAtPosition(0))
                                            .getLength();
                                }
                                if (i == tourCustomerCount) {
                                    assert (cost == 0);
                                    // customer is inserted at the end
                                    cost += new Edge(
                                            tour.getCustomerAtPosition(i - 1),
                                            customerToBeInserted)
                                            .getLength();
                                    cost += new Edge(
                                            customerToBeInserted,
                                            depot).getLength();
                                    cost -= new Edge(
                                            tour.getCustomerAtPosition(i - 1),
                                            depot).getLength();
                                }
                                if (i > 0 && i < tourCustomerCount) {
                                    assert (cost == 0);
                                    cost += new Edge(
                                            tour.getCustomerAtPosition(i - 1),
                                            customerToBeInserted)
                                            .getLength();
                                    cost += new Edge(
                                            customerToBeInserted,
                                            tour.getCustomerAtPosition(i))
                                            .getLength();
                                    cost -= new Edge(
                                            tour.getCustomerAtPosition(i - 1),
                                            tour.getCustomerAtPosition(i))
                                            .getLength();
                                }

                                // insert the cost value into the list of all
                                // cost
                                // values
                                CustomerWithCost cwc = new CustomerWithCost(
                                        customerToBeInserted,
                                        tour, cost);
                                cwc.setPosition(i);
                                retList.add(cwc);
                            } // end if insertionIsAllowed
                        } // end for all customersInTour
                        return retList; // retList is of type List<CustomerWithCost>
                    } // end method call
                };
                costFutures.add(service.submit(cwcCallable));
            }
            service.shutdown();

            for (Future<List<CustomerWithCost>> f : costFutures) {
                try {
                    insertionPoints.addAll(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.err.print("An error occurred in the parallelism");
                }
            }

            // now, we should have all insertion points for this customer

            if (insertionPoints.isEmpty()) {
                // go to step 4
                // generate a new route and add the customer
            	solution.createNewTourWithCustomer(customerToBeInserted);                
            } else {
                // do step 3
                // first, sort the insertion points
                Collections.sort(insertionPoints,
                        new Comparator<CustomerWithCost>() {

                            @Override
                            public int compare(CustomerWithCost o1,
                                    CustomerWithCost o2) {
                                if (o1.getCost() < o2.getCost()) {
                                    return -1;
                                }
                                if (o1.getCost() > o2.getCost()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                
                // get Customer and Tour and inserting position with the cheapest cost
                CustomerWithCost cwc = insertionPoints.get(0);

                // insert customer into the corresponding tour at the given position
                cwc.getTour().insertCustomerAtPosition(cwc.getCustomer(),cwc.getPosition());
            }

        }
        return solution;
    }

	public static void setSeedTo(int i) {
		seed = i;	
	}

}
