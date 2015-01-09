package de.rwth.lofip.library;

import org.junit.Test;
import static org.junit.Assert.*;

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
		solutionOther = SetUpUtils.SetUpSolutionWithTwoToursAndTwoCustomersEach();		
	}

	private void thenSolutionsShouldBeDifferent() {
		assertEquals(false, solution.equals(solutionOther));				
	}

}
