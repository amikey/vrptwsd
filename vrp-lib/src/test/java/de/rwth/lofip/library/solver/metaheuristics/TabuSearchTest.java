package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.*;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.SolutionElement;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	Tour tour;
	
	@Before
	public void initialise() {
		tour = SetUpUtils.SetUpFeasibleTour();	
	}

	@Test
	public void performTabuSearchTest() {
		TabuSearch tabuSearch = new TabuSearch(tour);
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(1));
		
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(0));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(2));		
		
		tabuSearch.generateNextNeigborhoodStep();
		tabuSearch.generateNextNeigborhoodStep();
		tabuSearch.generateNextNeigborhoodStep();
		assertEquals(tabuSearch.getNeigborhood().toArray()[0], tour.getCustomerAtPosition(1));
		assertEquals(tabuSearch.getNeigborhood().toArray()[1], tour.getCustomerAtPosition(1));				
	}

}
