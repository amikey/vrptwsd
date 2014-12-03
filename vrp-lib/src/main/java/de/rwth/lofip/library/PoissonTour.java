package de.rwth.lofip.library;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;

import de.rwth.lofip.library.util.CustomerInTour;

public class PoissonTour extends Tour {
	public PoissonTour() {
		id = new Random().nextLong();
	}

	public PoissonTour(Depot depot, Vehicle vehicle) {
		this();
		this.depot = depot;
		this.vehicle = vehicle;
	}

	@Override
	public BigDecimal getCumulatedDemandProbabilityAtPosition(int position,
			int demand)
	// proposition 1
	{
		return new BigDecimal(
				calculateProbabilityDemandEqualCapacityAtPosition(position));
	}

	public double calculateProbabilityDemandEqualCapacityAtPosition(int position)
	// calculate probability that demand at node {@param position} equals
	// vehicle capacity
	// probability for failure type 2 in Lei et al.
	{
		// first calculate deterministic demand up to position "position"
		long cumulatedDemand = 0;
		for (int i = 0; i <= position; i++)
			cumulatedDemand += this.getCustomerAtPosition(i).getCustomer()
					.getDemand();

		// this calculates the probability that the demand of all customers up
		// to
		// position "position" equals the vehicle capacity
		return new PoissonDistribution(cumulatedDemand)
				.probability((int) getVehicle().getCapacity());
	}

	@Override
	public BigDecimal getProbabilityOfFailureAtCustomer(
			CustomerInTour currentCustomer)
	// proposition 2
	{
		// 1 - failure occurs before currentCustomer - failure occurs after
		// currentCustomer
		// = 1 - P(X_{i-1} < Capacity) - P(X_i < Capacity)
		int position = currentCustomer.getPosition();
		BigDecimal probOfFailureBeforeCurrentCustomer = new BigDecimal(
				calculateProbabilityDemandLessThanCapacityAtPosition(position - 1));
		BigDecimal probOfFailureAfterCurrentCustomer = new BigDecimal(
				calculateProbabilityDemandLessThanCapacityAtPosition(position));

		// this calculation can be found in proposition 2 Lei et al paper
		return probOfFailureBeforeCurrentCustomer
				.subtract(probOfFailureAfterCurrentCustomer);
	}

	public double calculateProbabilityDemandLessThanCapacityAtPosition(
			int position) {
		// first calculate deterministic demand up to position "position"
		long cumulatedDemand = 0;
		for (int i = 0; i <= position; i++)
			cumulatedDemand += this.getCustomerAtPosition(i).getCustomer()
					.getDemand();

		// calculate probability that vehicle capacity is not exceeded
		// i.e. demand is less or equal vehicle capacity (i.e. P(X <= x)
		double probNotExceeded = new PoissonDistribution(cumulatedDemand)
				.cumulativeProbability((int) getVehicle().getCapacity());

		// substract probability that vehicle capacity is exactly used
		// P(X <= x) - P(X = x) = P(X < x)
		double probExactlyUsed = new PoissonDistribution(cumulatedDemand)
				.probability((int) getVehicle().getCapacity());

		return probNotExceeded - probExactlyUsed;
	}

}
