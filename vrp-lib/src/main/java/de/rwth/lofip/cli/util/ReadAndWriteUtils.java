package de.rwth.lofip.cli.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.VrpUtils;

public class ReadAndWriteUtils {
	
	private static FileOutputStream outputStreamZielfunktionswerteverlauf = null;
	private static FileOutputStream outputStreamSolutionAtEndOfTabuSearch = null;
	private static FileOutputStream outputStreamForPublishingSolutionAtEndOfAMTSSearch = null;
	
	private static Map<String,FileOutputStream> mapOfOutputStreamsForSolutionsAtEndOfTabuSearch = new HashMap<String,FileOutputStream>(200);
	
	private static List<Double> bestKnownSolutionValues = new LinkedList<Double>();
	private static List<Integer> bestKnownSolutionVehicleNumbers = new LinkedList<Integer>();	
	
	public static List<VrpProblem> readSolomonProblems(String inputDir) throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(inputDir);		
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "txt" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();			
			FileInputStream openInputStream = null;
			try {
				openInputStream = FileUtils.openInputStream(file);
				List<String> lines = IOUtils.readLines(openInputStream);
				VrpProblem problem = VrpUtils.createProblemFromStringList(lines);
				problems.add(problem);
			} finally {
				IOUtils.closeQuietly(openInputStream);
			}			
		}
		return problems;
	}
	
	public static List<VrpProblem> readSolomonProblemX(String contain, String notContain) throws IOException {
		return readProblemX(contain, notContain, getInputDirectoryForSolomon100());
	}
	
	public static List<VrpProblem> readProblemX(String contain, String notContain, String inputDirectory) throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(inputDirectory);		
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "txt" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();
			if (file.getName().contains(contain) && !file.getName().contains(notContain)) {
				FileInputStream openInputStream = null;
				try {
					openInputStream = FileUtils.openInputStream(file);
					List<String> lines = IOUtils.readLines(openInputStream);
					VrpProblem problem = VrpUtils.createProblemFromStringList(lines);
					problems.add(problem);
				} finally {
					IOUtils.closeQuietly(openInputStream);
				}
			}
		}
		return problems;	
	}
	
	public static List<VrpProblem> readPostProblems() throws IOException {
		return readSolomonProblems(getInputDirectoryForPostProblems());
	}
	
	public static List<VrpProblem> readOriginalSolomonProblems() throws IOException {
		return readSolomonProblems(getInputDirectoryForSolomon100());
	}
	
	public static List<VrpProblem> readEigeneModifiedSolomonProblems() throws IOException {
		return readSolomonProblems(getInputDirectoryForEigeneModifiedSolomonProblems());
	}
	
	public static List<VrpProblem> readModifiedSolomonProblems() throws IOException {
		return readSolomonProblems(getInputDirectoryForModifiedSolomonProblems());
	}
	
	public static List<VrpProblem> readSolomonProblems200() throws IOException {
		return readSolomonProblems(getInputDirectoryForGehring200Files());
	}
	
	public static List<VrpProblem> readSolomonProblems200_1000() throws IOException {
		return readSolomonProblems(getInputDirectoryForSolomon200_1000Files());
	}
	
	public static List<VrpProblem> readEigeneModifiedR1R2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("r1","rc1", getInputDirectoryForEigeneModifiedSolomonProblems());
		problems.addAll(readProblemX("r2","rc2", getInputDirectoryForEigeneModifiedSolomonProblems()));
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedR2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("r2","rc2", getInputDirectoryForEigeneModifiedSolomonProblems());
		return problems;
	}

	public static List<VrpProblem> readEigeneModifiedR1SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("r1","rc1", getInputDirectoryForEigeneModifiedSolomonProblems());
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedC1SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("c1","rc1", getInputDirectoryForEigeneModifiedSolomonProblems());
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedC1R1SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("c1","rc1", getInputDirectoryForEigeneModifiedSolomonProblems());
		problems.addAll(readProblemX("r1","rc1", getInputDirectoryForEigeneModifiedSolomonProblems()));
		return problems;
	}

	public static List<VrpProblem> readEigeneModifiedC2R2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("c2","rc2", getInputDirectoryForEigeneModifiedSolomonProblems());
		problems.addAll(readProblemX("r2","rc2", getInputDirectoryForEigeneModifiedSolomonProblems()));
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedC2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("c2","rc2", getInputDirectoryForEigeneModifiedSolomonProblems());
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedRC1RC2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("rc1","X", getInputDirectoryForEigeneModifiedSolomonProblems());
		problems.addAll(readProblemX("rc2","X", getInputDirectoryForEigeneModifiedSolomonProblems()));
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedRC1SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("rc1","X", getInputDirectoryForEigeneModifiedSolomonProblems());	
		return problems;
	}
	
	public static List<VrpProblem> readEigeneModifiedRC2SolomonProblems() throws IOException {
		List<VrpProblem> problems = readProblemX("rc2","X", getInputDirectoryForEigeneModifiedSolomonProblems());	
		return problems;
	}
	
	private static String getInputDirectoryForPostProblems() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\postProblems\\";	
		return s;	
	}
	
	private static String getInputDirectoryForEigeneModifiedSolomonProblems() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\eigene-modfied-solomon-problems\\";	
		return s;	
	}	
	
	private static String getInputDirectoryForSolomon100() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\original-solomon-problems\\";
		return s;		
	}
	
	private static String getInputDirectoryForModifiedSolomonProblems() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\modified-solomon-problems\\";
		return s;	
	}
	
	private static String getInputDirectoryForSolomon200_1000Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\";		
		return s;	
	}
	
	private static String getInputDirectoryForGehring200Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\200\\";		
		return s;	
	}
	
	public static String getInputDirectoryForGehring400Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\400\\";		
		return s;	
	}
	
	public static String getInputDirectoryForGehring600Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\600\\";		
		return s;	
	}
	
	public static String getInputDirectoryForGehring800Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\800\\";		
		return s;	
	}
	
	public static String getInputDirectoryForGehring1000Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\1000\\";		
		return s;	
	}
	
	public static String getOutputFile() {		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\output - " + sdf.format(Calendar.getInstance().getTime()) + ".txt";					
		return s;		
	}
	
	public static FileOutputStream getOutputStreamForPublishingSolutionValueProgress() throws IOException {
		if (outputStreamZielfunktionswerteverlauf == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
			String s = "\\Zielfunktionswerteverlauf\\output - " + sdf.format(Calendar.getInstance().getTime()) + ".txt";
			outputStreamZielfunktionswerteverlauf = openOutputFile(s);
		}
		return outputStreamZielfunktionswerteverlauf;
	}
	
	public static FileOutputStream getOutputStreamForPublishingSolutionAtEndOfTabuSearch(ElementWithTours solution) throws IOException {
		if (solution == null)
			throw new RuntimeException("solution ist null");
		if (mapOfOutputStreamsForSolutionsAtEndOfTabuSearch.get(((SolutionGot) solution).getVrpProblem().getDescription()) == null) { 
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
			String s = Parameters.getOutputDirectory() + sdf.format(Calendar.getInstance().getTime()) + " - " + ((SolutionGot) solution).getVrpProblem().getDescription() + ".txt";
			mapOfOutputStreamsForSolutionsAtEndOfTabuSearch.put(((SolutionGot) solution).getVrpProblem().getDescription(),openOutputFile(s));
		}
		return mapOfOutputStreamsForSolutionsAtEndOfTabuSearch.get(((SolutionGot) solution).getVrpProblem().getDescription());
	}	
	
	public static FileOutputStream getOutputStreamForPublishingSolutionAtEndOfAMTSSearch() throws IOException {
		if (outputStreamForPublishingSolutionAtEndOfAMTSSearch == null) { 		
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
			String s = Parameters.getOutputDirectory() + sdf.format(Calendar.getInstance().getTime()) + " - Ergebnisse";
			outputStreamForPublishingSolutionAtEndOfAMTSSearch = openOutputFile(s);
		}
		return outputStreamForPublishingSolutionAtEndOfAMTSSearch;
	}
	
	public static List<VrpProblem> readSolomonProblemC101() throws IOException {
		return readSolomonProblemX("c101","rc101");
	}
	
	public static List<VrpProblem> readSolomonProblemC102() throws IOException {
		return readSolomonProblemX("c102","rc102");
	}
	
	public static List<VrpProblem> readSolomonProblemC103() throws IOException {
		return readSolomonProblemX("c103","rc103");
	}
	
	public static List<VrpProblem> readSolomonProblemC106() throws IOException {
		return readSolomonProblemX("c106","rc106");
	}
	
	public static List<VrpProblem> readSolomonProblemC1XX() throws IOException {
		return readSolomonProblemX("c1","rc1");
	}
	
	public static List<VrpProblem> readSolomonProblemC2XX() throws IOException {
		return readSolomonProblemX("c2","rc2");
	}
	
	public static List<VrpProblem> readSolomonProblemC202() throws IOException {
		return readSolomonProblemX("c202","rc202");
	}

	public static List<VrpProblem> readSolomonProblemC204() throws IOException {
		return readSolomonProblemX("c204","rc204");
	}

	public static List<VrpProblem> readSolomonProblemC206() throws IOException {
		return readSolomonProblemX("c206","rc206");
	}
	
	public static List<VrpProblem> readSolomonProblemR1XX() throws IOException {
		return readSolomonProblemX("r1","rc1");
	}
	
	public static List<VrpProblem> readSolomonProblemR2XX() throws IOException {
		return readSolomonProblemX("r2","rc2");
	}
	
	public static List<VrpProblem> readSolomonProblemR201() throws IOException {
		return readSolomonProblemX("r201","rc201");
	}
	
	public static List<VrpProblem> readSolomonProblemR205() throws IOException {
		return readSolomonProblemX("r205","rc205");
	}
	
	public static List<VrpProblem> readSolomonProblemR208() throws IOException {
		return readSolomonProblemX("r208","rc208");
	}
	
	public static List<VrpProblem> readSolomonProblemR209() throws IOException {
		return readSolomonProblemX("r209","rc209");
	}
	
	public static List<VrpProblem> readSolomonProblemR210() throws IOException {
		return readSolomonProblemX("r210","rc210");
	}
	
	public static List<VrpProblem> readSolomonProblemRC1XX() throws IOException {
		return readSolomonProblemX("rc1","X");
	}
	
	public static List<VrpProblem> readSolomonProblemRC101AsList() throws IOException {
		return readSolomonProblemX("rc101","X");
	}

	public static VrpProblem readSolomonProblemRC101() throws IOException {
		return readSolomonProblemX("rc101","X").get(0);
	}
	
	public static List<VrpProblem> readSolomonProblemRC102AsList() throws IOException {
		return readSolomonProblemX("rc102","X");
	}
	
	public static VrpProblem readSolomonProblemRC103() throws IOException {
		return readSolomonProblemX("rc103","X").get(0);
	}
	
	public static List<VrpProblem> readSolomonProblemRC103AsList() throws IOException {
		return readSolomonProblemX("rc103","X");
	}
	
	public static List<VrpProblem> readSolomonProblemRC104AsList() throws IOException {
		return readSolomonProblemX("rc104","X");
	}

	public static List<VrpProblem> readSolomonProblemRC105AsList() throws IOException {
		return readSolomonProblemX("rc105","X");
	}
	
	public static List<VrpProblem> readSolomonProblemRC106AsList() throws IOException {
		return readSolomonProblemX("rc106","X");
	}
	
	public static List<VrpProblem> readSolomonProblemRC2XX() throws IOException {
		return readSolomonProblemX("rc2","X");
	}
		
	public static void createHeaderForPublishingSolutionAtEndOfTabuSearch(VrpProblem vrpProblem) throws IOException {				
		if (Parameters.isPublishSolutionAtEndOfTabuSearch()) 				
			IOUtils.write("" + ";" 
				+ "Distanz" + ";" 
				+ "Anzahl Touren" + ";"
				+ "RecourseCost" +";"
				+ "RecourseCostOnlyAdditionalTours" +";"
				+ "GrundkostenMitVerschiedenenTouren" +";"
				+ "Distanz+RecourseCost" + ";"
				+ "ConvenxkombinationRecourseCost+#RecourseActions" + ";"
				+ "Distanz+Convexkombination" + ";"
				+ "NumberOfRouteFailures" + ";"
				+ "NumberOfAdditionalTours" + ";"
				+ "NumberOfDifferentRecourseActions" + ";"
				+ "timeNeeded in sec" + ";"
				+ "UseOfCapacityInTours" + "; ;"	
				+ "SolutionAsTupel" + ";" 
				+ "NumberOfCustomersThatAreServedByNumberOfVehicles beginning with one and increasing with each column" + "\n" 
				, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(new SolutionGot(vrpProblem)));				
	}	
	
	public static void createHeaderForPublishingSolutionAtEndOfAMTSSearch() throws IOException {				
		if (Parameters.isPublishSolutionAtEndOfAMTSSearch()) 				
			IOUtils.write("" + ";" 
				+ "Distanz" + ";" 
				+ "Anzahl Touren" + ";"
				+ "RecourseCost" +";"
				+ "RecourseCostOnlyAdditionalTours" +";"
				+ "GrundkostenMitVerschiedenenTouren" +";"
				+ "Distanz+RecourseCost" + ";"
				+ "ConvenxkombinationRecourseCost+#RecourseActions" + ";"
				+ "Distanz+Convexkombination" + ";"
				+ "NumberOfRouteFailures" + ";"
				+ "NumberOfAdditionalTours" + ";"
				+ "NumberOfAdditionalToursPerDay" + ";"
				+ "NumberOfDifferentToursBasicVehicles" + ";"
				+ "NumberOfDifferentRecourseActions" + ";"
				+ "timeNeeded in sec" + ";"				
				+ "UseOfCapacityInBasicTours" + "; ;"
				+ "UseOfCapacityInAllTours" + ";"
				+ "BestKnownValue" +";" 
				+ "BestKnownVehicleNumber" + ";"
				+ "SolutionAsTupel" + ";" 
				+ "NumberOfCustomersThatAreServedByNumberOfVehicles beginning with one and increasing with each column" + "\n" 
				, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfAMTSSearch());				
	}	
	
	public static void publishSolutionAtEndOfAMTSSearch(SolutionGot bestOverallSolution, long timeNeeded) throws IOException {
		if (Parameters.isPublishSolutionAtEndOfAMTSSearch())			
			if (bestOverallSolution instanceof SolutionGot){
				
				setUpBestKnownSolutionValues();
				setUpBestKnownSolutionVehicleNumbers();
				
				//berechne GrundkostenMitVerschiedenenTouren
				double basicCostWithDifferentTours = bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()
														- bestOverallSolution.getExpectedRecourseCost().getRecourseCostOnlyAdditionalTours();
				
				//berechne Auslastung aller KFZ
				double allDemand = bestOverallSolution.getVrpProblem().getTotalDemandOfAllCustomers() * Parameters.getNumberOfDemandScenarioRuns();
				int alleKFZBasistouren = bestOverallSolution.getNumberOfTours() * Parameters.getNumberOfDemandScenarioRuns();
				double KFZZusatzfahrten = bestOverallSolution.getExpectedRecourseCost().getNumberOfAdditionalTours();
				double Fahrzeugkapazitaet = bestOverallSolution.getVrpProblem().getOriginalCapacity();
				double auslastungAllerKFZ = allDemand / ((alleKFZBasistouren + KFZZusatzfahrten) * Fahrzeugkapazitaet);
				
				//berechne Zeit in Sekunden 
				timeNeeded = timeNeeded / 1000;
				
				//berechne prozentuale abweichung					
//				double deviationObjValue = (bestOverallSolution.getTotalDistanceWithCostFactor() - bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) / bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue() * 100;
//				double deviationVehicleNumber = ( (double) bestOverallSolution.getVehicleCount() - (double) bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue());
				
				String s = SolutionGotUtils.createStringForCustomersServedByNumberOfVehicles(bestOverallSolution);					
		
				IOUtils.write(bestOverallSolution.getVrpProblem().getDescription() + ";" 
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor()) + ";" 
					+ bestOverallSolution.getNumberOfTours() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCostOnlyAdditionalTours()) + ";"
					+ String.format("%.3f",basicCostWithDifferentTours) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(bestOverallSolution)) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart(bestOverallSolution)) + ";"
					+ ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfRouteFailures() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours()) + ";"
					//numberOfAdditionalToursPerDay
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours() / Parameters.getNumberOfDemandScenarioRuns()) + ";"
					//numberOfDifferentToursForBasicVehicles
					+ String.format("%.3f", ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentToursForBasicVehicles()) + ";" 
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentRecourseActions()) + ";"
					+ timeNeeded + ";"
					+ bestOverallSolution.getUseOfCapacityInTours() + ";"
					+ String.format("%.3f", auslastungAllerKFZ) + ";"
					
