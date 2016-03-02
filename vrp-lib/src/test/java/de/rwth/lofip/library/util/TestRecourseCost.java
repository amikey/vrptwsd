package de.rwth.lofip.library.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class TestRecourseCost {
	
	SolutionGot solution;
	GroupOfTours got;
	private GroupOfTours got2;
	List<GroupOfTours> gots = new ArrayList<GroupOfTours>();
	
	@Test
	public void testRecourseCostOfModifiedC101Solution() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		Parameters.setRelativeStandardDeviationTo(0.15);
//		Parameters.setTestingMode(true);
		givenSolutionForModifiedC101Problem();
		thenRecourseCostShouldBeOfCertainValue();
	}

	private void givenSolutionForModifiedC101Problem() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readModifiedSolomonProblems().get(0);
		solution = SetUpSolutionFromString.SetUpSolution("( ( 0 5 3 4 2 1 ) ) ( ( 0 18 19 15 ) ) ( ( 0 16 14 12 ) ) ( ( 0 7 8 9 6 ) ) ( ( 0 13 17 10 11 ) ) ",
				problem);
	}

	private void thenRecourseCostShouldBeOfCertainValue() {
		System.out.println("TestRecourseCostOfModifiedC101Solution");
		System.out.println(solution.getAsTupel());
		SimulationUtils.resetSeed();
		System.out.println(solution.getExpectedRecourseCost().getRecourseCost());
		//Achtung, dieser Wert kann auch unterschiedlich sein wegen den Zufallszahlen. 
		//Das heiﬂt nicht unbedingt, dass ein Fehler im Algorithmus vorliegt
		assertEquals(77.12573,solution.getExpectedRecourseCost().getRecourseCost(),0.00001);
	}
	
	@Test
	public void testNumberOfAdditionalToursAndNumberOfRouteFailuresInRecourseCost() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		Parameters.setRelativeStandardDeviationTo(0.15);
//		Parameters.setTestingMode(true);
		givenSolutionForModifiedC101Problem();
		thenNumberOfAdditionalToursShouldBeOfCertainValue();
	}
	
	private void thenNumberOfAdditionalToursShouldBeOfCertainValue() {
		SimulationUtils.resetSeed();
		//the following line is just there so that println in getExpectedRecourseCost() are printed before the following lines
		@SuppressWarnings("unused")
		RecourseCost rc = solution.getExpectedRecourseCost();
		System.out.println();
		System.out.println(solution.getAsTupelWithDemand());
		System.out.println(solution.getUseOfCapacityInTours());
		System.out.println(solution.getNumberOfTours());
		System.out.println(solution.getExpectedRecourseCost().getNumberOfAdditionalTours());
		System.out.println(solution.getExpectedRecourseCost().getNumberOfAdditionalTours()/100);
		System.out.println(solution.getExpectedRecourseCost().getNumberOfRouteFailures());
		assertEquals(solution.getExpectedRecourseCost().getNumberOfRouteFailures(),solution.getExpectedRecourseCost().getNumberOfAdditionalTours(),0.00001);		
	}
	
	@Test
	public void testConstructorOfRecourseActionWithOneGotWrtGetNumberOfCustomersServedByNumberOfDifferentTours() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		Parameters.setRelativeStandardDeviationTo(0.15);
