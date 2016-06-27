package de.rwth.lofip.library.solver.metaheuristics.NumberRecAct;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestConvexcombinationOfCostAndNumberRecourseActions {
	
	@Test
	public void convexcombinationOfCostAndNumberRecourseActionsTest() throws IOException {
		Parameters.setWeightForConvexcombination(1);		
		SolutionGot solution = SetUpUtils.getSomeSolutionForRC104Problem();
		System.out.println("Solution det cost: " + solution.getTotalDistanceWithCostFactor());
		System.out.println("Solution rec cost: " + solution.getRecourseCost().getRecourseCost());
		System.out.println("Solution #differentToursBasicVehicles: " + solution.getRecourseCost().getNumberOfDifferentToursForBasicVehicles());
		System.out.println("Number of Tours " + solution.getNumberOfTours());
		double recourseCost = solution.getRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(solution);
		System.out.println("Solution convexcombination cost: " + recourseCost);
		
		System.out.println("");
		Parameters.setWeightForConvexcombination(0.1);
		System.out.println("Solution det cost: " + solution.getTotalDistanceWithCostFactor());
		System.out.println("Solution rec cost: " + solution.getRecourseCost().getRecourseCost());
		System.out.println("Solution #differentToursBasicVehicles: " + solution.getRecourseCost().getNumberOfDifferentToursForBasicVehicles());
		System.out.println("Number of Tours " + solution.getNumberOfTours());
		double recourseCost2 = solution.getRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(solution);
		System.out.println("Solution convexcombination cost: " + recourseCost2);	
	}
}
