package de.rwth.lofip.library.monteCarloSimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.parameters.Parameters;

public class SimulationUtils {
	
	private static int seed = 1;

	public static void resetSeed() {
		seed = 1;
	}
	
	public static void generateDemandsForNextScenario() {
		seed++;
	}
	
	public static GroupOfTours setDemandForCustomersWithDeviation(GroupOfTours got) {
		double deviation = Parameters.getFluctuationOfDemandInPercentage();
		Random randomDemandGeneration = new Random(seed);
		
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.addAll(got.getCustomers());
		sortListWrtToCustomerNo(customerList);
		
		for (Customer c : customerList) {
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
	
	private static void sortListWrtToCustomerNo(List<Customer> customerList) {
		Comparator<Customer> customerByNoComparator = (e1,e2) -> Double.compare(e1.getCustomerNo(), e2.getCustomerNo());
		Collections.sort(customerList, customerByNoComparator);
	}
	
	public static void setCapacityOfVehiclesToOriginalCapacity(GroupOfTours gotClone) {
		for (Tour t : gotClone.getTours())
			t.getVehicle().setCapacity(gotClone.getParentSolution().getVrpProblem().getOriginalCapacity());		
	}
	
}
