package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Edge;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.util.SimilarityUtils;
import de.rwth.lofip.library.util.CustomerInTour;
import de.rwth.lofip.stuffNotNeededRightNow.solver.util.CustomerWithCost;
import de.rwth.lofip.stuffNotNeededRightNow.util.RemovedCustomer;

/**
 * The Similarity Removal, as described by Lei et al. in section 3.3.1
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class SimilarityRemovalGot implements RemovalInterfaceGot {

	private static int seed = 0;
	Random r;

	public SimilarityRemovalGot() {
		seed++;
		r = new Random(seed);
	}

	public static void setSeed(int seed) {
		SimilarityRemovalGot.seed = seed;
	}

	@Override
	public String getName() {
		return "SR";
	}

	@Override
	public List<RemovedCustomer> removeCustomers(SolutionGot solution,
			int amountOfRemovedCustomers) {

		List<RemovedCustomer> removedCustomers = new ArrayList<RemovedCustomer>(
				amountOfRemovedCustomers);

		// first step, select a completely random customer from all customers
		int totalCustomerCount = solution.getCustomersInTours().size();

		int pos = r.nextInt(totalCustomerCount);
		CustomerInTour firstRandomCustomer = solution.getCustomersInTours()
				.get(pos);

		Customer firstRemovedCustomer = firstRandomCustomer.getTour()
				.removeCustomerAtPosition(firstRandomCustomer.getPosition());
		RemovedCustomer rc = new RemovedCustomer();
		rc.setCustomer(firstRemovedCustomer);
		rc.setTourId(firstRandomCustomer.getTour().getId());
		rc.setPosition(firstRandomCustomer.getPosition());
		removedCustomers.add(rc);
		solution.removeEmptyTours();

		return removeRemainingCustomers(solution, amountOfRemovedCustomers,
				removedCustomers, firstRemovedCustomer);
	}

	/**
	 * After first random customer has been removed, remove additional customers
	 * until {@code amountOfRemovedCustomers} customers have been removed
	 * removedCustomers is a set that
	 * 
	 * @param solution
	 * @param amountOfRemovedCustomers
	 * @param removedCustomers
	 * @param firstRemovedCustomer
	 * @return removedCustomers
	 */
	public List<RemovedCustomer> removeRemainingCustomers(SolutionGot solution,
			int amountOfRemovedCustomers,
			List<RemovedCustomer> removedCustomers,
			Customer firstRemovedCustomer) {

		double maxDistanceOfAll = Double.MIN_VALUE;
		for (CustomerInTour c : solution.getCustomersInTours()) { 
			double distance = new Edge(c, firstRemovedCustomer).getLength();
			if (distance > maxDistanceOfAll) {
				maxDistanceOfAll = distance;
			}
		}
		// now we have two cases.
		// Case 1: we (still) have more tours than we have customers removed
		// Case 2: amountOfRemovedCustomers > number of tours

		// in the first case, only find out the customer with the largest
		// similarity value, based on the firstRemoved Customer
		if (solution.getTours().size() >= amountOfRemovedCustomers - 1) {
			List<CustomerWithCost> customersWithCost = new ArrayList<CustomerWithCost>();
			for (Tour tour : solution.getTours()) {
				CustomerWithCost mostSimilarCustomer = new CustomerWithCost(
						null, tour, Double.MIN_VALUE);
				for (CustomerInTour customer : tour.getCustomersInTour()) {
					double similarity = calculateSimilarity(
							firstRemovedCustomer, customer, maxDistanceOfAll);
					if (similarity > mostSimilarCustomer.getCost()) {
						mostSimilarCustomer.setCost(similarity);
						mostSimilarCustomer.setPosition(customer.getPosition());
						mostSimilarCustomer.setCustomer(customer.getCustomer());
					}
				}
				customersWithCost.add(mostSimilarCustomer);
			}
			// now sort the set in decreasing order of the similarity value
			Collections.sort(customersWithCost,
					new Comparator<CustomerWithCost>() {

						@Override
						public int compare(CustomerWithCost o1,
								CustomerWithCost o2) {
							if (o1.getCost() > o2.getCost()) {
								return -1;
							}
							if (o1.getCost() < o1.getCost()) {
								return 1;
							}
							return 0;
						}

					});
			// only take the first amountOfCustomersToBeRemoved number of
			// customers
			if (customersWithCost.size() > amountOfRemovedCustomers - 1) {
				customersWithCost = customersWithCost.subList(0,
						amountOfRemovedCustomers - 1);
			}
			for (CustomerWithCost cwc : customersWithCost) {
				RemovedCustomer removedCustomer = new RemovedCustomer();
				removedCustomer.setCustomer(cwc.getTour()
						.removeCustomerAtPosition(cwc.getPosition()));
				removedCustomer.setTourId(cwc.getTour().getId());
				removedCustomer.setPosition(cwc.getPosition());
				removedCustomers.add(removedCustomer);
			}
		} else {
			// this is case 2:
			// now we iterate over the tours in a round robin fashion and
			// remove
			// the customer with the highest similarity measure in this tour
			int i = 0;
			while (removedCustomers.size() < amountOfRemovedCustomers) {
				Tour tourToRemoveFrom = solution.getTours().get(
						i % solution.getTours().size());
				CustomerWithCost mostSimilarCustomer = new CustomerWithCost(
						null, tourToRemoveFrom, Double.MIN_VALUE);
				for (CustomerInTour customer : tourToRemoveFrom
						.getUnfixedCustomersInTour()) {
					double similarity = calculateSimilarity(
							firstRemovedCustomer, customer, maxDistanceOfAll);
					if (similarity > mostSimilarCustomer.getCost()) {
						mostSimilarCustomer.setCost(similarity);
						mostSimilarCustomer.setPosition(customer.getPosition());
						mostSimilarCustomer.setCustomer(customer.getCustomer());
					}
				}
				// now remove the found vertex
				RemovedCustomer removedCustomer = new RemovedCustomer();
				if (!tourToRemoveFrom.getCustomerAtPosition(
						mostSimilarCustomer.getPosition())
						.isCustomerInTourFixed()) {// -OB-Abfrage ob dieser
													// Kunde fixiert ist. erst
													// dann herausl�sen von der
													// L�sung in
													// removedCustomers
					removedCustomer.setCustomer(tourToRemoveFrom
							.removeCustomerAtPosition(mostSimilarCustomer
									.getPosition()));
					removedCustomer.setTourId(tourToRemoveFrom.getId());
					removedCustomer.setPosition(mostSimilarCustomer
							.getPosition());
					removedCustomers.add(removedCustomer);
				}
				solution.removeEmptyTours();
				// recalculate the maximum distance - only the existing
				// tours should be considered
				maxDistanceOfAll = Double.MIN_VALUE;
				for (CustomerInTour c : solution.getCustomersInTours()) {
					double distance = new Edge(c, firstRemovedCustomer)
							.getLength();
					if (distance > maxDistanceOfAll) {
						maxDistanceOfAll = distance;
					}
				}

				i++;
			}
		}

		return removedCustomers;
	}

	/**
	 * Calculate the similarity between the two given customers. The formulas
	 * can be found as numbers 16 and 17 in the paper by Lei et al.
	 * 
	 * @param firstRemovedCustomer
	 * @param customer
	 * @param maxDistanceOfAll
	 * @return
	 */
	private double calculateSimilarity(Customer firstRemovedCustomer,
			CustomerInTour customer, double maxDistanceOfAll) {
		SimilarityUtils.setMaxDistance(maxDistanceOfAll);
		return SimilarityUtils.calculateSimilarity(firstRemovedCustomer, customer.getCustomer());
	}

}
