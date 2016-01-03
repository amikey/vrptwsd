package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForUmplanungMitRiskPooling extends RunAdaptiveMemorySearchWithSolomonInstances{
	
	// Ein Got für alle Touren
	
	//--- Eigene Modified Solomon Instances
	
		@Test
		public void HundredPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(1);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungMitRiskPooling\\HundredPercent\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void NinetyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.95);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungMitRiskPooling\\NinetyFivePercent\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void NinetyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.9);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungMitRiskPooling\\NinetyPercent\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void EightyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.85);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungMitRiskPooling\\EightyFivePercent\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void EightyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.8);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungMitRiskPooling\\EightyPercent\\EigeneModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
	//--- Original Solomon Instances
		
		@Test
		public void EightyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.8);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\EigthyPercent\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void EightyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.85);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void NinetyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.9);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void NinetyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.95);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void HundredPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(1);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\OriginalSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
			
	//--- Modified Solomon Instances
		
		@Test
		public void EightyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.8);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\EightyPercent\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void EightyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.85);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void NinetyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.9);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}		
			
		@Test
		public void NinetyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(0.95);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
		
		@Test
		public void HundredPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			Parameters.setAllParametersToDefaultValues();
			Parameters.setPercentageOfCapacity(1);
			Parameters.setNumberOfToursInGot(100);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\ModifiedSolomon\\");
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			
			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
			reduceCapacityOfVehiclesInProblems();
			processProblems();
		}
			
		
	//-------------------------------------

		private void reduceCapacityOfVehiclesInProblems() {
			for (VrpProblem problem : problems)
				VrpUtils.decreaseCapacityOfVehiclesToPercentageOf(problem, Parameters.getPercentageOfCapacity());
		}

}
