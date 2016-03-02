package de.rwth.lofip.testing.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;

public class PrintDifferentRecourseActions {
	
	@Test
	public void TestPrintDifferentRecourseActions() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setRecourseActionNumberMinimization(true);
		
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 69 68 40 44 45 42 41 ) ( 65 53 58 60 ) ) ( ( 28 26 23 18 19 10 ) ( 20 6 32 34 ) ) ( ( 91 84 83 78 77 87 ) ( 99 100 97 92 ) ) ( ( 5 94 95 98 3 4 ) ( 25 8 21 ) ) ( ( 54 56 59 ) ( 24 29 33 31 ) ) ( ( 35 37 38 39 36 ) ( 93 88 86 90 ) ) ( ( 75 2 1 7 89 ) ( 22 27 30 51 50 52 47 48 ) ) ( ( 74 46 43 ) ( 15 17 13 ) ) ( ( 82 85 76 73 80 96 ) ( 16 14 12 9 11 ) ) ( ( 72 61 64 49 55 57 ) ( 71 70 79 81 ) ) ( ( 67 63 62 66 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));

		for (GroupOfTours got : solution.getGots()) {
			System.out.println(got.getAsTupel());
			System.out.println(got.getExpectedRecourse().getDifferentRecourseActionsAsString());
			System.out.println(got.getExpectedRecourse().getDifferentRecourseActions().size());
		}
		
		System.out.println(solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + solution.getGots().size());
	}

	@Test
	public void TestPrintDifferentRecourseActionsIstSituation() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setRecourseActionNumberMinimization(false);
		
//		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 20 30 26 23 18 17 ) ) ( ( 91 88 84 86 90 ) ) ( ( 56 58 60 ) ) ( ( 99 100 95 98 ) ) ( ( 22 24 27 29 21 ) ) ( ( 76 71 70 73 80 ) ) ( ( 54 53 59 40 ) ) ( ( 19 16 14 12 ) ) ( ( 51 50 52 47 43 42 48 ) ) ( ( 15 13 10 ) ) ( ( 31 35 37 38 ) ) ( ( 74 72 61 64 ) ) ( ( 83 82 85 79 96 ) ) ( ( 49 55 57 44 45 ) ) ( ( 81 78 77 87 ) ) ( ( 1 97 92 94 3 ) ) ( ( 33 39 28 ) ) ( ( 25 9 11 8 ) ) ( ( 5 2 7 4 89 ) ) ( ( 66 69 68 65 46 41 ) ) ( ( 6 32 36 34 ) ) ( ( 67 63 62 ) ) ( ( 93 75 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 20 30 26 23 18 17 ) ) ( ( 99 100 95 98 ) ) ( ( 91 88 84 86 90 ) ) ( ( 22 24 27 29 21 ) ) ( ( 76 71 70 73 80 ) ) ( ( 93 75 ) ) ( ( 51 50 52 47 43 42 48 ) ) ( ( 1 97 92 94 3 ) ) ( ( 83 82 85 79 96 ) ) ( ( 66 69 68 65 46 41 ) ) ( ( 25 9 11 8 ) ) ( ( 5 2 7 4 89 ) ) ( ( 49 55 57 44 45 ) ) ( ( 81 78 77 87 ) ) ( ( 15 13 10 ) ) ( ( 6 32 36 34 ) ) ( ( 19 16 14 12 ) ) ( ( 31 35 37 38 ) ) ( ( 33 39 28 ) ) ( ( 67 63 62 ) ) ( ( 54 53 59 40 ) ) ( ( 74 72 61 64 ) ) ( ( 56 58 60 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));
		
		for (GroupOfTours got : solution.getGots()) {
			System.out.println(got.getAsTupel());
			System.out.print(got.getExpectedRecourse().getDifferentRecourseActionsAsString());
			System.out.println(got.getExpectedRecourse().getDifferentRecourseActions().size() + "\n");
		}
		
		System.out.println(solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + solution.getGots().size()/2);
		System.out.println((solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + solution.getGots().size()/2) / (solution.getGots().size()/2));
		System.out.println(solution.getExpectedRecourseCost().getRecourseCost());
		System.out.println(solution.getTotalDistanceWithCostFactorAndRecourse());
	}
	 
	@Test
	public void TestPrintDifferentRecourseActionsIstSituationMitMatching() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setRecourseActionNumberMinimization(true);
		
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 20 30 26 23 18 17 ) ) ( ( 91 88 84 86 90 ) ) ( ( 56 58 60 ) ) ( ( 99 100 95 98 ) ) ( ( 22 24 27 29 21 ) ) ( ( 76 71 70 73 80 ) ) ( ( 54 53 59 40 ) ) ( ( 19 16 14 12 ) ) ( ( 51 50 52 47 43 42 48 ) ) ( ( 15 13 10 ) ) ( ( 31 35 37 38 ) ) ( ( 74 72 61 64 ) ) ( ( 83 82 85 79 96 ) ) ( ( 49 55 57 44 45 ) ) ( ( 81 78 77 87 ) ) ( ( 1 97 92 94 3 ) ) ( ( 33 39 28 ) ) ( ( 25 9 11 8 ) ) ( ( 5 2 7 4 89 ) ) ( ( 66 69 68 65 46 41 ) ) ( ( 6 32 36 34 ) ) ( ( 67 63 62 ) ) ( ( 93 75 ) ) ", ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems().get(0));
		TourMatching tm = new TourMatching();
		Parameters.setMaximalNumberOfToursInGot(2);
		solution = tm.matchToursToGots(solution);
		solution.printSolutionAsTupel();

		for (GroupOfTours got : solution.getGots()) {
			System.out.println(got.getAsTupel());
			System.out.print(got.getExpectedRecourse().getDifferentRecourseActionsAsString());
			System.out.println(got.getExpectedRecourse().getDifferentRecourseActions().size());
			System.out.println(got.getExpectedRecourse().getNumberOfRouteFailures() + "\n");
		}
		
		System.out.println(solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + solution.getGots().size());
		System.out.println((solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + solution.getGots().size()) / solution.getGots().size());
		System.out.println(solution.getExpectedRecourseCost().getRecourseCost());
		System.out.println(solution.getTotalDistanceWithCostFactorAndRecourse());
		
		//prüfe getAverageNumberOfToursPerDriver() in Recourse Cost
		assertEquals(1.833333333, solution.getExpectedRecourseCost().getAverageNumberOfToursPerDriver(), 0.00001);
		
	}
	
	

}
