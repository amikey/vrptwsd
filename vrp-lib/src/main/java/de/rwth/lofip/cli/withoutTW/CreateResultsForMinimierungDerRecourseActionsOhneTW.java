package de.rwth.lofip.cli.withoutTW;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.recourseActionNumberMinimization.CreateResultsForMinimierungDerRecourseActions;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;

public class CreateResultsForMinimierungDerRecourseActionsOhneTW extends CreateResultsForMinimierungDerRecourseActions {

	//--- Eigene Modified Solomon Instances - 15% relative standardabweichung
	
		@Test
		public void AllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Algorithm
			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
			Parameters.setMaximalNumberOfToursInGot(2);
			Parameters.setRelativeStandardDeviationTo(0.15);
			Parameters.setRecourseActionNumberMinimization(true);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\OhneTW\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			deleteTimeWindows();
			processProblems();
		}
		
		//--- Modified Solomon Instances - 15% relative standardabweichung
		
		@Test
		public void AllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Algorithm
			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
			Parameters.setMaximalNumberOfToursInGot(2);
			Parameters.setRelativeStandardDeviationTo(0.15);
			Parameters.setRecourseActionNumberMinimization(true);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\OhneTW\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			deleteTimeWindows();
			processProblems();
		}
		
		
		//--- Original Solomon Instances - 15% relative standardabweichung
		
		@Test
		public void AllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Algorithm
			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
			Parameters.setMaximalNumberOfToursInGot(2);
			Parameters.setRelativeStandardDeviationTo(0.15);
			Parameters.setRecourseActionNumberMinimization(true);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\OhneTW\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			deleteTimeWindows();
			processProblems();
		}
		
	//------------

		private void deleteTimeWindows() {
			for (VrpProblem problem : problems) 
				for (Customer c : problem.getCustomers()) {
					c.setTimeWindowOpen(0);
					//RUNTIME_TODO: delete
					assertEquals(true, problem.getMaxTime() >= c.getTimeWindowClose());
					c.setTimeWindowClose(problem.getMaxTime());
				}
		}
		
}
