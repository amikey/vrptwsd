package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.groups;

import java.util.Comparator;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractDemandAndFailureSortingInsertionGot;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.util.DeterministicTourComparatorWrtDemand;
import de.rwth.lofip.stuffNotNeededRightNow.solver.util.TourUtilsGot;

/**
 * The {@code Demand and failure sorting insertion} as described by Lei et al.
 * (2011), section 3.3.7.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class DemandAndFailureSortingInsertionGot extends
        AbstractDemandAndFailureSortingInsertionGot {

	//TODO: Check whether sorting according to probability of failure makes sense for GroupOfTours
	// I'm quite sure that it doesn't since probability of failure is not well defined for GroupOfTours
	// => rewrite	
    
    @Override
    public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i, double approximateEquality)
    {
    	//stochastic variant
    	return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer, tour, i, approximateEquality);
    }
    
    @Override
    protected Tour createTourHook(Depot depot, int vehicleId, double vehicleCapacity)
    {
    	throw new RuntimeException("Create Tour Hook in DFSI sollte nicht verwendet werden");
    }
    
    @Override
    protected CustomerWithCost calculateCostHook(Customer customer, Tour tour, GroupOfTours got, double approximateEquality) 
    {
    	return TourUtilsGot.calculateCostStochasticSolver(customer, tour, got, approximateEquality);
    }

	@Override
	protected Comparator<Tour> getTourComparator() {
		// TODO Hier müsste noch ein Comparator hin, der auf die einzelnen Touren zugreifen kann.
		// Dazu müsste die ganze Insertion Heuristik so umgeschrieben werden, dass sie auf die 
		// einzelnen Touren zugreifen kann
		return new DeterministicTourComparatorWrtDemand();
	}

}
