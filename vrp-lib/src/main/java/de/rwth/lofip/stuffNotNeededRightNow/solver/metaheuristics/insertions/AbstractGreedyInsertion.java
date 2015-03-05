package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions;

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
import java.util.concurrent.TimeUnit;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.util.RemovedCustomer;

public abstract class AbstractGreedyInsertion implements InsertionInterface {

	@Override
	public String getName() {
		return "GI";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rwth.lofip.library.solver.metaheuristics.insertions.
	 * AbstractGreedyInsertion#insertCustomers(de.rwth.lofip.library.Solution,
	 * java.util.Collection, int, de.rwth.lofip.library.solver.VrpConfiguration)
	 */
	@Override
    public Solution insertCustomers(Solution solution,
            Collection<RemovedCustomer> customers, final int iteration,
            final VrpConfiguration configuration) {

        final Depot depot = solution.getVrpProblem().getDepot();

        // for all customers, find their N (=10) best insertion points wrt the
        // deterministic part of the cost (distance)
        for (final RemovedCustomer customerToBeInserted : customers) {

            List<CustomerWithCost> insertionPoints = new ArrayList<CustomerWithCost>();

            int threads = Runtime.getRuntime().availableProcessors();
            ExecutorService service = Executors.newFixedThreadPool(threads);
            List<Future<List<CustomerWithCost>>> costFutures = new ArrayList<Future<List<CustomerWithCost>>>();

            // determine the deterministic costs of inserting a customer at a
            // position in a tour
            for (final Tour tour : solution.getTours()) {
            	
                final int numberOfInsertionPositions = tour.getCustomerSize();

                Callable<List<CustomerWithCost>> cwcCallable = new Callable<List<CustomerWithCost>>() {

                    @Override
                    public List<CustomerWithCost> call() throws Exception {
                        List<CustomerWithCost> retList = new ArrayList<CustomerWithCost>();

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
                                CustomerWithCost cwc = new CustomerWithCost(
                                        customerToBeInserted.getCustomer(),
                                        tour, cost);
                                cwc.setPosition(i);
                                retList.add(cwc);
                            }
                        }
                        return retList;
                    }                    
                    
                };
                costFutures.add(service.submit(cwcCallable));
            }
            service.shutdown();

            for (Future<List<CustomerWithCost>> f : costFutures) {
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
                Tour newTour =  createTourHook(depot, new Random().nextInt(Integer.MAX_VALUE), solution.getVrpProblem().getVehicles().iterator().next().getCapacity());
                
                newTour.addCustomer(customerToBeInserted.getCustomer(),
                        iteration, getClass().getSimpleName());
                solution.addTour(newTour);
            } else {
                // do step 3
                // first, sort the insertion points
                Collections.sort(insertionPoints,
                        new CustomerWithCostComparator());
                // now take the first N (N=10) values, if there are more
                if (insertionPoints.size() > 10) {
                    insertionPoints = insertionPoints.subList(0, 10);
                }
                // now we need to calculate the expected recourse cost for
                // all these insertion points

                ExecutorService secondService = Executors
                        .newFixedThreadPool(threads);
                for (final CustomerWithCost c : insertionPoints) 
                {
                    Runnable runnable = new Runnable() {

                        public void run() {
                            // clone a tour and insert the customer at the
                            // position into the cloned tour
                            // then calculate the expected recourse cost
                            // we need to clone here, otherwise we may run into
                            // concurrency problems
                            Tour clonedTour = c.getTour().clone();

							clonedTour.insertCustomerAtPosition(
							        c.getCustomer(), c.getPosition());
                            c.setCost(clonedTour.getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost()
                                    - c.getTour().getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost());
                        }
                    };
                    secondService.execute(runnable);
                }
                secondService.shutdown();
                try {
                    secondService.awaitTermination(Long.MAX_VALUE,
                            TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Collections.sort(insertionPoints,
                        new CustomerWithCostComparator());
                CustomerWithCost cwc = insertionPoints.get(0);

                cwc.getTour().insertCustomerAtPosition(cwc.getCustomer(),
                        cwc.getPosition(), iteration,
                        getClass().getSimpleName());
            }
        }
        return solution;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rwth.lofip.library.solver.metaheuristics.insertions.
	 * AbstractGreedyInsertion
	 * #isInsertionAllowed(de.rwth.lofip.library.Customer,
	 * de.rwth.lofip.library.Tour, int)
	 */
	@Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
		return true;
	}

	protected abstract Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity);

	protected abstract boolean isInsertionPossibleHook(Customer customer,
			Tour tour, int i, double approximateEquality);

	/**
	 * A simple comparator which sorts the {@code CustomerWithCost} ascending by
	 * their cost.
	 * 
	 * @author Dominik Sandjaja <dominik@dadadom.de>
	 * 
	 */
	private class CustomerWithCostComparator implements
			Comparator<CustomerWithCost> {
		@Override
		public int compare(CustomerWithCost o1, CustomerWithCost o2) {
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