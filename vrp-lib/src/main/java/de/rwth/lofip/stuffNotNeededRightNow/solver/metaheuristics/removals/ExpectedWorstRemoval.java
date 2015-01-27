package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * Implementation of the heuristic proposed by Lei et al. (2011).
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class ExpectedWorstRemoval implements RemovalInterface {

	@Override
	public String getName() {
		return "EWR";
	}

	@Override
    public List<RemovedCustomer> removeCustomers(Solution solution,
            int amountOfRemovedCustomers) {
        List<RemovedCustomer> removedCustomers = new ArrayList<RemovedCustomer>();

        int theta = 0;
        List<CustomerWithChangingCost> changingCosts = new ArrayList<CustomerWithChangingCost>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<CustomerWithChangingCost>> costsFutures = new ArrayList<Future<CustomerWithChangingCost>>();

        for (final Tour tour : solution.getTours()) {
            Callable<CustomerWithChangingCost> cwccCallable = new Callable<CustomerWithChangingCost>() {
                @Override
                public CustomerWithChangingCost call() throws Exception {
                    CustomerWithChangingCost cwcc = new CustomerWithChangingCost();
                    cwcc.setTour(tour);
                    cwcc.setChangingCost(Double.MIN_VALUE);                  

                	Tour testTour = tour.clone();
                    
                    for (int i = 0; i < getNumberOfRemovablePositions(tour); i++) {
                        double changingCost = calculateChangingCost(testTour, i);
                        if (changingCost > cwcc.getChangingCost()) {
                            cwcc.setCustomerPosition(i);
                            cwcc.setChangingCost(changingCost);
                        }
                    }
                    return cwcc;
                }

            };
            costsFutures.add(service.submit(cwccCallable));
        }

        service.shutdown();

        for (Future<CustomerWithChangingCost> f : costsFutures) {
            try {
                changingCosts.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.err.print("An error occurred in the parallelism");
            }
        }

        // this is Step 2
        if (changingCosts.size() > amountOfRemovedCustomers) {
            // sort the tours, aka their associated changing costs, in
            // decreasing order of that changing cost
            Collections.sort(changingCosts,
                    new Comparator<CustomerWithChangingCost>() {

                        @Override
                        public int compare(CustomerWithChangingCost c1,
                                CustomerWithChangingCost c2) {
                            if (c1.getChangingCost() > c2.getChangingCost()) {
                                return -1;
                            }
                            if (c1.getChangingCost() < c2.getChangingCost()) {
                                return 1;
                            }
                            return 0;
                        }

                    });
            changingCosts = changingCosts.subList(0, amountOfRemovedCustomers);
            for (CustomerWithChangingCost c : changingCosts) {
                RemovedCustomer removedCustomer = new RemovedCustomer();
                try {
					removedCustomer.setCustomer(c.getTour()
					        .removeCustomerAtPosition(c.getCustomerPosition()));
				} catch (Exception e) {
					e.printStackTrace();					
				}
                removedCustomer.setTourId(c.getTour().getId());
                removedCustomer.setPosition(c.getCustomerPosition());
                removedCustomers.add(removedCustomer);
            }
        } else {
            // this is Steps 3 and 4
            while (theta < amountOfRemovedCustomers) {
                // Step 3
                // find the correct tour to remove from. Just take the next
                // one in a round robin fashion
                Tour tourToRemoveFrom = solution.getTours().get(
                        theta % solution.getTours().size());
                // find the customer with the highest cost in this tour
                CustomerWithChangingCost cwcc = new CustomerWithChangingCost();
                cwcc.setTour(tourToRemoveFrom);
                cwcc.setChangingCost(Double.MIN_VALUE);

                for (int i = 0; i < getNumberOfRemovablePositions(tourToRemoveFrom); i++) {
                    double changingCost = calculateChangingCost(
                            tourToRemoveFrom, i);
                    if (changingCost > cwcc.getChangingCost()) {
                        cwcc.setCustomerPosition(i);
                        cwcc.setChangingCost(changingCost);
                    }
                }
                RemovedCustomer removedCustomer = new RemovedCustomer();
                try {
					removedCustomer.setCustomer(cwcc.getTour()
					        .removeCustomerAtPosition(cwcc.getCustomerPosition()));
				} catch (Exception e) {
					e.printStackTrace();
				}
                removedCustomer.setTourId(cwcc.getTour().getId());
                removedCustomer.setPosition(cwcc.getCustomerPosition());
                removedCustomers.add(removedCustomer);
                solution.removeEmptyTours();
                theta++;
            }

        }
        return removedCustomers;
    }
    
	protected int getNumberOfRemovablePositions(Tour tour) {					
		return tour.getCustomers().size();
	}

    /**
     * Calculates the cost difference when removing the customer at position i
     * from the given tour.
     * 
     * @param tour
     * @param i
     * @return
     */
    protected double calculateChangingCost(Tour tour, int i) {
        double costWithCustomer = tour.getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost();
        Customer c = tour.removeCustomerAtPosition(i);
        double costWithoutCustomer = tour.getSumOfDistanceMultipliedWithCostFactorAndExpectedRecourseCost();

		// restore the previous state of the tour
		tour.insertCustomerAtPosition(c, i);

		return costWithCustomer - costWithoutCustomer;
	}

    /**
     * Save for every position of customers the associated change cost.
     * 
     * @author Dominik Sandjaja <dominik@dadadom.de>
     * 
     */
    protected class CustomerWithChangingCost {

		private Tour tour;
		private int customerPosition;
		private double changingCost;

		public Tour getTour() {
			return tour;
		}

		public void setTour(Tour tour) {
			this.tour = tour;
		}

		public int getCustomerPosition() {
			return customerPosition;
		}

		public void setCustomerPosition(int customerPosition) {
			this.customerPosition = customerPosition;
		}

		public double getChangingCost() {
			return changingCost;
		}

		public void setChangingCost(double changingCost) {
			this.changingCost = changingCost;
		}

	}

}
