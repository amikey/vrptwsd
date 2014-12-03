package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.library.solver.util.CustomerWithCostGot;
import de.rwth.lofip.library.solver.util.GotUtils;
import de.rwth.lofip.library.util.RemovedCustomer;

public abstract class AbstractGreedyInsertionGot implements InsertionInterfaceGot {

	@Override
	public String getName() {
		return "GI";
	}

	@Override
    public SolutionGot insertCustomers(SolutionGot solution,
            Collection<RemovedCustomer> customers, final int iteration,
            final VrpConfiguration configuration) {

        final Depot depot = solution.getVrpProblem().getDepot();

        // for all customers, find their N (=10) best insertion points wrt the
        // deterministic part of the cost (distance)
        for (final RemovedCustomer customerToBeInserted : customers) {

            List<CustomerWithCostGot> insertionPoints = new ArrayList<CustomerWithCostGot>();

            int threads = Runtime.getRuntime().availableProcessors();
            ExecutorService service = Executors.newFixedThreadPool(threads);
            List<Future<List<CustomerWithCostGot>>> costFutures = new ArrayList<Future<List<CustomerWithCostGot>>>();

            // determine the deterministic costs of inserting a customer at a
            // position in a tour
            for (final GroupOfTours got : solution.getGots())
	            for (final Tour tour : got.getTours()) {
	            	
	                final int numberOfInsertionPositions = tour.getCustomerSize();
	
	                Callable<List<CustomerWithCostGot>> cwcCallable = new Callable<List<CustomerWithCostGot>>() {
	
	                    @Override
	                    public List<CustomerWithCostGot> call() throws Exception {
	                        List<CustomerWithCostGot> retList = new ArrayList<CustomerWithCostGot>();
	
	                        for (int i = 0; i <= numberOfInsertionPositions; i++) {
	                            if (isInsertionAllowed(
	                                    customerToBeInserted.getCustomer(), tour, i)
		                            	&& isInsertionPossibleHook(customerToBeInserted.getCustomer(), tour, i, 
		                            			configuration.getConfigurationValueDouble(LeiEtAlHeuristic.APPROXIMATE_EQUALITY)))
	                            {          	
	                                // calculate the cost of insertion
	                                double cost = 0;
	                                if (i == 0) {
	                                    assert (cost == 0);
	                                    cost += new Edge(depot,
	                                            customerToBeInserted.getCustomer())
	                                            .getLength();
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            tour.getCustomerAtPosition(0))
	                                            .getLength();
	                                    cost -= new Edge(depot,
	                                            tour.getCustomerAtPosition(0))
	                                            .getLength();
	                                }
	                                if (i == numberOfInsertionPositions) {
	                                    assert (cost == 0);
	                                    // customer is inserted at the end
	                                    cost += new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            customerToBeInserted.getCustomer())
	                                            .getLength();
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            depot).getLength();
	                                    cost -= new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            depot).getLength();
	                                }
	                                if (i > 0 && i < numberOfInsertionPositions) {
	                                    assert (cost == 0);
	                                    cost += new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            customerToBeInserted.getCustomer())
	                                            .getLength();
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            tour.getCustomerAtPosition(i))
	                                            .getLength();
	                                    cost -= new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            tour.getCustomerAtPosition(i))
	                                            .getLength();
	                                }
	
	                                // insert the cost value into the list of all
	                                // cost values
	                                CustomerWithCostGot cwc = new CustomerWithCostGot(
	                                        customerToBeInserted.getCustomer(),
	                                        tour, cost);
	                                cwc.setPosition(i);
	                                cwc.setGot(got);
	                                retList.add(cwc);
	                            }
	                        }
	                        return retList;
	                    }                    
	                    
	                };
	                costFutures.add(service.submit(cwcCallable));
	            }
            service.shutdown();

            for (Future<List<CustomerWithCostGot>> f : costFutures) {
                try {
                    insertionPoints.addAll(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.err.print("An error occurred in the parallelism \n");
                }
            }

            // now, we should have all insertion points for this customer
            if (insertionPoints.isEmpty()) {
                // go to step 4
                // generate a new route and add the customer
            	solution.createNewTourWithCustomer(customerToBeInserted.getCustomer(), iteration, getClass().getSimpleName());            	
            } else {
                // do step 3
                // first, sort the insertion points
                Collections.sort(insertionPoints,
                        new CustomerWithCostGotComparator());
                // now take the first N (N=10) values, if there are more
                if (insertionPoints.size() > 10) {
                    insertionPoints = insertionPoints.subList(0, 10);
                }

                //das könnte auch parallelisiert werden (siehe auskommentierter Code unten)                
                for (CustomerWithCostGot cwc : insertionPoints) {
                	double detCost = cwc.getCost();
                	double recCost = GotUtils.calculateRecourseGot(cwc.getGot(),cwc.getTour(), cwc.getCustomer(), cwc.getPosition());                	                	               
                	double cost = detCost + recCost;
                	cwc.setCost(cost);
                }
                 
//                ExecutorService secondService = Executors
//                        .newFixedThreadPool(threads);
//                for (final CustomerWithCostGot c : insertionPoints) 
//                {
//                    Runnable runnable = new Runnable() {
//
//                        public void run() {
//                            // clone a tour and insert the customer at the
//                            // position into the cloned tour
//                            // then calculate the expected recourse cost
//                            // we need to clone here, otherwise we may run into
//                            // concurrency problems
//                            Tour clonedTour = c.getTour().clone();
//
//							clonedTour.insertCustomerAtPosition(
//							        c.getCustomer(), c.getPosition());
//                            c.setCost(clonedTour.getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost()
//                                    - c.getTour().getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost());
//                        }
//                    };
//                    secondService.execute(runnable);
//                }
//                secondService.shutdown();
//                try {
//                    secondService.awaitTermination(Long.MAX_VALUE,
//                            TimeUnit.NANOSECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                
                Collections.sort(insertionPoints, new CustomerWithCostGotComparator());
                CustomerWithCostGot cwc = insertionPoints.get(0);

                cwc.getTour().insertCustomerAtPosition(cwc.getCustomer(),
                        cwc.getPosition(), iteration,
                        getClass().getSimpleName());
            }
        }
        return solution;
    }

	@Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
		return true;
	}

	protected abstract boolean isInsertionPossibleHook(Customer customer,
			Tour tour, int i, double approximateEquality);

	
	/**
	 * A simple comparator which sorts the {@code CustomerWithCostGot} ascending by
	 * their cost.
	 */
	private class CustomerWithCostGotComparator implements
			Comparator<CustomerWithCostGot> {
		@Override
		public int compare(CustomerWithCostGot o1, CustomerWithCostGot o2) {
			if (o1.getCost() < o2.getCost()) {
				return -1;
			}
			if (o1.getCost() > o2.getCost()) {
				return 1;
			}
			return 0;
		}
	}

}