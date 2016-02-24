package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneListInAM extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void TestLenthOfListInAMWithLenth100() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfToursInAdaptiveMemory(100);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AM100\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TestLenthOfListInAMWithLenth200() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfToursInAdaptiveMemory(200);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AM200\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TestLenthOfListInAMWithLenth500() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfToursInAdaptiveMemory(500);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AM500\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}

	@Test
	public void TestLenthOfListInAMWithLenth1000() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfToursInAdaptiveMemory(1000);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\AM1000\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
}
