package de.rwth.lofip.library.solver.metaheuristics.insertions.groups;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractGreedyInsertionGot;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * Implement the Greedy Insertion as described by Lei et al. in section 3.3.5 of
 * their paper.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class GreedyInsertionGot extends AbstractGreedyInsertionGot {

    public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i, double approximateEquality)
    {
    	//stochastic variant
    	return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer, tour, i, approximateEquality);
    }
       
}
