package de.rwth.lofip.library.util;

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
import de.rwth.lofip.library.VrpProblem;

public class TestUtils {
	
	private static List<Double> bestKnownSolutionValues = new LinkedList<Double>();
	private static List<Integer> bestKnownSolutionVehicleNumbers = new LinkedList<Integer>();
	
	public static List<VrpProblem> readSolomonProblems() throws IOException {
		List<VrpProblem> problems = new LinkedList<VrpProblem>();
//		File dir = new File("C:/Users/Andreas/Dropbox/Uni/Diss/Code/vrp-lib/original-solomon-problems");
		File dir = new File("C:/Users/abraun/Dropbox/Uni/Diss/Code/vrp-lib/original-solomon-problems");
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

	public static void printResultsToFile(String string,List<SolutionGot> solutions) throws IOException {
		List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>();	
		printResultsToFile(string, solutions, dummySolutions); 
	}
	
	public static void printResultsToFile(String string, List<SolutionGot> solutions,List<SolutionGot> solutions2) throws IOException {
		setUpBestKnownSolutionValues();
		setUpBestKnownSolutionVehicleNumbers();
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
		File outputFile = new File("C:/Users/Andreas/Dropbox/Uni/Diss/Code/output/"
				+ string + " " 
                + sdf.format(Calendar.getInstance().getTime())
                + ".csv");
		FileOutputStream outputStream = FileUtils.openOutputStream(outputFile, true);
		int i = 0;
		IOUtils.write("Problem; InitialSolverValues; InitialSolverVehicleNumber; ", outputStream);
		if (!solutions2.isEmpty()) 
			IOUtils.write("LocalSearchValues; LocalSearchVehicleNumber; ", outputStream);
		IOUtils.write("BestKnownValue; BestKnownVehicleNumber \n", outputStream);
		for (SolutionGot solution : solutions) {
			IOUtils.write(solution.getVrpProblem().getDescription() + ";", outputStream);
			IOUtils.write(String.format("%.3f",solution.getTotalDistance()) + ";", outputStream);
			IOUtils.write(solution.getVehicleCount() + ";", outputStream);
			if (!solutions2.isEmpty()) {
				IOUtils.write(String.format("%.3f",solutions2.get(i).getTotalDistance()) + ";", outputStream);
				IOUtils.write(solutions2.get(i).getVehicleCount() + ";", outputStream);
			}
			IOUtils.write(bestKnownSolutionValues.get(i) + ";",outputStream);
			IOUtils.write(bestKnownSolutionVehicleNumbers.get(i) + "\n",outputStream);
			i++;
		}
	}
	
	private static void setUpBestKnownSolutionValues() {
		//c1xx
		for (int i = 1; i<=3; i++)
			bestKnownSolutionValues.add(new Double(828.94));
		bestKnownSolutionValues.add(new Double(824.78));
		for (int i = 1; i<=6; i++)
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
