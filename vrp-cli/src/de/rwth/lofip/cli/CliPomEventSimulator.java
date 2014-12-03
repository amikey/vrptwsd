package de.rwth.lofip.cli;

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

import de.rwth.lofip.cli.util.SystemOutShowLocation;
import de.rwth.lofip.library.Solution;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.VrpUtils;
import de.rwth.lofip.library.analyse.CollectStatistics;
import de.rwth.lofip.library.analyse.KeyPerformanceIndicators;
import de.rwth.lofip.library.analyse.util.Scenario;
import de.rwth.lofip.library.scenario.DemandScenario;
import de.rwth.lofip.library.scenario.DemandScenarioUtils;
import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.scenario.PenetrationOfTheMarkedScenario;
import de.rwth.lofip.library.solver.DeterministicPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.VrpConfiguration;
import de.rwth.lofip.library.solver.metaheuristics.DeterministicLeiEtAlHeuristic;
import de.rwth.lofip.library.solver.repair.RepairSolution;
import de.rwth.lofip.library.solver.repair.util.RepairedSolution;
import de.rwth.lofip.library.util.VrpProblemUtils;
/**
 * 
 * @author obock
 *
 */
public class CliPomEventSimulator {

	/* Eingabe der Paramether für die  Simulation:
	 * 
	 * pc  - Plan Kapazität in %, Range: [0.0:1.0]
	 * pom - Penetration of the marked , Range: [0.0:1.0]
	 * sol - Schwankung ohne Leitstand in %, Range: [0.0:1.0]
	 * sml - Schwankung mit Leitstand in %, Range: [0.0:1.0]
	 * ds  - Anzahl der zu erzeugenden demand scenarios, ganzzahlig
	 */
	
	private static double[] pcArray = {1.0};
    private static double[] pomArray = {1.0};
	private static double[] solArray = {0.45};
	private static double[] smlArray = {0.45}; //,0.15,0.2};//weitere :,0.15,0.1
	private static int[]    dsArray = {30};
	/*Ende Eingabe*/
	
	private static List<Scenario> scenarios = new LinkedList<Scenario>();
	
    public final static String INPUT_DIRECTORY = "baseDirectory";
    public final static String OUTPUT_DIRECTORY = "outputDirectory";    
    private static VrpConfiguration configuration = new VrpConfiguration();
    private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
    
    private FileOutputStream outputStream;
	
	public static void main(String[] args) {		
//		PrintStream out = new PrintStream(new FileOutputStream("Output - " + sdf.format(Calendar.getInstance().getTime())));
//		System.setOut(out);
		
		try {	
//			PrintStream err = new PrintStream(new FileOutputStream(configuration.getConfigurationValueString(OUTPUT_DIRECTORY) + "Output-Error-Stream - " + sdf.format(Calendar.getInstance().getTime())));
//			System.setErr(err);	
			SystemOutShowLocation.activate();	
					
			CliPomEventSimulator s = new CliPomEventSimulator();
			List<VrpProblem> vrps = s.createVrpProblems();
			for (double pc: pcArray){
				List<Solution> solutionsWithPlanCapacity =s.solveVrpProblems(vrps, pc);						
				for (double pom : pomArray){
					for (double sol: solArray){
						for (double sml: smlArray){
							int ds =dsArray[0];
							s.writeAverageStats(vrps, solutionsWithPlanCapacity, pc, pom, sol, sml, ds);
						}
					}
				}
			}
			//writeReportOnAllConsideredScenarios();
		} catch (Exception e) {
		    e.printStackTrace();
		  }
	}

