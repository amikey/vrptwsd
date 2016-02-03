package de.rwth.lofip.cli.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	private static Map<String,FileOutputStream> mapOfOutputStreamsForSolutionsAtEndOfTabuSearch = new HashMap<String,FileOutputStream>(200);
	
	private static List<Double> bestKnownSolutionValues = new LinkedList<Double>();
	private static List<Integer> bestKnownSolutionVehicleNumbers = new LinkedList<Integer>();
	
	
	
	public static List<VrpProblem> readEigeneModifiedSolomonProblems() throws IOException {
		return readSolomonProblems(getInputDirectoryForEigeneModifiedSolomonProblems());
	}
	
	private static String getInputDirectoryForEigeneModifiedSolomonProblems() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\eigene-modfied-solomon-problems\\";	
		System.out.println(s);
		return s;	
	}

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
	
	public static List<VrpProblem> readOriginalSolomonProblems() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectoryForSolomon100());		
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
	
	public static List<VrpProblem> readModifiedSolomonProblems() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectoryForModifiedSolomonFiles());		
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "txt" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();	
			System.out.println(file.getName());
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
	
	public static List<VrpProblem> readSolomonProblems200() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectoryForGehring200Files());		
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "TXT" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();	
			System.out.println(file.getName());
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
	
	public static List<VrpProblem> readSolomonProblems200_1000() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectoryForSolomon200_1000Files());		
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "TXT" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();	
			System.out.println(file.getName());
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
	
	public static List<VrpProblem> createListOfProblemsFromInputDirectory(File dir) throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "TXT" }, false);
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		while (files.hasNext()) {
			File file = files.next();	
			System.out.println(file.getName());
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

	private static String getInputDirectoryForSolomon100() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\original-solomon-problems\\";
//		System.out.println(s);
		return s;		
	}
	
	private static String getInputDirectoryForModifiedSolomonFiles() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\modified-solomon-problems\\";
//		System.out.println(s);
		return s;	
	}
	
	private static String getInputDirectoryForSolomon200_1000Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\";		
//		System.out.println(s);
		return s;	
	}
	
	private static String getInputDirectoryForGehring200Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\200\\";		
//		System.out.println(s);
		return s;	
	}
	
	public static String getInputDirectoryForGehring400Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\400\\";		
