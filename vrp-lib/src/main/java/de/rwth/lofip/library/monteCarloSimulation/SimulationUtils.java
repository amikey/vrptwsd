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
	//specifies the deviation of all Customers that are in one Group
	static double deviationInGroup;
	static int numberOfCustomersInGroup;

	public static void resetSeed() {
		seed = 1;
	}
	
	public static void generateDemandsForNextScenario() {
		seed++;
	}
	
	public static GroupOfTours setDemandForCustomersWithDeviation(GroupOfTours got) {
		double deviation = Parameters.getFluctuationOfDemandInPercentage();
		Random randomDemandGeneration = new Random(seed);
		generateSomeRandomness(randomDemandGeneration);
		
		List<Customer> customerList = new ArrayList<Customer>();
		customerList.addAll(got.getCustomers());
		sortListWrtToCustomerNo(customerList);
		//this counts the number of customers that will be in one group
		//gets reset to 0 when Parameter.getNumberOfCustomersInGroupForCorrelation() is reached
		numberOfCustomersInGroup = 0;
		
		for (Customer c : customerList) {
			increaseNumberOfCustomersInGroup();
			generateDeviationForGroup(numberOfCustomersInGroup, randomDemandGeneration);
			
			long customerDemand = c.getOriginalDemand();					
			double sd = deviation * customerDemand;
			int demand;        	
		    double val = (deviationInGroup * sd) + customerDemand;
		    demand = (int) Math.round(val);
		    if (demand > got.getFirstTour().getVehicle().getCapacity())
			    demand = (int) got.getFirstTour().getVehicle().getCapacity();
		    if (demand < 1)
		        demand = 1;       
		    c.setDemand(demand);
		}
		
		//baue touren in got neu auf, damit Refs auch richtig sind.
		// warum ist das denn noch mal nötig???
		for (Tour tour : got.getTours()) {			
			List<Customer> customers = tour.removeCustomersBetween(0, tour.getCustomerSize());
			tour.insertCustomersAtPosition(customers, 0);			
		}
		return got;						
	}

	private static void generateSomeRandomness(Random randomDemandGeneration) {
		for (int i = 0; i <= seed; i++)
			randomDemandGeneration.nextGaussian();
	}
	
	private static void sortListWrtToCustomerNo(List<Customer> customerList) {
		Comparator<Customer> customerByNoComparator = (e1,e2) -> Double.compare(e1.getCustomerNo(), e2.getCustomerNo());
		Collections.sort(customerList, customerByNoComparator);
	}
	
	private static void increaseNumberOfCustomersInGroup() {
		numberOfCustomersInGroup++;
		if (numberOfCustomersInGroup > Parameters.getNumberOfCustomersInCorrelatedGroup())
			numberOfCustomersInGroup = 1;
	}
	
	private static void generateDeviationForGroup(int numberOfCustomersInGroup, Random randomDemandGeneration) {
		if (numberOfCustomersInGroup == 1)
			//generateNewDeviation
			deviationInGroup = (randomDemandGeneration.nextGaussian());
		//else do nothing
	}
	
	public static void setCapacityOfVehiclesToOriginalCapacity(GroupOfTours gotClone) {
		for (Tour t : gotClone.getTours())
			t.getVehicle().setCapacity(gotClone.getParentSolution().getVrpProblem().getOriginalCapacity());		
	}

	public static int getSeed() {
		return seed;
	}

	public static void setSeed(int i) {
		seed = i;
	}
	
}
