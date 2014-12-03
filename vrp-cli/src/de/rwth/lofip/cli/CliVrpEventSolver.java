package de.rwth.lofip.cli;

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

import de.rwth.lofip.cli.util.SystemOutShowLocation;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;
import de.rwth.lofip.library.analyse.KeyPerformanceIndicators;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.DemandScenarioUtils;
import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.solver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.library.solver.repair.RepairSolution;
import de.rwth.lofip.library.solver.repair.util.RepairedSolution;
import de.rwth.lofip.library.util.VrpProblemUtils;

/**
 * CLI application for the VRP and "Capacity Exceeded"-Event Solver.
 * 
 * TODO this needs to be enhanced on so many levels! Add command line
 * parameters, add selectable heuristics, add selectable output format, ...
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * @author Andreas Braun <braun@dpor.rwth-aachen.de>
 */
public class CliVrpEventSolver {
	
	String timeTaken;
	Solution solution;
	VrpProblem originalVrpProblem;

    public final static String INPUT_DIRECTORY = "baseDirectory";
    public final static String OUTPUT_DIRECTORY = "outputDirectory";
    
    //this is used to set the vehicle capacity to e.g. 80%.
    public final static double percentage = 0.8; 

    private VrpConfiguration configuration = new VrpConfiguration();

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	private boolean condition;

