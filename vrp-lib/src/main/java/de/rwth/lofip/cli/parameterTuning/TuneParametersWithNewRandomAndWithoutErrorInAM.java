package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;

public class TuneParametersWithNewRandomAndWithoutErrorInAM extends RunAdaptiveMemorySearchWithSolomonInstances {

	@Test
	public void TestPublishSolutionsAtEndOfAMTSSearch() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);		
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\TestPublishSolutionAtEndOfAMTSAndTS2AndNewRandom\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readOriginalSolomonProblems();		
		processProblems();
	}
	
	@Test
	public void TestC104() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);		
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\TestPublishSolutionAtEndOfAMTSAndTS2AndNewRandom\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = new ArrayList<VrpProblem>();
		problems.add(ReadAndWriteUtils.readOriginalSolomonProblems().get(3));		
		processProblems();
	}
	
}
