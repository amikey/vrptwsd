package de.rwth.lofip.library.monteCarloSimulation;

import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;

public class SimulationUtils {
	
	private static int seed = 1;

	public static void resetSeed() {
		seed = 1;
	}
	
	public static GroupOfTours setDemandForCustomersWithDeviation(GroupOfTours got, double deviation) {
		seed++;
		Random randomDemandGeneration = new Random(seed);
		
		for (Customer c : got.getCustomers()) {
			//IMPORTANT_TODO: hier wirklich mit 1/3 multiplizieren?
			double sd = deviation * c.getDemand();
			int demand;        	
		    double val = randomDemandGeneration.nextGaussian() * sd + c.getDemand();
		    demand = (int) Math.round(val);
		    if (demand > got.getFirstTour().getVehicle().getCapacity())
			    demand = (int) got.getFirstTour().getVehicle().getCapacity();
		    if (demand < 1)
		        demand = 1;       
		    c.setDemand(demand);
		}
		
		//baue touren in got neu auf, damit Refs auch richtig sind.
		for (Tour tour : got.getTours()) {			
			List<Customer> customers = tour.removeCustomersBetween(0, tour.getCustomerSize());
			tour.insertCustomersAtPosition(customers, 0);			
		}
		return got;						
	}

	public static void setCapacityOfVehiclesToOriginalCapacity(GroupOfTours gotClone) {
		for (Tour t : gotClone.getTours())
			t.getVehicle().setCapacity(gotClone.getParentSolution().getVrpProblem().getOriginalCapacity());		
	}
	
}
