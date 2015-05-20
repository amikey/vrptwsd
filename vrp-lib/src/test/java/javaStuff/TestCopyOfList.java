package javaStuff;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestCopyOfList {
	
	@Test
	public void testCopyOfTest() {
		Tour tour1 = SetUpUtils.getTourWithCustomer2And3();
		Tour tour2 = SetUpUtils.getTourWithCustomer1();
		Tour tour3 = SetUpUtils.getTourWithCustomer4();
		
		List<Tour> tours = new ArrayList<Tour>();
		tours.add(tour1);
		tours.add(tour2);
		tours.add(tour3);
		
		tour1.print();
		
		List<Tour> copiedTours = new ArrayList<Tour>(tours);
		
		tour1.removeCustomerAtPosition(1);
		
		tours.get(0).print();
		copiedTours.get(0).print();
	}

}