//		System.out.println(s);
		return s;	
	}
	
	public static String getInputDirectoryForGehring600Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\600\\";		
		System.out.println(s);
		return s;	
	}
	
	public static String getInputDirectoryForGehring800Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\800\\";		
		System.out.println(s);
		return s;	
	}
	
	public static String getInputDirectoryForGehring1000Files() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\gehringerHombergerInstanzen\\1000\\";		
		System.out.println(s);
		return s;	
	}
	
	public static String getOutputFile() {		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^input\\output - " + sdf.format(Calendar.getInstance().getTime()) + ".txt";					
		System.out.println(s);
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
//		if (outputStreamSolutionAtEndOfTabuSearch == null) {
//			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
//			String s = "\\ZwischenergebnisseNachTabuSearchModifiedInstances\\" + sdf.format(Calendar.getInstance().getTime()) + " - " + ((SolutionGot) solution).getVrpProblem().getDescription() + ".txt";
//			outputStreamSolutionAtEndOfTabuSearch = openOutputFile(s);
//		}
//		return outputStreamSolutionAtEndOfTabuSearch;
	}	
	
	public static FileOutputStream getOutputStreamForPublishingSolutionAtEndOfAMTSSearch() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		String s = Parameters.getOutputDirectory() + sdf.format(Calendar.getInstance().getTime()) + " - Ergebnisse";
		return openOutputFile(s);
	}
		
	public static List<VrpProblem> readSolomonProblemX(String contain, String notContain) throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectoryForSolomon100());		
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
		if (Parameters.publishSolutionAtEndOfTabuSearch()) 				
			IOUtils.write("" + ";" 
				+ "Distanz" + ";" 
				+ "Anzahl Touren" + ";"
				+ "RecourseCost" +";"
				+ "Distanz+RecourseCost" + ";"
				+ "ConvenxkombinationRecourseCost+#RecourseActions" + ";"
				+ "Distanz+Convexkombination" + ";"
				+ "NumberOfRouteFailures" + ";"
				+ "NumberOfAdditionalTours" + ";"
				+ "NumberOfDifferentRecourseActions" + ";"
				+ "timeNeeded in ms" + ";"
				+ "UseOfCapacityInTours" + "; ;"	
				+ "SolutionAsTupel" + ";" 
				+ "NumberOfCustomersThatAreServedByNumberOfVehicles beginning with one and increasing with each column" + "\n" 
				, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(new SolutionGot(vrpProblem)));				
	}	
	
	public static void createHeaderForPublishingSolutionAtEndOfAMTSSearch() throws IOException {				
		if (Parameters.publishSolutionAtEndOfAMTSSearch()) 				
			IOUtils.write("" + ";" 
				+ "Distanz" + ";" 
				+ "Anzahl Touren" + ";"
				+ "RecourseCost" +";"
				+ "Distanz+RecourseCost" + ";"
				+ "ConvenxkombinationRecourseCost+#RecourseActions" + ";"
				+ "Distanz+Convexkombination" + ";"
				+ "NumberOfRouteFailures" + ";"
				+ "NumberOfAdditionalTours" + ";"
				+ "NumberOfDifferentRecourseActions" + ";"
				+ "timeNeeded in ms" + ";"
				+ "UseOfCapacityInTours" + "; ;"	
				+ "BestKnownValue" +";" 
				+ "BestKnownVehicleNumber" + ";"
				+ "SolutionAsTupel" + ";" 
				+ "NumberOfCustomersThatAreServedByNumberOfVehicles beginning with one and increasing with each column" + "\n" 
				, ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfAMTSSearch());				
	}	
	
	public static void publishSolutionAtEndOfAMTSSearch(SolutionGot bestOverallSolution, long timeNeeded) throws IOException {
		if (Parameters.publishSolutionAtEndOfAMTSSearch())			
			if (bestOverallSolution instanceof SolutionGot){
				
				setUpBestKnownSolutionValues();
				setUpBestKnownSolutionVehicleNumbers();
				
				//berechne prozentuale abweichung					
				double deviationObjValue = (bestOverallSolution.getTotalDistanceWithCostFactor() - bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) / bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue() * 100;
				double deviationVehicleNumber = ( (double) bestOverallSolution.getVehicleCount() - (double) bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue()) / (double) bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue() * 100;
				
				String s = SolutionGotUtils.createStringForCustomersServedByNumberOfVehicles(bestOverallSolution);					
		
				IOUtils.write("Lösung am Ende der TS: " + ";" 
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor()) + ";" 
					+ bestOverallSolution.getNumberOfTours() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActions()) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActions()) + ";"
					+ ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfRouteFailures() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentRecourseActions()) + ";"
					+ timeNeeded + ";"
					+ bestOverallSolution.getUseOfCapacityInTours() + ";"
					
					+ String.format("%.3f",bestKnownSolutionValues.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).doubleValue()) + ";"
					+ bestKnownSolutionVehicleNumbers.get(getNumberOfVrpProblemInBestKnownSolutionValues(bestOverallSolution.getVrpProblem())).intValue() + ";"
					+ String.format("%.3f",deviationObjValue) + ";"
					+ String.format("%.3f",deviationVehicleNumber) + ";" 
					
					+ bestOverallSolution.getAsTupel() + ";"
					+ s 
					+ "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfAMTSSearch());
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
		if (problem.getDescription().charAt(0) == "C".charAt(0)) {
			if (problem.getDescription().charAt(1) == "1".charAt(0)) {
				//C1 Problems
				int number = (int) problem.getDescription().charAt(3);
				number--;
				return number;
			} else {
				//C2 Problems
				int number = (int) problem.getDescription().charAt(3);
				number+=8;
				return number;
			}				
		}
		if (problem.getDescription().charAt(0) == "R".charAt(0)) {
			if (problem.getDescription().charAt(1) != "C".charAt(0)) {
				//R - Problems
				if (problem.getDescription().charAt(1) == "1".charAt(0)) {
//					//R1 Problems
					if (problem.getDescription().charAt(2) == "1".charAt(0)) {
						//110-112
						int number = (int) problem.getDescription().charAt(3);
						number+=26;
						return number;
					} else {
						int number = (int) problem.getDescription().charAt(3);
						number+=16;
						return number;
					}
				} else {
//					//R2 Problems
					if (problem.getDescription().charAt(2) == "1".charAt(0)) {
						int number = (int) problem.getDescription().charAt(3);
						number+=38;
						return number;
					} else {
						int number = (int) problem.getDescription().charAt(3);
						number+=28;
						return number;
					}
				}
			} else {
				//RC Problems 
				if (problem.getDescription().charAt(2) == "1".charAt(0)) {
					//RC1 Problems
					int number = (int) problem.getDescription().charAt(4);
					number+=39;
					return number;
				} else {
					//RC2 Problems
					int number = (int) problem.getDescription().charAt(4);
					number+=47;
					return number;
				}
			}
		}
		throw new RuntimeException("Problem nicht gefunden. sollte nicht passieren.");
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
	


	
	
	
//	*************************** utilities for printing results such as average values etc of several runs

