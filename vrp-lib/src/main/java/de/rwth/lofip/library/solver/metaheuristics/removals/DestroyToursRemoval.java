package de.rwth.lofip.library.solver.metaheuristics.removals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Tour;

/**
 * This heuristic destroys as many tours as possible
 */
public class DestroyToursRemoval extends FeasibilityOrientedRemoval implements RemovalInterface {
	
	private static int seed = 0;  	
	Random r;
	
	public DestroyToursRemoval() {  
	    seed++;  
	    r = new Random(seed);
	  }  
	
	public static void setSeed(int seed){
		DestroyToursRemoval.seed = seed;
	}
	
	@Override
	public String getName() {
		return "DTR";
	}
	
    @Override
	protected List<Tour> sortToursWrtToAmountOfCustomers(List<Tour> tours) {
        // sort the tours in decreasing order of their amount of customers
        Collections.sort(tours, new Comparator<Tour>() {
            @Override
            public int compare(Tour t1, Tour t2) {
                int cc1 = t1.getCustomers().size();
                int cc2 = t2.getCustomers().size();
                if (cc1 < cc2) {
                    return 1;
                }
                if (cc1 > cc2) {
                    return -1;
                }
                return 0;
            }
        });
        return tours;
	}
    
    @Override
	protected boolean toursShouldBeDeleted(int vehicleCountInSolution, int targetVehicleCount) {
		return true;
	}

}
