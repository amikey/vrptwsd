package de.rwth.lofip.cli.SchwankungsbreiteMonteCarlo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization.AMTSwithRecourseAndRecourseActionNumber;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.VrpUtils;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class AnalyseSchwankungsbreiteMonteCarloSimulation {
	
	private List<VrpProblem> problems = new ArrayList<VrpProblem>();
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();
		
	@Test
	public void analysiereSchwankungsbreiteMonteCarloSimulation() throws IOException {
		throw new RuntimeException("Achtung, wenn ich das verwenden will muss ich in Recourse Cost den Befehl 'SimulationUtils.resetSeed();' auskommentieren");
		
		Parameters.setNumberOfDemandScenarioRuns(2000);
		
		Parameters.setAlParametersToValuesForAuswertung();
		Parameters.setWeightForConvexcombination(0.1);
		Parameters.setMaximalNumberOfToursInGot(2);
		VrpProblem problem = ReadAndWriteUtils.readEigeneModifiedR1SolomonProblems().get(1);

//		R101; 90% Auslastung: SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 28 21 73 41 56 4 25 ) ( 29 81 34 35 77 ) ) ( ( 27 52 18 6 ) ( 99 94 96 13 ) ) ( ( 45 82 7 8 46 17 93 ) ( 69 30 51 66 1 ) ) ( ( 39 67 55 24 80 ) ( 12 76 79 26 ) ) ( ( 36 47 19 10 ) ( 5 83 84 60 89 ) ) ( ( 98 61 85 37 ) ( 16 86 91 100 ) ) ( ( 59 95 40 53 ) ( 72 23 54 ) ) ( ( 65 71 9 50 ) ) ( ( 33 78 3 68 ) ( 92 42 15 87 57 97 ) ) ( ( 62 11 90 20 32 70 ) ) ( ( 63 64 49 ) ) ( ( 2 75 22 74 58 ) ( 31 88 48 ) ) ( ( 14 44 38 43 ) )", problem);
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 92 98 16 86 17 ) ( 5 83 61 84 96 ) ) ( ( 23 67 55 25 ) ( 69 30 51 20 32 70 ) ) ( ( 27 31 88 7 10 ) ( 36 47 19 8 46 60 89 ) ) ( ( 2 73 22 74 58 ) ( 12 76 79 50 ) ) ( ( 65 71 9 35 77 ) ( 64 49 48 ) ) ( ( 63 62 11 90 66 1 ) ( 45 82 18 6 13 ) ) ( ( 33 81 3 54 24 80 ) ( 39 75 41 56 4 ) ) ( ( 28 29 78 34 68 ) ( 72 21 40 53 26 ) ) ( ( 59 95 94 ) ( 52 99 85 37 ) ) ( ( 42 15 87 57 97 93 ) ( 14 44 38 43 91 100 ) )", problem); 
//		R101; 80% Auslastung: SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 39 75 41 74 ) ) ( ( 63 64 49 89 ) ( 92 16 86 37 ) ) ( ( 5 99 94 ) ( 36 47 19 8 60 ) ) ( ( 62 11 90 10 1 ) ( 42 15 57 97 13 ) ) ( ( 28 12 54 24 80 ) ( 14 44 38 43 ) ) ( ( 33 81 34 35 70 ) ( 27 52 88 7 46 17 93 ) ) ( ( 59 95 6 96 ) ( 69 51 9 20 32 ) ) ( ( 72 2 87 ) ( 76 79 3 77 ) ) ( ( 65 71 66 ) ( 40 53 26 58 ) ) ( ( 31 30 50 ) ) ( ( 98 61 85 ) ( 82 48 ) ) ( ( 23 67 55 25 ) ( 45 83 18 84 91 100 ) ) ( ( 21 73 22 56 4 ) ( 29 78 68 ) )", problem);
		
