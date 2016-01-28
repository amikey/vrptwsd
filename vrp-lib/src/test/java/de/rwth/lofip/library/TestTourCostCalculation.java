package de.rwth.lofip.library;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class TestTourCostCalculation {
	
	Tour tour;
	private double costBefore;
	private List<Customer> customers;

	@Test
	public void testTSOnSomeSolutionForC101() throws IOException {
		GivenTourFromSolutionForC101();
		AndTakingCostOfThisTour();
		WhenDeletingSomeCustomers();
		AndInsertingTheSameCustomersAtTheSamePosition();
		ThenCostShouldEqualCostFromBefore();
	}

	private void GivenTourFromSolutionForC101() throws IOException {
		tour = SetUpSolutionFromString.SetUpSolution("( ( 72 23 67 55 24 80 ) ) ( ( 2 40 56 4 25 ) ) ( ( 36 47 7 8 46 48 ) ) ( ( 33 71 78 3 54 ) ) ( ( 27 31 18 84 96 60 93 ) ) ( ( 63 30 51 10 32 70 ) ) ( ( 59 5 44 86 37 17 100 ) ) ( ( 39 79 34 35 77 ) ) ( ( 65 90 50 ) ) ( ( 21 73 57 43 91 ) ) ( ( 45 38 74 ) ) ( ( 28 12 76 81 66 1 ) ) ( ( 52 99 6 13 ) ) ( ( 95 98 16 85 89 ) ) ( ( 14 15 22 68 58 ) ) ( ( 75 41 26 ) ) ( ( 83 82 49 ) ) ( ( 92 42 87 97 ) ) ( ( 69 19 20 ) ) ( ( 64 ) ) ( ( 62 9 ) ) ( ( 11 ) ) ( ( 29 53 ) ) ( ( 61 94 ) ) ( ( 88 ) )"
					, ReadAndWriteUtils.readSolomonProblemC101().get(0)).getTour(0);
	}
	
	private void AndTakingCostOfThisTour() {
		costBefore = tour.getTotalDistanceWithCostFactor();
	}

	private void WhenDeletingSomeCustomers() {
		customers = tour.removeCustomersBetween(2, 4);
		assertEquals(67, customers.get(0).customerNo, 0.00001);
	}

	private void AndInsertingTheSameCustomersAtTheSamePosition() {
		tour.insertCustomersAtPosition(customers, 2);
	}

	private void ThenCostShouldEqualCostFromBefore() {
		assertEquals(costBefore, tour.getTotalDistanceWithCostFactor(), 0.00001);
		
	}


}
