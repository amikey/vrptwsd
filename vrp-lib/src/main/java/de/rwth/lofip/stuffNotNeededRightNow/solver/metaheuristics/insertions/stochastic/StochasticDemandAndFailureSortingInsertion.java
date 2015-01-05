package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic;

import java.util.Comparator;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractDemandAndFailureSortingInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.util.StochasticTourComparatorWrtFailureProbability;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * The {@code Demand and failure sorting insertion} as described by Lei et al.
 * (2011), section 3.3.7.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class StochasticDemandAndFailureSortingInsertion extends
        AbstractDemandAndFailureSortingInsertion {
	
	Comparator<Tour> tourComparator = new StochasticTourComparatorWrtFailureProbability();
    
    @Override
    public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i, double approximateEquality)
    {
    	//stochastic variant
    	return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer, tour, i, approximateEquality);
    }
    
    @Override
    protected Tour createTourHook(Depot depot, int vehicleId, double vehicleCapacity)
    {
    	Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
    }
    
    @Override
    protected CustomerWithCost calculateCostHook(Customer customer, Tour tour, double approximateEquality) 
    {
    	return TourUtils.calculateCostStochasticSolver(customer, tour, approximateEquality);
    }

	@Override
	protected Comparator<Tour> getTourComparator() {
		return new StochasticTourComparatorWrtFailureProbability();
	}
}