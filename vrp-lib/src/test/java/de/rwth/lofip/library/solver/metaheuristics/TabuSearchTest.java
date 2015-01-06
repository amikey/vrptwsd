package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	private GroupOfTours got1;
	private GroupOfTours got2;
	
	@Before
	public void initialise() {
		got1 = SetUpUtils.setUpFeasibleGroupOfToursWithTwoToursEachWithTwoCustomers();
		got2 = SetUpUtils.setUpFeasibleGroupOfToursWithTwoToursEachWithTwoCustomers();
	}

	@Test
	public void performHasNextNeighborhoodStepTest() {
		TabuSearch tabuSearch = new TabuSearch(got1, got2);
		TabuSearch.printNeighborhoodStep();
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		while (tabuSearch.HasNextNeighborhoodStep()) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		};
//		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
//		for (int i = 1; i <=40; i++) {
//			tabuSearch.generateNextNeigborhoodStep();
//			TabuSearch.printNeighborhoodStep();
//		}
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), false);
	}
	
	@Test void performHasNextNeighborhoodStepTestWhereRoutesInGotsAreTheSame() {
		//i.e. tabuSearch is instantiated with two times the same got.
		TabuSearch tabuSearch = new TabuSearch(got1, got1);
		TabuSearch.printNeighborhoodStep();
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		while (tabuSearch.HasNextNeighborhoodStep()) {
			tabuSearch.generateNextNeigborhoodStep();
			TabuSearch.printNeighborhoodStep();
		};
	}
	
//	@Test
//	public void performGenerateNextNeighborhoodStepTest() {
//		TabuSearch tabuSearch = new TabuSearch(tour1, tour2);
//		tabuSearch.generateNextNeigborhoodStep();
//		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(0));
//		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(1));
//		
//		tabuSearch.generateNextNeigborhoodStep();
//		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(0));
//		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(2));		
//				
//		tabuSearch.generateNextNeigborhoodStep();
//		tabuSearch.generateNextNeigborhoodStep();
//		assertEquals(tabuSearch.getNeigborhood().toArray()[2], tour2.getCustomerAtPosition(1));
//		assertEquals(tabuSearch.getNeigborhood().toArray()[3], tour2.getCustomerAtPosition(2));				
//	}
	
}