	/**
	 * Writing of statistics in a DIRECTORY.
	 * 
	 * @param solutionsForSolomonProblemsWithPlanCapacityPC - a List of solutions
	 * @param pc        - plan capacity
	 * @param pom		- penetration of the marked
	 * @param sol		- deviation without control tower
	 * @param sml		- deviation with control tower
	 * @param ds		- number of demand scenarios 
	 * @throws IOException 
	 */
	private void writeAverageStats(List<VrpProblem> vrpProblems, List<Solution> solutionsForSolomonProblemsWithPlanCapacityPC, double pc, double pom, double sol, double sml, int ds) throws IOException
    {     
		double deviationOfDemandInDSAll = 0;
        CollectStatistics statisticsForAllSolomonInstances= new CollectStatistics();//OB: Sammelcontainer für Gesammtauswertung
        CollectStatistics statisticsForAllSolomonInstancesIst = new CollectStatistics();               
        Scenario scenario = new Scenario();

        setOutputStream(pc, pom, sol, sml, ds);
        writeHeader(pc, pom, sol, sml, ds);               
        
	    for (Solution solution: solutionsForSolomonProblemsWithPlanCapacityPC) 
        {            	    		
    		// Process DemandScenarios	    		
            int numberOfScenarios = ds;	           
            CollectStatistics statisticsForSolomonInstance= new CollectStatistics();//OB: Sammelcontainer für eine Instance
            CollectStatistics statisticsForSolomonInstanceIst = new CollectStatistics();
            double deviationOfDemandInDS = 0;	            
            for (int i = 1; i <= numberOfScenarios; i++)
        	{
            	Solution temporarySolution = solution.cloneCompletelySeperateCopy();            
            	Solution temporaryIstSolution = temporarySolution.clone();
            	DemandScenario demandScenario = PenetrationOfTheMarkedScenario.createScenarioForPenetrationOfTheMarked(temporarySolution.getVrpProblem(), temporarySolution, pom, sml, sol);
            	KeyPerformanceIndicators statisticsForDemandScenario = new KeyPerformanceIndicators(demandScenario);
            	KeyPerformanceIndicators statisticsForDemandScenarioIst = new KeyPerformanceIndicators(demandScenario);            
            	
        		//now process demandScenario	            	
            	int eventNumber = 0;
            	for (Event e : demandScenario.getEvents())
        		{	
            		eventNumber++;
            		e.setEventNumber(eventNumber);
            		e.setIterationDemandScenarios(i);
            		
            		VrpProblem oldVrpProblem = temporarySolution.getVrpProblem().clone();		            		
        			List<RepairedSolution> solutionList = new RepairSolution().repair(temporarySolution, e);	        			
        			temporarySolution = solutionList.get(0).getNewSolution();
        			solutionList.get(0).getOldSolution().setVrpProblem(oldVrpProblem);
        			statisticsForDemandScenario.addRepairedSolution(solutionList.get(0));
        			
        			List<RepairedSolution> solutionListIst = new RepairSolution().repairIstSituation(temporaryIstSolution, e);	        			
        			temporaryIstSolution = solutionListIst.get(0).getNewSolution();
        			statisticsForDemandScenarioIst.addRepairedSolution(solutionListIst.get(0));	     
        			
        			DemandScenarioUtils.updateTWinDS(temporarySolution, demandScenario);
        			
        		}/** Ende Events*/
            	
            	 deviationOfDemandInDS += Math.pow(statisticsForDemandScenario.getDeviationOfDemand(),2);
            	 deviationOfDemandInDSAll += Math.pow(statisticsForDemandScenario.getDeviationOfDemand(),2);
            	 //OB: Hinzufügen der Statistiken zur Instanzauswertung
        		 statisticsForSolomonInstance.addRepairedSolutions(statisticsForDemandScenario.getRepairedSolutions());
        		 statisticsForSolomonInstance.addFinalSolutionForDemandScenario(temporarySolution);
        		 statisticsForSolomonInstanceIst.addRepairedSolutions(statisticsForDemandScenarioIst.getRepairedSolutions());
        		 statisticsForSolomonInstanceIst.addFinalSolutionForDemandScenario(temporaryIstSolution);
        			        		
        	}/** Ende DemandScenarios*/
            
//	            solution.setVrpProblem(originalVrp);
            statisticsForAllSolomonInstances.addRepairedSolutions(statisticsForSolomonInstance.getRepairedSolutions());
            statisticsForAllSolomonInstances.addFinalSolutionsForDemandScenario(statisticsForSolomonInstance.getFinalSolutionsForDemandScenarios());
            statisticsForAllSolomonInstancesIst.addRepairedSolutions(statisticsForSolomonInstanceIst.getRepairedSolutions());
            statisticsForAllSolomonInstancesIst.addFinalSolutionsForDemandScenario(statisticsForSolomonInstanceIst.getFinalSolutionsForDemandScenarios());
            
            //display stats for all demand Scenarios for one solomon Instance
            String averageDistanceSolution = String.format("%.3f", solution.getTotalDistance()/solution.getTours().size());	           
            
            IOUtils.write( 
            		solution.getVrpProblem().getDescription()+";" +
            		solution.getVrpProblem().getTotalDemand()+";" +
            		solution.getVehicleCount()+";" +
            		solution.getVrpProblem().getVehicles().iterator().next().getCapacity()+";" +
            		solution.totalUtilizedCapacityAsString()+";" +
            		solution.averageUtilizedCapacityPerTourAsString()+";" +
            		solution.getTotalDistanceAsString()+";" +
            		averageDistanceSolution+";" +
            		solution.getCostInEuroAsString() + ";" +
            		solution.getTimeNeeded()+";" +
            		solution.getMinNumberOfCustomersInTourAsString() + ";" + 
            		solution.getMaxNumberOfCustomersInTourAsString() + ";" +
            		statisticsForSolomonInstance.getNumberOfExistingProblems()+";" +
            		String.format("%.3f", (statisticsForSolomonInstance.getNumberOfExistingProblems()/ (double) ds)) +";" +
            		statisticsForSolomonInstance.getDeviationOfDemand()+";" +
            		Math.sqrt((double)deviationOfDemandInDS/ (double) ds)+";" +
            		statisticsForSolomonInstance.getAverageTimeForCalculationAsString()+";" +
            		statisticsForSolomonInstance.getMinTimeForCalculationAsString()+";" +
            		statisticsForSolomonInstance.getMaxTimeForCalculationAsString()+";" +
            		statisticsForSolomonInstance.getNumbersOfNewCreatedTours()+";" +
            		statisticsForSolomonInstance.getTotalAdditionalDistancesAsString()+";" +
            		statisticsForSolomonInstance.getAverageOfAdditionalDistancesAsString()+";" +
            		statisticsForSolomonInstance.getAverageUtilizedCapacityFromRepairedSolutionsAsString()+"; " +
            		statisticsForSolomonInstance.getAverageUtilizedCapacityPerTourFromRepairedSolutionsAsString()+"; " +
            		statisticsForSolomonInstance.getAverageOfAdditionalCostInEuroPerInstanceAsString(numberOfScenarios)+";" +
            		statisticsForSolomonInstance.getAverageOfAdditionalCostInEuroAsString() +";" +
            		statisticsForSolomonInstance.getAveragePercentageOfCustomersServedTheSameDayAsString() + ";" +
            		statisticsForSolomonInstance.getAveragePercentageOfParcelsCollectedTheSameDayAsString() + ";" + 
            		statisticsForSolomonInstanceIst.getNumbersOfNewCreatedTours()+ ";" +  //ab hier sind die Werte für die Ist-Situation vorgesehen; Ich verstehe aber nicht wie du das vorhatets umzusetzten!
            		statisticsForSolomonInstanceIst.getTotalAdditionalDistancesAsString() + ";" +
            		statisticsForSolomonInstanceIst.getAverageOfAdditionalDistancePerInstanceAsString(numberOfScenarios)+";" + //ist das der richtige Wert?
            		statisticsForSolomonInstanceIst.getAverageUtilizedCapacityFromRepairedSolutionsAsString()+";" +
            		statisticsForSolomonInstanceIst.getAverageUtilizedCapacityPerTourFromRepairedSolutionsAsString()+";" +
            		statisticsForSolomonInstanceIst.getAverageOfAdditionalCostInEuroPerInstanceAsString(numberOfScenarios)+";" +
            		statisticsForSolomonInstanceIst.getAverageOfAdditionalCostInEuroAsString()+ ";" + 
            		statisticsForSolomonInstanceIst.getAveragePercentageOfCustomersServedTheSameDayAsString() + ";" + 
            		statisticsForSolomonInstanceIst.getAveragePercentageOfParcelsCollectedTheSameDayAsString() + ";" + 
//	            		solution.getVrpProblem().equals(vrpProblems.get(indexSolutionList)) + ";" +
//	            		solution.getVrpProblem().print() + ";" + 
//	            		vrpProblems.get(indexSolutionList). print() + 
            		" \n"
            		, outputStream);
            	            
            scenario.addSolutionAndStatistics(solution, statisticsForSolomonInstance, statisticsForSolomonInstanceIst);	            	           

        } /** Ende Instancen*/           
        
        // write the total values
	    
	    //calculate distance of all solutions
	    double solutionDistanceAll = 0; 
	    for (Solution solution: solutionsForSolomonProblemsWithPlanCapacityPC)         
	    	solutionDistanceAll += solution.getTotalDistance();        
	    String solutionDistanceAllString = String.format("%.3f", solutionDistanceAll);
	    
	    //calculate CostInEuro of all solutions
	    double solutionCostInEuroAll = 0; 
	    for (Solution solution: solutionsForSolomonProblemsWithPlanCapacityPC)         
	    	solutionCostInEuroAll += solution.getSumOfDistanceAndExpectedRecourseCostAndPenaltyCost();        
	    String solutionCostInEuroAllString = String.format("%.3f", solutionCostInEuroAll);	                   
	    
	    IOUtils.write( "Alle;" +
        		" - ;" +
        		" - ;" +
        		" - ;" +
        		" - ;" +
        		" - ;" +
        		solutionDistanceAllString + ";" +
        		" - ;" +
        		solutionCostInEuroAllString + ";" +
        		" - ;" +
        		" - ;" +
        		" - ;" +
        		statisticsForAllSolomonInstances.getNumberOfExistingProblems()+";" +
        		String.format("%.3f",statisticsForAllSolomonInstances.getNumberOfExistingProblems()/ (double) ds) +";" +
        		statisticsForAllSolomonInstances.getDeviationOfDemand()+";" +
        		String.format("%.3f",Math.sqrt(deviationOfDemandInDSAll/ (double) ds*solutionsForSolomonProblemsWithPlanCapacityPC.size()))+";" +
        		statisticsForAllSolomonInstances.getAverageTimeForCalculationAsString()+";" +
        		statisticsForAllSolomonInstances.getMinTimeForCalculationAsString()+";" +
        		statisticsForAllSolomonInstances.getMaxTimeForCalculationAsString()+";" +
        		statisticsForAllSolomonInstances.getNumbersOfNewCreatedTours()+";" +
        		statisticsForAllSolomonInstances.getTotalAdditionalDistancesAsString()+";" +
        		statisticsForAllSolomonInstances.getAverageOfAdditionalDistancesAsString()+";" +
        		statisticsForAllSolomonInstances.getAverageUtilizedCapacityFromRepairedSolutionsAsString()+"; " +
        		statisticsForAllSolomonInstances.getAverageUtilizedCapacityPerTourFromRepairedSolutionsAsString()+"; " +
        		statisticsForAllSolomonInstances.getTotalAdditionalCostInEuroAsString()+";" +
        		statisticsForAllSolomonInstances.getAverageOfAdditionalCostInEuroAsString()+";" +
        		statisticsForAllSolomonInstances.getAveragePercentageOfCustomersServedTheSameDayAsString() + ";" +
        		statisticsForAllSolomonInstances.getAveragePercentageOfParcelsCollectedTheSameDayAsString() + ";" + 
        		statisticsForAllSolomonInstancesIst.getNumbersOfNewCreatedTours()+ ";" +  //ab hier sind die Werte für die Ist-Situation vorgesehen; Ich verstehe aber nicht wie du das vorhattest umzusetzen!
        		statisticsForAllSolomonInstancesIst.getTotalAdditionalDistancesAsString()+";" +
        		statisticsForAllSolomonInstancesIst.getAverageOfAdditionalDistancesAsString()+";" + //ist das der richtige Wert?
        		statisticsForAllSolomonInstancesIst.getAverageUtilizedCapacityFromRepairedSolutionsAsString()+";" +
        		statisticsForAllSolomonInstancesIst.getAverageUtilizedCapacityPerTourFromRepairedSolutionsAsString()+";" +
        		statisticsForAllSolomonInstancesIst.getTotalAdditionalCostInEuroAsString()+";" +
        		statisticsForAllSolomonInstancesIst.getAverageOfAdditionalCostInEuroAsString()+ ";" + 
        		statisticsForAllSolomonInstancesIst.getAveragePercentageOfCustomersServedTheSameDayAsString() + ";" +
	            statisticsForAllSolomonInstancesIst.getAveragePercentageOfParcelsCollectedTheSameDayAsString() + " \n" 
        		, outputStream);  	
	    
	    scenarios.add(scenario);
    }
	
	
	
	
	private void setOutputStream(double pc, double pom, double sol, double sml, int ds) throws IOException {
		String outputDirectory = configuration
                .getConfigurationValueString(OUTPUT_DIRECTORY);
              
        File outputFile = new File(outputDirectory + "Simulation-Stats - "         
                + sdf.format(Calendar.getInstance().getTime()) 
                + "- PC " + pc + ", POM  "+ pom + ", SoL "+ sol + ", SmL  "+ sml + ", DS "+ds
                + ".csv");
        outputStream = FileUtils.openOutputStream(
                outputFile, true);		
	}
	
