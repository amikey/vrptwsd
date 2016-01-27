package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneNumberOfNonImprovingIterationsInTS extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void TestNumberOfNonImprovingIterationsInTS0() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingIterationsInTS(0);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\TS0\\");
		Parameters.setPublishSolutionValueProgress(true);
//		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TestNumberOfNonImprovingIterationsInTS20() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingIterationsInTS(20);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\TS20\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TestNumberOfNonImprovingIterationsInTS100() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingIterationsInTS(100);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\TS100\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TestNumberOfNonImprovingIterationsInTS200() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		Parameters.setNumberOfNonImprovingIterationsInTS(200);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\TS200\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}

}
