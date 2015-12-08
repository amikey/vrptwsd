package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;


public class TestSolutionGot {
	
	SolutionGot solution;
	SolutionGot solutionOther;
	
	@Test 
	public void testEqualsMethodWithPositiveOutcome() {
		givenTwoIdenticalSolutionsInTermsOfTours();
		thenSolutionsShouldBeEqual();
	}
	
		private void givenTwoIdenticalSolutionsInTermsOfTours() {
			solution = SetUpUtils.SetUpSolutionWithTwoToursWithOneAndThreeCustomersRespectively();
			solutionOther = SetUpUtils.SetUpSolutionWithTwoToursWithOneAndThreeCustomersOtherWayRound();
		}
	
		private void thenSolutionsShouldBeEqual() {
			assertEquals(true, solution.equals(solutionOther));		
		}
	
	
	@Test 
	public void testEqualsMethodWithNegativeOutcome() {
		givenTwoDifferentSolutionsInTermsOfTours();
		thenSolutionsShouldBeDifferent();
	}

		private void givenTwoDifferentSolutionsInTermsOfTours() {
			solution = SetUpUtils.SetUpSolutionWithTwoToursWithOneAndThreeCustomersRespectively();
			solutionOther = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();		
		}
	
		private void thenSolutionsShouldBeDifferent() {
			assertEquals(false, solution.equals(solutionOther));				
		}
	
	@Test
	public void testRecourseCostOfSolution(){
		givenSolutionWithOneTourWithC1C3C2C4();
		thenRecourseCostShouldBeOfCertainValue();		
	}

		private void givenSolutionWithOneTourWithC1C3C2C4() {
			solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		}
	
		private void thenRecourseCostShouldBeOfCertainValue() {
			assertEquals(0.0,solution.getExpectedRecourseCost().getRecourseCost(),0.0001); 
		}
		
	@Test
	public void printCostOfSomeTours() {
		System.out.println(SetUpUtils.getTourWithCustomers1And2And3().getTotalDistanceWithCostFactor());
		System.out.println(SetUpUtils.getTourWithCustomer4().getTotalDistanceWithCostFactor());
		System.out.println(SetUpUtils.getTourWithCustomers1And2And3().getTotalDistanceWithCostFactor() + SetUpUtils.getTourWithCustomer4().getTotalDistanceWithCostFactor());
	}

}
