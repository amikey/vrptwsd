package de.rwth.lofip.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;
import de.rwth.lofip.library.solver.AbstractPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.AbstractSolver;
import de.rwth.lofip.library.solver.StochasticPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.library.solver.metaheuristics.PoissonLeiEtAlHeuristic;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchHeuristic;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.IntermediateSolution;

/**
 * CLI application for the VRP Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class VrpSolver {

    public final static String INPUT_DIRECTORY = "baseDirectory";
    public final static String OUTPUT_DIRECTORY = "outputDirectory";
    
    FileOutputStream outputStream;

    private VrpConfiguration configuration = new VrpConfiguration();

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

    public static void main(String... args) throws IOException {
        VrpSolver s = new VrpSolver();
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
            outputStream = FileUtils.openOutputStream(
                    outputFile, true);
            IOUtils.write(configurationAsString, outputStream);
//            IOUtils.closeQuietly(outputStream);
        } else {
            System.out.println(configurationAsString);
        }

        // get all problems / files in the directory
        Iterator<File> files = FileUtils.iterateFiles(new File(directory),
                new String[] { "txt" }, false);
        while (files.hasNext()) {
        	File file = files.next();
        	if ( !file.getName().contains("scenario") && file.getName().contains("r101"))
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
//	                try {
//	                    createTsSolution(problem);
//	                } catch (Exception e) {
//	                    System.err
//	                            .println("An error occurred while calculating the solution with the Tabu Search heuristic.");
//	                    e.printStackTrace();
//	                }
//	                try {
//	                	createPoissonLeiSolution(problem);
//	                } catch (Exception e) {
//	                    System.err
//	                            .println("An error occurred while calculating the solution with the Poisson-Lei Search heuristic.");
//	                    e.printStackTrace();
//	                }	                
	            } catch (Exception e) {
	                e.printStackTrace();
	            } finally {
	                IOUtils.closeQuietly(openInputStream);
	            }
	        }
        }
    }

    private void createTsSolution(VrpProblem problem) throws IOException {
        // first, create an initial stochastic solution
        StochasticPushForwardInsertionSolver initialSolver = new StochasticPushForwardInsertionSolver();
        Solution solution = initialSolver.solve(problem);

        // open the output file
        Calendar c = Calendar.getInstance();
        String outputDirectory = configuration
                .getConfigurationValueString(OUTPUT_DIRECTORY);
        if (StringUtils.isEmpty(outputDirectory)) {
            TabuSearchHeuristic tsHeuristic = new TabuSearchHeuristic() {
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
                            + is.isImprovedSolution() + ";" 
                            + is.isAccepted());
                }
            };
                       
            long startTime = System.nanoTime();
            Solution improvedSolution = tsHeuristic.improve(solution,
                    configuration);
            long endTime = System.nanoTime();
            String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

            // create an intermediate --- line, just for easier cutting
            System.out.println("----------");
            System.out.println(timeTaken);

            System.out.println(improvedSolution.getIteration());
            System.out.println(improvedSolution.getSolutionAsString());
        } else {
            File outputFile = new File(outputDirectory + "TabuSearch-"
                    + problem.getDescription() + "-" + sdf.format(c.getTime())
                    + ".csv");
            final FileOutputStream outputStream = FileUtils.openOutputStream(
                    outputFile, true);

            // now, first create the non-TS-solution
            TabuSearchHeuristic tsHeuristic = new TabuSearchHeuristic() {
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
                                                .doubleValue() / 1000) + ";"
                                        + is.isBestSolution() + ";"
                                        + is.isImprovedSolution() + ";"
                                        + is.isAccepted() + "\n", outputStream);
                    } catch (IOException ioe) {
                        System.err.println("oops ... error!");
                    }

                }
            };
            
            IOUtils.write("Iteration; #CustomersChanged; RemovalHeuristic; TimeForRemoval; InsertionHeuristic; " +
            		"TimeForInsertion; BestSolution?; ImprovedSolution?; AcceptedSolution? \n\n", outputStream);
            long startTime = System.nanoTime();
            Solution improvedSolution = tsHeuristic.improve(solution,
                    configuration);
            long endTime = System.nanoTime();
            String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

            // create an intermediate --- line, just for easier cutting
            IOUtils.write("----------\n", outputStream);
            IOUtils.write(timeTaken, outputStream);

            IOUtils.write(improvedSolution.getIteration() + "\n", outputStream);
            IOUtils.write(improvedSolution.getSolutionAsString(), outputStream);

            IOUtils.closeQuietly(outputStream);
        }
    }

    private void createLeiEtAlSolution(VrpProblem problem) throws IOException {
        // first, create an initial solution
        StochasticPushForwardInsertionSolver initialSolver = new StochasticPushForwardInsertionSolver();
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
            outputStream = FileUtils.openOutputStream(
                    outputFile, true);
            // now, first create the non-TS-solution
            LeiEtAlHeuristic leaHeuristic = new LeiEtAlHeuristic() {
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
                                                .doubleValue() / 1000) + ";"
                                        + is.isBestSolution() + ";"
                                        + is.isImprovedSolution() + ";"
                                        + is.isAccepted() + "\n", outputStream);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                }
            };
            IOUtils.write("Iteration; #CustomersChanged; RemovalHeuristic; TimeForRemoval; InsertionHeuristic; " +
            		"TimeForInsertion; BestSolution?; ImprovedSolution?; AcceptedSolution? \n\n", outputStream);
            
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

//            IOUtils.closeQuietly(outputStream);
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
    
    
    private void createSolution(VrpProblem problem, AbstractSolver initialSolver, LeiEtAlHeuristic metaheuristicPrint, LeiEtAlHeuristic metaheuristicWrite, String name) throws Exception {
        // first, create an initial solution
        Solution solution = initialSolver.solve(problem);

        // open the output file
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

        String outputDirectory = configuration
                .getConfigurationValueString(OUTPUT_DIRECTORY);
        if (StringUtils.isNotEmpty(outputDirectory)) {
            File outputFile = new File(outputDirectory + name + "-"
                    + problem.getDescription() + "-" + sdf.format(c.getTime())
                    + ".csv");
            outputStream = FileUtils.openOutputStream(
                    outputFile, true);
            // now, first create the non-TS-solution
            
            IOUtils.write("Iteration; #CustomersChanged; RemovalHeuristic; TimeForRemoval; InsertionHeuristic; " +
            		"TimeForInsertion; BestSolution?; ImprovedSolution?; AcceptedSolution? \n\n", outputStream);
            
            long startTime = System.nanoTime();
            Solution improvedSolution = metaheuristicWrite.improve(solution,
                    configuration);
            long endTime = System.nanoTime();
            String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

            // create an intermediate --- line, just for easier cutting
            IOUtils.write("----------\n", outputStream);
            IOUtils.write(timeTaken, outputStream);
            IOUtils.write(improvedSolution.getIteration() + "\n", outputStream);
            IOUtils.write(improvedSolution.getSolutionAsString(), outputStream);

//            IOUtils.closeQuietly(outputStream);
        } else {            
            long startTime = System.nanoTime();
            Solution improvedSolution = metaheuristicPrint.improve(solution,
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
    
    private void createPoissonLeiSolution(VrpProblem problem) throws Exception{
    	LeiEtAlHeuristic metaheuristicPrint = new PoissonLeiEtAlHeuristic() {
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
        
        LeiEtAlHeuristic metaheuristicWrite = new PoissonLeiEtAlHeuristic() {
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
                                            .doubleValue() / 1000) + ";"
                                    + is.isBestSolution() + ";"
                                    + is.isImprovedSolution() + ";"
                                    + is.isAccepted() + "\n", outputStream);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        
        AbstractPushForwardInsertionSolver initialSolver = 
        	new AbstractPushForwardInsertionSolver() {            
	            @Override
	            protected CustomerWithCost calculateCustomerHook(Customer customer, Tour tour, double approximateEquality) {
	            	return TourUtils.calculateCostStochasticSolver(customer, tour, approximateEquality);
	            }
	
	            @Override
	            protected Tour createTourHook(Depot depot, int vehicleId, double vehicleCapacity) {
	            	Tour tour = new PoissonTour(depot, new Vehicle(vehicleId, vehicleCapacity));
	        		return tour;
	            }   
        };
        
        createSolution(problem, initialSolver, metaheuristicPrint, metaheuristicWrite, "PoissonLei");
    }

}
