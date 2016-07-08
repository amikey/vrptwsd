package de.rwth.lofip.cli.SchwankungsbreiteMonteCarlo;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.monteCarloSimulation.SimulationUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class AnalyseSchwankungsbreiteMonteCarloSimulation {
	
	@Test
	public void analysiereSchwankungsbreiteMonteCarloSimulation() throws IOException {
		Parameters.setWeightForConvexcombination(0.1);
		Parameters.setMaximalNumberOfToursInGot(2);
		VrpProblem problem = ReadAndWriteUtils.readEigeneModifiedRC1SolomonProblems().get(2);
		SolutionGot solution = SetUpSolutionFromString.SetUpSolution("( ( 1 3 45 5 8 46 4 100 ) ( 65 52 99 87 59 97 75 58 ) ) ( ( 83 64 51 84 56 66 91 80 ) ( 82 11 10 13 16 17 90 ) ) ( ( 33 27 30 32 28 26 29 71 96 ) ( 69 88 53 78 60 55 ) ) ( ( 85 63 89 76 19 25 77 ) ( 61 42 44 43 40 35 37 72 54 ) ) ( ( 92 95 62 67 50 31 34 93 94 ) ( 12 14 47 15 9 74 86 57 ) ) ( ( 81 39 36 38 41 68 ) ( 98 73 79 7 6 2 70 ) ) ( ( 18 48 21 23 49 22 20 24 ) ) ", problem);
		
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
		+ "NumberOfDifferentRecourseActions";
		System.out.println(s1);		
		
		for (int i = 1; i <= 100; i++) {
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
			+ String.format("%.3f",rc.getNumberOfDifferentRecourseActions());
			
			System.out.println(s);
			
		}
		
	}

}
