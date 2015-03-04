package de.rwth.lofip.cli.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;
import de.rwth.lofip.library.interfaces.SolverInterface;
import de.rwth.lofip.library.solver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.StochasticPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.LeiEtAlHeuristic;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.FeasibilityOrientedRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.RandomRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.removals.SimilarityRemoval;
import de.rwth.lofip.stuffNotNeededRightNow.util.IntermediateSolution;

/**
 * CLI application for the VRP Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public abstract class AbstractVrpSolverZumTesten {

    public final static String INPUT_DIRECTORY = "baseDirectory";
    public final static String OUTPUT_DIRECTORY = "outputDirectory";        
    protected static VrpConfiguration configuration = new VrpConfiguration();
    protected static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
    protected FileOutputStream outputStream;
    protected String fileNameShouldContain = "";
    protected String fileNameShouldNotContain = "";
    protected boolean isUseLeiEtAlHeuristic = true;
    protected static int seed = 2;

	private static void setPrintStream() throws FileNotFoundException {
		PrintStream err = new PrintStream(new FileOutputStream(configuration.getConfigurationValueString(OUTPUT_DIRECTORY) + "Output-Error-Stream - " + sdf.format(Calendar.getInstance().getTime())));
		System.setErr(err);		
	}

	public void calculateAll() throws IOException {
        String directory = configuration.getConfigurationValueString(INPUT_DIRECTORY);
        if (StringUtils.isEmpty(directory)) {
            throw new IllegalArgumentException("No input directory specified. Please configure the parameter 'baseDirectory'.");
        }                
        dumpCurrentConfigurationIntoDirectory();        
        setSeedInClassesWithRandomFeatures();
               
        List<File> files = getFilesInDirectory(directory);
        for (File file : files)
        	processFile(file);
    }
	
	protected void dumpCurrentConfigurationIntoDirectory() throws IOException {
        String configurationAsString = configuration.getConfigurationAsString();
        if (StringUtils.isNotEmpty(configuration
                .getConfigurationValueString(OUTPUT_DIRECTORY))) {
            File outputFile = new File(
                    configuration.getConfigurationValueString(OUTPUT_DIRECTORY)
                            + "configuration" + "-"
                            + sdf.format(Calendar.getInstance().getTime()));
            outputStream = FileUtils.openOutputStream(outputFile, true);
            IOUtils.write(configurationAsString, outputStream);
            IOUtils.closeQuietly(outputStream);
        } else {
            System.out.println(configurationAsString);
        }		       
	}
	
	protected void setSeedInClassesWithRandomFeatures() {
    	//maybe I have forgotten some classes that make use of a seed;
        LeiEtAlHeuristic.setSeed(seed);
        RandomRemoval.setSeed(seed);
        FeasibilityOrientedRemoval.setSeed(seed);
        SimilarityRemoval.setSeed(seed);                
	}
   
	protected List<File> getFilesInDirectory(String directory) {
		List<File> files = new LinkedList<File>();
        Iterator<File> fileIterator = FileUtils.iterateFiles(new File(directory),new String[] { "txt" }, false);
        while (fileIterator.hasNext()) {
        	File file = fileIterator.next();
        	if ( !file.getName().contains("scenario") && file.getName().contains(fileNameShouldContain)) // && !file.getName().contains(fileNameShouldNotContain)) // 
        	{	            
        		files.add(file);
	        }
        }
        if (files.isEmpty())
        	throw new RuntimeException("Es konnten keine Dateien eingelesen werden");
        else
        	return files;
	}
	
	protected void processFile(File file) {
        System.out.println("processing file " + file.getName() + "at time " + sdf.format(Calendar.getInstance().getTime()));
        FileInputStream openInputStream = null;
        try {
            openInputStream = FileUtils.openInputStream(file);
            List<String> lines = IOUtils.readLines(openInputStream);
            VrpProblem problem = VrpUtils.createProblemFromStringList(lines);	
            try {
                Solution solution = createInitialSolution(problem);
               	createLeiEtAlSolution(solution);
            } catch (Exception e) {
                System.err.println("An error occurred while calculating the solution with the Lei et al. heuristic.");
                e.printStackTrace();
            }           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(openInputStream);
        }
	}
	
	private Solution createInitialSolution(VrpProblem problem) throws IOException {
        // first, create an initial solution
        SolverInterface initialSolver = getInitialSolver();
        Solution solution = initialSolver.solve(problem);
        System.err.println("Lsg nach Group-PFI: " +  solution.getSolutionAsTupel());
        
        SolverInterface normalInitialSolver = new StochasticPushForwardInsertionSolver();
        Solution solution2 = normalInitialSolver.solve(problem);
        System.err.println("Lsg nach normalem-PFI: " +  solution2.getSolutionAsTupel());
        
        return solution;
	}

	private void createLeiEtAlSolution(Solution solution) throws IOException {
        // open the output file
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");

        String outputDirectory = getOutputDirectory(); 

        if (StringUtils.isNotEmpty(outputDirectory)) {
            File outputFile = new File(outputDirectory + "LeiEtAl-"
                    + solution.getVrpProblem().getDescription() + "-" + sdf.format(c.getTime())
                    + ".csv");
            outputStream = FileUtils.openOutputStream(
                    outputFile, true);
            LeiEtAlHeuristic metaHeuristic = getMetaheuristicOutputStream(); 
            		
            IOUtils.write("Iteration; #CustomersChanged; RemovalHeuristic; TimeForRemoval; InsertionHeuristic; " +
            		"TimeForInsertion; BestSolution?; ImprovedSolution?; AcceptedSolution?; TotalDistance; RecourseCost;" + 
            		"PenaltyCost; Anzahl Touren; KFZ Soll; Solution; Weights \n\n", outputStream);
            
            long startTime = System.nanoTime();          
            Solution improvedSolutionVehicleMinimizationStage = minimizationStage(solution);
            Solution improvedSolutionDistanceMinimizationStage = metaHeuristic.improve(improvedSolutionVehicleMinimizationStage,
                    configuration);
            long endTime = System.nanoTime();
            String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";

            // create an intermediate --- line, just for easier cutting
            IOUtils.write("----------\n", outputStream);
            IOUtils.write(timeTaken, outputStream);
            IOUtils.write(improvedSolutionDistanceMinimizationStage.getIteration() + "\n", outputStream);
            IOUtils.write(improvedSolutionDistanceMinimizationStage.getSolutionAsString(), outputStream);

//            IOUtils.closeQuietly(outputStream);
        } else {
            LeiEtAlHeuristic metaHeuristic = getMetaheuristicConsoleOutput();
            
            long startTime = System.nanoTime();
            Solution improvedSolutionVehicleMinimizationStage = minimizationStage(solution);
            Solution improvedSolutionDistanceMinimizationStage = metaHeuristic.improve(improvedSolutionVehicleMinimizationStage,
            		configuration);
            long endTime = System.nanoTime();
            String timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";
           
            System.out.println("----------");
            System.out.println(timeTaken);
            System.out.println(improvedSolutionDistanceMinimizationStage.getIteration());
            System.out.println(improvedSolutionDistanceMinimizationStage.getSolutionAsString());
        }
    }
	
	protected String getOutputDirectory() {
		return configuration.getConfigurationValueString(OUTPUT_DIRECTORY);
	}     
		
	protected abstract SolverInterface getInitialSolver();
	
	protected abstract LeiEtAlHeuristic getMetaheuristicOutputStream();
	
	protected abstract LeiEtAlHeuristic getMetaheuristicConsoleOutput();

	protected Solution minimizationStage(Solution solution) throws IOException {
		return solution;
	}
	
    
}

