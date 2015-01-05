package de.rwth.lofip.library.solver.metaheuristics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.PoissonTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.initialSolver.AbstractPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.IntermediateSolution;
import de.rwth.lofip.library.util.VrpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;
import de.rwth.lofip.stuffNotNeededRightNow.solver.initialSolver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;

/**
 * CLI application for the VRP Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class DeterministicSolver_einzelneInstanzen_TEST {

	public final static String INPUT_DIRECTORY = "baseDirectory";
	public final static String OUTPUT_DIRECTORY = "outputDirectory";

	FileOutputStream outputStream;

	private VrpConfiguration configuration = new VrpConfiguration();

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"YYYY-MM-dd-HH-mm-ss");

	public static void main(String... args) throws IOException {

		PrintStream out = new PrintStream(new FileOutputStream(
				"DeterministischeTourenJaNein - "
						+ sdf.format(Calendar.getInstance().getTime())));
		System.setOut(out);

		DeterministicSolver_einzelneInstanzen_TEST s = new DeterministicSolver_einzelneInstanzen_TEST();
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
						createLeiEtAlSolution(problem);
					} catch (Exception e) {
						System.err
								.println("An error occurred while calculating the solution with the Lei et al. heuristic.");
						e.printStackTrace();
					}
					// try {
					// createTsSolution(problem);
					// } catch (Exception e) {
					// System.err
					// .println("An error occurred while calculating the solution with the Tabu Search heuristic.");
					// e.printStackTrace();
					// }
					// try {
					// createPoissonLeiSolution(problem);
					// } catch (Exception e) {
					// System.err
					// .println("An error occurred while calculating the solution with the Poisson-Lei Search heuristic.");
					// e.printStackTrace();
					// }
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(openInputStream);
				}
			}
		}
	}

	private void createLeiEtAlSolution(VrpProblem problem) throws IOException {
		// first, create an initial solution
		DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
		Solution solution = initialSolver.solve(problem);

		// open the output file
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

		String outputDirectory = configuration
				.getConfigurationValueString(OUTPUT_DIRECTORY);
		if (StringUtils.isNotEmpty(outputDirectory)) {
			File outputFile = new File(outputDirectory + "LeiEtAl-"
					+ problem.getDescription() + "-" + sdf.format(c.getTime())
					+ ".csv");
			outputStream = FileUtils.openOutputStream(outputFile, true);
			// now, first create the non-TS-solution
			LeiEtAlHeuristic leaHeuristic = new DeterministicLeiEtAlHeuristic() {
				@Override
				public void publishSolution(IntermediateSolution is) {
					try {
						DecimalFormat df = new DecimalFormat("000.000");
						IOUtils.write(
								is.getSolution().getIteration()
										+ ";"
										+ is.getCustomersChanged()
										+ ";"
										+ is.getRemovalHeuristic()
										+ ";"
										+ df.format(new Double(is
												.getTimeForRemoval() / 1000)
												.doubleValue() / 1000)
										+ ";"
										+ is.getInsertionHeuristic()
										+ ";"
										+ df.format(new Double(is
												.getTimeForInsertion() / 1000)
												.doubleValue() / 1000)
										+ ";"
										+ is.isBestSolution()
										+ ";"
										+ is.isImprovedSolution()
										+ ";"
										+ is.isAccepted()
										+ ";"
										+ is.getSolution()
												.getTourTypesAsString() + "\n",
								outputStream);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}

			};
			IOUtils.write(solution.getSolutionAsString() + "\n", outputStream);
			IOUtils.write(solution.getTourTypesAsString() + "\n", outputStream);

			IOUtils.write(
					"Iteration; #CustomersChanged; RemovalHeuristic; TimeForRemoval; InsertionHeuristic; "
							+ "TimeForInsertion; BestSolution?; ImprovedSolution?; AcceptedSolution? \n\n",
					outputStream);

			long startTime = System.nanoTime();
			Solution improvedSolution = leaHeuristic.improve(solution,
					configuration);
			long endTime = System.nanoTime();
			String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

			// create an intermediate --- line, just for easier cutting
			IOUtils.write("----------\n", outputStream);
			IOUtils.write(timeTaken, outputStream);
			IOUtils.write(improvedSolution.getIteration() + "\n", outputStream);
			IOUtils.write(improvedSolution.getSolutionAsString(), outputStream);

			// IOUtils.closeQuietly(outputStream);
		} else {
			LeiEtAlHeuristic leaHeuristic = new LeiEtAlHeuristic() {
				@Override
				public void publishSolution(IntermediateSolution is) {
					DecimalFormat df = new DecimalFormat("000.000");
					System.out.println(is.getSolution().getIteration()
							+ ";"
							+ is.getCustomersChanged()
							+ ";"
							+ is.getRemovalHeuristic()
							+ ";"
							+ df.format(new Double(
									is.getTimeForRemoval() / 1000)
									.doubleValue() / 1000)
							+ ";"
							+ is.getInsertionHeuristic()
							+ ";"
							+ df.format(new Double(
									is.getTimeForInsertion() / 1000)
									.doubleValue() / 1000) + ";"
							+ is.isBestSolution() + ";"
							+ is.isImprovedSolution() + ";" + is.isAccepted());
				}
			};
			long startTime = System.nanoTime();
			Solution improvedSolution = leaHeuristic.improve(solution,
					configuration);
			long endTime = System.nanoTime();
			String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

			// create an intermediate --- line, just for easier cutting
			System.out.println("----------");
			System.out.println(timeTaken);
			System.out.println(improvedSolution.getIteration());
			System.out.println(improvedSolution.getSolutionAsString());
		}
	}

	AbstractPushForwardInsertionSolver initialSolver = new AbstractPushForwardInsertionSolver() {
		@Override
		protected CustomerWithCost calculateCustomerHook(Customer customer,
				Tour tour, double approximateEquality) {
			return TourUtils.calculateCostStochasticSolver(customer, tour,
					approximateEquality);
		}

		@Override
		protected Tour createTourHook(Depot depot, int vehicleId,
				double vehicleCapacity) {
			Tour tour = new PoissonTour(depot, new Vehicle(vehicleId,
					vehicleCapacity));
			return tour;
		}
	};

}