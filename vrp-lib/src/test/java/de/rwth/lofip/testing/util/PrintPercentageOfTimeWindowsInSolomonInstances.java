package de.rwth.lofip.testing.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.VrpProblem;

public class PrintPercentageOfTimeWindowsInSolomonInstances {

	protected List<VrpProblem> problems = new LinkedList<VrpProblem>();
	
	@Test
	public void percentageOfTimeWindowsInSolomonInstances() throws IOException {
		problems = ReadAndWriteUtils.readOriginalSolomonProblems();
		
		for (VrpProblem problem : problems)
			System.out.println(problem.getDescription() + " & " + problem.getPercentageWithTW() + " & " + problem.getTotalDemandOfAllCustomers() + " \\" + "\\");
	}
	
	@Test
	public void demandInSolomonInstances() throws IOException {
		problems = ReadAndWriteUtils.readOriginalSolomonProblems();
		
		for (VrpProblem problem : problems)
			System.out.println(problem.getDescription() + " & " + problem.getTotalDemandOfAllCustomers() + " \\" + "\\");
	}
	
}
