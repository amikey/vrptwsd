package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	
	private GroupOfTours gotWithTwoTours;
	private GroupOfTours got2;
	private GroupOfTours gotWithThreeTours;
	
	@Before
	public void initialise() {
//		gotWithTwoTours = SetUpUtils.setUpFeasibleGroupOfToursWithTwoToursEachWithTwoCustomers();
//		got2 = SetUpUtils.setUpFeasibleGroupOfToursWithTwoToursEachWithTwoCustomers();
		gotWithThreeTours = SetUpUtils.setUpFeasibleGroupOfToursWithThreeToursEachWithTwoCustomers();
		
		createLoggingFileForLog4J2();
	}
	
	private static void createLoggingFileForLog4J2() {
		String logFilename = "C:/Users/Andreas/Dropbox/Uni/Diss/Code/logging/" + "Log - "        
                + sdf.format(Calendar.getInstance().getTime()) + ".log";
		File outputFile = new File(logFilename);		
		System.setProperty("logFilename", logFilename);	  
	}

//	@Test
//	public void performHasNextNeighborhoodStepTest() {
//		TabuSearch tabuSearch = new TabuSearch(got1, got2);
//		TabuSearch.printNeighborhoodStep();
//		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
//		while (tabuSearch.HasNextNeighborhoodStep()) {
//			tabuSearch.generateNextNeigborhoodStep();
//			TabuSearch.printNeighborhoodStep();
//		};
////		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
////		for (int i = 1; i <=40; i++) {
////			tabuSearch.generateNextNeigborhoodStep();
////			TabuSearch.printNeighborhoodStep();
////		}
//		assertEquals(tabuSearch.HasNextNeighborhoodStep(), false);
//	}
	
	@Test
	public void performHasNextNeighborhoodStepTestWhereRoutesInGotsAreTheSame() {
		//i.e. tabuSearch is instantiated with two times the same got.
		TabuSearch tabuSearch = new TabuSearch(gotWithThreeTours, gotWithThreeTours);
		TabuSearch.printNeighborhoodStep();
		assertEquals(tabuSearch.HasNextNeighborhoodStep(), true);
		while (tabuSearch.HasNextNeighborhoodStep()) {
			tabuSearch.generateNextNeighborhoodStep();
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
