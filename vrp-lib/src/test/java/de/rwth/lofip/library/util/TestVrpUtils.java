package de.rwth.lofip.library.util;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.VrpProblem;

public class TestVrpUtils {
	
	private VrpProblem problem;
	
	@Test
	public void testReadPostProblem() throws IOException {
		whenReadingPostProblem();
		ThenRheineProblemShouldBeCorrect();
	}
	
	public void whenReadingPostProblem() throws IOException {
		List<VrpProblem> problems = ReadAndWriteUtils.readPostProblems();
		for (VrpProblem problemTemp : problems)
			if (problemTemp.getDescription().equals("Rheine"))
				problem = problemTemp;
		System.out.println("Eingelesenes Problem ist " + problem.getDescription());
	}
		
	private void ThenRheineProblemShouldBeCorrect() {
		assertEquals("Rheine", problem.getDescription());
		assertEquals(400, problem.getVehicles().iterator().next().getCapacity(), 0.00001);
		assertEquals(4188777.511, problem.getDepot().getxCoordinate(), 0.00001);
		assertEquals(5803406.6, problem.getDepot().getyCoordinate(), 0.00001);
		
		assertEquals(4201607.718, problem.getCustomerWithCustomerNo(710380).getxCoordinate(), 0.000001);
		assertEquals(5800845.94, problem.getCustomerWithCustomerNo(710380).getyCoordinate(), 0.000001);
		assertEquals(40, problem.getCustomerWithCustomerNo(710380).getDemand(), 0.000001);
		assertEquals(55800, problem.getCustomerWithCustomerNo(710380).getTimeWindowOpen(), 0.000001);
		assertEquals(60600, problem.getCustomerWithCustomerNo(710380).getTimeWindowClose(), 0.000001);
		assertEquals(1200, problem.getCustomerWithCustomerNo(710380).getServiceTime(), 0.000001);
		
		assertEquals(3600 + problem.getCustomerWithCustomerNo(710380).getServiceTime(), problem.getCustomerWithCustomerNo(710380).getTimeWindowClose() - problem.getCustomerWithCustomerNo(710380).getTimeWindowOpen(), 0.00001);
	}


	
}
