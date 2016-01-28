package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneNumberOfNonImprovingAMcalls extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void TuneNumberOfNonImprovingAMCalls0() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingAMCalls(0);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AMCalls0\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneNumberOfNonImprovingAMCalls20() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingAMCalls(0);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AMCalls20\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}

	@Test
	public void TuneNumberOfNonImprovingAMCalls100() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingAMCalls(0);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AMCalls100\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneNumberOfNonImprovingAMCalls200() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingAMCalls(0);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AMCalls200\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
}
