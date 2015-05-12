package de.rwth.lofip.cli.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.library.util.VrpUtils;

public class ReadAndWriteUtils {
	
	private static List<Double> bestKnownSolutionValues = new LinkedList<Double>();
	private static List<Integer> bestKnownSolutionVehicleNumbers = new LinkedList<Integer>();
	
	public static List<VrpProblem> readSolomonProblems() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectory());		
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
	
	private static String getInputDirectory() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Code\\vrp-lib\\original-solomon-problems\\";
		System.out.println(s);
		return s;		
	}
	
	public static String getOutputFile() {		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");		
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Code\\output\\output - " + sdf.format(Calendar.getInstance().getTime()) + ".txt";					
		System.out.println(s);
		return s;		
	}
	
	public static List<VrpProblem> readSolomonProblemX(String contain, String notContain) throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
		File dir = new File(getInputDirectory());		
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
	
	public static List<VrpProblem> readSolomonProblemC202() throws IOException {
		return readSolomonProblemX("c202","rc202");
	}
	

	public static List<VrpProblem> readSolomonProblemC204() throws IOException {
		return readSolomonProblemX("c204","rc204");
	}

	public static List<VrpProblem> readSolomonProblemC206() throws IOException {
		return readSolomonProblemX("c206","rc206");
	}
	
	public static List<VrpProblem> readSolomonProblemRC101() throws IOException {
		return readSolomonProblemX("rc101","X");
	}

	public static void printResultsToFile(String string,List<SolutionGot> solutions) throws IOException {
		List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>();	
		printResultsToFile(string, solutions, dummySolutions); 		
	}
	
	public static void printResultsToFile(String string,
			List<SolutionGot> solutions, long timeNeeded) throws IOException {
		List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>();
		printResultsToFile(string, solutions, dummySolutions, timeNeeded);
	}
	
	public static void printResultsToFile(String string, List<SolutionGot> solutionsInitialSolver,List<SolutionGot> solutionsLocalSearch) throws IOException {
		printResultsToFile(string, solutionsInitialSolver, solutionsLocalSearch, 0);
	}
	
	public static void printResultsToFile(String nameOfFile, List<SolutionGot> solutions1,List<SolutionGot> solutions2, long timeNeeded,
											int initialNumberOfDifferentInitialSolutions, int initialNumberOfIterationsTabuSearch,
											int seedI1, int seedGI, int seedAM) throws IOException {
		setUpBestKnownSolutionValues();
		setUpBestKnownSolutionVehicleNumbers();
		
		FileOutputStream outputStream = openOutputFile(nameOfFile);
		
		int i = 0;
		IOUtils.write("Start-Parameter: " + 
				"initialNumberOfDifferentInitialSolutions " + initialNumberOfDifferentInitialSolutions + " " +
				"initialNumberOfIterationsTabuSearch " + initialNumberOfIterationsTabuSearch + " " +
				"seedI1 " + seedI1 + " " +
				"seedGI " + seedGI + " " +
				"seedAM " + seedAM + "\n "
				, outputStream);
		IOUtils.write("Time needed: " + timeNeeded + " min \n\n", outputStream);
		IOUtils.write("Problem; InitialSolverValues; InitialSolverVehicleNumber; ", outputStream);
		if (!solutions2.isEmpty()) 
			IOUtils.write("LocalSearchValues; LocalSearchVehicleNumber; ", outputStream);
		IOUtils.write("BestKnownValue; BestKnownVehicleNumber \n", outputStream);
		
		double averageDeviationObjValue = 0;
		double averageDeviationVehicleNumber = 0;
		
		for (SolutionGot solution : solutions1) {			
			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
			IOUtils.write(String.format("%.3f",solution.getTotalDistance()) + ";", outputStream);
			IOUtils.write(solution.getVehicleCount() + ";", outputStream);
			if (!solutions2.isEmpty()) {
				IOUtils.write(String.format("%.3f",solutions2.get(i).getTotalDistance()) + ";", outputStream);
				IOUtils.write(solutions2.get(i).getVehicleCount() + ";", outputStream);
				IOUtils.write(solutions2.get(i).getAsTupel() + ";", outputStream);
				IOUtils.write("Demands: ", outputStream);
				for (Tour tour : solutions2.get(i).getTours())
					IOUtils.write(tour.getDemandOnTour() + ", ", outputStream);
				IOUtils.write("; ", outputStream);
			}
			IOUtils.write(String.format("%.3f",bestKnownSolutionValues.get(i).doubleValue()) + ";",outputStream);
			IOUtils.write(bestKnownSolutionVehicleNumbers.get(i).intValue() + ";",outputStream);
			//print prozentuale abweichung
			double deviationObjValue = (solution.getTotalDistance() - bestKnownSolutionValues.get(i).doubleValue()) / bestKnownSolutionValues.get(i).doubleValue() * 100;
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
				distance += solution.getTotalDistance();
			}
		}
		return distance / numberOfSolutions;
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

	public static void printResultsOverSeveralRunsToFile(List<List<SolutionGot>> solutionsOfAllRuns, FileOutputStream outputStream) throws IOException {
		IOUtils.write("Results after " + solutionsOfAllRuns.size() + " Experiments \n\n", outputStream);
		
		IOUtils.write("; best ; ; average ; ; worst ; ; bestKnown; ; Experiment Best Solution Was Found in \n", outputStream);
		IOUtils.write("; value ; vehicleNumber ; value ; vehicleNumber ; value ; VehicleNumber ; value ; VehicleNumber ; \n", outputStream);
		
		int problemCounter = 0;
		for (SolutionGot solution : solutionsOfAllRuns.get(0)) {
			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
			
			SolutionGot bestSolution = getBestSolution(solutionsOfAllRuns, problemCounter);
			IOUtils.write(String.format("%.3f",bestSolution.getTotalDistance()) + ";", outputStream);
			IOUtils.write(bestSolution.getVehicleCount() + ";", outputStream);
			
			double averageSolutionValue = getAverageSolutionValue(solutionsOfAllRuns, problemCounter);
			double averageVehicleNumber = getAverageVehicleNumber(solutionsOfAllRuns, problemCounter);
			IOUtils.write(String.format("%.3f",averageSolutionValue) + ";", outputStream);
			IOUtils.write(String.format("%.3f",averageVehicleNumber) + ";", outputStream);
			
			SolutionGot worstSolution = getWorstSolution(solutionsOfAllRuns, problemCounter);
			IOUtils.write(String.format("%.3f",worstSolution.getTotalDistance()) + ";", outputStream);
			IOUtils.write(worstSolution.getVehicleCount() + ";", outputStream);
			
			IOUtils.write(String.format("%.3f",bestKnownSolutionValues.get(problemCounter).doubleValue()) + ";",outputStream);
			IOUtils.write(bestKnownSolutionVehicleNumbers.get(problemCounter).intValue() + ";",outputStream);
			
			IOUtils.write(getBestSolutionExperiment(solutionsOfAllRuns, problemCounter) + ";", outputStream);
			IOUtils.write("\n", outputStream);	
			
			problemCounter++;
		}
		IOUtils.write("\n\n", outputStream);
	}

	private static SolutionGot getBestSolution(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
		SolutionGot bestSolution = SetUpUtils.getDummySolutionThatIsWorseThanEveryPossibleSolution();
		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
			if (currentSolution.isBetterThan(bestSolution))
				bestSolution = currentSolution.clone();
		}
		return bestSolution;
	}
	
	private static int getBestSolutionExperiment(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
		int experimentNumber = 0;
		SolutionGot bestSolution = SetUpUtils.getDummySolutionThatIsWorseThanEveryPossibleSolution();
		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
			if (currentSolution.isBetterThan(bestSolution)) {
				bestSolution = currentSolution.clone();			
				experimentNumber = i;
			}
		}
		return experimentNumber+1;
	}

	private static double getAverageSolutionValue(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
		double averageValue = 0.0;
		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
			averageValue += currentSolution.getTotalDistance();
		}
		return averageValue / (double) solutionsOfAllRuns.size();
	}
	
	private static double getAverageVehicleNumber(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
		double averageNumber = 0.0;
		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
			averageNumber += currentSolution.getVehicleCount();
		}
		return averageNumber / (double) solutionsOfAllRuns.size();
	}
	
	private static SolutionGot getWorstSolution(List<List<SolutionGot>> solutionsOfAllRuns, int problemCounter) {
		SolutionGot worstSolution = solutionsOfAllRuns.get(0).get(problemCounter);
		for (int i = 0; i < solutionsOfAllRuns.size(); i++) {
			SolutionGot currentSolution = solutionsOfAllRuns.get(i).get(problemCounter);
			if (worstSolution.isBetterThan(currentSolution))
				worstSolution = currentSolution.clone();
		}
		return worstSolution;
	}

	public static void printResultsToFile(String nameOfFile,
			List<SolutionGot> solutions,
			long timeNeeded, int numberOfDifferentInitialSolutions,
			int maximalNumberOfIterationsTabuSearch, int seedI1, int seedGI,
			int seedAM) throws IOException {
		List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>();
		printResultsToFile(nameOfFile,solutions, dummySolutions, timeNeeded,
				numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);		
	}








}
