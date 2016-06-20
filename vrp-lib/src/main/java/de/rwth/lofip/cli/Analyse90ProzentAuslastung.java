package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.VrpUtils;

public class Analyse90ProzentAuslastung extends AnalyseIstSituation {
	
	@Test
	public void EightyPercentOfCapacityOnC1R1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\Analyse90ProzentAuslastung\\MitTourKombination\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void EightyPercentOfCapacityOnC2R2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\Analyse90ProzentAuslastung\\MitTourKombination\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void EightyPercentOfCapacityOnRC1RC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\Analyse90ProzentAuslastung\\MitTourKombination\\RC1CRC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
}
