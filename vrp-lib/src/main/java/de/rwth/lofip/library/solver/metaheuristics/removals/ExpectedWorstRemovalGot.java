package de.rwth.lofip.library.solver.metaheuristics.removals;

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
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.util.CustomerWithCostGot;
import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * Diese Datei implementiert die EWR Heuristik, die so modifiziert ist, dass sie mit GroupOfTours und SolutionGot klar kommt.
 * Insbesondere iteriert sie zuerst über die GroupOfTours in SolutionGot und dann über die Touren  innerhalb der Gots.
 */
public class ExpectedWorstRemovalGot implements RemovalInterfaceGot {

	@Override
	public String getName() {
		return "EWR";
	}

	@Override
    public List<RemovedCustomer> removeCustomers(SolutionGot solution,
            int amountOfRemovedCustomers) {
        List<RemovedCustomer> removedCustomers = new ArrayList<RemovedCustomer>();

        int theta = 0;
        List<CustomerWithCostGot> changingCosts = new ArrayList<CustomerWithCostGot>();

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        List<Future<CustomerWithCostGot>> costsFutures = new ArrayList<Future<CustomerWithCostGot>>();

        for (final GroupOfTours got : solution.getGots())
            for (final Tour tour : got.getTours()) 
            	 {            		
	            Callable<CustomerWithCostGot> cwccCallable = new Callable<CustomerWithCostGot>() {
	                @Override
	                public CustomerWithCostGot call() throws Exception {
	                    CustomerWithCostGot cwc = new CustomerWithCostGot();
	                    cwc.setGot(got);
	                    cwc.setTour(tour);
	                    cwc.setCost(Double.MIN_VALUE);                  
	                   
	                    for (int i = 0; i < getNumberOfRemovablePositions(tour); i++) {
		                    //calculate det cost
		                	Tour testTour = tour.clone();	                    	                    
		                    double detCost = calculateDetChangingCost(testTour, i);                                                                              	                   
		                    
		                    //calculate rec cost
		                    GroupOfTours gotToBeModified = got.clone();
		                    modifyClonedGot(gotToBeModified,cwc);
		                    double recCost = gotToBeModified.getExpectedRecourseCost() - got.getExpectedRecourseCost();
		                    
		                    double cost = detCost + recCost;
		                    if (cwc.getCost() < cost) {
			                    cwc.setPosition(i);
		                    	cwc.setCost(detCost + recCost);
		                    }
	                    }	                    
	                    return cwc;
	                }
		        };
	            costsFutures.add(service.submit(cwccCallable));
            }

        service.shutdown();

        for (Future<CustomerWithCostGot> f : costsFutures) {
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
                    new Comparator<CustomerWithCostGot>() {

                        @Override
                        public int compare(CustomerWithCostGot c1,
                                CustomerWithCostGot c2) {
                            if (c1.getCost() > c2.getCost()) {
                                return -1;
                            }
                            if (c1.getCost() < c2.getCost()) {
                                return 1;
                            }
                            return 0;
                        }

                    });                                  
            changingCosts = changingCosts.subList(0, amountOfRemovedCustomers);
            for (CustomerWithCostGot c : changingCosts) {
                RemovedCustomer removedCustomer = new RemovedCustomer();
                try {
					removedCustomer.setCustomer(c.getTour()
					        .removeCustomerAtPosition(c.getPosition()));
				} catch (Exception e) {
					e.printStackTrace();					
				}
                removedCustomer.setTourId(c.getTour().getId());
                removedCustomer.setPosition(c.getPosition());
                removedCustomers.add(removedCustomer);
            }
        } else {
            // this is Steps 3 and 4
            while (theta < amountOfRemovedCustomers) {
                // Step 3
                // find the correct tour to remove from. Just take the next
                // one in a round robin fashion
            	GroupOfTours gotToRemoveFrom = solution.getGots().get(theta % solution.getGots().size());
                Tour tourToRemoveFrom = gotToRemoveFrom.getTours().get(theta % solution.getTours().size());
                // find the customer with the highest cost in this tour
                CustomerWithCostGot cwc = new CustomerWithCostGot();
                cwc.setTour(tourToRemoveFrom);
                cwc.setCost(Double.MIN_VALUE);

                for (int i = 0; i < getNumberOfRemovablePositions(tourToRemoveFrom); i++) {
                	//calculate det cost
                	Tour testTour = tourToRemoveFrom.clone();	                    	                    
                    double detCost = calculateDetChangingCost(testTour, i);                                                                              	                   
                    
                    //calculate rec cost
                    GroupOfTours gotToBeModified = gotToRemoveFrom.clone();
                    modifyClonedGot(gotToBeModified,cwc);
                    double recCost = gotToBeModified.getExpectedRecourseCost() - gotToRemoveFrom.getExpectedRecourseCost();
                    
                    double cost = detCost + recCost;
                    if (cwc.getCost() < cost) {
	                    cwc.setPosition(i);
                    	cwc.setCost(detCost + recCost);
                    }
                }
                RemovedCustomer removedCustomer = new RemovedCustomer();
                try {
					removedCustomer.setCustomer(cwc.getTour()
					        .removeCustomerAtPosition(cwc.getPosition()));
				} catch (Exception e) {
					e.printStackTrace();
				}
                removedCustomer.setTourId(cwc.getTour().getId());
                removedCustomer.setPosition(cwc.getPosition());
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
    
	private double calculateDetChangingCost(Tour tour, int i) {
		double costWithCustomer = tour.getDistance();
		Customer c = tour.removeCustomerAtPosition(i);
		double costWithoutCustomer = tour.getDistance();
		// restore the previous state of the tour
		tour.insertCustomerAtPosition(c, i);
		return costWithCustomer - costWithoutCustomer;
	}
	
	private void modifyClonedGot(GroupOfTours gotToBeModified, CustomerWithCostGot cwc) {
		Tour t = gotToBeModified.identifyTour(cwc.getTour());
		t.removeCustomerAtPosition(cwc.getPosition());		
	}
    
}
