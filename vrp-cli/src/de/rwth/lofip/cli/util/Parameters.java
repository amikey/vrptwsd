package de.rwth.lofip.cli.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.cli.deterministic.DeterministicVrpSolver;
import de.rwth.lofip.cli.got.VrpSolverGot;
import de.rwth.lofip.cli.minimization.MinimizationVrpSolver;
import de.rwth.lofip.cli.normal.VrpSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.util.DistanceComparatorWithSimilarity;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristicGot;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.FeasibilityOrientedRemovalGot;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.SimilarityRemovalGot;

public class Parameters {
	
    protected static VrpConfiguration configuration = new VrpConfiguration();
    protected static FileOutputStream outputStream;
	
	//start heuristic
	private static double weightDistanceVsTw; //Range: [0,1]
	private static double weightSimValVsDistanceAndTw; //Range: [0,1]
	
	//metaheuristic
	private static double minimalPercentageOfRemovedCustomers; //Range: [0,1]
	private static double maximalPercentageOfRemovedCustomers; //Range: [0,1]; maximalPercentage > minimalPercentage
	//private static int numberOfOverallIterations; //{0,...,inf}
	//private static int numberOfImprovementIterations; //{0,...,inf} 
	private static int penaltyFactorForDeviationInNumberOfVehicles; //Range: {1,...,inf}
	//private acceptanceCriteria;
	//private deviationFactorInAcceptanceCriteria;
	
	//adaptive search
	private static double r; //Änderungsgeschwindigkeit der W'keiten für die Auswahl einer Heuristik, Range: [0,1]
	//addedValueGlobalBestSolution > addedValueImprovedSolution > addedValueAcceptedSolution
	private static int addedValueGlobalBestSolution; //Wert, um den o erhöht wird, wenn Heuristik i in globalbestsolution resultiert. Range: {1,...,inf} 
	private static int addedValueImprovedSolution; //Wert, um den o erhöht wird, wenn Heuristik i in improvedsolution resultiert. Range: {1,...,inf}
	private static int addedValueAcceptedSolution; //Wert, um den o erhöht wird, wenn Heuristik i in acceptedsolution resultiert. Range: {1,...,inf}
	
	//seeds
	private static int seedNumberOfRemovedCustomersInMetaheuristic; // Range: {0,...,inf}
	private static int seedWhichHeuristicIsChosen; // Range: {0,...,inf}
	private static int seedRR; // Range: {0,...,inf}
	private static int seedFOR; // Range: {0,...,inf}
	private static int seedSR; // Range: {0,...,inf}
	
	//local variables
	private static int i7;
	private static int i8;	
	private static double sumOfAllSolutions;
	private static int numberOfAllVehicles;

	public static void setParametersInHeuristic() {
		setParametersStartHeuristic();
		setParametersMetaheuristic();
		setParametersAdaptiveSearch();
		setSeeds();
	}
	
	private static void setParametersStartHeuristic() {
    	DistanceComparatorWithSimilarity.setWeightForDistanceAsOpposedToTW(weightDistanceVsTw);
    	DistanceComparatorWithSimilarity.setWeightSimvalAsOpposedToDistanceAndTW(weightSimValVsDistanceAndTw);
	}
	
	private static void setParametersMetaheuristic() {
		LeiEtAlHeuristicGot.setMinimalPercentageOfRemovedCustomers(minimalPercentageOfRemovedCustomers);
		LeiEtAlHeuristicGot.setMaximalPercentageOfRemovedCustomers(maximalPercentageOfRemovedCustomers);	
		//setNumberOfOverallIterations()
		//setNumberOfImprovementIterations()
		LeiEtAlHeuristicGot.setInitialPenaltyCoefficient(penaltyFactorForDeviationInNumberOfVehicles);
		//LeiEtAlHeuristicGot.setHowPenaltyCoefficientIsRecalculated();
		//LeiEtAlHeuristicGot.setAcceptanceCondition();
		//LeiEtAlHeuristicGot.setdefiationFactorInAcceptanceCriteria();
	}
	
	private static void setParametersAdaptiveSearch() {
		LeiEtAlHeuristicGot.setLearningFactorAdaptiveWeight(r);
		LeiEtAlHeuristicGot.setAddedValueGlobalBestSolution(addedValueGlobalBestSolution);
		LeiEtAlHeuristicGot.setAddedValueImprovedSolution(addedValueImprovedSolution);
		LeiEtAlHeuristicGot.setAddedValueAcceptedSolution(addedValueAcceptedSolution);
	}

	private static void setSeeds() {
		LeiEtAlHeuristicGot.setSeedNumberOfRemovedCustomersInMetaheuristic(seedNumberOfRemovedCustomersInMetaheuristic);
		LeiEtAlHeuristicGot.setSeedWhichHeuristicIsChosen(seedWhichHeuristicIsChosen);
		RandomRemoval.setSeed(seedRR);
		FeasibilityOrientedRemovalGot.setSeed(seedFOR);
		SimilarityRemovalGot.setSeed(seedSR);	
	}

