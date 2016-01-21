package de.rwth.lofip.library.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;


/**
 * Helper class with static methods for handling VRP instances, e.g. load them
 * from a file.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class VrpUtils {

	public enum ImportFileState {
		NEW, VEHICLE, DEPOT, CUSTOMER
	}

	/**
	 * Given a list of Strings, create a VrpProblem from that information. The
	 * format of that list needs to be like
	 * 
	 * <pre>
	 * R101
	 * VEHICLE
	 * NUMBER     CAPACITY
	 *  9         50
	 *  
	 * CUSTOMER
	 * CUST NO.   XCOORD.   YCOORD.    DEMAND   READY TIME   DUE DATE   SERVICE TIME
	 * 0          35        35          0         0          230         0
	 * 1          41        49         10       161          171        10
	 * 2          35        17          7        50           60        10
	 * 3          55        45         13       116          126        10
	 * ...
	 * </pre>
	 * <p>
	 * The first line is a textual description of the problem instance.<br>
	 * The second and third line are fixed.<br>
	 * The numbers in the fourth line define the number of vehicles and their
	 * respective amount. The numbers must be separated by a whitespace.<br>
	 * The lines five to seven have to be as seen.<br>
	 * The first line of the customer information (no. 0) represents the depot.
	 * Only the values of the XCOORD. and the YCOORD. are respected at this
	 * time.<br>
	 * The following lines until the end of the file are definitions of
	 * customers. All values must be separated by whitespace.
	 * </p>
	 * 
	 * @param list
	 * @return
	 */
	public static VrpProblem createProblemFromStringList(List<String> list) {

		ImportFileState state = ImportFileState.NEW;
		VrpProblem vrpProblem = new VrpProblem();

		for (String line : list) {
			// ignore empty lines
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			line = StringUtils.normalizeSpace(StringUtils.trim(line));
			switch (state) {
			case NEW:
				if (line.startsWith("VEHICLE")) {
					state = ImportFileState.VEHICLE;
				} else {
					vrpProblem.setDescription(line);
				}
				break;
			case VEHICLE:
				if (!line.startsWith("NUMBER")) {
					String[] split = StringUtils.split(line);
					// create the given amount of vehicles with the given
					// capacity
					int vehicleAmount = Integer.parseInt(split[0]);
					double vehicleCapacity = Double.parseDouble(split[1]);
					Set<Vehicle> vehicles = new HashSet<Vehicle>(vehicleAmount);
					for (int i = 0; i < vehicleAmount; i++) {
						vehicles.add(new Vehicle(i, vehicleCapacity));
					}
					vrpProblem.setVehicles(vehicles);
					vrpProblem.setOriginalCapacity(vehicleCapacity);
					state = ImportFileState.DEPOT;
				}
				break;
			case DEPOT:
				if (line.startsWith("0")) {
					String[] split = StringUtils.split(line);
					Depot depot = new Depot();
					depot.setxCoordinate(Double.parseDouble(split[1]));
					depot.setyCoordinate(Double.parseDouble(split[2]));
					depot.setDemand(Long.parseLong(split[3]));
					depot.setTimeWindowOpen(Long.parseLong(split[4]));
					depot.setTimeWindowClose(Long.parseLong(split[5]));
					depot.setServiceTime(Long.parseLong(split[6]));
					vrpProblem.addDepot(depot);
					vrpProblem.setMaxTime(Long.parseLong(split[5]));
					state = ImportFileState.CUSTOMER;
				}
				break;
			case CUSTOMER:
//				System.out.println(line);
				String[] split = StringUtils.split(line);
				Customer customer = new Customer();
				customer.setCustomerNo(Long.parseLong(split[0]));
				customer.setxCoordinate(Double.parseDouble(split[1]));
				customer.setyCoordinate(Double.parseDouble(split[2]));
				customer.setDemand(Long.parseLong(split[3]));
				customer.setOriginalDemand(Long.parseLong(split[3]));
				customer.setTimeWindowOpen(Long.parseLong(split[4]));
				customer.setTimeWindowClose(Long.parseLong(split[5]));
				customer.setServiceTime(Long.parseLong(split[6]));
				vrpProblem.addCustomer(customer);
				break;
			}
		}
		return vrpProblem;
	}
	
	public static VrpProblem constructVrpProblemFromTour(Tour tour) {
		VrpProblem vrpProblem = new VrpProblem();
		vrpProblem.setDescription("Auxiliary vrpProblem for TDP - Intensification Phase");
		for (Customer c : tour.getCustomers())
			vrpProblem.addCustomer(c);
		vrpProblem.addDepot(tour.getDepot());
		
		Set<Vehicle> vehicles = new HashSet<Vehicle>(1);
		vehicles.add(tour.getVehicle());
		vrpProblem.setVehicles(vehicles);
		vrpProblem.setOriginalCapacity(tour.getVehicle().getCapacity());
		return vrpProblem;
	}
	
	/**
	 * Change capacity of vehicles in original problem to capacity * percentage.
	 * e.g if capacity of vehicles is 100 and percentage is 0,9 (=90%), then the
	 * new capacity will be 90.
	 */
	public static VrpProblem decreaseCapacityOfVehiclesToPercentageOf(VrpProblem vrpProblem, double percentage) {
		
		Set<Vehicle> vehicleSet = new HashSet<Vehicle>();
		for (Vehicle vehicle : vrpProblem.getVehicles()) {
			double capacity = vehicle.getCapacity();
			vehicle.setCapacity(capacity * percentage);
			vehicleSet.add(vehicle);
		}
		vrpProblem.setVehicles(vehicleSet);
		return vrpProblem;
	}

}
