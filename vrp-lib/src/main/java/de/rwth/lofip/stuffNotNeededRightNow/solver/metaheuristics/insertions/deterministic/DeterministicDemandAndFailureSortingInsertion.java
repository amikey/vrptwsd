package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic;

import java.util.Comparator;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractDemandAndFailureSortingInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.util.DeterministicTourComparatorWrtDemand;

/**
 * The {@code Demand and failure sorting insertion} as described by Lei et al.
 * (2011), section 3.3.7.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class DeterministicDemandAndFailureSortingInsertion extends
        AbstractDemandAndFailureSortingInsertion {
    
    @Override
    public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i, double approximateEquality)
    {    	
    	return TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, i);
    }
    
    @Override
    protected Tour createTourHook(Depot depot, int vehicleId, double vehicleCapacity)
    {
    	Tour tour = new DeterministicTour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
    }
    
    @Override
    protected CustomerWithCost calculateCostHook(Customer customer, Tour tour, double approximateEquality) 
    {
    	return TourUtils.calculateCost(customer, tour);
    }

	@Override
	protected Comparator<Tour> getTourComparator() {		
		return new DeterministicTourComparatorWrtDemand();
	}
}
