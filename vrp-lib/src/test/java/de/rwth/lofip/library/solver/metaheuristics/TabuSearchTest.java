package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	private Tour tour1;
	private Tour tour2;
	
	@Before
	public void initialise() {
		tour1 = SetUpUtils.setUpFeasibleTourWithThreeCustomers();
		tour2 = SetUpUtils.setUpFeasibleTourWithThreeCustomers();
	}

	@Test
	public void performHasNextNeighborhoodStepTest() {
		TabuSearch tabuSearch = new TabuSearch(tour1, tour2);
		TabuSearch.printNeighborhoodStep();
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		for (int i = 1; i <=5; i++) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		}
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		for (int i = 1; i <=30; i++) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		}
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), false);
	}
	
	@Test
	public void performGenerateNextNeighborhoodStepTest() {
		TabuSearch tabuSearch = new TabuSearch(tour1, tour2);
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(1));
		
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(2));		
				
		tabuSearch.generateNextNeigborhoodStep();
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(1));
		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(2));				
	}
	
}
