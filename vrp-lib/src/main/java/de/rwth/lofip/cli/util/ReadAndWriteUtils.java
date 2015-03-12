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
	
	public static List<VrpProblem> readSolomonProblemRC101() throws IOException {
		return readSolomonProblemX("rc101","X");
	}

	public static void printResultsToFile(String string,List<SolutionGot> solutions) throws IOException {
		List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>();	
		printResultsToFile(string, solutions, dummySolutions); 
	}
	
	public static void printResultsToFile(String string, List<SolutionGot> solutionsInitialSolver,List<SolutionGot> solutionsLocalSearch) throws IOException {
		setUpBestKnownSolutionValues();
		setUpBestKnownSolutionVehicleNumbers();
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		File outputFile = new File(getOutputDirectory()		
				+ string + " " 
                + sdf.format(Calendar.getInstance().getTime())
                + ".csv");
		FileOutputStream outputStream = FileUtils.openOutputStream(outputFile, true);
		int i = 0;
		IOUtils.write("Problem; InitialSolverValues; InitialSolverVehicleNumber; ", outputStream);
		if (!solutionsLocalSearch.isEmpty()) 
			IOUtils.write("LocalSearchValues; LocalSearchVehicleNumber; ", outputStream);
		IOUtils.write("BestKnownValue; BestKnownVehicleNumber \n", outputStream);
		for (SolutionGot solution : solutionsInitialSolver) {
			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
			IOUtils.write(String.format("%.3f",solution.getTotalDistance()) + ";", outputStream);
			IOUtils.write(solution.getVehicleCount() + ";", outputStream);
			if (!solutionsLocalSearch.isEmpty()) {
				IOUtils.write(String.format("%.3f",solutionsLocalSearch.get(i).getTotalDistance()) + ";", outputStream);
				IOUtils.write(solutionsLocalSearch.get(i).getVehicleCount() + ";", outputStream);
				IOUtils.write(solutionsLocalSearch.get(i).getSolutionAsTupel() + ";", outputStream);
				IOUtils.write("Demands: ", outputStream);
				for (Tour tour : solutionsLocalSearch.get(i).getTours())
					IOUtils.write(tour.getDemandOnTour() + ", ", outputStream);
				IOUtils.write("; ", outputStream);
			}
			IOUtils.write(bestKnownSolutionValues.get(i) + ";",outputStream);
			IOUtils.write(bestKnownSolutionVehicleNumbers.get(i) + "\n",outputStream);
			i++;
		}
	}
	
	private static String getOutputDirectory() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Code\\Output\\";		
		return s;		
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
