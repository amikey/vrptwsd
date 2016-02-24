package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneMaximalNumberOfCustomersConsideredInSegment extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment3() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(3);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment3\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment4() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(4);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment4\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment5() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(5);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment5\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment6() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(6);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment6\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}

	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment8() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(8);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment8\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment9() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(9);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment9\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment10() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(10);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment10\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
	
	@Test
	public void TuneMaximalNumberOfCustomersConsideredInSegment11() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(11);
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ParameterTuning\\CustomerInSegment11\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}
}
