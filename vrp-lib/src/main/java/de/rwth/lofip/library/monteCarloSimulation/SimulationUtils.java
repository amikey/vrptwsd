package de.rwth.lofip.library.monteCarloSimulation;

import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;

public class SimulationUtils {
	
	static int seed = 1;

	public static void resetSeed() {
		seed = 1;
	}
	
	public static GroupOfTours setDemandForCustomersWithDeviation(GroupOfTours got, double deviation) {
		seed++;
		Random randomDemandGeneration = new Random(seed);
		
		for (Customer c : got.getCustomers()) {
			double sd = deviation * 1/3 * c.getDemand();
			int demand;        	
		    double val = randomDemandGeneration.nextGaussian() * sd + c.getDemand();
		    demand = (int) Math.round(val);
		    if (demand > got.getFirstTour().getVehicle().getCapacity())
			    demand = (int) got.getFirstTour().getVehicle().getCapacity();
		    if (demand < 1)
		        demand = 1;       
		    c.setDemand(demand);
		}
		return got;			
			
	}
	
}
