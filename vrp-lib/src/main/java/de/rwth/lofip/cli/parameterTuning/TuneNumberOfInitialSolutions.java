package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneNumberOfInitialSolutions extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void TuneNumberOfInitialSolutions1() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfInitialSolutions(1);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\InitialSolutions1\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneNumberOfInitialSolutions10() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\InitialSolutions10\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneNumberOfInitialSolutions50() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfInitialSolutions(50);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\InitialSolutions50\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneNumberOfInitialSolutions100() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfInitialSolutions(100);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\InitialSolutions100\\");	
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
}