	public static void generateParametersAndSolveProblems() throws IOException {
		setOutputStream();
		
		for (int i1 = 0; i1 <= 10; i1++) 
		{
			weightDistanceVsTw = 0.1 * i1;			
			for (int i2 = 0; i2 <= 10; i2++)
			{
				weightSimValVsDistanceAndTw = 0.1 * i2;
				for (int i3 = 1; i3 <= 10; i3++)
				{
					minimalPercentageOfRemovedCustomers = 0.1 * i3;
					for (int i4 = i3; i4 <= 10; i4++)
					{
						maximalPercentageOfRemovedCustomers = 0.1 * i4;
						for (int i5 = 0; i5 <= 100; i5++)
						{
							penaltyFactorForDeviationInNumberOfVehicles = i5;
							for (int i6 = 1; i6 <= 10; i6++)
							{
								r = 0.1 * i6;
								for (i7 = 1; i7 <= 10; i7++)
								{
									addedValueAcceptedSolution = i7 * 10;
									for (i8 = i7; i8 <= 20; i8++)
									{
										addedValueImprovedSolution = i8 * 10;
										for (int i9 = i8; i9 <= 60; i9++)
										{
											addedValueGlobalBestSolution = i9 * 10;
											for (int i10 = 1; i10 <= 10; i10++)
											{
												seedNumberOfRemovedCustomersInMetaheuristic = i10;
												for (int i11 = 1; i11 <= 10; i11++)
												{
													seedWhichHeuristicIsChosen = i11;
													for (int i12 = 1; i12 <= 10; i12++)
													{
														seedFOR = i12;
														for (int i13 = 1; i13 <= 10; i13++)
														{
															seedRR = i13;
															for (int i14 = 1; i14 <= 10; i14++)
															{
																seedSR = i14;
															
																setParametersInHeuristic();	
																solveProblems();
																printParametercombinationAndSumAndNumberOfVehicles();
																//saveParametercombinationAndSumAndNumberOfVehiclesInList();
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//sortList();
		//printListAgain();	
		
		closeOutputStream();
	}

	private static void printParametercombinationAndSumAndNumberOfVehicles() throws IOException {
		IOUtils.write(getParametersAsString(), outputStream);
		IOUtils.write("sumOfAllSolutions: " + sumOfAllSolutions + "; ", outputStream);
		IOUtils.write("numberOfAllVehicles: " + numberOfAllVehicles + "\n\n", outputStream);
	}

	private static String getParametersAsString() {
		String s = "";
		s += "weightDistanceVsTw: " + weightDistanceVsTw + "\n";
		s += "weightSimValVsDistanceAndTw: " + weightSimValVsDistanceAndTw + "\n";
		s += "minimalPercentageOfRemovedCustomers: " + minimalPercentageOfRemovedCustomers + "\n";
		s += "maximalPercentageOfRemovedCustomers: " + maximalPercentageOfRemovedCustomers + "\n";
		s += "penaltyFactorForDeviationInNumberOfVehicles: " + penaltyFactorForDeviationInNumberOfVehicles + "\n";
		s += "r: " + r + "\n";
		s += "addedValueGlobalBestSolution: " + addedValueGlobalBestSolution + "\n";
		s += "addedValueImprovedSolution: " + addedValueImprovedSolution + "\n";
		s += "addedValueAcceptedSolution: " + addedValueAcceptedSolution + "\n";
		s += "seedNumberOfRemovedCustomersInMetaheuristic: " + seedNumberOfRemovedCustomersInMetaheuristic + "\n";
		s += "seedWhichHeuristicIsChosen: " + seedWhichHeuristicIsChosen + "\n";
		s += "seedRR: " + seedRR + "\n";
		s += "seedFOR: " + seedFOR + "\n";
		s += "seedSR: " + seedSR + "\n";
		return s;
	}

	private static void solveProblems() {
		MinimizationVrpSolver s = new MinimizationVrpSolver();
        try {
			s.calculateAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sumOfAllSolutions = s.getSumOfAllSolutions();
        numberOfAllVehicles = s.getNumberOfAllVehicles();
	}
	
	private static void setOutputStream() throws IOException {
        if (StringUtils.isNotEmpty(configuration.getConfigurationValueString("outputDirectory"))) {
            File outputFile = new File(
                    configuration.getConfigurationValueString("outputDirectory")
                            + "Parameter_mit_Solutions" + "-"
                            + new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime()));
            outputStream = FileUtils.openOutputStream(outputFile, true);
        } else {
        	throw new RuntimeException("Output-Verzeichnis nicht angegeben");
        }
	}
	
	private static void closeOutputStream() {
		IOUtils.closeQuietly(outputStream);
	}

	
}