//		Parameters.setTestingMode(true);
		Parameters.setMaximalNumberOfToursInGot(2);
		givenSomeGot();
		thenGetNumberOfCustomersServedByNumberOfDifferentToursShouldBeOfCorrectValue();
	}
	
	private void givenSomeGot() throws IOException {
		got = SetUpUtils.getSomeExampleGotFromRC103();
	}
	
	private void thenGetNumberOfCustomersServedByNumberOfDifferentToursShouldBeOfCorrectValue() {
		System.out.println(got.getAsTupel());
		
		for (Customer c : got.getCustomers()) {
			c.setDemand(c.getDemand()*2);
			c.setOriginalDemand(c.getOriginalDemand() * 2);
		}	
		
		int totalNumberOfServedCustomers = 0;
		
		Iterator<Entry<Integer, Integer>> it = got.getExpectedRecourse().getToursNeededToServeNumberOfCustomers().entrySet().iterator();		
		Entry<Integer, Integer> pair = it.next();
		System.out.println("Anzahl Customer, die von " + pair.getKey() + " Vehicles bedient werden: " + pair.getValue());
		assertEquals(true, 1 == pair.getKey());
		assertEquals(true, 5 == pair.getValue());
		totalNumberOfServedCustomers += pair.getValue();

		Entry<Integer, Integer> pair2 = it.next();
		System.out.println("Anzahl Customer, die von " + pair2.getKey() + " Vehicles bedient werden: " + pair2.getValue());
		assertEquals(true, 2 == pair2.getKey());
		assertEquals(true, 7 == pair2.getValue());
		totalNumberOfServedCustomers += pair.getValue();
		
		Entry<Integer, Integer> pair3 = it.next();
		System.out.println("Anzahl Customer, die von " + pair3.getKey() + " Vehicles bedient werden: " + pair3.getValue());
		assertEquals(true, 3 == pair3.getKey());
		assertEquals(true, 1 == pair3.getValue());
		totalNumberOfServedCustomers += pair.getValue();
		
		assertEquals(got.getCustomers().size(), totalNumberOfServedCustomers);
	}

	@Test
	public void testConstructorOfRecourseActionWithMultipleGotsWrtGetNumberOfCustomersServedByNumberOfDifferentTours() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		Parameters.setRelativeStandardDeviationTo(0.15);
