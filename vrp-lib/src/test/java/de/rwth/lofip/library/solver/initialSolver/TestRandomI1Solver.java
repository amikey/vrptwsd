package de.rwth.lofip.library.solver.initialSolver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.util.TestUtils;

public class TestRandomI1Solver {
	
	private RandomI1Solver solver = new RandomI1Solver();
	
	@Test
	public void testRandomI1Solver() throws IOException {
		solver.initialiseSolverWith(TestUtils.readSolomonProblemC101().get(0));
				
		List<Customer> seedCustomers = solver.randomlySelectSeedCustomers();		
		assertEquals(solver.getNumberOfSeedCustomers(), seedCustomers.size());
		
		assertEquals(solver.getNumberOfSeedCustomers(), solver.constructToursWithSeedCustomers().getNumberOfTours());
		
		solver.insertRemainingCustomersIntoTours();
		
		assertEquals(100, solver.getSolution().getCustomersInTours().size());
	}
	
}
