package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestTourGetDistanceBetweenTwoPositions {
	
	Tour tour; 
	
	@Test 
	public void testGetDistanceBetweenTwoPositions() {
		tour = SetUpUtils.getTourWithCustomers1And2And3();

		double distance = 0;
		distance += new Edge(SetUpUtils.getC1(), SetUpUtils.getC2()).getLength();
		assertEquals(distance,tour.getDistanceForSegmentBetweenPositions(0,2),0.0001);
		

		distance += new Edge(SetUpUtils.getC2(), SetUpUtils.getC3()).getLength();
		assertEquals(distance,tour.getDistanceForSegmentBetweenPositions(0,3),0.0001);		
	}

}
