package de.rwth.lofip.library;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;

public class TestVrpProblem {
	
	@Test
	public void testMinimalNumberOfVehiclesInVrpProblem() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemC101().get(0);
		System.out.println(problem.getMinimalNumberOfVehiclesWrtDemand());
	}
	

}
