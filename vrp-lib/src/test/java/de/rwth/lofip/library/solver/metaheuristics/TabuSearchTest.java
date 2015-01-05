package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	Tour tour;
	
	@Before
	public void initialise() {
		tour = SetUpUtils.setUpFeasibleTourWithFourCustomers();	
	}

	@Test
	public void performGenerateNextNeighborhoodStepTest() {
		TabuSearch tabuSearch = new TabuSearch(tour);
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(1));
		
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(2));		
				
		tabuSearch.generateNextNeigborhoodStep();
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(1));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(1));				
	}
	
	@Test
	public void performHasNextNeighborhoodStepTest() {
		TabuSearch tabuSearch = new TabuSearch(tour);
		TabuSearch.printNeighborhoodStep();
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		for (int i = 1; i <=5; i++) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		}
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		for (int i = 1; i <=4; i++) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		}
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), false);
	}

}
