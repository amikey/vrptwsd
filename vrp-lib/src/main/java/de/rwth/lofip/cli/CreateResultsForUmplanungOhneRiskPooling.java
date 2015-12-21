package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForUmplanungOhneRiskPooling extends RunAdaptiveMemorySearchWithSolomonInstances{
	
	@Test
	public void EightyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setNumberOfToursInGot(1);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\EigthyPercent\\ModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		reduceCapacityOfVehiclesInProblems();
		processProblems();
	}
	
	@Test
	public void NinetyPercentOfCapacityOnAllModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setNumberOfToursInGot(1);
		
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
		Parameters.setPercentageOfCapacity(0.95);
		Parameters.setNumberOfToursInGot(1);
		
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
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\HundredPercent\\ModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		reduceCapacityOfVehiclesInProblems();
		processProblems();
	}
	
	@Test
	public void EightyPercentOfCapacityOnAllSolomonInstances() throws IOException {	
		//Set Parameters for Scenario
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setOutputDirectory("\\ErgebnisseUmplanungOhneRiskPooling\\Solomon100\\");
		
		//Set Output parameters
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblems();
		reduceCapacityOfVehiclesInProblems();
		processProblems();
	}
	
//-------------------------------------

	private void reduceCapacityOfVehiclesInProblems() {
		for (VrpProblem problem : problems)
			VrpUtils.decreaseCapacityOfVehiclesToPercentageOf(problem, Parameters.getPercentageOfCapacity());
	}

}
