package de.rwth.lofip.cli.withoutTW;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.CreateResultsForUmplanungMitRiskPooling;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;

public class CreateResultsForUmplanungMitRiskPoolingOhneTW extends CreateResultsForUmplanungMitRiskPooling {
	
	// Ein Got für alle Touren
	
		//--- Eigene Modified Solomon Instances
		
			@Test
			public void HundredPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(1);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungMitRiskPooling\\HundredPercent\\EigeneModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void NinetyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.95);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungMitRiskPooling\\NinetyFivePercent\\EigeneModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void NinetyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.9);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungMitRiskPooling\\NinetyPercent\\EigeneModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void EightyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.85);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungMitRiskPooling\\EightyFivePercent\\EigeneModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void EightyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.8);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungMitRiskPooling\\EightyPercent\\EigeneModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
		//--- Original Solomon Instances
			
			@Test
			public void EightyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.8);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EigthyPercent\\OriginalSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readOriginalSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void EightyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.85);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\OriginalSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readOriginalSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void NinetyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.9);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\OriginalSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readOriginalSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void NinetyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.95);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\OriginalSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readOriginalSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void HundredPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(1);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\OriginalSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readOriginalSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
				
		//--- Modified Solomon Instances
			
			@Test
			public void EightyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.8);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyPercent\\ModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void EightyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.85);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\ModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void NinetyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.9);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\ModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}		
				
			@Test
			public void NinetyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(0.95);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\ModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
				deleteTimeWindows();
				processProblems();
			}
			
			@Test
			public void HundredPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
				//Set Parameters for Scenario
				Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
				Parameters.setPercentageOfCapacity(1);
				Parameters.setNumberOfToursInGot(100);
				
				//Set Output parameters
				Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\ModifiedSolomon\\");
				Parameters.setPublishSolutionAtEndOfTabuSearch(true);
				
				problems = ReadAndWriteUtils.readModifiedSolomonProblems();
				reduceCapacityOfVehiclesInProblems();
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
