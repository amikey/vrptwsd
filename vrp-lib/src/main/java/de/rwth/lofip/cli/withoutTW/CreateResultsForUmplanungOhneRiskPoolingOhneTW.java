package de.rwth.lofip.cli.withoutTW;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.CreateResultsForUmplanungOhneRiskPooling;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization.AMTSwithRecourseAndRecourseActionNumber;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForUmplanungOhneRiskPoolingOhneTW extends CreateResultsForUmplanungOhneRiskPooling {
	
	//--- Eigene Modified Solomon Instances
	
		private double weight = 0.03;
		private double twFactor;

		@Test
		public void HundredPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
			//Set Parameters for Scenario
			setParameters();
//			Parameters.setPercentageOfCapacity(0.9);
			Parameters.setWeightForConvexcombination(weight);
			Parameters.setMaximalNumberOfToursInGot(2);
			
			//Set Output parameters
			Parameters.setOutputDirectory("\\Ab20160720\\TWAnalyse\\PostProblems\\XGleichh2\\integriertH2Gleich0Punkt03_2Touren\\");
			
			problems = ReadAndWriteUtils.readPostProblems();
			Parameters.setIsPostScenario(true);
			
//			reduceCapacityOfVehiclesInProblems();
			
			twFactor = 2;
			setTimeWindowsWithFactor();
//			deleteTimeWindows();
			
			solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo();
//			processProblems();
		}
		
		@Override
		protected void setParameters() {
			//Set Parameters for Scenario
			//Set Parameters for Algorithm
			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
			Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
			Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
			Parameters.setNumberOfInitialSolutions(20);
			Parameters.setNumberOfIntensificationTries(0);		
			Parameters.setMaximalNumberOfToursInGot(2);
			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
			Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		}
		
		private void solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo() throws IOException {		
			ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
			for (VrpProblem problem : problems) {
				System.out.println("SOLVING PROBLEM " + problem.getDescription());						
				AMTSwithRecourseAndRecourseActionNumber adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
				SolutionGot solution = adaptiveMemoryTabuSearch.solveWithNewAlgo(problem);
				solutions.add(solution);			
			}		
		}
		
		
		
//		
//		@Test
//		public void NinetyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.95);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\EigeneModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void NinetyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.9);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\EigeneModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void EightyFivePercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.85);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\EigeneModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void EightyPercentOfCapacityOnAllEigeneModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.8);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyPercent\\EigeneModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//	//--- Original Solomon Instances
//		
//		@Test
//		public void EightyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.8);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EigthyPercent\\OriginalSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void EightyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.85);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\OriginalSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void NinetyPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.9);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\OriginalSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void NinetyFivePercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.95);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\OriginalSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void HundredPercentOfCapacityOnAllOriginalSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(1);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\OriginalSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readOriginalSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//			
//	//--- Modified Solomon Instances
//		
//		@Test
//		public void EightyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.8);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyPercent\\ModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void EightyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.85);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\EightyFivePercent\\ModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void NinetyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.9);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyPercent\\ModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}		
//			
//		@Test
//		public void NinetyFivePercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(0.95);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\NinetyFivePercent\\ModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
//		
//		@Test
//		public void HundredPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
//			//Set Parameters for Scenario
//			Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
//			Parameters.setPercentageOfCapacity(1);
//			Parameters.setMaximalNumberOfToursInGot(1);
//			
//			//Set Output parameters
//			Parameters.setOutputDirectory("\\OhneTW\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\ModifiedSolomon\\");
//			Parameters.setPublishSolutionAtEndOfTabuSearch(true);
//			
//			problems = ReadAndWriteUtils.readModifiedSolomonProblems();
//			reduceCapacityOfVehiclesInProblems();
//			deleteTimeWindows();
//			processProblems();
//		}
		
	//------------
		
		private void setTimeWindowsWithFactor() {
			for (VrpProblem problem : problems)
				for (Customer c : problem.getCustomers()) {
					double oldTWOpen = c.getTimeWindowOpen();
					double oldTWClose = c.getTimeWindowClose();
					double oldTWCenter = (oldTWOpen + oldTWClose) / 2;
					double oldTWRange = oldTWClose - oldTWOpen;
					
					double newTWOpen = oldTWCenter - twFactor * (oldTWRange / 2);
					newTWOpen = Math.max(0, newTWOpen);
					c.setTimeWindowOpen(newTWOpen);
					
					double newTWClose = oldTWCenter + twFactor * (oldTWRange / 2);
					newTWClose = Math.min(problem.getMaxTime(),newTWClose);
					c.setTimeWindowClose(newTWClose);
				}
		}

		private void deleteTimeWindows() {
			for (VrpProblem problem : problems) 
				for (Customer c : problem.getCustomers()) {
					c.setTimeWindowOpen(0);
					//RUNTIME_TODO: delete
					assertEquals(true, problem.getMaxTime() >= c.getTimeWindowClose());
					c.setTimeWindowClose(problem.getMaxTime());
				}
		}
		
		//for testing
		public List<VrpProblem> deleteTimeWindwos(List<VrpProblem> problems2) {
			problems = problems2;
			deleteTimeWindows();
			return problems;
		}
}
