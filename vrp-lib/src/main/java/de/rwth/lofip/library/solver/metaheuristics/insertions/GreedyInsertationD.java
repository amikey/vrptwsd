package de.rwth.lofip.library.solver.metaheuristics.insertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.library.util.RemovedCustomer;

/**
 * Implement the deterministic Greedy Insertion .
 * 
 * @author Olga Bock <bock@dpor.rwth-aachen.de>
 */
public class GreedyInsertationD implements InsertionInterface {

	@Override
	public String getName() {
		return "detGI";
	}

	@Override
	public Solution insertCustomers(Solution solution,
            Collection<RemovedCustomer> customers, final int iteration,
            final VrpConfiguration configuration) 
	{
		//System.out.println("		START GREEDY"); 
		final Depot depot = solution.getVrpProblem().getDepot();

	        // for all customers, find their N (=10) best insertion points wrt the
	        // deterministic part of the cost (distance)
	        for (final RemovedCustomer customerToBeInserted : customers) 
	        {

	        	//System.out.println("		Kunde " + customerToBeInserted.getCustomer().getCustomerNo() + " wird mit Greedy Insertation eingefügt.");
                
	            List<CustomerWithCost> insertionPoints = new ArrayList<CustomerWithCost>();

	            int threads = Runtime.getRuntime().availableProcessors();
	            ExecutorService service = Executors.newFixedThreadPool(threads);
	            List<Future<List<CustomerWithCost>>> costFutures = new ArrayList<Future<List<CustomerWithCost>>>();

	            // determine the deterministic costs of inserting a customer at a
	            // position in a tour
	            for (final Tour tour : solution.getTours()) 
	            {	
	            
	                int minPosition = 10000000;
	                for (CustomerInTour customer : tour.getUnfixedCustomersInTour()){
	                		
	                	
						if (customer.getPosition() < minPosition)
	                	{
	    					minPosition = customer.getPosition();
	    				}             	
	                }
	                
	                final int minPos = minPosition;	                
	                final int tourCustomerCount = tour.getCustomerSize();

	                Callable<List<CustomerWithCost>> cwcCallable = new Callable<List<CustomerWithCost>>() 
	                {

	                    @Override
	                    public List<CustomerWithCost> call() throws Exception 
	                    {
	                        List<CustomerWithCost> retList = new ArrayList<CustomerWithCost>();

	                        for (int i = minPos; i <= tourCustomerCount; i++) 
	                        {	                        	
	                            if (isInsertionAllowed(
	                                    customerToBeInserted.getCustomer(), tour, i)
	                                    && TourUtils
	                                            .isInsertionPossibleWrtDeterministicDemandAndTW(
	                                                    customerToBeInserted
	                                                            .getCustomer(),
	                                                    tour,
	                                                    i)) 
	                            {
	                                // calculate the cost of insertion
	                            	double costFactor = tour.getCostFactor();
	                            	double cost = 0;
	                                if (i == 0) 
	                                {
	                                    assert (cost == 0);
	                                    cost += new Edge(depot,
	                                            customerToBeInserted.getCustomer())
	                                            .getLength()*costFactor;
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            tour.getCustomerAtPosition(0))
	                                            .getLength()*costFactor;
	                                    cost -= new Edge(depot,
	                                            tour.getCustomerAtPosition(0))
	                                            .getLength()*costFactor;
	                                }
	                                if (i == tourCustomerCount) 
	                                {
	                                    assert (cost == 0);
	                                    // customer is inserted at the end
	                                    cost += new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            customerToBeInserted.getCustomer())
	                                            .getLength()*costFactor;
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            depot).getLength()*costFactor;
	                                    cost -= new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            depot).getLength()*costFactor;
	                                }
	                                if (i > 0 && i < tourCustomerCount) 
	                                {
	                                    assert (cost == 0);
	                                    cost += new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            customerToBeInserted.getCustomer())
	                                            .getLength()*costFactor;
	                                    cost += new Edge(
	                                            customerToBeInserted.getCustomer(),
	                                            tour.getCustomerAtPosition(i))
	                                            .getLength()*costFactor;
	                                    cost -= new Edge(
	                                            tour.getCustomerAtPosition(i - 1),
	                                            tour.getCustomerAtPosition(i))
	                                            .getLength()*costFactor;
	                                }

	                                // insert the cost value into the list of all
	                                // cost values
	                                CustomerWithCost cwc = new CustomerWithCost(
	                                        customerToBeInserted.getCustomer(),
	                                        tour, cost);
	                                cwc.setPosition(i);
	                                retList.add(cwc);
	                            } 
	                        }
	                        return retList;
	                    }
	                };
	                costFutures.add(service.submit(cwcCallable));
	            }
	            service.shutdown();
	            
	            for (Future<List<CustomerWithCost>> f : costFutures) {
	                try {
	                    insertionPoints.addAll(f.get());
	                } catch (InterruptedException | ExecutionException e) {
	                    e.printStackTrace();
	                    System.err.print("An error occurred in the parallelism");
	                }
	            }
	            
	            if (insertionPoints.isEmpty()) {
	                // no insertion possible
	                // generate a new route and add the customer
	            	double costFactorForNewTour = 2;
	            	Tour newTour = new DeterministicTour(depot, new Vehicle(
	                        new Random().nextInt(Integer.MAX_VALUE), (solution
	                				.getVrpProblem().getVehicles()).iterator().next().getCapacity()), costFactorForNewTour);
	                newTour.addCustomer(customerToBeInserted.getCustomer(),
	                        iteration, getClass().getSimpleName(), false);
	                solution.addTour(newTour);
	                
	                //System.out.println("		Mit Kunde " + customerToBeInserted.getCustomer().getCustomerNo() + " wird eine neue Tour erstellt, da ein Wechsel in eine andere Tour nicht möglich war.");
	                
	            } else {
	                Collections.sort(insertionPoints,
	                        new CustomerWithCostComparator());
	                CustomerWithCost cwc = insertionPoints.get(0);
	                
	                //System.out.println("		Kunde " + cwc.getCustomer().getCustomerNo() + " wird in Tour " + cwc.getTour().getId() + " an Position " + cwc.getPosition() + " eingefügt");
	                
	                cwc.getTour().insertCustomerAtPosition(cwc.getCustomer(),
	                        cwc.getPosition(), iteration,
	                        getClass().getSimpleName(), false);
	            }
	      }
	        
	        return solution;
	    
	}

	// OB - Überprüft ob die angegebene oder die vorherige Position bereits von
	// einem fixierten Kunden belegt wird und
	// gibt dann false aus, sonst true
	@Override
	public boolean isInsertionAllowed(Customer customer, Tour tour, int position) {

		if (position > 0 && position < tour.getCustomers().size()) {
			if (tour.getCustomerAtPosition(position)
					.getPreviousCustomerInTour().isCustomerInTourFixed()) {
				return false;
			}
		}
		return true;

	}

	/**
	 * A simple comparator which sorts the {@code CustomerWithCost} ascending by
	 * their cost.
	 * 
	 * @author Dominik Sandjaja <dominik@dadadom.de>
	 * 
	 */
	private class CustomerWithCostComparator implements
			Comparator<CustomerWithCost> {
		@Override
		public int compare(CustomerWithCost o1, CustomerWithCost o2) {
			if (o1.getCost() < o2.getCost()) {
				return -1;
			}
			if (o1.getCost() > o2.getCost()) {
				return 1;
			}
			return 0;
		}
	}

}
