package de.rwth.lofip.library.solver.metaheuristics.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestTourMatching {
	
	SolutionGot solution;
	
	@Test
	public void testToursThatAreUsedInRecourseCostAreAlreadyAssignedToGots() {
		TourMatching tm = new TourMatching();
		List<Integer> usedIndizes = new ArrayList<Integer>();
		usedIndizes.add(1);
		usedIndizes.add(2);
		usedIndizes.add(3);
		tm.setListOfUsedIndizesForTesting(usedIndizes);
		
		RecourseCost rc = new RecourseCost(0, 0, 0, 0);
		rc.addTourIndex(1);
		
		RecourseCost rc2 = new RecourseCost(0, 0, 0, 0);
		
		assertEquals(true, tm.toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots(rc));		
		assertEquals(false, tm.toursThatAreUsedInRecourseCostAreAlreadyAssignedToGots(rc2));
	}
	
	@Test 
	public void testMatchToursToGots() throws IOException {
		Parameters.setAllParametersToDefaultValues();
//		Parameters.setTestingMode(true);
		Parameters.setMaximalNumberOfToursInGot(2);
		
		givenASolutionWithGots();
		System.out.println("Solution vor Matching Tours: ");
		solution.printSolutionAsTupel();
		System.out.println("Recourse Cost: " + solution.getExpectedRecourseCost().getRecourseCost());
		System.out.println("");
		
		whenMatchingToursWithConvexcombination();
		solution.printSolutionAsTupel();
		System.out.println("\n");
		System.out.println("Recourse Cost: " + solution.getExpectedRecourseCost().getRecourseCost());
		assertEquals(12, solution.getGots().size());
		
		whenMatchingToursOnlyWithRecourseCost();
		solution.printSolutionAsTupel();
		System.out.println("\n");
		System.out.println("Recourse Cost: " + solution.getExpectedRecourseCost().getRecourseCost());
		assertEquals(6, solution.getGots().size());
		
		Parameters.setAllParametersToDefaultValues();
	}

	private void givenASolutionWithGots() throws IOException {
		solution = SetUpUtils.getSomeSolutionForRC104Problem();		
	}
	
	private void whenMatchingToursWithConvexcombination() throws IOException {
		solution = new TourMatching().matchToursToGots(solution);		
	}
	
	private void whenMatchingToursOnlyWithRecourseCost() throws IOException {
		TourMatching tm = new TourMatching(){
				@Override
				protected void sortListOfRecourseCostsAccordingToConvexCombinationOfRecourseCostAndNumberOfRecourseActions() {		
					Comparator<RecourseCost> byRecourseCost = (e1,e2) -> Double.compare(e1.getRecourseCost(),e2.getRecourseCost());		
					Collections.sort(listOfRecourseCosts, byRecourseCost);						
				}
				@Override
				protected SolutionGot returnEitherNewOrOldSolutionDependingOnWhichHasLessCost() {
					if (newSolution.getTotalDistanceWithCostFactorAndRecourse() <= 
							oldSolution.getTotalDistanceWithCostFactorAndRecourse())	
						return newSolution;
					else
						return oldSolution;
				}
			};
		solution = tm.matchToursToGots(solution);
	}
	

}
