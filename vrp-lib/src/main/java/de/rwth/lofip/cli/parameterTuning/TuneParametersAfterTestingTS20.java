package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneParametersAfterTestingTS20 extends RunAdaptiveMemorySearchWithSolomonInstances {

	@Test
	public void TestNumberOfNonImprovingIterationsInTS20() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingIterationsInTS(20);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\NachTuningMitTS20\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
}
