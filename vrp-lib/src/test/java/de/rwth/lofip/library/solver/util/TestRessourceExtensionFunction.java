package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.solver.util.TourUtils;

public class TestRessourceExtensionFunction {
	
	@Test
	public void testThatConcatenatingEmptyRefAndAnotherRefAreFeasible() {
		ResourceExtensionFunction emptyRef = new ResourceExtensionFunction(); 
		ResourceExtensionFunction anotherRef = new ResourceExtensionFunction(195.81138830084188, 345.0, 425.0,20);
		
		assertEquals(true, TourUtils.isConcatenationOfRefsTWFeasible(emptyRef,anotherRef));
	}

}
