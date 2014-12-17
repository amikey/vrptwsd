package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.util.RemovedCustomer;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;

/**
 * This is the base class for the two insertions mentioned in the paper by Lei
 * et al. (2011). The Demand and failure sorting insertion (DFSI) and the
 * "Time and failure sorting insertion" (TFSI) only differ in the way the
 * removed customers are sorted before being reinserted. Hence, the only
 * difference in programming is the sorting logic, implemented by a
 * {@code Comparator<Customer>()}.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractFailureSortingInsertion implements
        InsertionInterface {
	
	Comparator<Tour> tourComparator = getTourComparator();

    @Override
    public Solution insertCustomers(Solution solution,
            Collection<RemovedCustomer> customers, int iteration,
            VrpConfiguration configuration) {
        List<RemovedCustomer> customerList = new ArrayList<RemovedCustomer>(
                customers);

        // first, sort the customers in decreasing order of their expected
        // demand
        if (customerList.size()==0) System.out.println("keine entfernten Kunden");
                
        Collections.sort(customerList, getCustomerComparator());
               
        // now iterate over all customers which are to be (re-)inserted
        for (RemovedCustomer customerToInsert : customerList) {

            // next, sort the routes depending on the comparator used
            List<Tour> sortedTours = new ArrayList<Tour>(solution.getTours());
            Collections.sort(sortedTours, tourComparator);

            Tour chosenTour = null;
            // now iterate over the (sorted) tours and insert it into the
            // first
            // route where it is possible to retain the constraint (total
            // expected demand below 2Q)
            // within that tour, from all possible insertion points, choose
            // the
            // cheapest one (lowest additional cost).

            for (Tour t : sortedTours) {
                // check, if the insertion is possible at all
                for (int pos = 0; pos < t.getCustomerSize(); pos++)
                {
                    if (isInsertionAllowed(customerToInsert.getCustomer(), t,
                            pos)
                            && isInsertionPossibleHook(customerToInsert.getCustomer(), t, pos, 
                        			configuration.getConfigurationValueDouble(LeiEtAlHeuristic.APPROXIMATE_EQUALITY)))
                    {                            
                        chosenTour = t;
                        break;
                    }
                }
                if (chosenTour != null) {
                    break;
                }
            }
            if (chosenTour == null) 
            {
                // case 1: no tour was found. Add a new one.
                // create a new, random vehicle
                int vehicleId = new Random().nextInt(Integer.MAX_VALUE);
                Tour newTour = createTourHook(solution.getVrpProblem().getDepot(), vehicleId, solution.getVrpProblem()
                        .getVehicles().iterator().next().getCapacity());
                		
                newTour.addCustomer(customerToInsert.getCustomer(), iteration,
                        getClass().getSimpleName());
                solution.addTour(newTour);
            } else {
                // case 2: we do have a possible tour to insert in. Take
                // that and calculate the cheapest insertion point
                CustomerWithCost cheapestInsertion = 
                        calculateCostHook(
                                customerToInsert.getCustomer(),
                                chosenTour,
                                configuration
                                        .getConfigurationValueDouble(LeiEtAlHeuristic.APPROXIMATE_EQUALITY));
                chosenTour.insertCustomerAtPosition(customerToInsert
                        .getCustomer(), cheapestInsertion.getPosition(),
                        iteration, getClass().getSimpleName());
            }
        }

        // return the enriched solution
        return solution;
    }       

	protected abstract CustomerWithCost calculateCostHook(Customer customer, Tour tour, double approximateEquality);

    @Override
    public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
        return true;
    }
    
    protected abstract boolean isInsertionPossibleHook(Customer customer, Tour tour, int i, double approximateEquality);
    
    protected abstract Tour createTourHook(Depot depot, int vehicleId, double vehicleCapacity);

    protected abstract Comparator<RemovedCustomer> getCustomerComparator();
    
    protected abstract Comparator<Tour> getTourComparator();
    
}
