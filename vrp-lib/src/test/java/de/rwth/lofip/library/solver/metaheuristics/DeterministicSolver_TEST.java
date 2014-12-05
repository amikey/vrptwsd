package de.rwth.lofip.library.solver.metaheuristics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.initialSolver.StochasticPushForwardInsertionSolver;
import de.rwth.lofip.library.util.VrpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

/**
 * This writes all values into one file as in table 1, page 1781, Lei et al.
 * paper
 */
public class DeterministicSolver_TEST {

	public final static String INPUT_DIRECTORY = "baseDirectory";
	public final static String OUTPUT_DIRECTORY = "outputDirectory";

	FileOutputStream outputStream;

	private VrpConfiguration configuration = new VrpConfiguration();

	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

	public static void main(String... args) throws IOException {
		DeterministicSolver_TEST s = new DeterministicSolver_TEST();
		s.calculateAll();
	}

	public void calculateAll() throws IOException {
		String directory = configuration
				.getConfigurationValueString(INPUT_DIRECTORY);
		if (StringUtils.isEmpty(directory)) {
			throw new IllegalArgumentException(
					"No input directory specified. Please configure the parameter 'baseDirectory'.");
		}
		// first, dump the current configuration into the directory

		String configurationAsString = configuration.getConfigurationAsString();
		if (StringUtils.isNotEmpty(configuration
				.getConfigurationValueString(OUTPUT_DIRECTORY))) {
			File outputFile = new File(
					configuration.getConfigurationValueString(OUTPUT_DIRECTORY)
							+ "configuration" + "-"
							+ sdf.format(Calendar.getInstance().getTime()));
			outputStream = FileUtils.openOutputStream(outputFile, true);
			IOUtils.write(configurationAsString, outputStream);
			// IOUtils.closeQuietly(outputStream);
		} else {
			System.out.println(configurationAsString);
		}

		// open the output file
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

		String outputDirectory = configuration
				.getConfigurationValueString(OUTPUT_DIRECTORY);
		if (StringUtils.isNotEmpty(outputDirectory)) {
			File outputFile = new File(outputDirectory + "AllInstancesLeiEtAl"
					+ sdf.format(Calendar.getInstance().getTime()) + ".csv");
			outputStream = FileUtils.openOutputStream(outputFile, true);
		}

		IOUtils.write("Inst.;V;m;Q;det;stoch;gesamt;ms\n", outputStream);

		// get all problems / files in the directory
		Iterator<File> files = FileUtils.iterateFiles(new File(directory),
				new String[] { "txt" }, false);
		while (files.hasNext()) {
			File file = files.next();
			if (!file.getName().contains("scenario")) // &&
														// !file.getName().contains("c"))
			{
				System.out.println("processing file " + file.getName());
				FileInputStream openInputStream = null;
				try {
					openInputStream = FileUtils.openInputStream(file);
					List<String> lines = IOUtils.readLines(openInputStream);
					VrpProblem problem = VrpUtils
							.createProblemFromStringList(lines);
					try {
						createLeiEtAlSolution(problem, file.getName());
					} catch (Exception e) {
						System.err
								.println("An error occurred while calculating the solution with the Lei et al. heuristic.");
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(openInputStream);
				}
			}
		}
	}

	private void createLeiEtAlSolution(VrpProblem problem, String name)
			throws IOException {
		// first, create an initial solution
		StochasticPushForwardInsertionSolver initialSolver = new StochasticPushForwardInsertionSolver();
		Solution solution = initialSolver.solve(problem);

		LeiEtAlHeuristic leaHeuristic = new LeiEtAlHeuristic();

		long startTime = System.nanoTime();
		Solution improvedSolution = leaHeuristic.improve(solution,
				configuration);
		long endTime = System.nanoTime();
		String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

		IOUtils.write(
				name
						+ ";"
						+ problem.getCustomers().size()
						+ 1
						+ ";"
						+ problem.getVehicleCount()
						+ ";"
						+ problem.getVehicles().iterator().next().getCapacity()
						+ ";"
						+ String.format("%.3f",
								improvedSolution.getTotalDistanceOfAllTours())
						+ ";"
						+ String.format("%.3f",
								improvedSolution.getExpectedRecourseCost())
						+ ";"
						+ String.format(
								"%.3f",
								improvedSolution
										.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost())
						+ ";" + timeTaken
						+ improvedSolution.getTourTypesAsString() + "\n",
				outputStream);
	}

}
