package de.rwth.lofip.stuffNotNeededRightNow.simulation.repair.util;

import java.util.List;

public class RepairedSolutionUtils {
	
	static void printAllSolutionsInRepairedSolutionList(List<RepairedSolution> list) {
		String s = "Solutions in RepairedSolutionList: \n";
		for (RepairedSolution rs : list)
		{
			s += rs.getNewSolution().getSolutionAsTupel();
			s += "\n";
		}
		System.out.println(s);
	}	

}
