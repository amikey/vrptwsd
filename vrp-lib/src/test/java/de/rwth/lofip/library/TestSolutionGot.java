package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.parameters.Parameters;
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
			solution = SetUpUtils.getSolutionWithTwoToursWithOneAndThreeCustomersRespectively();
			solutionOther = SetUpUtils.getSolutionWithTwoToursWithOneAndThreeCustomersOtherWayRound();
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
			solution = SetUpUtils.getSolutionWithTwoToursWithOneAndThreeCustomersRespectively();
			solutionOther = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();		
		}
	
		private void thenSolutionsShouldBeDifferent() {
			assertEquals(false, solution.equals(solutionOther));				
		}
	
	@Test
	public void testRecourseCostOfSolution(){
		Parameters.setAllParametersToDefaultValues();
		Parameters.setRelativeStandardDeviationTo(0.15);
		givenSolutionWithOneTourWithC1C3C2C4();
		thenRecourseCostShouldBeOfCertainValue();		
	}

		private void givenSolutionWithOneTourWithC1C3C2C4() {
			solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		}
	
		private void thenRecourseCostShouldBeOfCertainValue() {
			assertEquals(10.37136,solution.getExpectedRecourseCost().getRecourseCost(),0.0001); 
		}
		
	@Test
	public void printCostOfSomeTours() {
		System.out.println(SetUpUtils.getTourWithCustomers1And2And3().getTotalDistanceWithCostFactor());
		System.out.println(SetUpUtils.getTourWithCustomer4().getTotalDistanceWithCostFactor());
		System.out.println(SetUpUtils.getTourWithCustomers1And2And3().getTotalDistanceWithCostFactor() + SetUpUtils.getTourWithCustomer4().getTotalDistanceWithCostFactor());

	}

}