//					+ String.format("%.3f",bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) + ";"
//					+ bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue() + ";"
//					+ String.format("%.3f",deviationObjValue) + ";"
//					+ String.format("%.3f",deviationVehicleNumber) + ";" 
//					
					+ bestOverallSolution.getAsTupel() + ";"
					+ s 
					+ "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfAMTSSearch());
			} else {
				throw new RuntimeException("bestOverallSolution ist nicht vom Typ SolutionGot");
			}
	}
	
	public static void publishSolutionAtEndOfTabuSearch(ElementWithTours bestOverallSolution, long timeNeeded) throws IOException {
		if (Parameters.isPublishSolutionAtEndOfTabuSearch())			
			if (bestOverallSolution instanceof SolutionGot){
				
				setUpBestKnownSolutionValues();
				setUpBestKnownSolutionVehicleNumbers();
				
				//berechne GrundkostenMitVerschiedenenTouren
				double basicCostWithDifferentTours = bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()
														- ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCostOnlyAdditionalTours();
				
				//berechne Auslastung aller KFZ
				double allDemand = ((SolutionGot) bestOverallSolution).getVrpProblem().getTotalDemandOfAllCustomers() * Parameters.getNumberOfDemandScenarioRuns();
				int alleKFZBasistouren = bestOverallSolution.getNumberOfTours() * Parameters.getNumberOfDemandScenarioRuns();
				double KFZZusatzfahrten = ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours();
				double Fahrzeugkapazitaet = ((SolutionGot) bestOverallSolution).getVrpProblem().getOriginalCapacity();
				double auslastungAllerKFZ = allDemand / ((alleKFZBasistouren + KFZZusatzfahrten) * Fahrzeugkapazitaet);
				
				//berechne Zeit in Sekunden 
				timeNeeded = timeNeeded / 1000;
				
				//berechne prozentuale abweichung					
//				double deviationObjValue = (bestOverallSolution.getTotalDistanceWithCostFactor() - bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) / bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue() * 100;
//				double deviationVehicleNumber = ( (double) bestOverallSolution.getVehicleCount() - (double) bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue());
				
				String s = SolutionGotUtils.createStringForCustomersServedByNumberOfVehicles(bestOverallSolution);					
		
				IOUtils.write(((SolutionGot) bestOverallSolution).getVrpProblem().getDescription() + ";" 
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor()) + ";" 
					+ bestOverallSolution.getNumberOfTours() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCostOnlyAdditionalTours()) + ";"
					+ String.format("%.3f",basicCostWithDifferentTours) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart((SolutionGot) bestOverallSolution)) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActionsButOnlyStochasticPart((SolutionGot) bestOverallSolution)) + ";"
					+ ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfRouteFailures() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours()) + ";"
					//numberOfAdditionalToursPerDay
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours() / Parameters.getNumberOfDemandScenarioRuns()) + ";"
					//numberOfDifferentToursForBasicVehicles
					+ String.format("%.3f", ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentToursForBasicVehicles()) + ";" 
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentRecourseActions()) + ";"
					+ timeNeeded + ";"
					+ bestOverallSolution.getUseOfCapacityInTours() + ";"
					+ String.format("%.3f", auslastungAllerKFZ) + ";"
					
