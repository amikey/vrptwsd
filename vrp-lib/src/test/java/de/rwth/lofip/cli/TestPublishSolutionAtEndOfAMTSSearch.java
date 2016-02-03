//package de.rwth.lofip.cli;
//
//import java.io.IOException;
//
//import org.junit.Test;
//
//import de.rwth.lofip.cli.util.ReadAndWriteUtils;
//import de.rwth.lofip.library.parameters.Parameters;
//
//public class TestPublishSolutionAtEndOfAMTSSearch extends RunAdaptiveMemorySearchWithSolomonInstances {
//	
//	@Test
//	public void TestPublishSolutionsAtEndOfAMTSSearch() throws IOException {
//		Parameters.setAllParametersToMinimalTestingValues();
//		Parameters.setPublishSolutionValueProgress(true);
//		//Set Parameters for Scenario
//		Parameters.setNumberOfNonImprovingIterationsInTS(0);
//		Parameters.setNumberOfNonImprovingAMCalls(1);
//		Parameters.setPercentageOfCapacity(1);
//		Parameters.setNumberOfToursInGot(1);		
//		Parameters.setRunningTimeInHours(6);
//		
//		//Set Output parameters
//		Parameters.setOutputDirectory("\\TestPublishSolutionAtEndOfAMTSAndTS2AndNewRandom\\");
//		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
//		
//		setNumberOfExperimentsTo(1);
//		
//		problems = ReadAndWriteUtils.readModifiedSolomonProblems().subList(0, 1);		
//		processProblems();
//	}
//
//}