//		RC101: SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 23 21 19 18 48 24 ) ( 95 62 67 71 94 96 54 68 ) ) ( ( 5 45 2 7 8 3 1 ) ( 65 52 99 57 86 74 25 ) ) ( ( 98 73 79 6 46 4 100 ) ( 82 11 9 10 55 ) ) ( ( 83 64 51 85 84 56 91 80 ) ( 92 33 30 28 26 34 32 93 ) ) ( ( 63 76 49 22 20 66 ) ( 14 47 12 15 16 17 13 ) ) ( ( 72 39 36 35 37 70 ) ( 61 81 90 ) ) ( ( 42 44 40 38 41 43 ) ( 59 75 87 97 58 77 ) ) ( ( 69 88 53 78 60 ) ( 27 29 31 50 89 ) )", problem);
		
		String s1 = ""
		+ "RecourseCost" +";"
		+ "RecourseCostOnlyAdditionalTours" +";"
		+ "GrundkostenMitVerschiedenenTouren" +";"
		+ "Distanz+RecourseCost" + ";"
		+ "ConvenxkombinationRecourseCost+#RecourseActions" + ";"
		+ "Distanz+Convexkombination" + ";"
		+ "NumberOfRouteFailures" + ";"
		+ "NumberOfAdditionalTours" + ";"
		+ "NumberOfAdditionalToursPerDay" + ";"
		+ "NumberOfDifferentToursBasicVehicles" + ";"
		+ "NumberOfDifferentToursPerBasicVehicle";
		System.out.println(s1);		
				
		long startTime = System.currentTimeMillis();
		
		for (int i = 1; i <= 100000; i+=5000) {
			SimulationUtils.setSeed(i);
			
			solution.resetRecourseCost();
			RecourseCost rc = solution.getRecourseCost();
			
			//berechne GrundkostenMitVerschiedenenTouren
			double basicCostWithDifferentTours = solution.getTotalDistanceWithCostFactor() + ((SolutionGot) solution).getExpectedRecourseCost().getRecourseCost()
													- solution.getExpectedRecourseCost().getRecourseCostOnlyAdditionalTours();		
			
			//berechne Auslastung aller KFZ
			double allDemand = solution.getVrpProblem().getTotalDemandOfAllCustomers() * Parameters.getNumberOfDemandScenarioRuns();
			int alleKFZBasistouren = solution.getNumberOfTours() * Parameters.getNumberOfDemandScenarioRuns();
			double KFZZusatzfahrten = solution.getExpectedRecourseCost().getNumberOfAdditionalTours();
			double Fahrzeugkapazitaet = solution.getVrpProblem().getOriginalCapacity();
			double auslastungAllerKFZ = allDemand / ((alleKFZBasistouren + KFZZusatzfahrten) * Fahrzeugkapazitaet);
			
			String s = ""
			+ String.format("%.3f",rc.getRecourseCost()) + ";"
			+ String.format("%.3f",rc.getRecourseCostOnlyAdditionalTours()) + ";"
			+ String.format("%.3f",basicCostWithDifferentTours) + ";"
			+ String.format("%.3f",solution.getTotalDistanceWithCostFactor() + rc.getRecourseCost()) + ";"
			+ String.format("%.3f",rc.getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(solution)) + ";"
			+ String.format("%.3f",solution.getTotalDistanceWithCostFactor() + rc.getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(solution)) + ";"
			+ rc.getNumberOfRouteFailures() + ";"
			+ String.format("%.3f",rc.getNumberOfAdditionalTours()) + ";"
			//numberOfAdditionalToursPerDay
			+ String.format("%.3f",rc.getNumberOfAdditionalTours() / Parameters.getNumberOfDemandScenarioRuns()) + ";"
			//numberOfDifferentToursForBasicVehicles
			+ String.format("%.3f", rc.getNumberOfDifferentToursForBasicVehicles()) + ";" 
			+ String.format("%.3f",rc.getNumberOfDifferentToursForBasicVehicles()/solution.getNumberOfTours());
			
			System.out.println(s);
			
		}
		
		long endTime = System.currentTimeMillis();
		long timeNeededMS = endTime - startTime;
		int numberOfSimulationRuns = 20;
		long timeNeededSeconds = timeNeededMS/20;
		
		System.out.println("Time needed in Milliseconds per Simulation Run: " + timeNeededSeconds);
		
	}
	
	@Test
	public void testRuntimeAndSolutionQualityOnRC2Instances() throws IOException {
		setParameters();
		Parameters.setNumberOfDemandScenarioRuns(30);
		Parameters.setWeightForConvexcombination(0);
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory("\\Ab20160720\\AnalyseSchwankungsbreiteMonteCarloSimulation\\RC2\\30simulierteTage\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC2SolomonProblems();
				
		solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo();	
	}
	
	private void setParameters() {
		//Set Parameters for Scenario
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);		
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
	}
	
	private void solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AMTSwithRecourseAndRecourseActionNumber adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
			SolutionGot solution = adaptiveMemoryTabuSearch.solveWithNewAlgo(problem);
			solutions.add(solution);
			
			postProcessSolutionsWith5000SimulatedDays();
		}		
	}


	private void postProcessSolutionsWith5000SimulatedDays() throws IOException {		
		Parameters.setNumberOfDemandScenarioRuns(5000);
		IOUtils.write("\n\n Assessed mit 5000 simulierten Tagen: \n\n"
				, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfAMTSSearch());
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();		
				
		for (SolutionGot sol : solutions) {
			sol.resetRecourseCost();
			ReadAndWriteUtils.publishSolutionAtEndOfAMTSSearch(sol, 0000);
		}	
	}
	


		
	
	

}
