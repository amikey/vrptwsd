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

}
