package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.insertions.NeighborhoodInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.TabuInsertionInterface;
import de.rwth.lofip.library.solver.metaheuristics.insertions.deterministic.tabu.DeterministicGreedyInsertionTabuSearch;
import de.rwth.lofip.library.solver.util.TabuTourPosition;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RemovedCustomer;

public class Relocate implements NeighborhoodInterface {
	
	private Set<TabuTourPosition> tabuPositions = new HashSet<TabuTourPosition>();
	private Set<TabuTourPosition> tempTabuPositions = new HashSet<TabuTourPosition>();
	private Set<TabuTourPosition> bestTabuPositions = new HashSet<TabuTourPosition>();
	
	@Override
	public Solution insertCustomers(Solution solution,
            Collection<RemovedCustomer> customers, final int iteration,
            VrpConfiguration configuration) {
		
		Solution bestSolution = null;
		double bestCost = Double.MAX_VALUE;
		
		for (CustomerInTour cit : solution.getCustomersInTours())
		{
			RemovedCustomer removedCustomer = removeCit(cit, solution);
			LinkedList<RemovedCustomer> removedCustomers = new LinkedList<RemovedCustomer>();
			removedCustomers.add(removedCustomer);
			tempTabuPositions = tabuPositions;
			addRemovedCustomerToTempTabuList(removedCustomers, iteration);
			TabuInsertionInterface GI = new DeterministicGreedyInsertionTabuSearch();
			GI.setTabuPositions(tempTabuPositions);
			Solution newSolution = GI.insertCustomers(solution, removedCustomers, iteration, configuration);
			if (newSolution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost() <= bestCost)
			{
				bestSolution = newSolution;
				bestCost = newSolution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();
				bestTabuPositions = tempTabuPositions;
			}
		}
		return bestSolution;
	}
	
	private RemovedCustomer removeCit(CustomerInTour cit, Solution solution) {
		RemovedCustomer rc = new RemovedCustomer();
        rc.setCustomer(cit.getTour().removeCustomerAtPosition(
                cit.getPosition()));
        rc.setTourId(cit.getTour().getId());
        rc.setPosition(cit.getPosition());
        solution.removeEmptyTours();
        return rc;        
	}

	@Override
	public String getName() {
		return "Relocate";
	}

	@Override
    public void setTabuPositions(Set<TabuTourPosition> tabuPositions) {
    	this.tabuPositions = tabuPositions;        
    }
    
	@Override
    public Set<TabuTourPosition> getTabuPositions() {
		if (bestTabuPositions.isEmpty())
			throw new RuntimeException("Insertion ist kein Neighborhood");
		else 
			return bestTabuPositions;    	
    }
    
    protected void addRemovedCustomerToTempTabuList(List<RemovedCustomer> removedCustomers,
            int iteration) {
        for (RemovedCustomer rc : removedCustomers) {
            TabuTourPosition ttp = new TabuTourPosition();
            ttp.setIteration(iteration);
            ttp.setTabuValue(rc.getCustomer().getCustomerNo() + ":"
                    + rc.getTourId());
            tempTabuPositions.add(ttp);
        }
    }

    @Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {
		throw new RuntimeException("isInsertionAllowed sollte nicht benutzt werden in Relocate");
	}
}
