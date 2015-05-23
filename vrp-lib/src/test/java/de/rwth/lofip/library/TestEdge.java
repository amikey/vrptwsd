package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class TestEdge {
	
	@Test
	public void testLengthMethod() {
		Customer c1 = SetUpUtils.getC1();
		Customer c2 = SetUpUtils.getC2();
		Customer c3 = SetUpUtils.getC3();
		Customer c4 = SetUpUtils.getC4();
		AbstractPointInSpace depot = SetUpUtils.getDepot();
//		AbstractPointInSpace depot = new Depot(50.0,40.0);
		
		Edge edge1 = new Edge(depot, c1);
//		assertEquals(56.5685425, edge1.getLength(), 0.001);	
		
		Edge edge2 = new Edge(c1,c2);
		assertEquals(11.180, edge2.getLength(), 0.001);
		
		Edge edge3 = new Edge(c2, c3);
		assertEquals(5, edge3.getLength(), 0.001);
		
		Edge edge4 = new Edge(c3, c4);
		assertEquals(18.02776, edge4.getLength(), 0.001);
		
		Edge edge5 = new Edge(c4, depot);
//		assertEquals(33.541, edge5.getLength(), 0.001);
		
		double distanceTour1234 = edge1.getLength() + edge2.getLength() + edge3.getLength() + edge4.getLength() + edge5.getLength();
		assertEquals(97.250, distanceTour1234, 0.001);
		
		Edge edgeN = new Edge(c1,c4);
		assertEquals(26.925824, edgeN.getLength(), 0.001);
		
		
	}

}
