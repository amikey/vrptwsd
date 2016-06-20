package de.rwth.lofip.cli.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

public class CreateSolutionsFromFile {
	
	public static List<SolutionGot> createSolutionsFromFileForProblem(VrpProblem problem) throws IOException {
		List<String> lines = getContentFromSolutionFileForProblem(problem);
		List<SolutionGot> solutions = createSolutionsFromString(lines, problem);
		return solutions;
	}

	private static List<String> getContentFromSolutionFileForProblem(VrpProblem problem) throws IOException {
		List<String> lines = null;
		File dir = new File(getInputDirectoryForSolutions());
		if (dir.listFiles() == null)
			throw new RuntimeException("Directory enthält keine Files");
		Iterator<File> files = FileUtils.iterateFiles(dir,new String[] { "txt" }, false);
		while (files.hasNext()) {			
			File file = files.next();
			if (file.getName().contains(problem.getDescription()) && !file.getName().contains("R" + problem.getDescription())) {
				FileInputStream openInputStream = null;
				try {
					openInputStream = FileUtils.openInputStream(file);
					lines = IOUtils.readLines(openInputStream);				
				} finally {
					IOUtils.closeQuietly(openInputStream);					
				}
				break;
			}
		}
		if (lines == null)
			throw new RuntimeException("lines == null; Vermutlich existiert keine Datei für Problem " + problem);
		return lines;
	}

	private static String getInputDirectoryForSolutions() {
		String s = System.getenv("USERPROFILE");
		s += "\\Dropbox\\Uni\\Diss\\Ergebnisse\\^^output\\SaveBestDeterministicSolutionsForEachVehicleNumber\\";
		return s;
	}

	private static List<SolutionGot> createSolutionsFromString(List<String> lines, VrpProblem problem) {
		List<SolutionGot> solutions = new ArrayList<SolutionGot>();
		for (String line : lines) {
			// ignore empty lines
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			line = StringUtils.normalizeSpace(StringUtils.trim(line));
			if (lineContainsSolution(line)) {
				SolutionGot solution = SetUpSolutionFromString.SetUpSolution(line, problem);
				solutions.add(solution);
			}
		}
		return solutions;
	}

	private static boolean lineContainsSolution(String line) {
		if (line.startsWith("("))
			// line contains solution if it starts with a bracket
			return true;
		return false;
	}
}