//					+ String.format("%.3f",bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) + ";"
//					+ bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue() + ";"
//					+ String.format("%.3f",deviationObjValue) + ";"
//					+ String.format("%.3f",deviationVehicleNumber) + ";" 
//					
					+ bestOverallSolution.getAsTupel() + ";"
					+ s 
					+ "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(bestOverallSolution));
			} else {
				throw new RuntimeException("bestOverallSolution ist nicht vom Typ SolutionGot");
			}
	}
		
	public static void printResultsToFile(String nameOfFile, List<SolutionGot> solutions1, long timeNeeded,
											int initialNumberOfDifferentInitialSolutions, 
											int initialNumberOfIterationsTabuSearch,
											int maximalNumberOfIterationsWithoutImprovementTabuSearch,
											int maximalNumberOfCallsToAdaptiveMemory,
											int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory,
											int seedI1, int seedGI, int seedAM) throws IOException {
		setUpBestKnownSolutionValues();
		setUpBestKnownSolutionVehicleNumbers();
		
		FileOutputStream outputStream = openOutputFile(nameOfFile);
		
		int i = 0;
		IOUtils.write("Start-Parameter:; " + 
				"initialNumberOfDifferentInitialSolutions " + initialNumberOfDifferentInitialSolutions + "; " +
				"initialNumberOfIterationsTabuSearch " + initialNumberOfIterationsTabuSearch + "; " +
				"maximalNumberOfIterationsWithoutImprovementTabuSearch " + maximalNumberOfIterationsWithoutImprovementTabuSearch + ";" + 
				"maximalNumberOfCallsToAdaptiveMemory " + maximalNumberOfCallsToAdaptiveMemory + "; " +  
				"maximalNumberOfCallsWithoutImprovementToAdaptiveMemory " + maximalNumberOfCallsWithoutImprovementToAdaptiveMemory +";" + 
				"seedI1 " + seedI1 + " " +
				"seedGI " + seedGI + " " +
				"seedAM " + seedAM + "\n "
				, outputStream);
		IOUtils.write("Time needed: " + timeNeeded + " min \n\n", outputStream);
		IOUtils.write("Problem; SolverObjectiveValues; SolverVehicleNumber; RecourseValue; #DifferentRecourseActions", outputStream);
		IOUtils.write("BestKnownValue; BestKnownVehicleNumber \n", outputStream);
		
		double averageDeviationObjValue = 0;
		double averageDeviationVehicleNumber = 0;
		
		for (SolutionGot solution : solutions1) {			
			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
			IOUtils.write(String.format("%.3f",solution.getTotalDistanceWithCostFactor()) + ";", outputStream);
			IOUtils.write(String.format("%.3f",solution.getExpectedRecourseCost().getRecourseCost()) + ";", outputStream);
			IOUtils.write(solution.getExpectedRecourseCost().getNumberOfDifferentRecourseActions() + ";", outputStream);
			IOUtils.write(solution.getVehicleCount() + ";", outputStream);
			IOUtils.write(solution.getUseOfCapacityInTours() + ";", outputStream);
			IOUtils.write(solution.getAsTupel() + ";", outputStream);
			
			IOUtils.write(String.format("%.3f",bestKnownSolutionValues.get(i % 56).doubleValue()) + ";",outputStream);
			IOUtils.write(bestKnownSolutionVehicleNumbers.get(i % 56).intValue() + ";",outputStream);
			//print prozentuale abweichung
			double deviationObjValue = (solution.getTotalDistanceWithCostFactor() - bestKnownSolutionValues.get(i).doubleValue()) / bestKnownSolutionValues.get(i).doubleValue() * 100;
			averageDeviationObjValue += deviationObjValue;
			double deviationVehicleNumber = ( (double) solution.getVehicleCount() - (double) bestKnownSolutionVehicleNumbers.get(i).intValue()) / (double) bestKnownSolutionVehicleNumbers.get(i).intValue() * 100;
			averageDeviationVehicleNumber += deviationVehicleNumber;
			IOUtils.write(String.format("%.3f",deviationObjValue) + ";",outputStream);
			IOUtils.write(String.format("%.3f",deviationVehicleNumber) + "\n",outputStream);
			i++;
		}
		
		averageDeviationObjValue = averageDeviationObjValue / (double) i;
		averageDeviationVehicleNumber = averageDeviationVehicleNumber / (double) i;
		IOUtils.write(";;;;;" + String.format("%.3f",averageDeviationObjValue) + ";" + String.format("%.3f",averageDeviationVehicleNumber), outputStream);
		
		printAverageValues(solutions1, outputStream);
	}
	
	public static FileOutputStream openOutputFile(String nameOfFile) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		File outputFile = new File(getOutputDirectory()		
				+ nameOfFile + " " 
                + sdf.format(Calendar.getInstance().getTime())
                + ".csv");
		FileOutputStream outputStream = FileUtils.openOutputStream(outputFile, true);
		return outputStream;
	}

	private static String getOutputDirectory() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Code\\Output\\";		
		return s;		
	}
	
	public static void printAverageValues(List<SolutionGot> solutions, FileOutputStream outputStream) throws IOException {
		double averageNumberVehiclesR1 = calculateAverageNumberVehicles("R1", solutions);
		double averageDistanceR1 = calculateAverageDistance("R1", solutions);
		
		double averageNumberVehiclesC1 = calculateAverageNumberVehicles("C1", "RC1", solutions);
		double averageDistanceC1 = calculateAverageDistance("C1", "RC1", solutions);

		double averageNumberVehiclesRC1 = calculateAverageNumberVehicles("RC1", solutions);
		double averageDistanceRC1 = calculateAverageDistance("RC1", solutions);
		
		double averageNumberVehiclesR2 = calculateAverageNumberVehicles("R2", solutions);
		double averageDistanceR2 = calculateAverageDistance("R2", solutions);
		
		double averageNumberVehiclesC2 = calculateAverageNumberVehicles("C2", "RC2", solutions);
		double averageDistanceC2 = calculateAverageDistance("C2", "RC2", solutions);
		
		double averageNumberVehiclesRC2 = calculateAverageNumberVehicles("RC2", solutions);
		double averageDistanceRC2 = calculateAverageDistance("RC2", solutions);	
		
		IOUtils.write("\n\n\n", outputStream);
		IOUtils.write("ProblemType;Average Vehicle Number; Average Distance \n", outputStream);
		IOUtils.write("R1;" + String.format("%.3f",averageNumberVehiclesR1) + ";" + String.format("%.3f",averageDistanceR1) + "\n", outputStream);
		IOUtils.write("C1;" + String.format("%.3f",averageNumberVehiclesC1) + ";" + String.format("%.3f",averageDistanceC1) + "\n", outputStream);
		IOUtils.write("RC1;" + String.format("%.3f",averageNumberVehiclesRC1) + ";" + String.format("%.3f",averageDistanceRC1) + "\n", outputStream);
		IOUtils.write("R2;" + String.format("%.3f",averageNumberVehiclesR2) + ";" + String.format("%.3f",averageDistanceR2) + "\n", outputStream);
		IOUtils.write("C2;" + String.format("%.3f",averageNumberVehiclesC2) + ";" + String.format("%.3f",averageDistanceC2) + "\n", outputStream);
		IOUtils.write("RC2;" + String.format("%.3f",averageNumberVehiclesRC2) + ";" + String.format("%.3f",averageDistanceRC2) + "\n", outputStream);
	}	
	
	private static double calculateAverageNumberVehicles(String string, List<SolutionGot> solutions) {
		return calculateAverageNumberVehicles(string, "X", solutions);
	}
	
	private static double calculateAverageNumberVehicles(String string, String stringNotContain, List<SolutionGot> solutions) {
		double numberOfSolutions = 0.0;
		double vehicleNumber = 0.0;
		for (SolutionGot solution : solutions) {
			if (solution.getVrpProblem().getDescription().contains(string) 
					&& !solution.getVrpProblem().getDescription().contains(stringNotContain)) {
				numberOfSolutions++;
				vehicleNumber += solution.getVehicleCount();
			}
		}
		return vehicleNumber / numberOfSolutions;
	}
	
	private static double calculateAverageDistance(String string, List<SolutionGot> solutions) { 
		return calculateAverageDistance(string, "X", solutions); 
	}

	private static double calculateAverageDistance(String string, String stringNotContain, List<SolutionGot> solutions) {
		double numberOfSolutions = 0.0;
		double distance = 0.0;
		for (SolutionGot solution : solutions) {
			if (solution.getVrpProblem().getDescription().contains(string)
				&& !solution.getVrpProblem().getDescription().contains(stringNotContain)) {
				numberOfSolutions++;
				distance += solution.getTotalDistanceWithCostFactor();
			}
		}
		return distance / numberOfSolutions;
	}
	
	private static int getNumberOfVrpProblemInBestKnownSolutionValues(VrpProblem problem) {
		int i = 0;
		if (problem.getDescription().equals("C101"))
			return i;
		i++;
		if (problem.getDescription().equals("C102"))
			return i;
		i++;
		if (problem.getDescription().equals("C103"))
			return i;
		i++;
		if (problem.getDescription().equals("C104"))
			return i;
		i++;
		if (problem.getDescription().equals("C105"))
			return i;
		i++;
		if (problem.getDescription().equals("C106"))
			return i;
		i++;
		if (problem.getDescription().equals("C107"))
			return i;
		i++;
		if (problem.getDescription().equals("C108"))
			return i;
		i++;
		if (problem.getDescription().equals("C109"))
			return i;
		i++;
		if (problem.getDescription().equals("C201"))
			return i;
		i++;
		if (problem.getDescription().equals("C202"))
			return i;
		i++;
		if (problem.getDescription().equals("C203"))
			return i;
		i++;
		if (problem.getDescription().equals("C204"))
			return i;
		i++;
		if (problem.getDescription().equals("C205"))
			return i;
		i++;
		if (problem.getDescription().equals("C206"))
			return i;
		i++;
		if (problem.getDescription().equals("C207"))
			return i;
		i++;
		if (problem.getDescription().equals("C208"))
			return i;
		i++;
		if (problem.getDescription().equals("R101"))
			return i;
		i++;
		if (problem.getDescription().equals("R102"))
			return i;
		i++;
		if (problem.getDescription().equals("R103"))
			return i;
		i++;
		if (problem.getDescription().equals("R104"))
			return i;
		i++;
		if (problem.getDescription().equals("R105"))
			return i;
		i++;
		if (problem.getDescription().equals("R106"))
			return i;
		i++;
		if (problem.getDescription().equals("R107"))
			return i;
		i++;
		if (problem.getDescription().equals("R108"))
			return i;
		i++;
		if (problem.getDescription().equals("R109"))
			return i;
		i++;
		if (problem.getDescription().equals("R110"))
			return i;
		i++;
		if (problem.getDescription().equals("R111"))
			return i;
		i++;
		if (problem.getDescription().equals("R112"))
			return i;
		i++;
		if (problem.getDescription().equals("R201"))
			return i;
		i++;
		if (problem.getDescription().equals("R202"))
			return i;
		i++;
		if (problem.getDescription().equals("R203"))
			return i;
		i++;
		if (problem.getDescription().equals("R204"))
			return i;
		i++;
		if (problem.getDescription().equals("R205"))
			return i;
		i++;
		if (problem.getDescription().equals("R206"))
			return i;
		i++;
		if (problem.getDescription().equals("R207"))
			return i;
		i++;
		if (problem.getDescription().equals("R208"))
			return i;
		i++;
		if (problem.getDescription().equals("R209"))
			return i;
		i++;
		if (problem.getDescription().equals("R210"))
			return i;
		i++;
		if (problem.getDescription().equals("R211"))
			return i;
		i++;
		if (problem.getDescription().equals("RC101"))
			return i;
		i++;
		if (problem.getDescription().equals("RC102"))
			return i;
		i++;
		if (problem.getDescription().equals("RC103"))
			return i;
		i++;
		if (problem.getDescription().equals("RC104"))
			return i;
		i++;
		if (problem.getDescription().equals("RC105"))
			return i;
		i++;
		if (problem.getDescription().equals("RC106"))
			return i;
		i++;
		if (problem.getDescription().equals("RC107"))
			return i;
		i++;
		if (problem.getDescription().equals("RC108"))
			return i;
		i++;
		if (problem.getDescription().equals("RC201"))
			return i;
		i++;
		if (problem.getDescription().equals("RC202"))
			return i;
		i++;
		if (problem.getDescription().equals("RC203"))
			return i;
		i++;
		if (problem.getDescription().equals("RC204"))
			return i;
		i++;
		if (problem.getDescription().equals("RC205"))
			return i;
		i++;
		if (problem.getDescription().equals("RC206"))
			return i;
		i++;
		if (problem.getDescription().equals("RC207"))
			return i;
		i++;
		if (problem.getDescription().equals("RC208"))
			return i;
		throw new RuntimeException("Problem " + problem.getDescription() + " nicht gefunden. sollte nicht passieren.");
	}

	private static void setUpBestKnownSolutionValues() {
		//c1xx
		for (int i = 1; i<=3; i++)
			bestKnownSolutionValues.add(new Double(828.94));
		bestKnownSolutionValues.add(new Double(824.78));
		for (int i = 1; i<=5; i++)
			bestKnownSolutionValues.add(new Double(828.94));
		//c2xx
		bestKnownSolutionValues.add(new Double(591.56));
		bestKnownSolutionValues.add(new Double(591.56));
		bestKnownSolutionValues.add(new Double(591.17));
		bestKnownSolutionValues.add(new Double(590.60));
		bestKnownSolutionValues.add(new Double(588.29));
		bestKnownSolutionValues.add(new Double(588.49));
		bestKnownSolutionValues.add(new Double(588.29));
		bestKnownSolutionValues.add(new Double(588.32));
		//r1xx
		bestKnownSolutionValues.add(new Double(1650.80));
		bestKnownSolutionValues.add(new Double(1486.12));
		bestKnownSolutionValues.add(new Double(1292.68));
		bestKnownSolutionValues.add(new Double(1007.31));
		bestKnownSolutionValues.add(new Double(1377.11));
		bestKnownSolutionValues.add(new Double(1252.03));
		bestKnownSolutionValues.add(new Double(1104.66));
		bestKnownSolutionValues.add(new Double(960.88));
		bestKnownSolutionValues.add(new Double(1194.73));
		bestKnownSolutionValues.add(new Double(1118.84));
		bestKnownSolutionValues.add(new Double(1096.72));
		bestKnownSolutionValues.add(new Double(982.14));
		//r2xx
		bestKnownSolutionValues.add(new Double(1252.37));
		bestKnownSolutionValues.add(new Double(1191.70));
		bestKnownSolutionValues.add(new Double(939.50));
		bestKnownSolutionValues.add(new Double(825.52));
		bestKnownSolutionValues.add(new Double(994.42));
		bestKnownSolutionValues.add(new Double(906.14));
		bestKnownSolutionValues.add(new Double(890.61));
		bestKnownSolutionValues.add(new Double(726.82));
		bestKnownSolutionValues.add(new Double(909.16));
		bestKnownSolutionValues.add(new Double(939.37));
		bestKnownSolutionValues.add(new Double(885.71));
		//rc1xx
		bestKnownSolutionValues.add(new Double(1696.94));
		bestKnownSolutionValues.add(new Double(1554.75));
		bestKnownSolutionValues.add(new Double(1261.67));
		bestKnownSolutionValues.add(new Double(1135.48));
		bestKnownSolutionValues.add(new Double(1629.44));
		bestKnownSolutionValues.add(new Double(1424.73));
		bestKnownSolutionValues.add(new Double(1230.48));
		bestKnownSolutionValues.add(new Double(1139.82));
		//rc2xx
		bestKnownSolutionValues.add(new Double(1406.94));
		bestKnownSolutionValues.add(new Double(1365.65));
		bestKnownSolutionValues.add(new Double(1049.62));
		bestKnownSolutionValues.add(new Double(798.46));
		bestKnownSolutionValues.add(new Double(1297.65));
		bestKnownSolutionValues.add(new Double(1146.32));
		bestKnownSolutionValues.add(new Double(1061.14));
		bestKnownSolutionValues.add(new Double(828.14));
	}
	
	private static void setUpBestKnownSolutionVehicleNumbers() {
		//c1xx
		for (int i = 1; i<=9; i++)
			bestKnownSolutionVehicleNumbers.add(10);
		//c2xx
		for (int i = 1; i<=8; i++)
			bestKnownSolutionVehicleNumbers.add(3);	
		//r1xx
		bestKnownSolutionVehicleNumbers.add(19);
		bestKnownSolutionVehicleNumbers.add(17);
		bestKnownSolutionVehicleNumbers.add(13);
		bestKnownSolutionVehicleNumbers.add(9);
		bestKnownSolutionVehicleNumbers.add(14);
		bestKnownSolutionVehicleNumbers.add(12);
		bestKnownSolutionVehicleNumbers.add(10);
		bestKnownSolutionVehicleNumbers.add(9);
		bestKnownSolutionVehicleNumbers.add(11);
		bestKnownSolutionVehicleNumbers.add(10);
		bestKnownSolutionVehicleNumbers.add(10);
		bestKnownSolutionVehicleNumbers.add(9);
		//r2xx
		bestKnownSolutionVehicleNumbers.add(4);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(2);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(2);
		bestKnownSolutionVehicleNumbers.add(2);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(2);
		//rc1xx
		bestKnownSolutionVehicleNumbers.add(14);
		bestKnownSolutionVehicleNumbers.add(12);
		bestKnownSolutionVehicleNumbers.add(11);
		bestKnownSolutionVehicleNumbers.add(10);
		bestKnownSolutionVehicleNumbers.add(13);
		bestKnownSolutionVehicleNumbers.add(11);
		bestKnownSolutionVehicleNumbers.add(11);
		bestKnownSolutionVehicleNumbers.add(10);
		//rc2xx
		bestKnownSolutionVehicleNumbers.add(4);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(4);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
		bestKnownSolutionVehicleNumbers.add(3);
	}


}
