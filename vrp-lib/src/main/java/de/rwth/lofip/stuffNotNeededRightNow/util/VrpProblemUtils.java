package de.rwth.lofip.stuffNotNeededRightNow.util;

import java.util.HashSet;
import java.util.Set;

import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;

public class VrpProblemUtils {

	/**
	 * Change capacity of vehicles in original problem to capacity * percentage.
	 * e.g if capacity of vehicles is 100 and percentage is 0,9 (=90%), then the
	 * new capacity will be 90.
	 */
	public static VrpProblem decreaseCapacityOfVehiclesToPercentageOf(
			VrpProblem vrpProblem, double percentage) {
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
