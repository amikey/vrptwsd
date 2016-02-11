package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.VrpUtils;

public class AnalyseIstSituation extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void HundredPercentOfCapacityOnC1R1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void HundredPercentOfCapacityOnC2R2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void HundredPercentOfCapacityOnRC1RC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\RC1CRC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}

	private void setParameters() {
		//Set Parameters for Scenario
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
	}

}