    public static void main(String... args) throws IOException {
        CliVrpEventSolver s = new CliVrpEventSolver();
//        SystemOutShowLocation.activate();
        SystemOutShowLocation.activate();
        //s.calculateAllEvents();
        try {
        	s.writeAverageStats();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    
    /**
     * Read in Solomon instances, solve solomon instance,
     * process N demand scenarios per instance and write mean values to file. 
     *     
     * @author Andreas Braun <braun@dpor.rwth-aachen.de>
     */
    private void writeAverageStats() throws Exception
    {
    	String directory = configuration
                .getConfigurationValueString(INPUT_DIRECTORY);
    	Iterator<File> problemFiles = FileUtils.iterateFiles(new File(directory),
                new String[] { "txt" }, false);
    	
    	double globalAverageAdditionalDistance = 0;
        double globalAverageAffectedTours = 0;
        long globalAverageTimeForCalculation = 0;
        double globalNumberOfExistingProblems = 0;
        double globalNumberOfNewCreatedTours = 0;
        int solomonInstancesWhereThereWasAProblemInDemandScenarioCounter = 0;
        int solomonInstanceCounter = 1;
        
        String outputDirectory = configuration
                .getConfigurationValueString(OUTPUT_DIRECTORY);
                  
        File outputFile = new File(outputDirectory + "Repair-Average-Stats - "         
                + new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime()) 
                + " " + percentage + " Auslastung"
                + ".csv");
        final FileOutputStream outputStream = FileUtils.openOutputStream(
                outputFile, true);
    	
        while (problemFiles.hasNext()) 
        {
        	//first solve solomon instance
            File file = problemFiles.next();
            if ( !file.getName().contains("scenario")) 
            		//&& !file.getName().contains("c") && file.getName().contains("2"))
            {
	            System.out.println("solving solomon instance " + file.getName());
	            solomonInstanceCounter += 1;
	            FileInputStream openInputStream = null;
	            try {
	                //create VRP Problem
	            	openInputStream = FileUtils.openInputStream(file);
	                List<String> lines = IOUtils.readLines(openInputStream);
	                originalVrpProblem = VrpUtils
	                        .createProblemFromStringList(lines);
	                //modify Problem such that vehicles have capacity of percentage wrt original problem	                
	                VrpProblem vrpProblemWithModifiedCapacity = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(originalVrpProblem, percentage);
	                try
	                {
	                	//Solve VRP Problem
	                	long startTime = System.nanoTime();
	                	solution = createSolution(vrpProblemWithModifiedCapacity);
	                	long endTime = System.nanoTime();
	                	timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";                	                       	
	                }
	                catch(Exception id)
	                {
	                	System.err.println( "Solution aus Lei Solver ist nicht feasible" );
	                	System.exit(0);
	                }
	            }
	            catch (Exception e)
	            {
	            	System.err.println( "Ups, Problem beim Dateienauslesen" );
	            }
	            
	            // Process DemandScenarios
	            int numberOfScenarios = 5;
	            double averageAdditionalDistance = 0;
	            double averageAffectedTours = 0;
	            long averageTimeForCalculation = 0;
	            double numberOfExistingProblems = 0;
	            double numberOfNewCreatedTours = 0;
	            
	            for (int i = 1; i <= numberOfScenarios; i++)
	        	{
	        		DemandScenario demandScenario = DemandScenarioUtils.createScenarioFromVrpProblem(originalVrpProblem, 0.2);
	        		KeyPerformanceIndicators keyPerformanceIndicators = new KeyPerformanceIndicators(demandScenario);
	        		//now process demandScenario
	            	Solution temporarySolution = solution.clone();
	        		for (Event e : demandScenario.getEvents())
	        		{				            		
	        			System.out.println("Processing Event for Customer " + e.getCustomerNo() + " at time " + e.getPointInTime());		            		
	        			List<RepairedSolution> solutionList = new RepairSolution().repair(temporarySolution, e);	        			
	        			temporarySolution = solutionList.get(0).getNewSolution();
	        			keyPerformanceIndicators.addRepairedSolution(solutionList.get(0));
	        		}
	        		//display stats for one demand Scenario
	        		IOUtils.write(";;;Number Of Existing Problems für Solomon Instanz " + file.getName() + " und Demand Scenario Nr " + i + ":; " + keyPerformanceIndicators.getNumberOfExistingProblems() + "\n", outputStream);
	        		IOUtils.write(";;;Number Of New Created Tours für Solomon Instanz " + file.getName() + " und Demand Scenario Nr " + i + ":; " + keyPerformanceIndicators.getNumbersOfNewCreatedTours() + "\n", outputStream);
	        		IOUtils.write(";;;Average Time For Calculation pro Event mit Problem:; " + keyPerformanceIndicators.getAverageTimeForCalculation() / 1000 + " microsec" + "\n\n", outputStream);
//	                System.out.println("Press key");
//	                System.in.read();	                
	        		
	        		//update stats
	        		averageAdditionalDistance += keyPerformanceIndicators.getAverageOfAdditionalDistances();
	        		averageAffectedTours += keyPerformanceIndicators.getAverageOfAffectedTours();
	        		averageTimeForCalculation += keyPerformanceIndicators.getAverageTimeForCalculation();
	        		numberOfExistingProblems += keyPerformanceIndicators.getNumberOfExistingProblems();
	        		numberOfNewCreatedTours += keyPerformanceIndicators.getNumbersOfNewCreatedTours();	        		
	        	}
	            // calculate averages on 30 Demand Scenarios for the Solomon Instance that is processed at the moment
	            averageAdditionalDistance = averageAdditionalDistance / numberOfScenarios;
	            averageAffectedTours = averageAffectedTours / numberOfScenarios;	           
	            //average time is calculated for events only where there exists a problem:
	            averageTimeForCalculation = (long) (averageTimeForCalculation / numberOfExistingProblems);
	            numberOfExistingProblems = numberOfExistingProblems / numberOfScenarios;
	            numberOfNewCreatedTours = numberOfNewCreatedTours / numberOfScenarios;
	            
	            //display stats for alle demand Scenario for one solomon Instance
	            IOUtils.write(";Average Number Of Existing Problems für Solomon Instanz " + file.getName() + " und alle Demand Scenarios:; " + numberOfExistingProblems + "\n", outputStream);
	            IOUtils.write(";Average Number Of New Created Tours pro Solomon Instanz " + file.getName() + " und alle Demand Scenarios:; " + numberOfNewCreatedTours + "\n", outputStream);
	            IOUtils.write(";Average Time For Calculation pro Event mit Problem:; " + averageTimeForCalculation / 1000 + " microsec" + "\n\n", outputStream);
//                System.out.println("Press key");
//                System.in.read();
	            
	            // add averages to values for all solomon instances
	            globalAverageAdditionalDistance += averageAdditionalDistance;
	            globalAverageAffectedTours += averageAffectedTours;	
	            if (averageTimeForCalculation != 0)
	            {
	            	solomonInstancesWhereThereWasAProblemInDemandScenarioCounter++;
	            	globalAverageTimeForCalculation += averageTimeForCalculation;
	            }	            
	            globalNumberOfExistingProblems += numberOfExistingProblems;
	            globalNumberOfNewCreatedTours += numberOfNewCreatedTours;           
            }            
        }
        //write total values
        String string1 = String.format("%.3f", globalNumberOfExistingProblems);
        String string2 = String.format("%.3f", globalNumberOfNewCreatedTours);
        IOUtils.write("Total Number Of Existing Problems pro Solomon Instanz und Demand Scenario:; " + string1 + "\n", outputStream);
        IOUtils.write("Total Number Of New Created Tours pro Solomon Instanz und Demand Scenario:; " + string2 + "\n\n", outputStream);     
        
        //calculate averages for all Solomon Instances
        globalAverageAdditionalDistance = globalAverageAdditionalDistance / (double) solomonInstanceCounter;
        globalAverageAffectedTours = globalAverageAffectedTours / (double) solomonInstanceCounter;
        
        globalAverageTimeForCalculation = globalAverageTimeForCalculation / solomonInstancesWhereThereWasAProblemInDemandScenarioCounter;
        globalNumberOfExistingProblems = globalNumberOfExistingProblems / (double) solomonInstanceCounter;
        globalNumberOfNewCreatedTours = globalNumberOfNewCreatedTours / (double) solomonInstanceCounter; 
        
        //write average values
        String string3 = String.format("%.3f", globalNumberOfExistingProblems);
        String string4 = String.format("%.3f", globalNumberOfNewCreatedTours);
        IOUtils.write("Average Number Of Existing Problems pro Solomon Instanz und Demand Scenario:; " + string3 + "\n", outputStream);
        IOUtils.write("Average Number Of New Created Tours pro Solomon Instanz und Demand Scenario:; " + string4 + "\n", outputStream);
        IOUtils.write("Average Time For Calculation pro Event mit Problem:; " + globalAverageTimeForCalculation / 1000 + " microsec" + "\n\n", outputStream);
    }
    
    
    
    
    /**
     * Read in Solomon instance, solve solomon instance,
     * iteratively read in scenarios and solve scenarios 
     * 
     * @author Dominik Sandjaja <dominik@dadadom.de>
     * @author Andreas Braun <braun@dpor.rwth-aachen.de>
     */
    public void calculateAllEvents() throws Exception
    {
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
            final FileOutputStream outputStream = FileUtils.openOutputStream(
                    outputFile, true);
            IOUtils.write(configurationAsString, outputStream);
            IOUtils.closeQuietly(outputStream);
        } else {
            System.out.println(configurationAsString);
        }
        
        // get all problems / files in the directory
        
        // Convention: all solomon instances are named like c101.txt
        // and all scenarios (a set of events) are named like c101_scenarioX.txt (where X is a number)
        
        // first iterate over all problem files
        Iterator<File> problemFiles = FileUtils.iterateFiles(new File(directory),
                new String[] { "txt" }, false);
        while (problemFiles.hasNext()) 
        {
        	//first solve solomon instance
            File file = problemFiles.next();
            if ( !file.getName().contains("scenario")) 
            		//&& !file.getName().contains("c"))
            {
	            System.out.println("solving solomon instance " + file.getName());
	            FileInputStream openInputStream = null;
	            try {
	                openInputStream = FileUtils.openInputStream(file);
	                List<String> lines = IOUtils.readLines(openInputStream);
	                originalVrpProblem = VrpUtils
	                        .createProblemFromStringList(lines);
	                try
	                {
	                	long startTime = System.nanoTime();
	                	solution = createSolution(originalVrpProblem);
	                	long endTime = System.nanoTime();
	                	timeTaken = (endTime - startTime) / 1000 / 1000 + " ms\n";                	                       	
	                }
	                catch(Exception id)
	                {
	                	System.err.println( "Solution aus Lei Solver ist nicht feasible" );
	                	System.exit(0);
	                }
	                	
		            //put out solution into file	          
		            Calendar c = Calendar.getInstance();
		            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	
		            String outputDirectory = configuration
		                    .getConfigurationValueString(OUTPUT_DIRECTORY);
		           
		            if (StringUtils.isNotEmpty(outputDirectory)) 
		            {
			            File outputFile = new File(outputDirectory + "Repair-"
			                    + originalVrpProblem.getDescription() + "-" + sdf.format(c.getTime())
			                    + ".csv");
			            final FileOutputStream outputStream = FileUtils.openOutputStream(
			                    outputFile, true);
			            
			            
			            System.out.println("print solution = " + solution.getSolutionAsTupel());
			            
			            //IOUtils.write("Benötigte Zeit;" + timeTaken, outputStream);
			            //IOUtils.write("Anzahl Iterationen;" + solution.getIteration() + "\n", outputStream);
			            IOUtils.write("Lösung:\n", outputStream);
			            IOUtils.write("Distanz;", outputStream);
			            String s = String.format("%.3f", solution.getTotalDistance());
			            IOUtils.write(s + "\n", outputStream);
			            IOUtils.write(solution.getSolutionAsTupel() + "\n", outputStream);
			            
			            // create an intermediate --- line, just for easier cutting
			            IOUtils.write("----------\n", outputStream);
			            
			            IOUtils.write("Event;Kunde;Nachfrage;urspr. Nachfr.;Zeitpunkt;Anfahrtszeit;Problem?;neue Lsg;zus.Strecke;zus.Kosten;Versp.auf Tour;Anz.betr.Touren;neue Tour eröffnet?;Kosten Modell vs. Kosten neue Tour;benötigte Zeit zur Berechnung der Lösung \n \n", outputStream);	            

			            //processDemandScenariosFromFiles(file, directory, outputStream);
			            processDemandScenariosFromGenerator(file, directory, outputStream, 30);

			            IOUtils.closeQuietly(outputStream);	
		            }		            	      		             
		            else
		            {
		            	System.out.println("No output directory specified");
		            }
	            
	            } finally
	            {
	            	
	            }
		    		
            }
            //TODO: entferne break, damit alle Instanzen durchgerechnet werden.
            //break;
        }
    }
    
    
    //create Solution using LeiEtAl
    private Solution createSolution(VrpProblem problem) throws Exception
    {
        // first, create an initial solution
        DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
        Solution solution = initialSolver.solve(problem);
        
        System.out.println("Berechnete Lösung mit PFI: " + solution.getSolutionAsTupel());             
        
        //then, improve solution
        Solution improvedSolution = new	DeterministicLeiEtAlHeuristic().improve(solution,
                configuration);
        
        System.out.println("Berechnete Lösung mit LeiEtAl: " + improvedSolution.getSolutionAsTupel());
        
        if (!solution.isSolutionFeasibleWrtDemand() == true) throw new Exception();
        
        return improvedSolution;               
    }
    
    
    private void processDemandScenarioAndWriteEachEventToFile(DemandScenario demandScenario, FileOutputStream outputStream) throws Exception
    {
	    //now process demandScenario
    	Solution temporarySolution = solution.clone();
		for (Event e : demandScenario.getEvents())
		{				            		
			System.out.println("Processing Event for Customer " + e.getCustomerNo() + " at time " + e.getPointInTime());		            		
			List<RepairedSolution> solutionList = new RepairSolution().repair(temporarySolution, e);	
			try {
	            String additionalDistanceString = String.format("%.3f", solutionList.get(0).getAdditionalDistance());
	            String timeTakenString = solutionList.get(0).getTimeNeededForCalculationOfSolution() / 1000 + " microsec";
	            	 
	            IOUtils.write(
	            		";" + 
	                    solutionList.get(0).getEvent().getCustomerNo()
	                            + ";"
	                            + solutionList.get(0).getEvent().getDemand()
	                            + ";"
	                            //get old demand
	                            + ";"
	                            + solutionList.get(0).getEvent().getPointInTime()
	                            + ";"
	                            //+ get arrival time 
	                            + ";"
	                            + solutionList.get(0).isExistsProblem() + ";" 
	                            + solutionList.get(0).getNewSolution().getSolutionAsTupel() + ";"
	                            + additionalDistanceString + ";"
	                            //+ solutionList.get(0).getAdditionalCost() 
	                            + ";"
	                            //+ solutionList.get(0).getDelayAtDepot()
	                            + ";"
	                            + solutionList.get(0).getNumberOfAffectedTours() + ";"
	                            + solutionList.get(0).isWasNewTourCreated() + ";"
	                            //+ solutionList.get(0).getCostRepairVsCostNewTour()
	                            + ";"
	                            + timeTakenString
	                            + "\n" , outputStream);			                                        
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
			temporarySolution = solutionList.get(0).getNewSolution();
		}
    }
    
    @SuppressWarnings("unused")
	private void processDemandScenariosFromFiles(File file, String directory, FileOutputStream outputStream) throws Exception
    {
        //now read in all scenario files associated with the current problem 
        //and iterate over these files          	
        
        //first get string that describes solomon instance (e.g. c101)
    	String solomonInstanceString = file.getName().substring(0, file.getName().length() - 4);
    	System.out.println("solomonInstanceString: " + solomonInstanceString);
    	
        Iterator<File> scenarioFiles = FileUtils.iterateFiles(new File(directory), new String[] { "txt" }, false);
        while (scenarioFiles.hasNext())
        {
        	File scenarioFile = scenarioFiles.next();		            	
        	if (solomonInstanceString.contains("rc"))
        		condition = scenarioFile.getName().contains(solomonInstanceString) && scenarioFile.getName().contains("scenario");
        	else
        		condition = scenarioFile.getName().contains(solomonInstanceString) && scenarioFile.getName().contains("scenario") && !scenarioFile.getName().contains("rc");
     
        	if (condition)		            			
            {
            	//here comes the action for the scenario
            	System.out.println("reading in " + scenarioFile.getName() + " for solomon instance " + file.getName()); 
            	
            	//first create scenario from file
            	FileInputStream openScenarioInputStream = null;
            	openScenarioInputStream = FileUtils.openInputStream(scenarioFile);
                List<String> scenarioLines = IOUtils.readLines(openScenarioInputStream);
            	DemandScenario demandScenario = DemandScenarioUtils.createScenarioFromStringList(scenarioLines);
        				           					            	
            	processDemandScenarioAndWriteEachEventToFile(demandScenario, outputStream);
            	IOUtils.write("\n", outputStream);
            }        
        }
    }
    
    private void processDemandScenariosFromGenerator(File file, String directory, FileOutputStream outputStream, int numberOfScenarios) throws Exception
    {
    	for (int i = 1; i <= numberOfScenarios; i++)
    	{
    		DemandScenario demandScenario = DemandScenarioUtils.createScenarioFromVrpProblem(originalVrpProblem, 0.2);
    		processDemandScenarioAndWriteEachEventToFile(demandScenario, outputStream);
    		IOUtils.write("\n", outputStream);
    	}
    }

    
    
    
}