//		Parameters.setTestingMode(true);
		Parameters.setMaximalNumberOfToursInGot(2);
		givenListOfTwoGots();
		thenGetNumberOfCustomersServedByNumberOfDifferentToursShouldBeOfCorrectValueForTwoGots();
	}

	private void givenListOfTwoGots() throws IOException {
		givenSomeGot();
		AndGivenAnotherGot();
		gots.add(got);
		gots.add(got2);
	}

	private void AndGivenAnotherGot() throws IOException {
		got2 = SetUpUtils.getGotWithCustomer1And2();
		got2.addTour(SetUpUtils.getTourWithCustomer_42_44_fromRC101());
	}
	
	private void thenGetNumberOfCustomersServedByNumberOfDifferentToursShouldBeOfCorrectValueForTwoGots() {
		for (Customer c : got.getCustomers()) {
			c.setDemand(c.getDemand()*2);
			c.setOriginalDemand(c.getOriginalDemand() * 2);
		}		
		
		for (Customer c : got2.getLastTour().getCustomers()) {
			c.setDemand(c.getDemand()*9);
			c.setOriginalDemand(c.getOriginalDemand() * 9);
		}	
		
		printGetNumberOfCustomersServedByNumberOfDifferentToursForGot(got);
		printGetNumberOfCustomersServedByNumberOfDifferentToursForGot(got2);
		
		//print gots in list
//		for (GroupOfTours got : gots)
//			got.print();
		
		RecourseCost rc = new RecourseCost(gots);
		int totalNumberOfServedCustomers = 0;
		
//		Iterator<Entry<Integer, Integer>> it = rc.getNumberOfCustomersServedByNumberOfDifferentTours().entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<Integer, Integer> pair = it.next();
//			System.out.println("Anzahl Customer, die von " + pair.getKey() + " Vehicles bedient werden: " + pair.getValue());
//		}
		
		Iterator<Entry<Integer, Integer>> it = rc.getToursNeededToServeNumberOfCustomers().entrySet().iterator();
		Entry<Integer, Integer> pair = it.next();
		System.out.println("Anzahl Customer, die von " + pair.getKey() + " Vehicles bedient werden: " + pair.getValue());
		assertEquals(1, pair.getKey(), 0.00001);
		assertEquals(8, pair.getValue(), 0.00001);
		totalNumberOfServedCustomers += pair.getValue(); 
		
		Entry<Integer, Integer> pair2 = it.next();
		System.out.println("Anzahl Customer, die von " + pair2.getKey() + " Vehicles bedient werden: " + pair2.getValue());
		assertEquals(2, pair2.getKey(), 0.00001);
		assertEquals(6, pair2.getValue(), 0.00001);
		totalNumberOfServedCustomers += pair.getValue();

		Entry<Integer, Integer> pair3 = it.next();
		System.out.println("Anzahl Customer, die von " + pair3.getKey() + " Vehicles bedient werden: " + pair3.getValue());
		assertEquals(3, pair3.getKey(), 0.00001);
		assertEquals(3, pair3.getValue(), 0.00001);
		totalNumberOfServedCustomers += pair.getValue();
		
		assertEquals(got.getCustomers().size() + got2.getCustomers().size(), totalNumberOfServedCustomers);
	}

	private void printGetNumberOfCustomersServedByNumberOfDifferentToursForGot(GroupOfTours gotTemp) {
		GotUtils.printCustomerWithDemandAndOriginalDemand(gotTemp);
		
		Iterator<Entry<Integer, Integer>> it = gotTemp.getExpectedRecourse().getToursNeededToServeNumberOfCustomers().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> pair = it.next();
			System.out.println("Anzahl Customer, die von " + pair.getKey() + " Vehicles bedient werden: " + pair.getValue());
		}
	}
	
	@Test
	public void testPercentageOfParcelsDeliverdTheSameDay() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(2);
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 20 30 26 23 18 17 ) ( 99 100 95 98 ) ) ( ( 91 88 84 86 90 ) ( 22 24 27 29 21 ) ) ( ( 76 71 70 73 80 ) ( 93 75 ) ) ( ( 51 50 52 47 43 42 48 ) ( 1 97 92 94 3 ) ) ( ( 83 82 85 79 96 ) ( 66 69 68 65 46 41 ) ) ( ( 25 9 11 8 ) ( 5 2 7 4 89 ) ) ( ( 49 55 57 44 45 ) ( 81 78 77 87 ) ) ( ( 15 13 10 ) ( 6 32 36 34 ) ) ( ( 19 16 14 12 ) ( 31 35 37 38 ) ) ( ( 33 39 28 ) ( 67 63 62 ) ) ( ( 54 53 59 40 ) ( 74 72 61 64 ) ) ( ( 56 58 60 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));
		assertEquals(1.0,solution.getGots().get(0).getExpectedRecourse().getPercentageOfParcelsThatAreCollectedTheSameDay(), 0.0001);
		System.out.println(solution.getCustomerWithNo(74).getDemand());
		System.out.println(solution.getGots().get(solution.getGots().size()-1).getNumberOfParcelsCollectedTheSameDay()*100);
		System.out.println(solution.getGots().get(solution.getGots().size()-1).getExpectedRecourse().getNumberOfParcelsCollectedTheSameDay());
		System.out.println(solution.getGots().get(solution.getGots().size()-1).getExpectedRecourse().getNumberOfParcelsCollectedNextDay());
		System.out.println(solution.getGots().get(solution.getGots().size()-1).getExpectedRecourse().getPercentageOfParcelsThatAreCollectedTheSameDay());
		
//		System.out.println(solution.getGots().get(solution.getGots().size()-2).getExpectedRecourse().getPercentageOfParcelsThatAreCollectedTheSameDay());
	}
	
	@Test
	public void testAverageNumberOfToursPerDriver() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setRecourseActionNumberMinimization(false);
		
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 20 30 26 23 18 17 ) ( 99 100 95 98 ) ) ( ( 91 88 84 86 90 ) ( 22 24 27 29 21 ) ) ( ( 76 71 70 73 80 ) ( 93 75 ) ) ( ( 51 50 52 47 43 42 48 ) ( 1 97 92 94 3 ) ) ( ( 83 82 85 79 96 ) ( 66 69 68 65 46 41 ) ) ( ( 25 9 11 8 ) ( 5 2 7 4 89 ) ) ( ( 49 55 57 44 45 ) ( 81 78 77 87 ) ) ( ( 15 13 10 ) ( 6 32 36 34 ) ) ( ( 19 16 14 12 ) ( 31 35 37 38 ) ) ( ( 33 39 28 ) ( 67 63 62 ) ) ( ( 54 53 59 40 ) ( 74 72 61 64 ) ) ( ( 56 58 60 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));
		assertEquals(1.833333333, solution.getExpectedRecourseCost().getAverageNumberOfToursPerDriver(), 0.00001);

	}

}
