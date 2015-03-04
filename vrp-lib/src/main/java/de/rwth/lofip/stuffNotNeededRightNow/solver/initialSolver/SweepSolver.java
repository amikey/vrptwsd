package de.rwth.lofip.stuffNotNeededRightNow.solver.initialSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * A simple solver which only does a sweep algorithm: Start at a given angle
 * from the depot and turn the "beam" in one direction (clockwise or
 * counter-clockwise) and just take the next customer into the route until the
 * vehicle is full.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class SweepSolver extends AbstractSolver {

	@Override
	public Solution solve(VrpProblem problem) {
		// SweepSolverConfiguration ssc = (SweepSolverConfiguration)
		// getConfiguration();

		// first, calculate the angle in which the customers are, relative to
		// the depot
		Depot depot = problem.getDepot();

		List<Customer> customersByAngle = new ArrayList<Customer>(
				problem.getCustomers());

		Collections.sort(customersByAngle, new AngleComparator(depot));

		double capacity = problem.getVehicles().iterator().next().getCapacity();

		Solution solution = new Solution(problem);

		// iterate over all customers and add them to the route as long as the
		// vehicle currently used can still serve the demand.
		Vehicle currentVehicle = new Vehicle(1, capacity);
		Tour currentTour = new Tour(depot, currentVehicle);
		int vehicleId = 2;
		for (Customer customer : customersByAngle) {

			// case 1: initial customer
			if (solution.getTours().isEmpty()) {
				solution.addTour(currentTour);
				currentTour.addCustomer(customer);
				continue;
			}

			// case 2a: last customer and his demand fits
			// case 2b: last customer but the demand is bigger than the
			// available space
			if (customer
					.equals(customersByAngle.get(customersByAngle.size() - 1))) {
				if (currentVehicle.isCapacityAvailable(customer.getDemand())) {
					currentTour.addCustomer(customer);
				} else {
					// case 2b
					currentVehicle = new Vehicle(vehicleId, capacity);
					vehicleId++;
					currentTour = new Tour(depot, currentVehicle);
					solution.addTour(currentTour);
					currentTour.addCustomer(customer);
				}
				// because this was the last edge, break now.
				break;
			}

			// case 3: next customer fits fully into the vehicle
			if (currentVehicle.isCapacityAvailable(customer.getDemand())) {
				currentTour.addCustomer(customer);
				continue;
			}

			// case 4: next customer does not fit into the vehicle
			if (currentVehicle.isCapacityAvailable(customer.getDemand()) == false) {
				// return to the depot, get the next vehicle and drive with that
				// to the customer
				currentVehicle = new Vehicle(vehicleId, capacity);
				vehicleId++;
				currentTour = new Tour(depot, currentVehicle);
				solution.addTour(currentTour);
				currentTour.addCustomer(customer);
				continue;
			}
		}

		return solution;
	}

	/**
	 * Determine which customer is further around a circle which has its center
	 * at the depot.
	 * 
	 * @author Dominik Sandjaja <dominik@dadadom.de>
	 * 
	 */
	protected class AngleComparator implements Comparator<Customer> {

		private Depot depot;

		public AngleComparator(Depot depot) {
			this.depot = depot;
		}

		// compare the two customers depending on their angle, relative to the
		// depot
		@Override
		public int compare(Customer customer1, Customer customer2) {

			// Angle between the center of the circle and p1,
			// measured in degrees counter-clockwise from the positive X axis
			// (horizontal)
			double angle1 = (Math.atan2(
					customer1.getyCoordinate() - depot.getyCoordinate(),
					customer1.getxCoordinate() - depot.getxCoordinate())
					* 180
					/ Math.PI + 360) % 360;
			double angle2 = (Math.atan2(
					customer2.getyCoordinate() - depot.getyCoordinate(),
					customer2.getxCoordinate() - depot.getxCoordinate())
					* 180
					/ Math.PI + 360) % 360;
			if (angle1 < angle2) {
				return -1;
			}
			if (angle1 > angle2) {
				return 1;
			}
			// if they both have the same angle, just - arbitrarily - sort from
			// left to right and from top to bottom
			else { // angle1 == angle2
				if (customer1.getxCoordinate() < customer2.getxCoordinate()) {
					return -1;
				}
				if (customer1.getxCoordinate() > customer2.getxCoordinate()) {
					return 1;
				} else { // both have the same x coordinate
					if (customer1.getyCoordinate() < customer2.getyCoordinate()) {
						return -1;
					}
					if (customer1.getyCoordinate() > customer2.getyCoordinate()) {
						return 1;
					}
					return 0;
				}
			}
		}
	}

	@Override
	public String getDescriptiveName() {
		return "Sweep Solver";
	}
}
