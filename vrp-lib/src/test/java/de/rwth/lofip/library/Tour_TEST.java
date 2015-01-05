package de.rwth.lofip.library;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class Tour_TEST {
	
	Tour tour;
	
	@Test 
	public void TestThatTourIsExecutedTheSameDay() {
		givenTourThatIsExecutedTheSameDay();
		
		ThenTourShouldBeExecutedTheSameDay();
	}

	private void givenTourThatIsExecutedTheSameDay() {
		tour = SetUpUtils.setUpFeasibleTourWithFourCustomers();		
	}
	
	private void ThenTourShouldBeExecutedTheSameDay() {
		assertTrue(tour.isTourExecutedTheSameDay());
	}
	
	
	
	@Test
	public void TestThatTourIsExecutedTheNextDay() {
		givenTourThatIsExcecutedTheNextDay();
		
		ThenTourShouldBeExcecutedTheNextDay();
	}

	private void givenTourThatIsExcecutedTheNextDay() {
		tour = SetUpUtils.SetUpTourThatIsExecutedTheNextDay();
	}
	
	private void ThenTourShouldBeExcecutedTheNextDay() {
		assertFalse(tour.isTourExecutedTheSameDay());
	}

}
