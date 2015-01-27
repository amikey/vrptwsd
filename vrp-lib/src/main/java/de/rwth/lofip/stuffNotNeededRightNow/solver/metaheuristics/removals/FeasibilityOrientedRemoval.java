package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * <p>
 * Feasibility Oriented Removal, as described in section 3.3.2 of the paper by
 * Lei et al. (2011)
 * </p>
 * <p>
 * The idea of this heuristic is to get the number of tours down to the number,
 * pre-defined by the {@link de.rwth.lofip.library.VrpProblem VrpProblem}.
 * </p>
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class FeasibilityOrientedRemoval implements RemovalInterface {
	
	private static int seed = 0;  	
	Random r;
	
	public FeasibilityOrientedRemoval() {  
	    seed++;  
	    r = new Random(seed);
	  }  
	
	public static void setSeed(int seed){
		FeasibilityOrientedRemoval.seed = seed;
	}
	
	@Override
	public String getName() {
		return "FOR";
	}
	
	@Override
    public List<RemovedCustomer> removeCustomers(Solution solution,
            int customerCountToBeRemoved) {

        VrpProblem problem = solution.getVrpProblem();
        int targetVehicleCount = problem.getVehicleCount();

        List<RemovedCustomer> removedCustomers = new ArrayList<RemovedCustomer>(
                customerCountToBeRemoved);

        while (removedCustomers.size() < customerCountToBeRemoved) {

            List<Tour> tours = new ArrayList<Tour>(solution.getTours());
            tours = sortToursWrtToAmountOfCustomers(tours);           

            int vehicleCountInSolution = solution.getTours().size();
            Tour firstTour = tours.get(0);
            if (toursShouldBeDeleted(vehicleCountInSolution, targetVehicleCount)) {
                // this is step 1 -> 2
                if (firstTour.getCustomers().size() >= customerCountToBeRemoved
                        - removedCustomers.size()) {
                    int removedCustomersSize = removedCustomers.size();
                    for (int i = 0; i < customerCountToBeRemoved
                            - removedCustomersSize; i++) {
                        int customerPositionToRemove = r
                                .nextInt(firstTour.getCustomers().size());                  
                        RemovedCustomer removedCustomer = new RemovedCustomer();
                        removedCustomer
                                .setCustomer(firstTour
                                        .removeCustomerAtPosition(customerPositionToRemove));
                        removedCustomer.setTourId(firstTour.getId());
                        removedCustomer.setPosition(customerPositionToRemove);
                        removedCustomers.add(removedCustomer);
                    }
                } else {
                    for (int i = firstTour.getCustomers().size() - 1; i >= 0; i--) {
                        RemovedCustomer removedCustomer = new RemovedCustomer();
                        removedCustomer.setCustomer(firstTour
                                .removeCustomerAtPosition(i));
                        removedCustomer.setTourId(firstTour.getId());
                        removedCustomer.setPosition(i);
                        removedCustomers.add(removedCustomer);
                    }
                }
            } else {
                // this is step 1 -> 3
                // randomly remove one customer from the first tour
                int customerPositionToRemove = r.nextInt(firstTour
                        .getCustomers().size());     
                RemovedCustomer removedCustomer = new RemovedCustomer();
                removedCustomer.setCustomer(firstTour
                        .removeCustomerAtPosition(customerPositionToRemove));
                removedCustomer.setTourId(firstTour.getId());
                removedCustomer.setPosition(customerPositionToRemove);
                removedCustomers.add(removedCustomer);
            }
            solution.removeEmptyTours();
        }

        return removedCustomers;
    }

	protected boolean toursShouldBeDeleted(int vehicleCountInSolution, int targetVehicleCount) {
		return vehicleCountInSolution > targetVehicleCount;
	}

	protected List<Tour> sortToursWrtToAmountOfCustomers(List<Tour> tours) {
        // sort the tours in decreasing order of their amount of customers
        Collections.sort(tours, new Comparator<Tour>() {
            @Override
            public int compare(Tour t1, Tour t2) {
                int cc1 = t1.getCustomers().size();
                int cc2 = t2.getCustomers().size();
                if (cc1 < cc2) {
                    return -1;
                }
                if (cc1 > cc2) {
                    return 1;
                }
                return 0;
            }
        });
        return tours;
	}

}