	private void writeHeader(double pc, double pom, double sol, double sml,	int ds) throws IOException {
		IOUtils.write("Szenario Parameter \n", outputStream);
        IOUtils.write("Plankapazität: " + pc + "\n", outputStream);
        IOUtils.write("Durchdringung: ;"+ pom + "\n", outputStream);
        IOUtils.write("Schwankung ohne Leitstand: ; "+ sol + ";; Schwankung mit Leitstand: ; "+ sml + "\n", outputStream);
        IOUtils.write("Anzahl Demand Scenarios : ; "+ ds + "\n\n", outputStream);
        IOUtils.write("Instance ;;;;;;;;;;;; Demand Scenarios ;;;; Repair-Algorithmus ;;;;;;;;;;;; Ist-Verfahren \n", outputStream);
	    IOUtils.write("Name;" +
	    		"Auftragsvol.;" +
	    		"Anz.Fahrzeuge;" +
	    		"Kapazität;" +
	    		"Auslast. ges.;" +
	    		"(/)Auslast. p.Tour;" +
	    		"Strecke ges.;" +
	    		"(/)Strecke p.Tour;" +
	    		"Kosten ges;" +
	    		"Lösungsdauer;" +
	    		"min#Kunden/Tour;" +
	    		"max#Kunden/Tour;" + //ab hier Werte für DemandScenarios
	    		"Probl. ges.;" +
	    		"(/)Probl. p.DS;" +
	    		"(/)Schwank. ges.;" +
	    		"(/)Schwank.p.DS;" + //ab hier Werte für Reparatur-Algorithmus
	    		"(/)Time ms;" +
	    		"minTime ms;" +
	    		"maxTime ms;" +
	    		"add. Fahrzeuge;" +
	    		"add. Strecke ges.;" +
	    		"(/)add. Strecke p.Probl;" +
	    		"(/)Auslastung ges.;" +
	    		" (/)Auslastung p.Tour;" +
	    		" add.Kosten ges.;" +
	    		"(/)add. Kosten p.Problem;" +
	    		"(/) am selben Tag bediente Kunden;" + 
	    		"(/) am selben Tag abgeholte Pakete;" +
	    		"add. Fahrzeuge;" + //ab hier sind die Werte für die Ist-Situation vorgesehen;
	    		"add. Strecke ges.;" +
	    		"add. Strecke p.Probl;" +
	    		"(/)Auslastung ges.;" +
	    		"Auslastung p.Tour;" +
	    		"add.Kosten ges.;" +
	    		"(/)add. Kosten p.Problem;" +
	    		"(/) am selben Tag bediente Kunden;" +
	    		"(/) am selben Tag abgeholte Pakete;" +
	    		"\n", outputStream);
	}


	