//	public static void printResultsOverSeveralRunsToFile(List<List<SolutionGot>> solutionsOfAllRuns, FileOutputStream outputStream) throws IOException {
//		IOUtils.write("Results after " + solutionsOfAllRuns.size() + " Experiments \n\n", outputStream);
//		
//		IOUtils.write("; best ; ; average ; ; worst ; ; bestKnown; ; Experiment Best Solution Was Found in \n", outputStream);
//		IOUtils.write("; value ; vehicleNumber ; value ; vehicleNumber ; value ; VehicleNumber ; value ; VehicleNumber ; \n", outputStream);
//		
//		int problemCounter = 0;
//		for (SolutionGot solution : solutionsOfAllRuns.get(0)) {
//			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
//			
//			SolutionGot bestSolution = getBestSolution(solutionsOfAllRuns, problemCounter);
//			IOUtils.write(String.format("%.3f",bestSolution.getTotalDistance()) + ";", outputStream);
//			IOUtils.write(bestSolution.getVehicleCount() + ";", outputStream);
//			
//			double averageSolutionValue = getAverageSolutionValue(solutionsOfAllRuns, problemCounter);
//			double averageVehicleNumber = getAverageVehicleNumber(solutionsOfAllRuns, problemCounter);
//			IOUtils.write(String.format("%.3f",averageSolutionValue) + ";", outputStream);
//			IOUtils.write(String.format("%.3f",averageVehicleNumber) + ";", outputStream);
//			
//			SolutionGot worstSolution = getWorstSolution(solutionsOfAllRuns, problemCounter);
//			IOUtils.write(String.format("%.3f",worstSolution.getTotalDistance()) + ";", outputStream);
//			IOUtils.write(worstSolution.getVehicleCount() + ";", outputStream);
//			
//			IOUtils.write(String.format("%.3f",bestKnownSolutionValues.get(problemCounter).doubleValue()) + ";",outputStream);
//			IOUtils.write(bestKnownSolutionVehicleNumbers.get(problemCounter).intValue() + ";",outputStream);
//			
//			IOUtils.write(getBestSolutionExperiment(solutionsOfAllRuns, problemCounter) + ";", outputStream);
//			IOUtils.write("\n", outputStream);	
//			
//			problemCounter++;
//		}
//		IOUtils.write("\n\n", outputStream);
//	}
//
//	private static SolutionGot getBestSolution(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
//		SolutionGot bestSolution = SetUpUtils.getDummySolutionThatIsWorseThanEveryPossibleSolution();
//		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
//			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
//			if (currentSolution.isBetterThan(bestSolution))
//				bestSolution = currentSolution.clone();
//		}
//		return bestSolution;
//	}
//	
//	private static int getBestSolutionExperiment(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
//		int experimentNumber = 0;
//		SolutionGot bestSolution = SetUpUtils.getDummySolutionThatIsWorseThanEveryPossibleSolution();
//		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
//			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
//			if (currentSolution.isBetterThan(bestSolution)) {
//				bestSolution = currentSolution.clone();			
//				experimentNumber = i;
//			}
//		}
//		return experimentNumber+1;
//	}
//
//	private static double getAverageSolutionValue(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
//		double averageValue = 0.0;
//		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
//			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
//			averageValue += currentSolution.getTotalDistance();
//		}
//		return averageValue / (double) solutionsOfAllRuns.size();
//	}
//	
//	private static double getAverageVehicleNumber(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
//		double averageNumber = 0.0;
//		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
//			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
//			averageNumber += currentSolution.getVehicleCount();
//		}
//		return averageNumber / (double) solutionsOfAllRuns.size();
//	}
//	
//	private static SolutionGot getWorstSolution(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
//		SolutionGot worstSolution = solutionsOfAllRuns.get(0).get(problemCounter);
//		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
//			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
//			if (worstSolution.isBetterThan(currentSolution))
//				worstSolution = currentSolution.clone();
//		}
//		return worstSolution;
//	}









}
