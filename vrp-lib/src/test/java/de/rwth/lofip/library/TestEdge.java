package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class TestEdge {
	
	@Test
	public void testLengthMethod() {
		Customer c1 = SetUpUtils.getC1();
		Customer c3 = SetUpUtils.getC3();
		Customer c4 = SetUpUtils.getC4();
		AbstractPointInSpace depot = new Depot(50.0,40.0);
		
		Edge edge = new Edge(c1,c4);
		assertEquals(26.925824, edge.getLength(), 0.001);
		
		Edge edge2 = new Edge(depot, c1);
		assertEquals(56.5685425, edge2.getLength(), 0.001);
		
		Edge edge3 = new Edge(c3, c4);
		assertEquals(18.02776, edge3.getLength(), 0.001);					
	}

}