	/**
	 * Solve VrpProblems from solomon instances with modified plan capacity (pc).
	 * (The referenced VrpProblems are afterwards the original one.)
	 * 
	 * @param vrpProblems
	 * @param pc
	 * @return
	 */
	public List<Solution> solveVrpProblems(List<VrpProblem> vrpProblems, double pc)
	{
		
		List<Solution> solutions= new LinkedList<Solution>();
		
    	for (VrpProblem vrp: vrpProblems) 
        {
        
            {
	            System.out.println("solving solomon instance " + vrp.getDescription());
	            //modify Problem such that vehicles have capacity of percentage wrt original problem	                
	           
	            VrpProblem vrpTemp = vrp.clone();
	            vrpTemp = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrpTemp, pc);
	            
	            //VrpProblem vrpProblemWithModifiedCapacity = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrp, pc);
	            try
	            {
	                //Solve VRP Problem
	                long startTime = System.nanoTime();
	                Solution solution = createSolution(vrpTemp);
	                long endTime = System.nanoTime();
	                long timeTaken = (endTime - startTime);
	                
	                vrpTemp = VrpProblemUtils.decreaseCapacityOfVehiclesToPercentageOf(vrpTemp, (1/pc));
	                
	                solution.setVrpProblem(vrpTemp);
	                solution.setTimeNeeded(timeTaken);	                
	                //set vehicle capacities in solution to capacity of the vrp problem
	                for (Tour t : solution.getTours())
	                {
	                	t.getVehicle().setCapacity(vrpTemp.getVehicles().iterator().next().getCapacity());
	                }
	                solutions.add(solution);         
	            }
	            catch(Exception id)
	            {
	                System.err.println( "Solution aus Lei Solver ist nicht feasible" );
	                id.printStackTrace();
	                System.exit(0);
	            }
            }
        }
		return solutions;	
	}
	
	
	
	/**	
	 * Create a list of VRP problems from directory.
	 *  
	 * @return
	 */
	public List<VrpProblem> createVrpProblems() {
		
		List<VrpProblem> vrpProblems = new LinkedList<VrpProblem>();
		
		String directory = configuration
                .getConfigurationValueString(INPUT_DIRECTORY);
    	Iterator<File> problemFiles = FileUtils.iterateFiles(new File(directory),
                new String[] { "txt" }, false);
    	while (problemFiles.hasNext()) 
        {
            File file = problemFiles.next();
            if ( !file.getName().contains("scenario") && !file.getName().contains("n")) // && file.getName().contains("c102")) // && !file.getName().contains("r"))	
            {	           
	            FileInputStream openInputStream = null;
	            try {
	                //create VRP Problem
	            	openInputStream = FileUtils.openInputStream(file);
	                List<String> lines = IOUtils.readLines(openInputStream);
	                VrpProblem vrpProblem = VrpUtils
	                        .createProblemFromStringList(lines);
	                
//	                //AB: set vehicle capacity to double of the amount that is given in files => longer tours
//	                double capacity = vrpProblem.getVehicles().iterator().next().getCapacity();
//	                capacity = capacity * 2;
//	                vrpProblem.setVehicleCapacity(capacity);
	                
	                //AB: now set number of vehicles in problems such that utilized capacity is >80% (more like 90%, actually)
	                double percentage = 0.8;
	                double numberOfVehicles;
	                numberOfVehicles = vrpProblem.getTotalDemand() / (vrpProblem.getVehicles().iterator().next().getCapacity() * percentage);
	                numberOfVehicles = Math.ceil(numberOfVehicles);
	                vrpProblem.setVehicleCount((int) numberOfVehicles-1);
	                vrpProblems.add(vrpProblem);
	                
	                System.out.println("created VRP from solomon instance " + file.getName());
	            }
	            catch (Exception e)
	            {
	            	System.err.println( "Ups, Problem beim Dateien auslesen" );
	            	e.printStackTrace();
	            }
	         }
        }
    	//print vrpProblems
    	String s = "";
    	for (VrpProblem problem : vrpProblems)
    		s += problem.getDescription() + "\n ";
    	System.out.println("vrpProblems: ");
    	System.out.println(s);
    	
		return vrpProblems;
	}

	/**Create Solution using LeiEtAl.
	 * 
	 * @param problem
	 * @return
	 * @throws Exception
	 */
	public Solution createSolution(VrpProblem problem) throws Exception
	{
		// first, create an initial solution
		DeterministicPushForwardInsertionSolver initialSolver = new DeterministicPushForwardInsertionSolver();
		Solution solution = initialSolver.solve(problem);
    
		System.out.println("Berechnete Lösung mit PFI: " + solution.getSolutionAsTupel());             
    
		//then, improve solution
		Solution improvedSolution = new	DeterministicLeiEtAlHeuristic().improve(solution,
            configuration);
    
		System.out.println("Berechnete Lösung mit LeiEtAl: " + improvedSolution.getSolutionAsTupel());
    
		if (solution.isSolutionFeasibleWrtDemand() == false)
		{
			System.err.println("Lösung der Instanz " + problem.getDescription() + " nicht feasible wrt demand");
			System.err.println("Lösung sieht so aus: \n" + improvedSolution.getSolutionAsStringWithCustomer());
			throw new Exception();
		}						
    
		return improvedSolution;               
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static void writeReportOnAllConsideredScenarios() throws IOException {
		
		for (double sml: smlArray)
		{
			String outputDirectory = configuration
	                .getConfigurationValueString(OUTPUT_DIRECTORY);              
	        File outputFile = new File(outputDirectory + "Simulation-Stats-Auswertung - sml " + sml + " - "         
	                + sdf.format(Calendar.getInstance().getTime())                 
	                + ".csv");
	        final FileOutputStream outputStream = FileUtils.openOutputStream(
	                outputFile, true);
	        	       
	        //consider all instances
	        IOUtils.write("alle" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Grundkosten; Gesamt; Prozent; Alpha-Servicegrad; Beta-Servicegrad", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Grundkosten; Gesamt; Prozent; Alpah-Servicegrad; Beta-Servicegrad", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        int index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsAll();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions())) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						+ s1.getCSIST().getAveragePercentageOfCustomersServedTheSameDay() + ";"
						+ s1.getCSIST().getAveragePercentageOfParcelsCollectedTheSameDay() + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsAll();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +
							String.format("%.3f", (s1.getTotalCostConsideredSolutions())) + ";" +
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    + s1.getCS().getAveragePercentageOfCustomersServedTheSameDay() + ";"
						    + s1.getCS().getAveragePercentageOfParcelsCollectedTheSameDay() + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream);    
	        
	        //consider only clustered instances
	        IOUtils.write("clustered" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsClustered();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsClustered();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream);    
	        
	        //consider only random instances
	        IOUtils.write("random" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsRandom();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsRandom();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream);    
	        
	        //consider only RC instances
	        IOUtils.write("RC" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsRC();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsRC();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream);   
	        
	        //consider only instances with number of TW < 25%
	        IOUtils.write("TW < 25%" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsTW25();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsTW25();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream);   
	        
	        //consider only instances with number of TW < 25%
	        IOUtils.write("TW < 50%" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsTW50();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsTW50();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream); 
	        
	        //consider only instances with number of TW < 25%
	        IOUtils.write("TW < 75%" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsTW75();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsTW75();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream); 
	        
	        //consider only instances with tightly constrained TW
	        IOUtils.write("TW tightly constrained" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsTightlyConstrained();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsTightlyConstrained();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream); 
	        
	        //consider only instances with loosely constrained TW
	        IOUtils.write("TW loosely constrained" + "\n", outputStream);
	        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
	        for (double pom : pomArray)
	        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
	        IOUtils.write("\n", outputStream);        	   
	                	        
	        index = -1;
	        for (double pc: pcArray){
	        	index++;
	        	Scenario s1 = scenarios.get(index);
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	s1.setConsideredSolutionsLooselyConstrained();
	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
	        	//print ist-scenario
				IOUtils.write(String.format("%.3f",pc) + ";"
						+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
						+ s1.getTotalNumberOfUsedVehicles() + ";;"
						+ s1.getCSIST().getNumberOfExistingProblems() + ";"
						+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
						+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
						+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
						+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						, outputStream);	
				
				//print scenarios with control station
				index--;
				for (@SuppressWarnings("unused") double pom : pomArray){
					index++;
					Scenario s = scenarios.get(index);
					// setze die betrachteten Scenarios (CS) in Scenario
					s.setConsideredSolutionsLooselyConstrained();     	
					IOUtils.write(";" + 
							s.getCS().getNumberOfExistingProblems() + ";" +
							s.getCS().getNumbersOfNewCreatedTours() + ";" + 
							s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
							String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
						    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
						    , outputStream);
				}				
	        }
	        IOUtils.write("\n \n", outputStream); 
	        
	        
//	        //consider only instances with number of TW < 25%
//	        CliPomEventSimulator printUtil25 = new CliPomEventSimulator() {
//	        	@Override
//	        	public void setConsideredSolutions(Scenario s){
//	        		s.setConsideredSolutionsTW25();
//	        	}
//	        };
//	        printUtil25.printAll("TW < 25%", outputStream);
//	        
//	        //consider only instances with number of TW < 25%
//	        CliPomEventSimulator printUtil50 = new CliPomEventSimulator() {
//	        	@Override
//	        	public void setConsideredSolutions(Scenario s){
//	        		s.setConsideredSolutionsTW50();
//	        	}
//	        };
//	        printUtil50.printAll("TW < 50%", outputStream);
//	        
//	        //consider only instances with number of TW < 25%
//	        CliPomEventSimulator printUtil75 = new CliPomEventSimulator() {
//	        	@Override
//	        	public void setConsideredSolutions(Scenario s){
//	        		s.setConsideredSolutionsTW75();
//	        	}
//	        };
//	        printUtil75.printAll("TW < 75%", outputStream);
//	        
//	        //consider only instances with number of TW < 25%
//	        CliPomEventSimulator printUtilTight = new CliPomEventSimulator() {
//	        	@Override
//	        	public void setConsideredSolutions(Scenario s){
//	        		s.setConsideredSolutionsTightlyConstrained();
//	        	}
//	        };
//	        printUtilTight.printAll("TW tightly constrained (TW > 75%; (TW<200) >50%)", outputStream);
//	        
//	        //consider only instances with number of TW < 25%
//	        CliPomEventSimulator printUtilLoose = new CliPomEventSimulator() {
//	        	@Override
//	        	public void setConsideredSolutions(Scenario s){
//	        		s.setConsideredSolutionsLooselyConstrained();
//	        	}
//	        };
//	        printUtilLoose.printAll("TW loosely constrained (TW < 50%; (TW<200) <25%)", outputStream);
//	        
	        
	        	        
//	        //consider only instances with number of TW < 25%
//	        IOUtils.write("TW < 25% \n", outputStream);
//	        IOUtils.write(";Lösung VRP; Ist; #Probleme; Recourse; Gesamt; Prozent;", outputStream); 
//	        for (double pom : pomArray)
//	        	IOUtils.write("POM " + String.format("%.3f",pom) + ";#Probleme; Recourse; Gesamt; Prozent Rec/Gesamt; Prozent Ersparnis;", outputStream);
//	        IOUtils.write("\n", outputStream);        	   	                
//	        index = -1;
//	        for (double pc: pcArray){
//	        	index++;
//	        	Scenario s1 = scenarios.get(index);	        	
//	        	s1.setConsideredSolutionsTW25();
//	        	print(pc, s1, index, outputStream);
//	        }
//	        IOUtils.write("\n \n", outputStream); 
//	        
//	        //consider only instances with number of TW < 50%
//	        IOUtils.write("TW < 50% \n", outputStream);
//	        IOUtils.write(";Lösung VRP; Ist; #Probleme; Recourse; Gesamt; Prozent;", outputStream); 
//	        for (double pom : pomArray)
//	        	IOUtils.write("POM " + String.format("%.3f",pom) + ";#Probleme; Recourse; Gesamt; Prozent Rec/Gesamt; Prozent Ersparnis;", outputStream);
//	        IOUtils.write("\n", outputStream);        	   	                
//	        index = -1;
//	        for (double pc: pcArray){
//	        	index++;
//	        	Scenario s1 = scenarios.get(index);	        	
//	        	s1.setConsideredSolutionsTW50();
//	        	print(pc, s1, index, outputStream);
//	        }
//	        IOUtils.write("\n \n", outputStream); 
//	        
//	        //consider only instances with number of TW < 75%
//	        IOUtils.write("TW < 75% \n", outputStream);
//	        IOUtils.write(";Lösung VRP; Ist; #Probleme; Recourse; Gesamt; Prozent;", outputStream); 
//	        for (double pom : pomArray)
//	        	IOUtils.write("POM " + String.format("%.3f",pom) + ";#Probleme; Recourse; Gesamt; Prozent Rec/Gesamt; Prozent Ersparnis;", outputStream);
//	        IOUtils.write("\n", outputStream);        	   	                
//	        index = -1;
//	        for (double pc: pcArray){
//	        	index++;	        	
//	        	Scenario s1 = scenarios.get(index);	        	
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	s1.setConsideredSolutionsTW75();
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	print(pc, s1, index, outputStream);
//	        }
//	        IOUtils.write("\n \n", outputStream); 
//	        
//	        
//	        //consider only tightly constrained instances wrt tw
//	        IOUtils.write("tigthly constrained \n", outputStream);
//	        IOUtils.write(";Lösung VRP; Ist; #Probleme; Recourse; Gesamt; Prozent;", outputStream); 
//	        for (double pom : pomArray)
//	        	IOUtils.write("POM " + String.format("%.3f",pom) + ";#Probleme; Recourse; Gesamt; Prozent Rec/Gesamt; Prozent Ersparnis;", outputStream);
//	        IOUtils.write("\n", outputStream);        	   	                
//	        index = -1;
//	        for (double pc: pcArray){
//	        	index++;	        	
//	        	Scenario s1 = scenarios.get(index);	        	
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	s1.setConsideredSolutionsTightlyConstrained();
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	print(pc, s1, index, outputStream);
//	        }
//	        IOUtils.write("\n \n", outputStream); 
//	        
//	        //consider only loosely constrained instances wrt tw
//	        IOUtils.write("loosely constrained \n", outputStream);
//	        IOUtils.write(";Lösung VRP; Ist; #Probleme; Recourse; Gesamt; Prozent;", outputStream); 
//	        for (double pom : pomArray)
//	        	IOUtils.write("POM " + String.format("%.3f",pom) + ";#Probleme; Recourse; Gesamt; Prozent Rec/Gesamt; Prozent Ersparnis;", outputStream);
//	        IOUtils.write("\n", outputStream);        	   	                
//	        index = -1;
//	        for (double pc: pcArray){
//	        	index++;	        	
//	        	Scenario s1 = scenarios.get(index);	        	
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	s1.setConsideredSolutionsLooselyConstrained();
//	        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
//	        	print(pc, s1, index, outputStream);
//	        }
//	        IOUtils.write("\n \n", outputStream); 
	        
	        
	        
	        
	       
	//		for (double pc: pcArray){
	//			Scenario s1 = getScenario(pc, pomArray[0]);
	//			IOUtils.write(String.format("%.3f",pc) + ";"
	//					+ s1.getTotalCostAllSolutions() + ";;" 
	//					+ s1.getCSIST().getNumberOfExistingProblems() + ";" 
	//					+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
	//					+ String.format("%.3f", (s1.getTotalCostAllSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
	//					+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostAllSolutions())) + ";"
	//					, outputStream);				
	//						
	//			for (double pom : pomArray){
	//				Scenario s = getScenario(pc, pom);	
	//				// setze die betrachteten Scenarios (CS) in Scenario
	//				IOUtils.write(";" + 
	//						s.getCS().getNumberOfExistingProblems() + ";" +
	//						s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
	//						String.format("%.3f", (s1.getTotalCostAllSolutions() + s1.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]))) + ";" +
	//					    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostAllSolutions())) + ";"
	//					    , outputStream);
	//			}
	//		}		    
		} 
	}
	
