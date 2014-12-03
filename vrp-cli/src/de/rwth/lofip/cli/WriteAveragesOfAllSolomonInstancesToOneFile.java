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

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;
import de.rwth.lofip.library.analyse.AverageValuesForMultipleSolutionsOnOneSolomonInstance;
import de.rwth.lofip.library.solver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.StochasticPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.library.solver.metaheuristics.LeiEtAlHeuristic;

/**
 * This writes average value of {@value numberOfGeneratedSolutionsPerSolomonInstance}
 * into a table in one file.
 */
public class WriteAveragesOfAllSolomonInstancesToOneFile {

	public final static String INPUT_DIRECTORY = "baseDirectory";
    public final static String OUTPUT_DIRECTORY = "outputDirectory";
    
    FileOutputStream outputStream;
    
    static int numberOfGeneratedSolutionsPerSolomonInstance = 30;

    private VrpConfiguration configuration = new VrpConfiguration();

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

    public static void main(String... args) throws IOException {
    	//read in number of runs on each solomon file.
    	int firstArg = 30;
    	if (args.length > 0) {
    	    try {
    	        firstArg = Integer.parseInt(args[0]);
    	    } catch (NumberFormatException e) {
    	        System.err.println("Argument" + " must be an integer");
    	        System.exit(1);
    	    }
    	}
    	numberOfGeneratedSolutionsPerSolomonInstance = firstArg;
    	
        WriteAveragesOfAllSolomonInstancesToOneFile s = new WriteAveragesOfAllSolomonInstancesToOneFile();
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
        
        // open the output file	    
	    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	
	    String outputDirectory = configuration
	            .getConfigurationValueString(OUTPUT_DIRECTORY);
	    if (StringUtils.isNotEmpty(outputDirectory)) {
	        File outputFile = new File(outputDirectory + "AveragesOfAllInstancesLeiEtAl"
	                + sdf.format(Calendar.getInstance().getTime()) + ".csv");
	        outputStream = FileUtils.openOutputStream(
	                outputFile, true);
	    }

	    IOUtils.write("Anzahl Durchlaufe pro Instanz: " + numberOfGeneratedSolutionsPerSolomonInstance + "\n\n", outputStream);
	    IOUtils.write("Inst.;V;m;Q;avgObj;stdObj;minObj;maxObj;avgDet;stdDet;minDet;maxDet;avgStoch;stdStoch;minStoch;maxStoch;avgTime ms;stdTime ms;minTime ms;maxTime ms\n", outputStream);
	    		
        // get all problems / files in the directory
        Iterator<File> files = FileUtils.iterateFiles(new File(directory),
                new String[] { "txt" }, false);
        while (files.hasNext()) {
        	File file = files.next();
        	if ( !file.getName().contains("scenario")) //&& !file.getName().contains("c"))
        	{	            
	            System.out.println("processing file " + file.getName());
	            FileInputStream openInputStream = null;
	            try {
	                openInputStream = FileUtils.openInputStream(file);
	                List<String> lines = IOUtils.readLines(openInputStream);
	                VrpProblem problem = VrpUtils
	                        .createProblemFromStringList(lines);
	                try {
	                	AverageValuesForMultipleSolutionsOnOneSolomonInstance averageStats = new AverageValuesForMultipleSolutionsOnOneSolomonInstance();
	                	for (int i = 1; i <= numberOfGeneratedSolutionsPerSolomonInstance; i++)
	                	{
	                		averageStats.addSolution(createLeiEtAlSolution(problem));
	                	}
                		//write average stats
	                	DecimalFormat df = new DecimalFormat("000.000");
                		IOUtils.write(file.getName() + ";" 
                        		+ problem.getCustomers().size()+1 + ";" 
                        		+ problem.getVehicleCount() + ";"
                        		+ problem.getVehicles().iterator().next().getCapacity() + ";"
                        		+ String.format("%.3f", averageStats.getMeanValueOfObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getStandardDeviationOfObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getMinimalValueOfObjective()) + ";"        		
                        		+ String.format("%.3f", averageStats.getMaximalValueOfObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getMeanValueOfDetObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getStandardDeviationOfDetObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getMinimalValueOfDetObjective()) + ";"        		
                        		+ String.format("%.3f", averageStats.getMaximalValueOfDetObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getMeanValueOfStochObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getStandardDeviationOfStochObjective()) + ";"
                        		+ String.format("%.3f", averageStats.getMinimalValueOfStochObjective()) + ";"        		
                        		+ String.format("%.3f", averageStats.getMaximalValueOfStochObjective()) + ";"
                        		+ df.format(new Double(
                        				averageStats.getMeanValueOfTime() / 1000)
                                        .doubleValue() / 1000) + ";"
                        		+ df.format(new Double(
                        				averageStats.getStandardDeviationOfTime() / 1000)
                                		.doubleValue() / 1000) + ";"
                                + df.format(new Double(
                                		averageStats.getMinimalValueOfTime() / 1000)
                                		.doubleValue() / 1000) + ";"
                                + df.format(new Double(
                                		averageStats.getMaximalValueOfTime() / 1000)
                                		.doubleValue() / 1000) + "\n"                                       
                           		, outputStream);
	                    
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
    
    
    // deterministic variant
    private Solution createLeiEtAlSolution(VrpProblem problem)
    {
	    // first, create an initial solution
	    DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
	    Solution solution = initialSolver.solve(problem);
	
        LeiEtAlHeuristic leaHeuristic = new DeterministicLeiEtAlHeuristic();
        
        long startTime = System.nanoTime();
        Solution improvedSolution = leaHeuristic.improve(solution,
                configuration);
        long endTime = System.nanoTime();
        improvedSolution.setTimeNeeded(endTime - startTime);
        return improvedSolution;
    }

    
      // stochastic variant
//    private Solution createLeiEtAlSolution(VrpProblem problem)
//    {
//	    // first, create an initial solution
//	    StochasticPushForwardInsertionSolver initialSolver = new StochasticPushForwardInsertionSolver();
//	    Solution solution = initialSolver.solve(problem);
//	
//        LeiEtAlHeuristic leaHeuristic = new LeiEtAlHeuristic();
//        
//        long startTime = System.nanoTime();
//        Solution improvedSolution = leaHeuristic.improve(solution,
//                configuration);
//        long endTime = System.nanoTime();
//        improvedSolution.setTimeNeeded(endTime - startTime);
//        return improvedSolution;
//    }
            
}