//	private static void print(double pc, Scenario s1, int index, FileOutputStream outputStream) throws IOException {
//		//print ist-scenario
//		IOUtils.write(String.format("%.3f",pc) + ";"
//				+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";;" 
//				+ s1.getCSIST().getNumberOfExistingProblems() + ";" 
//				+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
//				+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
//				+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
//				, outputStream);	
//		
//		//print scenarios with control station
//		index--;
//		for (@SuppressWarnings("unused") double pom : pomArray){
//			index++;
//			Scenario s = scenarios.get(index);
//			// setze die betrachteten Scenarios (CS) in Scenario
//			s.setConsideredSolutionsRC();     	
//			IOUtils.write(";" + 
//					s.getCS().getNumberOfExistingProblems() + ";" +
//					s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
//					String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
//				    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";" +
//				    String.format("%.3f", (1 - ((s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0])) / (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))))) + ";"						
//				    , outputStream);
//		}				
//		IOUtils.write("\n", outputStream);
//	}
	
	
	public void setConsideredSolutions(Scenario s){
		
	}
	
	
	public void printAll(String modus, FileOutputStream outputStream) throws IOException {			
		IOUtils.write(modus + "\n", outputStream);
        IOUtils.write(";Lösung VRP; Anzahl KFZ; Ist; #Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream); 
        for (double pom : pomArray)
        	IOUtils.write(String.format("%.3f",pom) + ";#Probleme; neue KFZ; Recourse; Gesamt; Prozent;", outputStream);
        IOUtils.write("\n", outputStream);        	   
                
        int index = -1;
        for (double pc: pcArray){
        	index++;
        	Scenario s1 = scenarios.get(index);
        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
        	setConsideredSolutions(s1);
        	System.err.println("s1.consideredSolutions: " + s1.getConsideredSolutions().size());
        	//print ist-scenario
			IOUtils.write(String.format("%.3f",pc) + ";"
					+ String.format("%.3f",s1.getTotalCostConsideredSolutions()) + ";" 
					+ s1.getTotalNumberOfUsedVehicles() + ";;"
					+ s1.getCSIST().getNumberOfExistingProblems() + ";"
					+ s1.getCSIST().getNumbersOfNewCreatedTours() + ";"
					+ s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";"
					+ String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";"
					+ String.format("%.3f", (s1.getCSIST().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
					, outputStream);	
			
			//print scenarios with control station
			index--;
			for (@SuppressWarnings("unused") double pom : pomArray){
				index++;
				Scenario s = scenarios.get(index);
				// setze die betrachteten Scenarios (CS) in Scenario
				setConsideredSolutions(s);     	
				IOUtils.write(";" + 
						s.getCS().getNumberOfExistingProblems() + ";" +
						s.getCS().getNumbersOfNewCreatedTours() + ";" + 
						s.getCS().getAverageOfAdditionalCostInEuroPerInstanceAsString(dsArray[0]) + ";" +  
						String.format("%.3f", (s1.getTotalCostConsideredSolutions() + s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]))) + ";" +
					    String.format("%.3f", (s.getCS().getAverageOfAdditionalCostInEuroPerInstance(dsArray[0]) / s1.getTotalCostConsideredSolutions())) + ";"
					    , outputStream);
			}				
        }
        IOUtils.write("\n \n", outputStream);   
	}


}
